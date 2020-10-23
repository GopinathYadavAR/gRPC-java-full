package org.example.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import org.example.grpc.GreetRequest;
import org.example.grpc.GreetResponse;
import org.example.grpc.GreetServiceGrpc;
import org.example.grpc.service.GreetService;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase implements GreetService {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        super.greet(request, responseObserver);
    }


}
