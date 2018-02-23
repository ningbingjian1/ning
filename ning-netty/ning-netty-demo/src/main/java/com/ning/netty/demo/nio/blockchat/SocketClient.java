package com.ning.netty.demo.nio.blockchat;

import java.io.*;
import java.net.Socket;

/**
 * 基于socket的阻塞式编程
 */
public class SocketClient {
    public static void main(String[] args) throws Exception{
        new SocketClient().start();;

    }
    public void start() throws Exception{
        Socket socket = new Socket("localhost",9981);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(() ->{
            try {
                String line = reader.readLine();
                while(line != null){
                    System.out.println(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String line = consoleReader.readLine();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        while(line != null ){
            writer.write(line);
            writer.write("\r\n");
            writer.flush();;
            line = consoleReader.readLine();

        }
    }
}
