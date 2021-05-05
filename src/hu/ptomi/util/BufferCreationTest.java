package hu.ptomi.util;

import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntFunction;

/**
 * Creating a direct byte buffer is rather costly, because #1 we need to allocate direct memory and #2 it needs to be cleared up using phantom references. Creating heap byte buffers is not much more expensive than a byte[]. Still not cheap, but a lot less costly than direct buffers. The smaller the buffer, the larger the cost of allocating and especially deallocating the native buffer. Here is a little demo:
 */
public class BufferCreationTest {
    private static Object leak;
    private static final Timer timer = new Timer(true);

    public static void main(String... args) {
        IntFunction direct = ByteBuffer::allocateDirect;
        IntFunction heap = ByteBuffer::allocate;

        for (int i = 0; i < 10; i++) {
            for (int bufferSize = 16; bufferSize <= 16 * 1024 * 1024; bufferSize *= 4) {
                long buffers = getBuffers(direct, bufferSize);
                System.out.printf(Locale.US, "direct(%d): %,d%n", bufferSize, buffers);
                System.gc();
                buffers = getBuffers(heap, bufferSize);
                System.out.printf(Locale.US, "heap(%d): %,d%n", bufferSize, buffers);
                System.gc();
            }
            System.out.println();
        }
    }

    private static long getBuffers(IntFunction factory, int bufferSize) {
        AtomicBoolean running = new AtomicBoolean(true);
        timer.schedule(new TimerTask() {
            public void run() {
                running.setRelease(false);
            }
        }, 1000);
        long buffers = 0;
        while (running.getAcquire()) {
            leak = factory.apply(bufferSize);
            buffers++;
        }
        return buffers;
    }
}
