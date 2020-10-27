package org.example.grpc.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.*;

public class GreetServiceClient {

    public static void main(String... arg){
        // create a channel to communicate the grpc service
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",5001)
                .usePlaintext().build();
        // create greet service client (blocking, synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetServiceBlockingStub =
                GreetServiceGrpc.newBlockingStub(channel);
        // created greet protocol buffer message
        Greeting greeting = Greeting.newBuilder()
                                .setFirstName("First name")
                                .setLastName("Last name")
                                .build();

        GreetRequest request = GreetRequest
                                .newBuilder()
                                .setGreeting(greeting).build();
        // invoke greet service
       GreetResponse  response = greetServiceBlockingStub.greet(request);

       System.out.println(response.getResult());

       // shutdown the client
        channel.shutdown();
    }
}
