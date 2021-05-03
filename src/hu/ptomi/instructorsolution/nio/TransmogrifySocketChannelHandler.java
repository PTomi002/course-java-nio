package hu.ptomi.instructorsolution.nio;

import hu.ptomi.instructorsolution.Handler;
import hu.ptomi.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TransmogrifySocketChannelHandler implements Handler<SocketChannel> {
    @Override
    public void handle(SocketChannel sc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(80);
        int read = sc.read(buffer);
        if (read == -1) {
            sc.close();
            return;
        }
        if (read > 0) {
            Utils.transmogrify(buffer);
            while (buffer.hasRemaining()) {
                sc.write(buffer);
            }
            // in case the write was incomplete
            buffer.compact();
        }
    }
}
