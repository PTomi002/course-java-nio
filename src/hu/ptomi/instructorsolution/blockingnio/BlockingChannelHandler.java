package hu.ptomi.instructorsolution.blockingnio;

import hu.ptomi.instructorsolution.DecoratedHandler;
import hu.ptomi.instructorsolution.Handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class BlockingChannelHandler extends DecoratedHandler<SocketChannel> {
    public BlockingChannelHandler(Handler<SocketChannel> other) {
        super(other);
    }

    @Override
    public void handle(SocketChannel sc) throws IOException {
        // This handler will keep this thread running.
        while (sc.isConnected()) {
            super.handle(sc);
        }
    }
}
