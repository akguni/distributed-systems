package flynn.HA4.code.generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class DatabaseGrpc {

  private DatabaseGrpc() {}

  public static final java.lang.String SERVICE_NAME = "Database";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<flynn.HA4.code.generated.GetRecordRequest,
      flynn.HA4.code.generated.GetRecordResponse> getGetRecordMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRecord",
      requestType = flynn.HA4.code.generated.GetRecordRequest.class,
      responseType = flynn.HA4.code.generated.GetRecordResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<flynn.HA4.code.generated.GetRecordRequest,
      flynn.HA4.code.generated.GetRecordResponse> getGetRecordMethod() {
    io.grpc.MethodDescriptor<flynn.HA4.code.generated.GetRecordRequest, flynn.HA4.code.generated.GetRecordResponse> getGetRecordMethod;
    if ((getGetRecordMethod = DatabaseGrpc.getGetRecordMethod) == null) {
      synchronized (DatabaseGrpc.class) {
        if ((getGetRecordMethod = DatabaseGrpc.getGetRecordMethod) == null) {
          DatabaseGrpc.getGetRecordMethod = getGetRecordMethod =
              io.grpc.MethodDescriptor.<flynn.HA4.code.generated.GetRecordRequest, flynn.HA4.code.generated.GetRecordResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRecord"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  flynn.HA4.code.generated.GetRecordRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  flynn.HA4.code.generated.GetRecordResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DatabaseMethodDescriptorSupplier("GetRecord"))
              .build();
        }
      }
    }
    return getGetRecordMethod;
  }

  private static volatile io.grpc.MethodDescriptor<flynn.HA4.code.generated.AddRecordRequest,
      flynn.HA4.code.generated.AddRecordResponse> getAddRecordMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddRecord",
      requestType = flynn.HA4.code.generated.AddRecordRequest.class,
      responseType = flynn.HA4.code.generated.AddRecordResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<flynn.HA4.code.generated.AddRecordRequest,
      flynn.HA4.code.generated.AddRecordResponse> getAddRecordMethod() {
    io.grpc.MethodDescriptor<flynn.HA4.code.generated.AddRecordRequest, flynn.HA4.code.generated.AddRecordResponse> getAddRecordMethod;
    if ((getAddRecordMethod = DatabaseGrpc.getAddRecordMethod) == null) {
      synchronized (DatabaseGrpc.class) {
        if ((getAddRecordMethod = DatabaseGrpc.getAddRecordMethod) == null) {
          DatabaseGrpc.getAddRecordMethod = getAddRecordMethod =
              io.grpc.MethodDescriptor.<flynn.HA4.code.generated.AddRecordRequest, flynn.HA4.code.generated.AddRecordResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddRecord"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  flynn.HA4.code.generated.AddRecordRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  flynn.HA4.code.generated.AddRecordResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DatabaseMethodDescriptorSupplier("AddRecord"))
              .build();
        }
      }
    }
    return getAddRecordMethod;
  }

  private static volatile io.grpc.MethodDescriptor<flynn.HA4.code.generated.GetSizeRequest,
      flynn.HA4.code.generated.GetSizeResponse> getGetSizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSize",
      requestType = flynn.HA4.code.generated.GetSizeRequest.class,
      responseType = flynn.HA4.code.generated.GetSizeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<flynn.HA4.code.generated.GetSizeRequest,
      flynn.HA4.code.generated.GetSizeResponse> getGetSizeMethod() {
    io.grpc.MethodDescriptor<flynn.HA4.code.generated.GetSizeRequest, flynn.HA4.code.generated.GetSizeResponse> getGetSizeMethod;
    if ((getGetSizeMethod = DatabaseGrpc.getGetSizeMethod) == null) {
      synchronized (DatabaseGrpc.class) {
        if ((getGetSizeMethod = DatabaseGrpc.getGetSizeMethod) == null) {
          DatabaseGrpc.getGetSizeMethod = getGetSizeMethod =
              io.grpc.MethodDescriptor.<flynn.HA4.code.generated.GetSizeRequest, flynn.HA4.code.generated.GetSizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  flynn.HA4.code.generated.GetSizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  flynn.HA4.code.generated.GetSizeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DatabaseMethodDescriptorSupplier("GetSize"))
              .build();
        }
      }
    }
    return getGetSizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DatabaseStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DatabaseStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DatabaseStub>() {
        @java.lang.Override
        public DatabaseStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DatabaseStub(channel, callOptions);
        }
      };
    return DatabaseStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static DatabaseBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DatabaseBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DatabaseBlockingV2Stub>() {
        @java.lang.Override
        public DatabaseBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DatabaseBlockingV2Stub(channel, callOptions);
        }
      };
    return DatabaseBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DatabaseBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DatabaseBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DatabaseBlockingStub>() {
        @java.lang.Override
        public DatabaseBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DatabaseBlockingStub(channel, callOptions);
        }
      };
    return DatabaseBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DatabaseFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DatabaseFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DatabaseFutureStub>() {
        @java.lang.Override
        public DatabaseFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DatabaseFutureStub(channel, callOptions);
        }
      };
    return DatabaseFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getRecord(flynn.HA4.code.generated.GetRecordRequest request,
        io.grpc.stub.StreamObserver<flynn.HA4.code.generated.GetRecordResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRecordMethod(), responseObserver);
    }

    /**
     */
    default void addRecord(flynn.HA4.code.generated.AddRecordRequest request,
        io.grpc.stub.StreamObserver<flynn.HA4.code.generated.AddRecordResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddRecordMethod(), responseObserver);
    }

    /**
     */
    default void getSize(flynn.HA4.code.generated.GetSizeRequest request,
        io.grpc.stub.StreamObserver<flynn.HA4.code.generated.GetSizeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSizeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Database.
   */
  public static abstract class DatabaseImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DatabaseGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Database.
   */
  public static final class DatabaseStub
      extends io.grpc.stub.AbstractAsyncStub<DatabaseStub> {
    private DatabaseStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DatabaseStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DatabaseStub(channel, callOptions);
    }

    /**
     */
    public void getRecord(flynn.HA4.code.generated.GetRecordRequest request,
        io.grpc.stub.StreamObserver<flynn.HA4.code.generated.GetRecordResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRecordMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addRecord(flynn.HA4.code.generated.AddRecordRequest request,
        io.grpc.stub.StreamObserver<flynn.HA4.code.generated.AddRecordResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddRecordMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSize(flynn.HA4.code.generated.GetSizeRequest request,
        io.grpc.stub.StreamObserver<flynn.HA4.code.generated.GetSizeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSizeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Database.
   */
  public static final class DatabaseBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<DatabaseBlockingV2Stub> {
    private DatabaseBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DatabaseBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DatabaseBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public flynn.HA4.code.generated.GetRecordResponse getRecord(flynn.HA4.code.generated.GetRecordRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetRecordMethod(), getCallOptions(), request);
    }

    /**
     */
    public flynn.HA4.code.generated.AddRecordResponse addRecord(flynn.HA4.code.generated.AddRecordRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getAddRecordMethod(), getCallOptions(), request);
    }

    /**
     */
    public flynn.HA4.code.generated.GetSizeResponse getSize(flynn.HA4.code.generated.GetSizeRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetSizeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service Database.
   */
  public static final class DatabaseBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DatabaseBlockingStub> {
    private DatabaseBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DatabaseBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DatabaseBlockingStub(channel, callOptions);
    }

    /**
     */
    public flynn.HA4.code.generated.GetRecordResponse getRecord(flynn.HA4.code.generated.GetRecordRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRecordMethod(), getCallOptions(), request);
    }

    /**
     */
    public flynn.HA4.code.generated.AddRecordResponse addRecord(flynn.HA4.code.generated.AddRecordRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddRecordMethod(), getCallOptions(), request);
    }

    /**
     */
    public flynn.HA4.code.generated.GetSizeResponse getSize(flynn.HA4.code.generated.GetSizeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSizeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Database.
   */
  public static final class DatabaseFutureStub
      extends io.grpc.stub.AbstractFutureStub<DatabaseFutureStub> {
    private DatabaseFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DatabaseFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DatabaseFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<flynn.HA4.code.generated.GetRecordResponse> getRecord(
        flynn.HA4.code.generated.GetRecordRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRecordMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<flynn.HA4.code.generated.AddRecordResponse> addRecord(
        flynn.HA4.code.generated.AddRecordRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddRecordMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<flynn.HA4.code.generated.GetSizeResponse> getSize(
        flynn.HA4.code.generated.GetSizeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSizeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_RECORD = 0;
  private static final int METHODID_ADD_RECORD = 1;
  private static final int METHODID_GET_SIZE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_RECORD:
          serviceImpl.getRecord((flynn.HA4.code.generated.GetRecordRequest) request,
              (io.grpc.stub.StreamObserver<flynn.HA4.code.generated.GetRecordResponse>) responseObserver);
          break;
        case METHODID_ADD_RECORD:
          serviceImpl.addRecord((flynn.HA4.code.generated.AddRecordRequest) request,
              (io.grpc.stub.StreamObserver<flynn.HA4.code.generated.AddRecordResponse>) responseObserver);
          break;
        case METHODID_GET_SIZE:
          serviceImpl.getSize((flynn.HA4.code.generated.GetSizeRequest) request,
              (io.grpc.stub.StreamObserver<flynn.HA4.code.generated.GetSizeResponse>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetRecordMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              flynn.HA4.code.generated.GetRecordRequest,
              flynn.HA4.code.generated.GetRecordResponse>(
                service, METHODID_GET_RECORD)))
        .addMethod(
          getAddRecordMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              flynn.HA4.code.generated.AddRecordRequest,
              flynn.HA4.code.generated.AddRecordResponse>(
                service, METHODID_ADD_RECORD)))
        .addMethod(
          getGetSizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              flynn.HA4.code.generated.GetSizeRequest,
              flynn.HA4.code.generated.GetSizeResponse>(
                service, METHODID_GET_SIZE)))
        .build();
  }

  private static abstract class DatabaseBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DatabaseBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return flynn.HA4.code.generated.DatabaseOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Database");
    }
  }

  private static final class DatabaseFileDescriptorSupplier
      extends DatabaseBaseDescriptorSupplier {
    DatabaseFileDescriptorSupplier() {}
  }

  private static final class DatabaseMethodDescriptorSupplier
      extends DatabaseBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DatabaseMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DatabaseGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DatabaseFileDescriptorSupplier())
              .addMethod(getGetRecordMethod())
              .addMethod(getAddRecordMethod())
              .addMethod(getGetSizeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
