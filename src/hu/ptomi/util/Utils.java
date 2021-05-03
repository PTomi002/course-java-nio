package hu.ptomi.util;

import java.nio.ByteBuffer;

public final class Utils {
    public static int transmogrify(int data) {
        // data XOR 'space character'
        return Character.isLetter(data) ? data ^ ' ' : data;
    }

    public static void transmogrify(ByteBuffer buffer) {
        // buffer before read: pos=0, limit=80, capacity=80
        // "hello\n"
        // buffer after read: pos=6, limit=80, capacity=80
        //        buffer.limit(buffer.position());
        //        buffer.position(0);
        buffer.flip();
        // after buffer flip: pos=0, limit=6, capacity=80
        for (int i = 0; i < buffer.limit(); i++) {
            buffer.put(i, (byte) transmogrify(buffer.get(i)));
        }
    }
}
