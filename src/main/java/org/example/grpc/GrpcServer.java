package org.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String... str) throws InterruptedException, IOException {
        // create grpc server with port 5001
        Server grpcServer = ServerBuilder.forPort(5001)
                .build();
        grpcServer.start();

        // hook shutdown event to capture and terminate the server
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            System.out.println("Server Shutdown started");
            grpcServer.shutdown();
            System.out.println("Server Shutdown competed");

        }));
        System.out.println("Server Started Successfully");
        grpcServer.awaitTermination();


    }
}
