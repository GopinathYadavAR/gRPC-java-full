package org.example.grpc.client;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.*;

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


    public static void main(String... arg) throws InterruptedException {

        GreetServiceClient unaryClient = new GreetServiceClient();
        GreetServiceClient serverStreamClient = new GreetServiceClient();
        unaryClient.callgRPCUnaryService();
        serverStreamClient.callgRPCServerStreamService();


    }
}
