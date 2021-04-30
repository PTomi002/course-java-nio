package hu.ptomi.mysolution;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

import static hu.ptomi.util.Utils.transmogrify;

public class TransmogrifySocketImpl implements TransMogrifySocket {

    private final Socket socket;

    TransmogrifySocketImpl(Socket socket) {
        Objects.requireNonNull(socket);
        this.socket = socket;
    }

    @Override
    public void listen() {
        try (socket; InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            int data;
            while ((data = in.read()) != -1) {
                out.write(transmogrify(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Socket getSocket() {
        return socket;
    }
}
