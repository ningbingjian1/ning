package com.ning.netty.demo.nio.blockchat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 基于socket的阻塞式编程
 */
public class SocketServer {
    Map<String,BufferedWriter> writers = new HashMap<>();
    Map<String,BufferedReader> readers = new HashMap<>();
    public static void main(String[] args) throws Exception{
        new SocketServer().start();;

    }
    public void start() throws Exception{
        ServerSocket serverSocket = new ServerSocket(9981);
        while(true){
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String key = "[" + socket.getInetAddress() + ":" + socket.getPort() + "]";
            writers.put(key,writer);
            readers.put(key,reader);
            new Thread(new ClientReaderThread(key,reader,writer)).start();
        }
    }
    class ClientReaderThread extends Thread{
        private String key ;
        private BufferedReader reader;
        private BufferedWriter writer;
        public ClientReaderThread(String key,BufferedReader reader,BufferedWriter writer){
            this.key = key ;
            this.reader = reader;
            this.writer = writer;
        }

        @Override
        public void run() {

            try {
                String line = reader.readLine();
                while(line != null){
                    System.out.println("[服务端打印]" + line);
                    Iterator<Map.Entry<String,BufferedWriter>> writerIter = writers.entrySet().iterator();
                    while(writerIter.hasNext()){
                        Map.Entry<String,BufferedWriter> entry = writerIter.next();
                        if(entry.getKey() != this.key ){
                            try {
                                entry.getValue().write( "[来自" + entry.getKey() +"]" +line);
                                entry.getValue().write("\r\n");
                                entry.getValue().flush();
                            } catch (IOException e) {
                                System.out.println("对方已经关闭连接:" + entry.getKey());
                                writerIter.remove();
                                readers.remove(entry.getKey());
                            }
                        }
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println("已经关闭了链接:" + this.key);
            }
        }
    }
}
