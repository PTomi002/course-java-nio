package hu.ptomi.mysolution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket socket = ss.accept();
            new TransMogrifySocketDecorator(new TransmogrifySocketImpl(socket)).listen();
        }
    }

}