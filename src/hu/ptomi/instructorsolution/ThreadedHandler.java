package hu.ptomi.instructorsolution;

public class ThreadedHandler<S> extends UncheckedHandler<S> {
    ThreadedHandler(Handler<S> other) {
        super(other);
    }

    @Override
    public void handle(S s) {
        new Thread(() -> {
            super.handle(s);
        }).start();
    }
}

