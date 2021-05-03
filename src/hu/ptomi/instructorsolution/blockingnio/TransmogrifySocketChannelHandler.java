package hu.ptomi.instructorsolution.blockingnio;

import hu.ptomi.instructorsolution.Handler;
import hu.ptomi.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TransmogrifySocketChannelHandler implements Handler<SocketChannel> {
    @Override
    public void handle(SocketChannel sc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(80);
        int numOfReadBytes = sc.read(buffer);
        if (numOfReadBytes == -1) {
            sc.close();
            return;
        }
        if (numOfReadBytes > 0) {
            Utils.transmogrify(buffer);
            while (buffer.hasRemaining()) {
                sc.write(buffer);
            }
            // in case the write was incomplete
            buffer.compact();
        }
    }
}
