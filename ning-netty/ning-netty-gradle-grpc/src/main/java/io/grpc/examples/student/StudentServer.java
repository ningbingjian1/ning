package io.grpc.examples.student;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Created by ning on 2018/2/12.
 * User:ning
 * Date:2018/2/12
 * tIME:16:43
 */
public class StudentServer {
    private Server server;
    private void start() throws IOException {
        this.server = ServerBuilder.forPort(9981).addService(new StudentServiceImpl()).build().start();
        System.out.println("服务端启动");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("jvm 被关闭");

        }));
    }
    private void stop(){
        if(null != server){
            server.shutdown();
        }
    }
    private void awaitTermination() throws InterruptedException {
        if(null != server){
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        StudentServer server = new StudentServer();
        server.start();
        server.awaitTermination();
    }
    public static class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase{
        // 一次请求 一次响应
        @Override
        public void getByName(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
            System.out.println("server[getByName],request:" + request);
            responseObserver.onNext(StudentUtil.buildRandomResponse());
            responseObserver.onCompleted();;
        }

        //一次请求 流式响应
        @Override
        public void getByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
            System.out.println("server[getByAge],request:" + request);
            responseObserver.onNext(StudentUtil.buildRandomResponse());
            responseObserver.onNext(StudentUtil.buildRandomResponse());
            responseObserver.onCompleted();;

        }

        @Override
        public StreamObserver<StudentRequest> getByCity(StreamObserver<StudentResponseList> responseObserver) {
            return new StreamObserver<StudentRequest>() {
                @Override
                public void onNext(StudentRequest value) {
                    System.out.println("getByCity,request = " + value);
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    StudentResponse response1 = StudentUtil.buildRandomResponse();
                    StudentResponse response2 = StudentUtil.buildRandomResponse();
                    StudentResponseList list = StudentResponseList.newBuilder()
                            .addStudentResponse(response1)
                            .addStudentResponse(response2)
                            .build();
                    responseObserver.onNext(list);
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<StudentRequest> getByProvince(StreamObserver<StudentResponse> responseObserver) {
            return new StreamObserver<StudentRequest>() {
                @Override
                public void onNext(StudentRequest value) {
                    System.out.println("server[getByProvince]request = " + value);
                    StudentResponse response1 = StudentUtil.buildRandomResponse();
                    responseObserver.onNext(response1);
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    System.out.println("server[getByProvince]:onCompleted");
                    responseObserver.onCompleted();
                }
            };
        }
    }

}
