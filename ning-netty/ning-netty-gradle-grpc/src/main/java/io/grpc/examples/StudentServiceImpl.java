package io.grpc.examples;

import io.grpc.examples.student.proto.StudentRequest;
import io.grpc.examples.student.proto.StudentResponse;
import io.grpc.examples.student.proto.StudentResponseList;
import io.grpc.examples.student.proto.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.Random;

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
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        super.getByAge(request, responseObserver);
    }

    @Override
    public StreamObserver<StudentRequest> getByCity(StreamObserver<StudentResponseList> responseObserver) {
        return super.getByCity(responseObserver);
    }

    @Override
    public StreamObserver<StudentRequest> getByProvince(StreamObserver<StudentResponse> responseObserver) {
        return super.getByProvince(responseObserver);
    }
}
