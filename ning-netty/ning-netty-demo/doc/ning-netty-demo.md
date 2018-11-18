# protobuf实例
## Person
 -- 是通过protobuf命令生成的 windows下命令如下
```shell
D:\tools\protobuf>protoc-3.5.1-windows-x86_64.exe -I=D:\tools\protobuf\person\ --java_out=D:\tools\protobuf\person D:\tools\protobuf\person\\Person.proto
```
## ProtoBufServe
    - 负责服务端创建，接收客户端请求
    
    创建服务端通过模板代码创建即可，主要变化在ChannelInitializer添加的编码器和解码器
### 编码器
    ProtobufVarint32LengthFieldPrepender  添加头部编码处理
    ProtobufEncoder   内容编码处理
### 解码器
    ProtobufVarint32FrameDecoder  头部解码
    ProtobufDecoder  内容解码  传入我们的protoBuf解码实例
### inBound消息处理
    ProtoBufServerHandler  处理经过解码后的对象 此时已经是我们需要的真正对象了
## ProtoBufClient
    负责处理从服务端响应的内容，这里仅仅打印
    创建方式和服务端一样
### inBound消息处理
    ProtoBufClientHandler     处理经过解码后的对象 此时已经是我们需要的真正对象了
 
 
# protobuf2
这个实例主要实现了在netty通讯的时候，解决多个不同类型的消息传送问题。比如需要同时传送Person ,Point ,Car等不同的消息
通过protobuf这个实例时无法实现的。
只有通过定义msgType来区分传送的是哪种类型，然后进行对应的提取，而在proto文件中用oneof来封装多个消息，
每次只设置一个具体的消息实例即可
如下：定义消息为RpcMsg,内部包含type 和 oneof msg两个实体 
msgType表示当前传送的是哪个消息
oneof包裹了后边定义的多个消息
```
message RpcMsg{
    enum MsgType{
        PersonType = 1;
        CarType = 2;
        PointType = 3 ;
    }
    required MsgType msg_type = 1;
    oneof msg {
        Person person = 2;
        Car car = 3;
        Point point = 4;
    }

}
```


# httpServer实例
# echo 实例

# example1
这是一个启动好一直相互发送消息的例子，服务端和客户端会不停的发送消息，除非手动停止

主要是客户端在channelActive事件中触发消息发送，之后服务端和客户端不停止的通信

使用长度编码器
# chat聊天实例
使用分隔符解码器  行分隔符 一行表示一个消息 
客户端之间相互通信
## 重点知识点
DelimiterBasedFrameDecoder 分隔符解码器
DefaultChannelGroup :可以把多个channel组成一组，然后可以对这组channel进行消息发送
handlerAdded:group加入channel的时机点，表示在channel处理已经添加后加入group
NingChatClient:连接服务端后从控制台读取消息发送给服务端，服务端再转发
给其他客户端.

# readwrite.check

读写空闲检测

## 知识点
IdleStateHandler ：负责检测服务端和客户端的空闲策略 空闲包含读空闲 写空闲  读写空闲


# nio包的例子
nio是一些测试nio使用的例子

# NIO的原理
http://www.importnew.com/19816.html


# nio.blockchat
这是使用阻塞IO的聊天程序，客户端发送消息，服务端转发给其他客户端。
# nio.chat
使用NIO写的聊天程序 ，客户端发送消息给服务端，服务端负责转发给其他客户端



 

    







