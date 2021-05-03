package hu.ptomi.instructorsolution.blockingnio;

import hu.ptomi.instructorsolution.ExecutorServiceHandler;
import hu.ptomi.instructorsolution.Handler;
import hu.ptomi.instructorsolution.PrintingHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

public class BlockingNIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        Handler<SocketChannel> handler =
                new ExecutorServiceHandler<>(
                        new PrintingHandler<>(
                                new BlockingChannelHandler(
                                        new TransmogrifySocketChannelHandler()
                                )),
                        Executors.newFixedThreadPool(10),
                        (t, e) -> System.err.println("Unhandled error: " + e + " in:" + Thread.currentThread().getName())
                );
        while (true) {
            SocketChannel socket = ssc.accept();
            handler.handle(socket);
        }
    }
}
