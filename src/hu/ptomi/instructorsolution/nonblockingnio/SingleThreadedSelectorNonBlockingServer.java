package hu.ptomi.instructorsolution.nonblockingnio;

import hu.ptomi.instructorsolution.Handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadedSelectorNonBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        // OP_ACCEPT: the server can accept a new connection
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        // Does not need to be concurrent as only 1 thread will read and write it.
        Map<SocketChannel, Queue<ByteBuffer>> pendingData = new HashMap<>();
        Handler<SelectionKey> accept = new AcceptHandler(pendingData);
        Handler<SelectionKey> read = new ReadHandler(pendingData);
        Handler<SelectionKey> write = new WriteHandler(pendingData);

        while (true) {
            // Blocks until some event happens.
            selector.select();
            Set<SelectionKey> sKeys = selector.selectedKeys();
            for (Iterator<SelectionKey> it = sKeys.iterator(); it.hasNext(); ) {
                SelectionKey sKey = it.next();
                it.remove();
                if (sKey.isValid()) { // After this check the key can be invalid, someone pulls the cable out.
                    if (sKey.isAcceptable()) {
                        accept.handle(sKey);
                    } else if (sKey.isReadable()) {
                        read.handle(sKey);
                    } else if (sKey.isWritable()) {
                        write.handle(sKey);
                    }
                }
            }
        }
    }
}
