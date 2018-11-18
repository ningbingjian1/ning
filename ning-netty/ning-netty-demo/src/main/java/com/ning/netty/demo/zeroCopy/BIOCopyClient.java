package com.ning.netty.demo.zeroCopy;

import com.google.protobuf.ByteString;

import java.io.*;
import java.net.Socket;

public class BIOCopyClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost",9981);
        OutputStream out = socket.getOutputStream();
        for(int i = 0 ;i < 10;i ++){
            FileInputStream fis = new FileInputStream(new File(args[0]));
            send(fis,out);
            fis.close();
        }
        out.close();

    }
    public static void send(InputStream in, OutputStream out)throws Exception{
        DataInputStream dis = new DataInputStream(in);
        DataOutputStream dos = new DataOutputStream(out);
        byte []bytes = new byte[1024];
        int bytesread = -1;
        int total = 0 ;
        while ((bytesread = dis.read(bytes)) >= 0 ){
            total += bytesread ;
            dos.write(bytes,0,bytesread);
        }
        System.out.println("客户端发送:total = " + total);


    }
}
