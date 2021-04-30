package hu.ptomi.util;

import java.io.IOException;
import java.net.Socket;

/**
 * With num of sockets = 30_000 we got:
 * + This is only one type of error, there is a limit also for the num of threads on the Java/OS level or available sockets.
 * + Is is very costly to create and start Thread objects.
 * <p>
 * Exception in thread "main" java.net.SocketException: No buffer space available (maximum connections reached?): connect
 * at java.base/java.net.PlainSocketImpl.connect0(Native Method)
 * at java.base/java.net.PlainSocketImpl.socketConnect(PlainSocketImpl.java:101)
 * at java.base/java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:399)
 * at java.base/java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:242)
 * at java.base/java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:224)
 * at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:403)
 * at java.base/java.net.Socket.connect(Socket.java:591)
 * at java.base/java.net.Socket.connect(Socket.java:540)
 * at java.base/java.net.Socket.<init>(Socket.java:436)
 * at java.base/java.net.Socket.<init>(Socket.java:213)
 * at hu.ptomi.util.NastyChumpAttack.main(NastyChumpAttack.java:11)
 */
public class NastyChumpAttack {

    public static void main(String[] args) throws IOException, InterruptedException {
//        Socket[] sockets = new Socket[30000];
        Socket[] sockets = new Socket[3000];
        for (int i = 0; i < sockets.length; i++) {
            sockets[i] = new Socket("localhost", 8080);
        }
        Thread.sleep(10_000);
    }
}
