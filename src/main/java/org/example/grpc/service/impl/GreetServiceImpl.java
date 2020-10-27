package org.example.grpc.service.impl;


import io.grpc.stub.StreamObserver;
import org.example.grpc.*;
import org.example.grpc.service.GreetService;

import java.util.stream.IntStream;

/**
 * example for unary/server stream service request and response
 */
public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase implements GreetService {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        GreetResponse response = GreetResponse.newBuilder()
                .setResult("Hello " + greeting.getFirstName() + " " + greeting.getLastName())
                .build();

        // send the response
        responseObserver.onNext(response);
        // complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void greetServerStream(GreetServerStreamRequest request, StreamObserver<GreetServerStreamResponse> responseObserver) {

        try {
            for (int i = 0; i <= 10; i++) {

                GreetServerStreamResponse response = GreetServerStreamResponse.newBuilder()
                        .setResult("Response = " + i).build();
                // keep send the response to client as Stream mode
                responseObserver.onNext(response);

                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // complete the RPC Call
        responseObserver.onCompleted();

    }
}
