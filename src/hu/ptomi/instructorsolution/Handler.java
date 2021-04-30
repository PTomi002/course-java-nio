package hu.ptomi.instructorsolution;

import java.io.IOException;

// Component in the decorator pattern
public interface Handler<T> {
    void handle(T s) throws IOException;
}
