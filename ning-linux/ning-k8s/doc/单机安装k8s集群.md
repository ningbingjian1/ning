# ��ǽ
��

# �ر�ipv6

```
echo 1 > /proc/sys/net/ipv6/conf/all/disable_ipv6
echo 1 > /proc/sys/net/ipv6/conf/default/disable_ipv6
echo 1 > /proc/sys/net/ipv6/conf/all/disable_ipv6
echo 1 > /proc/sys/net/ipv6/conf/default/disable_ipv6
sysctl -p
```

# firewalld
```
systemctl disable firewalld 
systemctl stop firewalld

```

# selinux 
```
[root@node-254 ~]# cat /etc/selinux/config

SELINUX=permissive
[root@node-254 ~]# sed -i 's/SELINUX=.*/SELINUX=disabled/' /etc/selinux/config 
[root@node-254 ~]# cat /etc/selinux/config 
```
# iptables 
```
yum install iptables-services
[root@node-254 ~]# iptables -F 
[root@node-254 ~]# service iptables save 
iptables: Saving firewall rules to /etc/sysconfig/iptables:[  OK  ]
[root@node-254 ~]# systemctl enable iptables
Created symlink from /etc/systemd/system/basic.target.wants/iptables.service to /usr/lib/systemd/system/iptables.service.
[root@node-254 ~]# systemctl start  iptables
```


# ��װ
```
yum install -y kubernetes etcd 
```

# ����docker 
�޸� vim /etc/sysconfig/docker
```
OPTIONS='--selinux-enabled=false --insecure-registry gcr.io --log-driver=journald --signature-verification=false'
```

--selinux-enabled=false   --insecure-registry gcr.io


# ���ù��ھ���
```
sudo curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sudo sh -s http://382902ca.m.daocloud.io
```
��ʱ���������ִ����֮�� ��Ҫ�鿴/etc/docker/daemon.json��json��ʽ�Ƿ���ȷ�����ܻ���������,��������������  ɾ������
```
{"registry-mirrors": ["http://382902ca.m.daocloud.io"],}
```



# ����:
```
systemctl start etcd
systemctl start docker 
systemctl start kube-apiserver
systemctl start kube-controller-manager
systemctl start kube-scheduler
systemctl start kubelet 
systemctl start kube-proxy

```

# ֹͣ:

```
systemctl stop etcd
systemctl stop docker 
systemctl stop kube-apiserver
systemctl stop kube-controller-manager
systemctl stop kube-scheduler
systemctl stop kubelet 
systemctl stop kube-proxy

```



# ����
## ����pod��־���� 

```
Mar 29 01:12:42 localhost kubelet: E0329 01:12:42.006899    7322 pod_workers.go:184] Error syncing pod a468802c-32eb-11e8-9bbc-080027dd486f, skipping: failed to "StartContainer" for "POD" with ImagePullBackOff: "Back-off pulling image \"registry.access.redhat.com/rhel7/pod-infrastructure:latest\""

```


*  �������

```

yum install *rhsm* -y 
docker pull registry.access.redhat.com/rhel7/pod-infrastructure:latest
```



# ����һ��JavaӦ��
## ����mysql����
 * �Ȼ�ȡmysql����
```
docker pull docker.io/mysql
docker tag docker.io/mysql mysql:v1
```
 * ����rc

 ```
 [root@node-254 example1]# cat mysql-rc.yaml
apiVersion: v1
kind: ReplicationController
metadata:
  name: mysql
spec:
  replicas: 1
  selector:
    app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:v1
        ports:
        - containerPort: 3306
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: "123456"

 ```
 ִ������
 ```
[root@node-254 example1]# kubectl create -f mysql-rc.yaml 
replicationcontroller "mysql" created
[root@node-254 example1]# kubectl get rc
NAME      DESIRED   CURRENT   READY     AGE
mysql     1         1         1         12s
[root@node-254 example1]# kubectl get pod
NAME          READY     STATUS    RESTARTS   AGE
mysql-r0434   1/1       Running   0          19s
 ```
 
* mysql����

```
[root@node-254 example1]# cat mysql-svc.yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql
spec:
  ports:
    - port: 3306
  selector:
    app: mysql


```
ִ������

```
[root@node-254 example1]# kubectl create -f mysql-svc.yaml 
service "mysql" created
[root@node-254 example1]# kubectl get svc
NAME         CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
kubernetes   10.254.0.1       <none>        443/TCP    16h
mysql        10.254.131.205   <none>        3306/TCP   9s
```




## ����tomcat ����
  
*  ��ȡtomcat����  
```
docker pull docker.io/kubeguide/tomcat-app 
```  

* ����rc

```
[root@node-254 example1]# cat myweb-rc.yaml
kind: ReplicationController
metadata:
  name: myweb
spec:
  replicas: 3
  selector:
    app: myweb
  template:
    metadata:
      labels:
        app: myweb
    spec:
      containers:
        - name: myweb
          image: kubeguide/tomcat-app:v1
          ports:
          - containerPort: 8080
          env:
          - name: MYSQL_SERVICE_HOST
            value: 'mysql'
          - name: MYSQL_SERVICE_PORT
            value: '3306'
```


ִ������

```
[root@node-254 example1]# kubectl create -f myweb-rc.yaml 
replicationcontroller "myweb" created
[root@node-254 example1]# kubectl get rc 
NAME      DESIRED   CURRENT   READY     AGE
mysql     1         1         1         1m
myweb     3         3         0         16s
[root@node-254 example1]# kubectl get rc |grep myweb
myweb     3         3         3         21s
[root@node-254 example1]# kubectl get pod |grep myweb
^C
[root@node-254 example1]# kubectl get pods
NAME          READY     STATUS    RESTARTS   AGE
mysql-r0434   1/1       Running   0          3m
myweb-54r1f   1/1       Running   0          1m
myweb-7wh0w   1/1       Running   0          1m
myweb-99qwt   1/1       Running   0          1m
```

* ����service
```
[root@node-254 example1]# cat myweb-svc.yaml
apiVersion: v1
kind: Service
metadata:
  name: myweb
spec:
  type: NodePort
  ports:
    - port: 8080
      nodePort: 30001
  selector:
    app: myweb

```
ִ������
```
[root@node-254 example1]# kubectl create -f myweb-svc.yaml 
service "myweb" created
[root@node-254 example1]# kubectl get svc 
NAME         CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes   10.254.0.1       <none>        443/TCP          16h
mysql        10.254.131.205   <none>        3306/TCP         3m
myweb        10.254.167.247   <nodes>       8080:30001/TCP   44s
```
 


kubectl create -f mysql-rc.yaml 
kubectl create -f mysql-svc.yaml 
kubectl create -f myweb-rc.yaml 
kubectl create -f myweb-svc.yaml 








