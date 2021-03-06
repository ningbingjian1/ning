package io.grpc.examples;

import io.grpc.examples.student.proto.StudentRequest;
import io.grpc.examples.student.proto.StudentResponse;
import io.grpc.examples.student.proto.StudentResponseList;
import io.grpc.examples.student.proto.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.util.Random;
import io.grpc.examples.student.proto.StudentRequest;
import io.grpc.examples.student.proto.StudentResponse;
import io.grpc.examples.student.proto.StudentResponseList;
import io.grpc.examples.student.proto.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
    @Override
    public void getByName(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("getByName:" + request);
        Random random = new Random();
        int i = random.nextInt();
        StudentResponse response = StudentResponse.newBuilder()
                .setName("name" + i)
                .setAge(i)
                .setCity("city" + i )
                .build();
        //响应客户端请求
        responseObserver.onNext(response);
        //表示本次请求生效
        responseObserver.onCompleted();
    }

    @Override
    public void getByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println(request.getName() + "," + request.getAge());
        StudentResponse response1 = StudentResponse.newBuilder().setName("u1").build();
        StudentResponse response2 = StudentResponse.newBuilder().setName("u2").build();
        responseObserver.onNext(response1);
        responseObserver.onNext(response2);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getByCity(StreamObserver<StudentResponseList> responseObserver) {
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest value) {
                System.out.println(" receive client request : " + value);
            }
            @Override
            public void onError(Throwable t) {
                System.out.println("client error  : ");
            }
            @Override
            public void onCompleted() {
                System.out.println("client onCompleted");
                StudentResponse response1 = StudentResponse.newBuilder().setName("u1").setAge(100).build();
                StudentResponse response2 = StudentResponse.newBuilder().setName("u2").setAge(100).build();
                StudentResponseList list = StudentResponseList.newBuilder()
                        .addStudentResponse(response1)
                        .addStudentResponse( response2)
                        .build();
                responseObserver.onNext(list);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StudentRequest> getByProvince(StreamObserver<StudentResponse> responseObserver) {
        final AtomicInteger COUNTER = new AtomicInteger(0);
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest value) {
                System.out.println(" receive client request : " + value);
                int i = COUNTER.incrementAndGet();
                StudentResponse response  = StudentResponse.newBuilder()
                        .setName("u" + i)
                        .setProvince("province" + i)
                        .build();
                 responseObserver.onNext(response);
                System.out.println();

            }
            @Override
            public void onError(Throwable t) {
                System.out.println("client error  : ");
            }
            @Override
            public void onCompleted() {
                System.out.println("client onCompleted");
                responseObserver.onCompleted();
            }
        };
    }
}

