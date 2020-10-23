#Steps for build gRPC project on Java language 

1. Get latest gRPC dependencies from Github
  a. Add gRPC dependencies ( Go to https://github.com/grpc/grpc-java, Copy dependencies from non-Android section)
      "implementation 'io.grpc:grpc-netty-shaded:1.33.0'
      implementation 'io.grpc:grpc-protobuf:1.33.0'
      implementation 'io.grpc:grpc-stub:1.33.0'
      compileOnly 'org.apache.tomcat:annotations-api:6.0.53' // necessary for Java 9+"
  b. Add protoc and gRPC code generate gradle plugin
      plugins {
          id 'com.google.protobuf' version '0.8.13'
      }
      
      protobuf {
        protoc {
          artifact = "com.google.protobuf:protoc:3.12.0"
        }
        plugins {
          grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java:1.33.0'
          }
        }
        generateProtoTasks {
          all()*.plugins {
            grpc {}
          }
        }
      }
 2. Generate gRPC code 
      run gradle task 'generateProto'
      
 3. Start server
      gRPC has ServerBuilder to build the server object. Create server object with port and start the server and 
      server to wait to shutdown by invoking awaitTermination metho. As we keep server to wait, need to hook the shutdown event
      to capture the shutdown event and shutdownt the server
