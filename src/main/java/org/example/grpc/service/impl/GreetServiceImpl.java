package org.example.grpc.service.impl;


import io.grpc.stub.StreamObserver;
import org.example.grpc.*;
import org.example.grpc.service.GreetService;

import java.util.stream.IntStream;

/**
 * example for unary/server/client/bi stream service request and response
 */
public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase implements GreetService {


    /**
     * This method works as unary request and response
     * @param request
     * @param responseObserver
     */
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

    /**
     * This method works as  request and server stream response
     * @param request
     * @param responseObserver
     */
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

    /**
     * This method works as client stream request
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<GreetClientStreamRequest> greetClientStream(StreamObserver<GreetClientStreamResponse> responseObserver) {
        StringBuilder stringBuilder = new StringBuilder();
       StreamObserver<GreetClientStreamRequest>  requestStreamObserver = new StreamObserver<GreetClientStreamRequest>() {
           @Override
           public void onNext(GreetClientStreamRequest value) {
               // read all the request values sent by client
               stringBuilder.append("Hello ").append(value.getGreeting().getLastName()).append(" | ");

           }

           @Override
           public void onError(Throwable t) {

           }

           @Override
           public void onCompleted() {
               // send response once client finished sending the request
               responseObserver.onNext(
                       GreetClientStreamResponse
                               .newBuilder()
                               .setResult(stringBuilder.toString())
                               .build());
               responseObserver.onCompleted();

           }

       };
        return requestStreamObserver;
    }
}
