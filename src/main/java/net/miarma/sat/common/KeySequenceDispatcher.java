package net.miarma.sat.common;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miarma.sat.ui.TranslatorUI;

public class KeySequenceDispatcher implements KeyEventDispatcher {
    private final List<Integer> inputBuffer = new ArrayList<>();
    private final int MAX_SEQUENCE = 4; // La secuencia máxima es de 4 para coincidir con nuestras secuencias
    private boolean actionTaken = false;
    private TranslatorUI translatorUI;

    public KeySequenceDispatcher(TranslatorUI translatorUI) {
        this.translatorUI = translatorUI;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            int key = e.getKeyCode();
            if ((e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_C ||
			                    e.getKeyCode() == KeyEvent.VK_V ||
			                    e.getKeyCode() == KeyEvent.VK_X))) {
			System.out.println("Operación de copiar/cortar/pegar bloqueada.");
			return true;
			}
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                inputBuffer.add(key);
                if (inputBuffer.size() > MAX_SEQUENCE) {
                    inputBuffer.remove(0);
                }
                actionTaken = false;
                attemptMatchSequences();
                return true;
            }
        }
        return false;
    }

    private void attemptMatchSequences() {
        Map<List<Integer>, String> sequenceToAction = new HashMap<>();
        sequenceToAction.put(KeySequences.SEQUENCE_CONTROLS, "controls");
        sequenceToAction.put(KeySequences.SEQUENCE_BINARY, "binary");
        sequenceToAction.put(KeySequences.SEQUENCE_HEXADECIMAL, "hexadecimal");
        sequenceToAction.put(KeySequences.SEQUENCE_ABOUT, "about");
        sequenceToAction.put(KeySequences.SEQUENCE_OPEN, "open");
        sequenceToAction.put(KeySequences.SEQUENCE_CLEAR, "clear");
        sequenceToAction.put(KeySequences.SEQUENCE_CLOSE, "close");
        sequenceToAction.put(KeySequences.SEQUENCE_PASTE, "paste");
        sequenceToAction.put(KeySequences.SEQUENCE_COPY, "copy");
        sequenceToAction.put(KeySequences.SEQUENCE_CUT, "cut");

        for (Map.Entry<List<Integer>, String> entry : sequenceToAction.entrySet()) {
            if (actionTaken) break;
            checkSequence(entry.getKey(), new ActionEvent(this, ActionEvent.ACTION_PERFORMED, entry.getValue()));
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
