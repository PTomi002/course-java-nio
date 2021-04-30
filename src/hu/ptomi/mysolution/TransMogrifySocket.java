package hu.ptomi.mysolution;

import java.net.Socket;

public interface TransMogrifySocket {
    void listen();

    Socket getSocket();
}
