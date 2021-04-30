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
 * Connected to: Socket[addr=/0:0:0:0:0:0:0:1,port=51477,localport=8080]
 * 2nd shell: blocked until the first session is closed.
 * 1st shell closed.
 * Disconnected from: Socket[addr=/0:0:0:0:0:0:0:1,port=51477,localport=8080]
 * Connected to: Socket[addr=/0:0:0:0:0:0:0:1,port=51482,localport=8080]
 * 2nd shell closed.
 * Disconnected from: Socket[addr=/0:0:0:0:0:0:0:1,port=51482,localport=8080]
 * <p>
 * Socket End-Of-File:
 * An end-of-file error typically means the other side of the socket has closed their connection.
 * The reason it's end-of-file is that at a very low level within a program,
 * a file on the disk and a socket are both represented with a number -- a file descriptor --
 * that the OS translates into the object representing a file or socket or pipe or whatever.
 */
public class SingleThreadedBlockingServer {

    // Exercise: refactor this version to use try-with-resource pattern.
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket socket = ss.accept();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            int data;
            while ((data = in.read()) != -1) {
                out.write(transmogrify(data));
            }
            in.close();
            out.close();
            ss.close();
        }
    }

}