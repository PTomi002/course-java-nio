package hu.ptomi.instructorsolution.nonblockingnio;

import hu.ptomi.instructorsolution.Handler;
import hu.ptomi.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class ReadHandler implements Handler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public ReadHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey sKey) throws IOException {
        SocketChannel sc = (SocketChannel) sKey.channel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(80);
        int numOfReadBytes = sc.read(buffer);

        // ByteBuffer will simply be garbage collected, although if they are direct buffers,
        //      then the GC is a bit complicated.
        // Instead of just being collected, they need to be cleaned by their phantom references.
        // It might take a while and could require a full GC.
        if (numOfReadBytes == -1) { // Most probably closed the socket.
            sc.close();
            System.out.println("Disconnected from (in read): " + sc);
            return;
        }

        if (numOfReadBytes > 0) {
            Utils.transmogrify(buffer);
            pendingData.get(sc).add(buffer);
            sKey.interestOps(SelectionKey.OP_WRITE);
        }
    }
}
