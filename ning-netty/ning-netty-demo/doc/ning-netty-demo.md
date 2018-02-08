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
    ProtobufDecoder  内容解码
### inBound消息处理
    ProtoBufServerHandler  处理经过解码后的对象 此时已经是我们需要的真正对象了
## ProtoBufClient
    负责处理从服务端响应的内容，这里仅仅打印
    创建方式和服务端一样
### inBound消息处理
    ProtoBufClientHandler     处理经过解码后的对象 此时已经是我们需要的真正对象了
    
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

    







