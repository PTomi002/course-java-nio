package hu.ptomi.instructorsolution;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static hu.ptomi.util.Utils.transmogrify;

public class TransmogrifyHandler implements Handler<Socket> {
    @Override
    public void handle(Socket s) throws IOException {
        try (s; InputStream in = s.getInputStream(); OutputStream out = s.getOutputStream()) {
            int data;
            while ((data = in.read()) != -1) {
                // Error injection:
                if (data == '%') throw new IOException("Injected error!");
                // Done.
                out.write(transmogrify(data));
            }
        }
    }
}

