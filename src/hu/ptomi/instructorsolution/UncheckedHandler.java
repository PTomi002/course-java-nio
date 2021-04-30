package hu.ptomi.instructorsolution;

import java.io.IOException;
import java.io.UncheckedIOException;

public class UncheckedHandler<S> extends DecoratedHandler<S> {
    UncheckedHandler(Handler<S> other) {
        super(other);
    }

    @Override
    public void handle(S s) {
        try {
            super.handle(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

