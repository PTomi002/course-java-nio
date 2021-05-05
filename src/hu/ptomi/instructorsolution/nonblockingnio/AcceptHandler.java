package hu.ptomi.instructorsolution.nonblockingnio;

import hu.ptomi.instructorsolution.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * SelectionKey allows to attach() an arbitrary object to it, so as an alternative to using the pendingData Map, we could attach the ByteBuffer queue in AcceptHandler:
 *
 * sc.register(key.selector(), SelectionKey.OP_READ, new ConcurrentLinkedQueue<>());
 *
 * and retrieve it in ReadHandler and WriteHandler:
 *
 * @SuppressWarnings("unchecked") Queue queue = (Queue) key.attachment();
 *
 * It seems to also work :)
 */
public class AcceptHandler implements Handler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public AcceptHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey sKey) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) sKey.channel();
        Selector selector = sKey.selector();

        SocketChannel sc = Optional.ofNullable(ssc.accept()).orElseThrow();
        sc.configureBlocking(false);

        System.out.println("Connected to: " + sc);
        pendingData.put(sc, new ConcurrentLinkedQueue<>());

        sc.register(selector, SelectionKey.OP_READ);
    }
}
