package org.example.grpc.service;

import io.grpc.stub.StreamObserver;
import org.example.grpc.GreetRequest;
import org.example.grpc.GreetResponse;

public interface GreetService {
    void greet(GreetRequest greetRequest, StreamObserver<GreetResponse> responseObserver);
}
