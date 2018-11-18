package io.grpc.examples;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.student.proto.StudentRequest;
import io.grpc.examples.student.proto.StudentResponse;
import io.grpc.examples.student.proto.StudentServiceGrpc;

public class StudentClient {
    public static void main(String[] args)throws Exception {
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
}
