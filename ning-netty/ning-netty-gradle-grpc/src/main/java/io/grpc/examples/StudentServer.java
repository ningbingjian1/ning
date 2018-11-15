package io.grpc.examples;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class StudentServer {
    private Server server;
    private void start() throws IOException{
        this.server = ServerBuilder.forPort(8899)
                .addService(new StudentServiceImpl())
                .build()
                .start();
    }
    private void awaitTemination() throws InterruptedException {
        if(null != this.server){
            this.server.awaitTermination();
        }
    }
    public static void main(String[] args) throws Exception{
        StudentServer server = new StudentServer();
        server.start();
        server.awaitTemination();
    }
}
