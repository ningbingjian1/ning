package com.ning.netty.demo.zeroCopy;



import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个测试网络拷贝文件的例子，服务端使用socket接收客户端的内容
 */
public class BIOCopyServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(9981);
        Socket socket = serverSocket.accept();
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        byte[]bytes = new byte[1024];
        int bytesread = -1;
        int total = 0 ;
        while((bytesread = dis.read(bytes)) >= 0 ){
            total += bytesread;
        }
        System.out.println("服务端接收:total = " + Double.valueOf(total)/1024/1024 + "M");
        dis.close();
    }
}
