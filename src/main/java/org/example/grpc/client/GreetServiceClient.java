package org.example.grpc.client;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetServiceClient {
    ManagedChannel channel;

    public GreetServiceClient() {
        // create a channel to communicate the grpc service
        channel = ManagedChannelBuilder.forAddress("localhost", 5001)
                .usePlaintext().build();
    }

    public void callgRPCUnaryService() {
        // create greet service client (blocking, synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetServiceBlockingStub =
                GreetServiceGrpc.newBlockingStub(this.channel);
        // created greet protocol buffer message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("First name")
                .setLastName("Last name")
                .build();

        GreetRequest request = GreetRequest
                .newBuilder()
                .setGreeting(greeting).build();
        // invoke greet service
        GreetResponse response = greetServiceBlockingStub.greet(request);

        System.out.println(response.getResult());
        // shutdown the client
        channel.shutdown();
    }

    public void callgRPCServerStreamService() {
        GreetServiceGrpc.GreetServiceBlockingStub greetServiceBlockingStub =
                GreetServiceGrpc.newBlockingStub(channel);
        // created greet protocol buffer message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("First name")
                .setLastName("Last name")
                .build();

        GreetServerStreamRequest request = GreetServerStreamRequest
                .newBuilder()
                .setGreeting(greeting).build();
        // invoke greet service
        java.util.Iterator<org.example.grpc.GreetServerStreamResponse> responses = greetServiceBlockingStub.greetServerStream(request);
        responses.forEachRemaining(it -> {
            System.out.println(it.getResult());
        });
        // shutdown the client
        channel.shutdown();

    }

    public void callgRPCClientStreamService() throws InterruptedException {
        GreetServiceGrpc.GreetServiceStub asyncClient =  GreetServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetClientStreamRequest> requestStreamObserver=   asyncClient.greetClientStream(new StreamObserver<GreetClientStreamResponse>() {
            @Override
            public void onNext(GreetClientStreamResponse value) {
                // we get a response from server
                // we will get response only once.
              System.out.println("Server Sent the response");
              System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                // server done sending response
                // this method will be called right after onNext()
                System.out.println("Server has completed sending the response");
                latch.countDown();
            }
        });
        requestStreamObserver.onNext( GreetClientStreamRequest.newBuilder().setGreeting(Greeting.newBuilder().setLastName("First name 1").build()).build());
        requestStreamObserver.onNext( GreetClientStreamRequest.newBuilder().setGreeting(Greeting.newBuilder().setLastName("First name 2").build()).build());
        requestStreamObserver.onNext( GreetClientStreamRequest.newBuilder().setGreeting(Greeting.newBuilder().setLastName("First name 3").build()).build());
        requestStreamObserver.onNext( GreetClientStreamRequest.newBuilder().setGreeting(Greeting.newBuilder().setLastName("First name 4").build()).build());
       // tell the server that finish sending request.
        requestStreamObserver.onCompleted();
        latch.await(1L, TimeUnit.SECONDS);
    }


    public static void main(String... arg) throws InterruptedException {

        GreetServiceClient unaryClient = new GreetServiceClient();
        GreetServiceClient serverStreamClient = new GreetServiceClient();
        GreetServiceClient clientStreamClient = new GreetServiceClient();

        unaryClient.callgRPCUnaryService();
        serverStreamClient.callgRPCServerStreamService();
        clientStreamClient.callgRPCClientStreamService();


    }
}
