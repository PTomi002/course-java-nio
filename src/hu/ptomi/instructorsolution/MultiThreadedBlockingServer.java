package hu.ptomi.instructorsolution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        UncheckedHandler<Socket> handler =
                new ThreadedHandler<>(
                        new UncheckedHandler<>(
                                new PrintingHandler<>(
                                        new TransmogrifyHandler()
                                )
                        )
                );
        while (true) {
            Socket socket = ss.accept();
            handler.handle(socket);
        }
    }
}