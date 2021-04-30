package hu.ptomi.mysolution;

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
            try (
                    Socket socket = ss.accept();
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream()
            ) {
                System.out.println("Connected to: " + socket);
                int data;
                while ((data = in.read()) != -1) {
                    out.write(transmogrify(data));
                }
                System.out.println("Disconnected from: " + socket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
