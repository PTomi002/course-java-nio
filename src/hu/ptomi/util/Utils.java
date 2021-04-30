package hu.ptomi.util;

public final class Utils {
    public static int transmogrify(int data) {
        // data XOR 'space character'
        return Character.isLetter(data) ? data ^ ' ' : data;
    }
}
