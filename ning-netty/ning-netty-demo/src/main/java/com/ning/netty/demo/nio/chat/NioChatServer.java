package com.ning.netty.demo.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NioChatServer {
    private static final int BUF_SIZE=1024;
    private static final int PORT = 9981;
    private static final int TIMEOUT = 3000;
    private Selector selector = null;
    private ServerSocketChannel ssc = null;
    private Map<String,SocketChannel> sockets = new HashMap<>();
    public static void main(String[] args) throws Exception {
        new NioChatServer().select();
    }
    public void handleAccept(SelectionKey selectionKey) throws Exception{
        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
        SocketChannel sc = (SocketChannel) ssc.accept();
        sc.configureBlocking(false);
        sc.register(selectionKey.selector(),SelectionKey.OP_READ,
                ByteBuffer.allocateDirect(BUF_SIZE));
        String scKey =  sc.getRemoteAddress() + "";
        sockets.put(scKey,sc);
    }
    public void handleRead(SelectionKey key ) throws Exception{
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(("[" + sc.getRemoteAddress().toString() + "]").getBytes());
        long bytesRead = -1 ;
        try {
            bytesRead  = sc.read(buf);

        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        if(bytesRead == -1){
            return ;
        }
        buf.flip();
        Charset charset = Charset.forName("UTF-8");
        CharBuffer charBuffer = charset.decode(buf);
        String content = charBuffer.toString();
        System.out.println("[服务端打印,来自:" + sc.getRemoteAddress() + "]" + charBuffer);
        Iterator<Map.Entry<String,SocketChannel>> iter = sockets.entrySet().iterator();
        while(iter.hasNext()){
            buf.clear();
            buf.put(content.getBytes());
            Map.Entry<String,SocketChannel> entry = iter.next();
            if(!entry.getKey().equals(sc.getRemoteAddress().toString())){
                buf.flip();
                entry.getValue().write(buf);
            }

        }
    }
    public void select() throws Exception{
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                if(selector.select(TIMEOUT) == 0){
                    System.out.println("等待可用操作");
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    if(key.isAcceptable()){
                        handleAccept(key);
                    }
                    if(key.isReadable()){
                        handleRead(key);
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
