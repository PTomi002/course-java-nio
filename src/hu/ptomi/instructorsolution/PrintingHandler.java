package hu.ptomi.instructorsolution;

import java.io.IOException;

public class PrintingHandler<S> extends DecoratedHandler<S> {
    public PrintingHandler(Handler<S> other) {
        super(other);
    }

    @Override
    public void handle(S s) throws IOException {
        String tName = Thread.currentThread().getName();
        System.out.println("Connected to: " + s + " handler: " + tName);
        try {
            super.handle(s);
        } finally {
            System.out.println("Disconnected from: " + s + " handler: " + tName);
        }
    }
}

