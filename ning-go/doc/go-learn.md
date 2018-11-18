# GO环境安装[windows]
*  下载
从官网下载 go.msi的包 ，直接点击下一步安装即可
我的安装目录是G:\go，安装完成后会自动配置GOROOT,并把go配置到Path环境变量
* 创建go项目
1. 这里使用的是idea 的go-plugin,创建项目，选择```new project``,选择```go```第一步会提示配置GO SDK,点击下一步,选择项目位置,这里的位置是:
F:\all-workspace\ningbingjian1_github\ning\ning-go\ning-go-test,完成
2. 创建go目录结构
    - pkg
    - bin
    - src
 创建项目
    - src/com.ning/hello
可以认为com.ning是公司名称，hello是我的项目名称
3. 代码
 - 创建```src/com.ning/hello/main.go```

代码如下

```
//main 表示当前是一个可执行程序 而不是一个库
package main
//fmt是一个包名，这里表示要引入fmt这个包
import (
	"fmt"
)
func main(){
	//Printf是函数
	fmt.Printf("hello world -ning ")
}

```
4.执行
* 几个环境变量

```$ go env
set GOARCH=amd64
set GOBIN=
set GOCACHE=C:\Users\Administrator\AppData\Local\go-build
set GOEXE=.exe
set GOHOSTARCH=amd64
set GOHOSTOS=windows
set GOOS=windows
set GOPATH=C:\Users\Administrator\go
set GORACE=
set GOROOT=G:\Go
set GOTMPDIR=
set GOTOOLDIR=G:\Go\pkg\tool\windows_amd64
set GCCGO=gccgo
set CC=gcc
set CXX=g++
set CGO_ENABLED=1
set CGO_CFLAGS=-g -O2
set CGO_CPPFLAGS=
set CGO_CXXFLAGS=-g -O2
set CGO_FFLAGS=-g -O2
set CGO_LDFLAGS=-g -O2
set PKG_CONFIG=pkg-config
set GOGCCFLAGS=-m64 -mthreads -fno-caret-diagnostics -Qunused-arguments -fmessage-length=0 -fdebug-prefix-map=C:\Users\ADMINI~1\AppData\Local\Temp\go-build669768566=/tmp/go-build -gno-record-gcc-switches

```


有几个需要关注的环境变量

```
GOROOT=G:\Go 
表示go安装的根目录

GOPATH=C:\Users\Administrator\go 
表示代码存放路径，这里并没有包含我的项目路径，素以等下我要临时手动加上

GOOS=windows
运行的操作系统, go install 命令会直接把远吗编译成.exe文件，如果是linux会直接编译成bash文件，这个选项包含下面几种可选
darwin
freebsd
linux
windows
android
dragonfly
netbsd
openbsd
plan9
solaris


set GORACE=
架构 包含下面几种选择
arm
arm64
386
amd64
ppc64
ppc64le
mips64
mips64le
s390x

```

* install 
操作过程
```
# 导出GOPATH
$ export  GOPATH="/f/all-workspace/ningbingjian1_github/ning/ning-go/ning-go-test"
$ echo $GOPATH
/f/all-workspace/ningbingjian1_github/ning/ning-go/ning-go-test
# 安装hello
$ go install com.ning/hello
# 生成hello.exe可执行文件
$ ll bin/
total 2028
-rwxr-xr-x 1 Administrator 197121 2073600 Mar 24 21:20 hello.exe*
# 执行
$ bin/hello.exe
hello world -ning
# 设置生成Linux可执行文件
$ export GOOS=linux
$ export GOARCH=amd64

$ go install com.ning/hello
$ ls -rl bin/
total 2028
drwxr-xr-x 1 Administrator 197121       0 Mar 24 21:21 linux_amd64/
-rwxr-xr-x 1 Administrator 197121 2073600 Mar 24 21:20 hello.exe*
$ ls -rl bin/linux_amd64/
total 1984
-rw-r--r-- 1 Administrator 197121 2029446 Mar 24 21:21 hello




```





