package net.miarma.sat.common;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.miarma.sat.ui.TranslatorUI;

public class KeySequenceDispatcher implements KeyEventDispatcher {
    private final List<Integer> inputBuffer = new ArrayList<>();
    private final int MAX_SEQUENCE = 10;
    private boolean actionTaken = false;

    private final java.util.List<Integer> SEQUENCE_ABOUT = java.util.Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT);
    private final java.util.List<Integer> SEQUENCE_BINARY = java.util.Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_UP);
    private final java.util.List<Integer> SEQUENCE_CLEAR = java.util.Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
    private final java.util.List<Integer> SEQUENCE_CLOSE = java.util.Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN);
    private final java.util.List<Integer> SEQUENCE_CONTROLS = java.util.Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT);
    private final java.util.List<Integer> SEQUENCE_COPY = java.util.Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT);
    private final java.util.List<Integer> SEQUENCE_CUT = java.util.Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT);
    private final java.util.List<Integer> SEQUENCE_HEXADECIMAL = java.util.Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN);
    private final java.util.List<Integer> SEQUENCE_OPEN = java.util.Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_UP);
    private final java.util.List<Integer> SEQUENCE_PASTE = java.util.Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN);
    
    private TranslatorUI translatorUI;

    public KeySequenceDispatcher(TranslatorUI translatorUI) {
        this.translatorUI = translatorUI;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                if (shouldCaptureKeySequence()) {
                    inputBuffer.add(key);
                    if (inputBuffer.size() > MAX_SEQUENCE) {
                        inputBuffer.remove(0);
                    }
                    actionTaken = false;
                    attemptMatchSequences();
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean shouldCaptureKeySequence() {
        return !translatorUI.textArea.isFocusOwner() && !translatorUI.binaryArea.isFocusOwner();
    }

    private void attemptMatchSequences() {
        Map<List<Integer>, String> sequenceToAction = new HashMap<>();
        sequenceToAction.put(SEQUENCE_CONTROLS, "controls");
        sequenceToAction.put(SEQUENCE_BINARY, "binary");
        sequenceToAction.put(SEQUENCE_HEXADECIMAL, "hexadecimal");
        sequenceToAction.put(SEQUENCE_ABOUT, "about");
        sequenceToAction.put(SEQUENCE_OPEN, "open");
        sequenceToAction.put(SEQUENCE_CLEAR, "clear");
        sequenceToAction.put(SEQUENCE_CLOSE, "close");
        sequenceToAction.put(SEQUENCE_PASTE, "paste");
        sequenceToAction.put(SEQUENCE_COPY, "copy");
        sequenceToAction.put(SEQUENCE_CUT, "cut");

        List<List<Integer>> sortedSequences = sequenceToAction.keySet().stream()
            .sorted((a, b) -> b.size() - a.size())
            .collect(Collectors.toList());

        for (List<Integer> sequence : sortedSequences) {
            if (actionTaken) break;
            String actionCommand = sequenceToAction.get(sequence);
            checkSequence(sequence, new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand));
        }
    }


    private void checkSequence(List<Integer> sequence, ActionEvent action) {
        if (actionTaken) return;
        if (endsWithSequence(inputBuffer, sequence)) {
            performAction(action);
            inputBuffer.clear();
            actionTaken = true;
        }
    }

    private boolean endsWithSequence(List<Integer> buffer, List<Integer> sequence) {
        if (buffer.size() < sequence.size()) return false;
        for (int i = 0; i < sequence.size(); i++) {
            if (!buffer.get(buffer.size() - sequence.size() + i).equals(sequence.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void performAction(ActionEvent e) {
        translatorUI.performAction(e);
    }
}