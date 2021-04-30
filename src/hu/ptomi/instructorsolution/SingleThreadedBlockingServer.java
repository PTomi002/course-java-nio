package hu.ptomi.instructorsolution;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static hu.ptomi.util.Utils.transmogrify;

public class SingleThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket socket = ss.accept();
            handle(socket);
        }
    }

    public static void handle(Socket s) throws IOException {
        System.out.println("Connected to: " + s);
        try (s;
             InputStream in = s.getInputStream();
             OutputStream out = s.getOutputStream()
        ) {
            int data;
            while ((data = in.read()) != -1) {
                out.write(transmogrify(data));
            }
        }
        System.out.println("Disconnected from: " + s);
    }

}