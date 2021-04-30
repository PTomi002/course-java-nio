package hu.ptomi.instructorsolution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * With NastyChump attack started, we only have 10 threads, others are in the queue of the executor service.
 * Connected to: Socket[addr=/0:0:0:0:0:0:0:1,port=52057,localport=8080] handler: pool-1-thread-1
 * Connected to: Socket[addr=/127.0.0.1,port=52060,localport=8080] handler: pool-1-thread-2
 * Connected to: Socket[addr=/127.0.0.1,port=52061,localport=8080] handler: pool-1-thread-3
 * Connected to: Socket[addr=/127.0.0.1,port=52062,localport=8080] handler: pool-1-thread-4
 * Connected to: Socket[addr=/127.0.0.1,port=52063,localport=8080] handler: pool-1-thread-5
 * Connected to: Socket[addr=/127.0.0.1,port=52067,localport=8080] handler: pool-1-thread-9
 * Connected to: Socket[addr=/127.0.0.1,port=52066,localport=8080] handler: pool-1-thread-8
 * Connected to: Socket[addr=/127.0.0.1,port=52064,localport=8080] handler: pool-1-thread-6
 * Connected to: Socket[addr=/127.0.0.1,port=52065,localport=8080] handler: pool-1-thread-7
 * Connected to: Socket[addr=/127.0.0.1,port=52068,localport=8080] handler: pool-1-thread-10
 * // Disconnected from PuTTY
 * Disconnected from: Socket[addr=/0:0:0:0:0:0:0:1,port=52057,localport=8080] handler: pool-1-thread-1
 * // Next in the queue can join.
 * Connected to: Socket[addr=/127.0.0.1,port=52069,localport=8080] handler: pool-1-thread-1
 * <p>
 * OUTCOME:
 * + This does not solves our problem, that if we disconnect and reconnect we have to wait in our time in the queue to come.
 * + The waiting time can be long enough to drop the request.
 */
public class ExecutorServiceBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        Handler<Socket> handler =
                new ExecutorServiceHandler<>(
                        new PrintingHandler<>(
                                new TransmogrifyHandler()
                        ),
//                        Executors.newCachedThreadPool(),
                        Executors.newFixedThreadPool(10),
                        (t, e) -> System.err.println("Unhandled error: " + e + " in:" + Thread.currentThread().getName())
                );
        while (true) {
            Socket socket = ss.accept();
            handler.handle(socket);
        }
    }
}
