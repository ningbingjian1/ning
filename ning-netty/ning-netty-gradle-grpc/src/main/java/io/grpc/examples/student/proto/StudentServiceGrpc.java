package io.grpc.examples.student.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * 服务
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.0)",
    comments = "Source: Student.proto")
public final class StudentServiceGrpc {

  private StudentServiceGrpc() {}

  public static final String SERVICE_NAME = "io.grpc.examples.student.StudentService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetByNameMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> METHOD_GET_BY_NAME = getGetByNameMethod();

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> getGetByNameMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> getGetByNameMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponse> getGetByNameMethod;
    if ((getGetByNameMethod = StudentServiceGrpc.getGetByNameMethod) == null) {
      synchronized (StudentServiceGrpc.class) {
        if ((getGetByNameMethod = StudentServiceGrpc.getGetByNameMethod) == null) {
          StudentServiceGrpc.getGetByNameMethod = getGetByNameMethod = 
              io.grpc.MethodDescriptor.<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "io.grpc.examples.student.StudentService", "getByName"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StudentServiceMethodDescriptorSupplier("getByName"))
                  .build();
          }
        }
     }
     return getGetByNameMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetByAgeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> METHOD_GET_BY_AGE = getGetByAgeMethod();

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> getGetByAgeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> getGetByAgeMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponse> getGetByAgeMethod;
    if ((getGetByAgeMethod = StudentServiceGrpc.getGetByAgeMethod) == null) {
      synchronized (StudentServiceGrpc.class) {
        if ((getGetByAgeMethod = StudentServiceGrpc.getGetByAgeMethod) == null) {
          StudentServiceGrpc.getGetByAgeMethod = getGetByAgeMethod = 
              io.grpc.MethodDescriptor.<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "io.grpc.examples.student.StudentService", "getByAge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StudentServiceMethodDescriptorSupplier("getByAge"))
                  .build();
          }
        }
     }
     return getGetByAgeMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetByCityMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponseList> METHOD_GET_BY_CITY = getGetByCityMethod();

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponseList> getGetByCityMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponseList> getGetByCityMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponseList> getGetByCityMethod;
    if ((getGetByCityMethod = StudentServiceGrpc.getGetByCityMethod) == null) {
      synchronized (StudentServiceGrpc.class) {
        if ((getGetByCityMethod = StudentServiceGrpc.getGetByCityMethod) == null) {
          StudentServiceGrpc.getGetByCityMethod = getGetByCityMethod = 
              io.grpc.MethodDescriptor.<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponseList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "io.grpc.examples.student.StudentService", "getByCity"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentResponseList.getDefaultInstance()))
                  .setSchemaDescriptor(new StudentServiceMethodDescriptorSupplier("getByCity"))
                  .build();
          }
        }
     }
     return getGetByCityMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetByProvinceMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> METHOD_GET_BY_PROVINCE = getGetByProvinceMethod();

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> getGetByProvinceMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest,
      io.grpc.examples.student.proto.StudentResponse> getGetByProvinceMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponse> getGetByProvinceMethod;
    if ((getGetByProvinceMethod = StudentServiceGrpc.getGetByProvinceMethod) == null) {
      synchronized (StudentServiceGrpc.class) {
        if ((getGetByProvinceMethod = StudentServiceGrpc.getGetByProvinceMethod) == null) {
          StudentServiceGrpc.getGetByProvinceMethod = getGetByProvinceMethod = 
              io.grpc.MethodDescriptor.<io.grpc.examples.student.proto.StudentRequest, io.grpc.examples.student.proto.StudentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "io.grpc.examples.student.StudentService", "getByProvince"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.student.proto.StudentResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StudentServiceMethodDescriptorSupplier("getByProvince"))
                  .build();
          }
        }
     }
     return getGetByProvinceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StudentServiceStub newStub(io.grpc.Channel channel) {
    return new StudentServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StudentServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StudentServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StudentServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StudentServiceFutureStub(channel);
  }

  /**
   * <pre>
   * 服务
   * </pre>
   */
  public static abstract class StudentServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * 一次请求  一次响应
     * </pre>
     */
    public void getByName(io.grpc.examples.student.proto.StudentRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetByNameMethod(), responseObserver);
    }

    /**
     * <pre>
     * 一次请求 流式响应
     * </pre>
     */
    public void getByAge(io.grpc.examples.student.proto.StudentRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetByAgeMethod(), responseObserver);
    }

    /**
     * <pre>
     * 流式请求 一次响应
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentRequest> getByCity(
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponseList> responseObserver) {
      return asyncUnimplementedStreamingCall(getGetByCityMethod(), responseObserver);
    }

    /**
     * <pre>
     *流式请求 流式响应
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentRequest> getByProvince(
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getGetByProvinceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetByNameMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.grpc.examples.student.proto.StudentRequest,
                io.grpc.examples.student.proto.StudentResponse>(
                  this, METHODID_GET_BY_NAME)))
          .addMethod(
            getGetByAgeMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                io.grpc.examples.student.proto.StudentRequest,
                io.grpc.examples.student.proto.StudentResponse>(
                  this, METHODID_GET_BY_AGE)))
          .addMethod(
            getGetByCityMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                io.grpc.examples.student.proto.StudentRequest,
                io.grpc.examples.student.proto.StudentResponseList>(
                  this, METHODID_GET_BY_CITY)))
          .addMethod(
            getGetByProvinceMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                io.grpc.examples.student.proto.StudentRequest,
                io.grpc.examples.student.proto.StudentResponse>(
                  this, METHODID_GET_BY_PROVINCE)))
          .build();
    }
  }

  /**
   * <pre>
   * 服务
   * </pre>
   */
  public static final class StudentServiceStub extends io.grpc.stub.AbstractStub<StudentServiceStub> {
    private StudentServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StudentServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StudentServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StudentServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 一次请求  一次响应
     * </pre>
     */
    public void getByName(io.grpc.examples.student.proto.StudentRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetByNameMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 一次请求 流式响应
     * </pre>
     */
    public void getByAge(io.grpc.examples.student.proto.StudentRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetByAgeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 流式请求 一次响应
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentRequest> getByCity(
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponseList> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getGetByCityMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *流式请求 流式响应
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentRequest> getByProvince(
        io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getGetByProvinceMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * 服务
   * </pre>
   */
  public static final class StudentServiceBlockingStub extends io.grpc.stub.AbstractStub<StudentServiceBlockingStub> {
    private StudentServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StudentServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StudentServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StudentServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 一次请求  一次响应
     * </pre>
     */
    public io.grpc.examples.student.proto.StudentResponse getByName(io.grpc.examples.student.proto.StudentRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetByNameMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 一次请求 流式响应
     * </pre>
     */
    public java.util.Iterator<io.grpc.examples.student.proto.StudentResponse> getByAge(
        io.grpc.examples.student.proto.StudentRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetByAgeMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * 服务
   * </pre>
   */
  public static final class StudentServiceFutureStub extends io.grpc.stub.AbstractStub<StudentServiceFutureStub> {
    private StudentServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StudentServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StudentServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StudentServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 一次请求  一次响应
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.student.proto.StudentResponse> getByName(
        io.grpc.examples.student.proto.StudentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetByNameMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_BY_NAME = 0;
  private static final int METHODID_GET_BY_AGE = 1;
  private static final int METHODID_GET_BY_CITY = 2;
  private static final int METHODID_GET_BY_PROVINCE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StudentServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StudentServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_BY_NAME:
          serviceImpl.getByName((io.grpc.examples.student.proto.StudentRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse>) responseObserver);
          break;
        case METHODID_GET_BY_AGE:
          serviceImpl.getByAge((io.grpc.examples.student.proto.StudentRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_BY_CITY:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.getByCity(
              (io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponseList>) responseObserver);
        case METHODID_GET_BY_PROVINCE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.getByProvince(
              (io.grpc.stub.StreamObserver<io.grpc.examples.student.proto.StudentResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StudentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StudentServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.grpc.examples.student.proto.StudentProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StudentService");
    }
  }

  private static final class StudentServiceFileDescriptorSupplier
      extends StudentServiceBaseDescriptorSupplier {
    StudentServiceFileDescriptorSupplier() {}
  }

  private static final class StudentServiceMethodDescriptorSupplier
      extends StudentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StudentServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StudentServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StudentServiceFileDescriptorSupplier())
              .addMethod(getGetByNameMethod())
              .addMethod(getGetByAgeMethod())
              .addMethod(getGetByCityMethod())
              .addMethod(getGetByProvinceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
