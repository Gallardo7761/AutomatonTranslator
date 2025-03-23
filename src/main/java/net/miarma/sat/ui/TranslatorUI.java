/*
 * Created by JFormDesigner on Sun Mar 23 17:00:00 CET 2025
 */

package net.miarma.sat.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miarma.sat.common.Constants;
import net.miarma.sat.common.TextBinaryConverter;
import net.miginfocom.swing.MigLayout;

/**
 * @author jomaa
 */
public class TranslatorUI extends JFrame {
	private static final long serialVersionUID = -3949993063443585657L;

	private boolean updating = false;
	
	public TranslatorUI() {
		initComponents();
		setVersion();
		addListeners();
	}
	
	private void setVersion() {
		this.setTitle(this.getTitle() + " " + Constants.APP_VERSION);
	}
	
	private void addListeners() {
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { translate(); }
            public void removeUpdate(DocumentEvent e) { translate(); }
            public void changedUpdate(DocumentEvent e) {}
            
            private void translate() {
                if (updating) return;
                updating = true;
                String text = textArea.getText();
                String binary = TextBinaryConverter.textToBinary(text);
                binaryArea.setText(binary);
                updating = false;
            }
		});
		
		binaryArea.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { translate(); }
            public void removeUpdate(DocumentEvent e) { translate(); }
            public void changedUpdate(DocumentEvent e) {}
            
            private void translate() {
                if (updating) return;
                updating = true;
                String binary = textArea.getText();
                try {
                    String texto = TextBinaryConverter.binaryToText(binary);
                    textArea.setText(texto);
                } catch (Exception ex) {
                	textArea.setText("ERROR DE DECODIFICACIÓN");
                }
                updating = false;
            }
		});
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Educational license - José Manuel Amador Gallardo (José Manuel Amador)
		mainPanel = new JPanel();
		textsPanel = new JPanel();
		textScrollPane = new JScrollPane();
		textArea = new JTextArea();
		binaryScrollPane = new JScrollPane();
		binaryArea = new JTextArea();
		buttonsPanel = new JPanel();
		copyButton = new JButton();
		clearButton = new JButton();

		//======== this ========
		setTitle("SAT");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(450, 300));
		var contentPane = getContentPane();
		contentPane.setLayout(new MigLayout(
			"hidemode 3",
			// columns
			"[grow,fill]",
			// rows
			"[grow,fill]"));

		//======== mainPanel ========
		{
			mainPanel.setLayout(new MigLayout(
				"insets 0,hidemode 3",
				// columns
				"[grow,fill]",
				// rows
				"[grow,fill]" +
				"[fill]"));

			//======== textsPanel ========
			{
				textsPanel.setLayout(new MigLayout(
					"insets 0,hidemode 3",
					// columns
					"[grow,fill]" +
					"[grow,fill]",
					// rows
					"[grow,fill]"));

				//======== textScrollPane ========
				{

					//---- textArea ----
					textArea.setFont(new Font("Liberation Mono", Font.PLAIN, 24));
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					textScrollPane.setViewportView(textArea);
				}
				textsPanel.add(textScrollPane, "cell 0 0");

				//======== binaryScrollPane ========
				{

					//---- binaryArea ----
					binaryArea.setFont(new Font("Liberation Mono", Font.PLAIN, 24));
					binaryArea.setLineWrap(true);
					binaryArea.setWrapStyleWord(true);
					binaryScrollPane.setViewportView(binaryArea);
				}
				textsPanel.add(binaryScrollPane, "cell 1 0");
			}
			mainPanel.add(textsPanel, "cell 0 0");

			//======== buttonsPanel ========
			{
				buttonsPanel.setLayout(new MigLayout(
					"insets 0,hidemode 3",
					// columns
					"[grow,fill]" +
					"[grow,fill]",
					// rows
					"[grow,fill]"));

				//---- copyButton ----
				copyButton.setText("COPY");
				buttonsPanel.add(copyButton, "cell 0 0,height 48:48:48");

				//---- clearButton ----
				clearButton.setText("CLEAR");
				buttonsPanel.add(clearButton, "cell 1 0,height 48:48:48");
			}
			mainPanel.add(buttonsPanel, "cell 0 1");
		}
		contentPane.add(mainPanel, "cell 0 0");
		setSize(900, 600);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Educational license - José Manuel Amador Gallardo (José Manuel Amador)
	private JPanel mainPanel;
	private JPanel textsPanel;
	private JScrollPane textScrollPane;
	private JTextArea textArea;
	private JScrollPane binaryScrollPane;
	private JTextArea binaryArea;
	private JPanel buttonsPanel;
	private JButton copyButton;
	private JButton clearButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
