package io.grpc.examples.student;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by ning on 2018/2/12.
 * User:ning
 * Date:2018/2/12
 * tIME:16:42
 */
public class StudentClient {
    private final  ManagedChannel channel;
    //阻塞式
    private final  StudentServiceGrpc.StudentServiceBlockingStub blockingStub;
    //非阻塞式
    private final StudentServiceGrpc.StudentServiceStub asyncStub ;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public StudentClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true)
                .build());
    }
    /** Construct client for accessing RouteGuide server using the existing channel. */
    StudentClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = StudentServiceGrpc.newBlockingStub(channel);
        asyncStub = StudentServiceGrpc.newStub(channel);
    }
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
    public void testGetByName() throws InterruptedException {
        StudentRequest request = StudentUtil.buildRandomRequest();
        StudentResponse response = blockingStub.getByName(request);
        System.out.println(response);
        this.stop();

    }
    public void testGetByAge() throws InterruptedException {
        StudentRequest request = StudentUtil.buildRandomRequest();
        Iterator<StudentResponse> responseIterator = blockingStub.getByAge(request);
        System.out.println("client[testGetByAge],收到响应:");
        while(responseIterator.hasNext()){
            System.out.println(responseIterator.next());
        }
        this.stop();


    }
    public void testGetByCity() throws InterruptedException {
        StreamObserver<StudentResponseList> responseStreamObserver =
                new StreamObserver<StudentResponseList>() {
                    @Override
                    public void onNext(StudentResponseList value) {
                        value.getStudentResponseList().forEach(response -> {
                            System.out.println("client[getByCity],response = " + response);
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("client[onCompleted]");
                    }
                };
        StreamObserver<StudentRequest> requestStreamObserver = asyncStub.getByCity(responseStreamObserver);
        requestStreamObserver.onNext(StudentUtil.buildRandomRequest());
        requestStreamObserver.onNext(StudentUtil.buildRandomRequest());
        requestStreamObserver.onNext(StudentUtil.buildRandomRequest());
        requestStreamObserver.onCompleted();
        Thread.sleep(10000);
        this.stop();
    }

    public void testGetByProvince() throws InterruptedException {
        StreamObserver<StudentResponse> responseStreamObserver =
                new StreamObserver<StudentResponse>() {
                    @Override
                    public void onNext(StudentResponse value) {
                            System.out.println("client[getByProvince],response = " + value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("client[getByProvince] onCompleted");
                    }
                };
        StreamObserver<StudentRequest> requestStreamObserver = asyncStub.getByProvince(responseStreamObserver);
        requestStreamObserver.onNext(StudentUtil.buildRandomRequest());
        requestStreamObserver.onNext(StudentUtil.buildRandomRequest());
        requestStreamObserver.onNext(StudentUtil.buildRandomRequest());
        requestStreamObserver.onCompleted();
        Thread.sleep(10000);
        this.stop();
    }
    public void stop() throws InterruptedException {
        channel.shutdown().awaitTermination(3000,TimeUnit.SECONDS);
    }
    public static void main(String[] args) throws InterruptedException {
        StudentClient client = new StudentClient("localhost",9981);
        //client.testGetByName();
        //client.testGetByAge();
        //client.testGetByCity();
        client.testGetByProvince();
    }





}
