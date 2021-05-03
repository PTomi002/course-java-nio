package hu.ptomi.instructorsolution.nonblockingnio;

import hu.ptomi.instructorsolution.Handler;
import hu.ptomi.instructorsolution.blockingnio.TransmogrifySocketChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Wasteful solution, but works.
 * + We have to check all the open connections to see if someone sent something to the server.
 * + GC runs at a high level (thus CPU too), because JVM creates a lot of objects in: ssc.accept().
 */
public class SingleThreadedPollingNonBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);

        Handler<SocketChannel> handler = new TransmogrifySocketChannelHandler();
        Collection<SocketChannel> sockets = new ArrayList<>();
        while (true) {
            // One CPU is running in this cycle to detect changes.
            SocketChannel sc = ssc.accept(); // In non-blocking mode it is always null.
            if (sc != null) {
                sockets.add(sc);
                System.out.println("Connected to: " + sc);
                sc.configureBlocking(false);
            }
            for (SocketChannel socket : sockets) {
                if (socket.isConnected()) {
                    handler.handle(socket);
                }
            }
            sockets.removeIf(socket -> !socket.isConnected());
        }
    }
}
