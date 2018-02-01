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

