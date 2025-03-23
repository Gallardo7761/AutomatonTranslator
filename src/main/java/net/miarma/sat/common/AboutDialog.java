package net.miarma.sat.common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutDialog extends JDialog {

    public AboutDialog(Frame owner) {
        super(owner, "About SAT", true);
        initComponents();
    }

    private void initComponents() {
        setSize(300, 200);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JLabel label = new JLabel("<html><center><strong>Super Automaton Translator</strong><br>Version " + Constants.APP_VERSION + "<br>Developed by Thunder Struck<br>&copy;" + (LocalDateTime.now().getYear()+100) + " Super Earth</center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(label, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
