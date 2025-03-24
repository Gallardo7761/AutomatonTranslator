package net.miarma.sat.common;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class KeySequences {
    public static final List<Integer> SEQUENCE_ABOUT = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT);
    public static final List<Integer> SEQUENCE_BINARY = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_UP);
    public static final List<Integer> SEQUENCE_CLEAR = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN);
    public static final List<Integer> SEQUENCE_CLOSE = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT);
    public static final List<Integer> SEQUENCE_CONTROLS = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT);
    public static final List<Integer> SEQUENCE_COPY = Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT);
    public static final List<Integer> SEQUENCE_CUT = Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT);
    public static final List<Integer> SEQUENCE_HEXADECIMAL = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN);
    public static final List<Integer> SEQUENCE_OPEN = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT);
    public static final List<Integer> SEQUENCE_PASTE = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_UP);
}
