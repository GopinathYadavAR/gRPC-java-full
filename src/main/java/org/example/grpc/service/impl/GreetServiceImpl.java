package org.example.grpc.service.impl;


import io.grpc.stub.StreamObserver;
import org.example.grpc.GreetRequest;
import org.example.grpc.GreetResponse;
import org.example.grpc.GreetServiceGrpc;
import org.example.grpc.Greeting;
import org.example.grpc.service.GreetService;

/**
 * example for unary service request and response
 */
public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase implements GreetService {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        GreetResponse response = GreetResponse.newBuilder()
                    .setResult("Hello "+ greeting.getFirstName() + " " + greeting.getLastName())
                .build();
          responseObserver.onNext(response);
          responseObserver.onCompleted();
    }


}
