package hu.ptomi.instructorsolution.nonblockingnio;

import hu.ptomi.instructorsolution.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class WriteHandler implements Handler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public WriteHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey sKey) throws IOException {
        SocketChannel sc = (SocketChannel) sKey.channel();

        Queue<ByteBuffer> queue = pendingData.get(sc);

        while (!queue.isEmpty()) {
            ByteBuffer buffer = queue.peek();
            int written = sc.write(buffer);
            if (written == -1) { // Most probably closed the socket.
                sc.close();
                System.out.println("Disconnected from (in write): " + sc);
                pendingData.remove(sc);
                return;
            }
            // The capacity is 80, if I can only write 13 bytes in a non-blocking way,
            //      then I have to jump out of this loop, give up for now and return later.
            if (buffer.hasRemaining()) return;
            queue.remove();
        }

        sKey.interestOps(SelectionKey.OP_READ);
    }
}
