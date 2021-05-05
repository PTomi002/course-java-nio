package hu.ptomi.instructorsolution.nonblockingnio;

import hu.ptomi.instructorsolution.Handler;
import hu.ptomi.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class PooledReadHandler implements Handler<SelectionKey> {
    private final ExecutorService pool;
    private final Queue<Runnable> selectorActions;
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public PooledReadHandler(
            ExecutorService pool,
            Queue<Runnable> selectorActions,
            Map<SocketChannel, Queue<ByteBuffer>> pendingData) {

        this.pool = pool;
        this.selectorActions = selectorActions;
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey sKey) throws IOException {
        SocketChannel sc = (SocketChannel) sKey.channel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(80);
        int numOfReadBytes = sc.read(buffer);

        if (numOfReadBytes == -1) { // Most probably closed the socket.
            sc.close();
            System.out.println("Disconnected from (in pooledRead): " + sc);
            return;
        }

        if (numOfReadBytes > 0) {
            pool.submit(() -> {
                Utils.transmogrify(buffer);
                pendingData.get(sc).add(buffer);
                selectorActions.add(() -> sKey.interestOps(SelectionKey.OP_WRITE));
                // If it is sleeping, then we wake it up.
                // Instead of setting it directly, we wake up the selector in case there is nothing coming in the while
                //      on the sockets, for example: write available.
                sKey.selector().wakeup();
            });
        }
    }
}
