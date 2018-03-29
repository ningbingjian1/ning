# docker 安装

* 安装步骤
```shell
# 安装依赖工具
sudo yum install -y yum-utils   device-mapper-persistent-data   lvm2
# 配置docker-ce源
sudo yum-config-manager     --add-repo     https://download.docker.com/linux/centos/docker-ce.repo
# 安装docker-ce
sudo yum install docker-ce -y
# 启动docker
sudo systemctl start docker 
# 配置docker的国内镜像
sudo curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sudo sh -s http://382902ca.m.daocloud.io
# 重启docker
sudo systemctl restart docker 

```
* 测试

```
# 搜索busybox镜像
docker search busybox
# 拉取镜像
docker pull busybox
# 运行busybox镜像
docker run -itd --name bbox1 busybox
# 访问容器执行ls /命令
docker exec    bbox1  ls /
```