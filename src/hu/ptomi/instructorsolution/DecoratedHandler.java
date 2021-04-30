package hu.ptomi.instructorsolution;

import java.io.IOException;

// Decorator common abstract superclass
public abstract class DecoratedHandler<T> implements Handler<T> {
    private final Handler<T> other;

    protected DecoratedHandler(Handler<T> other) {
        this.other = other;
    }

    @Override
    public void handle(T s) throws IOException {
        other.handle(s);
    }
}
