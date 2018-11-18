package io.grpc.examples;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.student.proto.StudentRequest;
import io.grpc.examples.student.proto.StudentResponse;
import io.grpc.examples.student.proto.StudentResponseList;
import io.grpc.examples.student.proto.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Iterator;

public class StudentClient {
    public static void main(String[] args)throws Exception {
        //testGetByNames();
        //testGetByAge();
        //testGetByCity();
        return;

    }

    private static void testGetByNames() throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8899 )
                .usePlaintext(true)
                .build();
        StudentServiceGrpc.StudentServiceBlockingStub studentServiceBlockingStub = StudentServiceGrpc
                .newBlockingStub(managedChannel);
        StudentResponse response =
                studentServiceBlockingStub.getByName(StudentRequest.newBuilder().setName("NAME-client").build());
        System.out.println(response);
        System.in.read();
    }
    private static void testGetByAge()throws Exception{
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8899 )
                .usePlaintext(true)
                .build();
        StudentServiceGrpc.StudentServiceBlockingStub studentServiceBlockingStub = StudentServiceGrpc
                .newBlockingStub(managedChannel);
        Iterator<StudentResponse> responses =
                studentServiceBlockingStub.getByAge(StudentRequest.newBuilder().setName("client-u1").setAge(1).build());
        while(responses.hasNext()){
            System.out.println(responses.next());
        }
        System.in.read();
    }
    public static void testGetByCity() throws Exception{
        StreamObserver<StudentResponseList> responseObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList value) {
                value.getStudentResponseList().forEach(response -> {
                    System.out.println(response);
                });
            }
            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        };
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8899 )
                .usePlaintext(true)
                .build();
        StudentServiceGrpc.StudentServiceStub stub = StudentServiceGrpc.newStub(managedChannel);
        StreamObserver<StudentRequest> requestStreamObserver = stub.getByCity(responseObserver);
        requestStreamObserver.onNext(StudentRequest.newBuilder().setName("u1").setCity("city1").build());
        requestStreamObserver.onNext(StudentRequest.newBuilder().setName("u2").setCity("city2").build());
        requestStreamObserver.onNext(StudentRequest.newBuilder().setName("u3").setCity("city3").build());
        requestStreamObserver.onCompleted();
        System.in.read();
    }
}
