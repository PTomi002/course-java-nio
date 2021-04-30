package hu.ptomi.notrefactored;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static hu.ptomi.util.Utils.transmogrify;

/**
 * Connect from more telnet instance (PuTTY):
 * 1st shell: can connect, communication works.
 * 2nd shell: can connect, communication works.
 */
public class MultiThreadedBlockingServer {

    // Exercise:
    // 1) Split the handle into two methods using Decorator Pattern.
    // 2) And handle the printing methods in an other method/class.
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket socket = ss.accept();
            handle(socket);
        }
    }

    public static void handle(Socket socket) {
        new Thread(() -> {
            System.out.println("Connected to: " + socket);
            try (
                    socket;
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream()
            ) {
                int data;
                while ((data = in.read()) != -1) {
                    out.write(transmogrify(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Disconnected from: " + socket);
            }
        }).start();
    }

}