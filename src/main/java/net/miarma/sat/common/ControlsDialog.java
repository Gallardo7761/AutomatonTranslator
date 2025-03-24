package net.miarma.sat.common;

import javax.swing.*;
import java.awt.*;

public class ControlsDialog extends JDialog {
    private static final long serialVersionUID = 7532796838079061340L;

    public ControlsDialog(Frame owner) {
        super(owner, "Keyboard Controls", true);
        setSize(400, 600);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(0, 1));

        addControl("binary");
        addControl("hexadecimal");
        addControl("about");
        addControl("open");
        addControl("controls");
        addControl("clear");
        addControl("close");
        addControl("paste");
        addControl("copy");
        addControl("cut");
    }

    private void addControl(String action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource("/images/" + action + ".png"));
        } catch (Exception e) {
            System.out.println("Icon not found for action: " + action);
            icon = new ImageIcon();
        }

        JLabel label = new JLabel(action, JLabel.CENTER);
        label.setIconTextGap(16);
        label.setIcon(icon);
        label.setFont(new Font("FS Sinclair", Font.PLAIN, 20));

        panel.add(label, BorderLayout.CENTER);
        getContentPane().add(panel);
    }
}
