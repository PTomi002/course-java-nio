package hu.ptomi.mysolution;

import java.net.Socket;
import java.util.Objects;

public class TransMogrifySocketDecorator implements TransMogrifySocket {

    private final TransMogrifySocket delegate;

    TransMogrifySocketDecorator(TransMogrifySocket delegate) {
        Objects.requireNonNull(delegate);
        this.delegate = delegate;
    }

    @Override
    public void listen() {
        new Thread(() -> {
            System.out.println("Connected to: " + delegate.getSocket());
            delegate.listen();
            System.out.println("Disconnected from: " + delegate.getSocket());
        }).start();
    }

    @Override
    public Socket getSocket() {
        throw new UnsupportedOperationException();
    }
}