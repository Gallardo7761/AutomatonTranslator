/*
 * Created by JFormDesigner on Sun Mar 23 17:00:00 CET 2025
 */

package net.miarma.sat.ui;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miarma.sat.common.AboutDialog;
import net.miarma.sat.common.Constants;
import net.miarma.sat.common.ControlsDialog;
import net.miarma.sat.common.KeySequenceDispatcher;
import net.miarma.sat.common.TextBinaryConverter;
import net.miginfocom.swing.MigLayout;

/**
 * @author jomaa
 */
public class TranslatorUI extends JFrame {
	private static final long serialVersionUID = -3949993063443585657L;
	private String currentFormat = "BINARY";
	private boolean updating = false;
	
	public TranslatorUI() {
		initComponents();
		setVersion();
		addListeners();
		setupKeyDispatcher();
		this.getRootPane().putClientProperty("JRootPane.titleBarShowTitle", false);
	}
	
	private void setVersion() {
		this.setTitle(this.getTitle() + " " + Constants.APP_VERSION);
	}
	
	private void setupKeyDispatcher() {
        KeySequenceDispatcher keyDispatcher = new KeySequenceDispatcher(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyDispatcher);
    }
	
	public void performAction(ActionEvent e) {
        switch (e.getActionCommand()) {
	        case "open":
	            open(e);
	            break;
	        case "close":
	            close(e);
	            break;
	        case "copy":
	            copy(e);
	            break;
	        case "cut":
	            cut(e);
	            break;
	        case "paste":
	            paste(e);
	            break;
	        case "clear":
	            clear(e);
	            break;
	        case "binary":
	            binary(e);
	            break;
	        case "hexadecimal":
	            hexadecimal(e);
	            break;
	        case "controls":
	            controls(e);
	            break;
	        case "about":
	            about(e);
	            break;
	        default:
	            // No action
	            break;
        }
    }
	
	private void addListeners() {
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {
	        public void insertUpdate(DocumentEvent e) { translate(); }
	        public void removeUpdate(DocumentEvent e) { translate(); }
	        public void changedUpdate(DocumentEvent e) {}

	        private void translate() {
	            if (updating) return;
	            updating = true;
	            try {
	                String text = textArea.getText();
	                if ("BINARY".equals(currentFormat)) {
	                    String binary = TextBinaryConverter.textToBinary(text);
	                    binaryArea.setText(binary);
	                } else if ("HEXADECIMAL".equals(currentFormat)) {
	                    String hex = TextBinaryConverter.textToHexadecimal(text);
	                    binaryArea.setText(hex);
	                }
	            } catch (Exception ex) {
	                binaryArea.setText("Input Error: " + ex.getMessage());
	            } finally {
	                updating = false;
	            }
	        }
	    });

	    binaryArea.getDocument().addDocumentListener(new DocumentListener() {
	        public void insertUpdate(DocumentEvent e) { reverseTranslate(); }
	        public void removeUpdate(DocumentEvent e) { reverseTranslate(); }
	        public void changedUpdate(DocumentEvent e) {}

	        private void reverseTranslate() {
	            if (updating) return;
	            updating = true;
	            try {
	                String input = binaryArea.getText();
	                String text;

	                if (isBinary(input)) {
	                    text = TextBinaryConverter.binaryToText(input);
	                } else if (isHexadecimal(input)) {
	                    text = TextBinaryConverter.hexadecimalToText(input);
	                } else {
	                    throw new IllegalArgumentException("Unknown format");
	                }

	                textArea.setText(text);
	            } catch (Exception ex) {
	                textArea.setText("Decode Error: " + ex.getMessage());
	            } finally {
	                updating = false;
	            }
	        }

	        private boolean isBinary(String str) {
	            return str.matches("[01 ]+");
	        }

	        private boolean isHexadecimal(String str) {
	            return str.matches("[0-9A-Fa-f ]+");
	        }
	    });
	}

	private void open(ActionEvent e) {
	    FileDialog dialog = new FileDialog(this, "Open", FileDialog.LOAD);
	    dialog.setVisible(true);
	    String filename = dialog.getFile();
	    if (filename != null) {
	        try {
	            FileManager fileManager = new FileManager(dialog.getDirectory() + filename);
	            String content = fileManager.readAsString();
	            String firstLine = content.split("\n")[0].trim();
	            
	            if (firstLine.matches("[01 ]+")) {
	                currentFormat = "BINARY";
	                binaryArea.setText(content);
	                textArea.setText(TextBinaryConverter.binaryToText(content));
	            } else if (firstLine.matches("[0-9A-Fa-f ]+")) {
	                currentFormat = "HEXADECIMAL";
	                binaryArea.setText(content);
	                textArea.setText(TextBinaryConverter.hexadecimalToText(content));
	            } else {
	                textArea.setText(content);
	                if("BINARY".equals(currentFormat)) {
	                    binaryArea.setText(TextBinaryConverter.textToBinary(content));
	                } else {
	                    binaryArea.setText(TextBinaryConverter.textToHexadecimal(content));
	                }
                }
	            
	        } catch (IOException e1) {
	            Constants.LOGGER.error("Error reading file", e1);
	        }
	    }
	}


	private void close(ActionEvent e) {
		System.exit(0);
	}

	private void copy(ActionEvent e) {
	    JTextArea activeArea = textArea.isFocusOwner() ? textArea : binaryArea.isFocusOwner() ? binaryArea : null;
	    if (activeArea != null) {
	        String text = activeArea.getText();
	        StringSelection selection = new StringSelection(text);
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        clipboard.setContents(selection, null);
	    }
	}

	private void cut(ActionEvent e) {
	    JTextArea activeArea = textArea.isFocusOwner() ? textArea : binaryArea.isFocusOwner() ? binaryArea : null;
	    if (activeArea != null) {
	        String text = activeArea.getText();
	        StringSelection selection = new StringSelection(text);
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        clipboard.setContents(selection, null);
	        activeArea.setText("");
	    }
	}

	private void paste(ActionEvent e) {
	    JTextArea activeArea = textArea.isFocusOwner() ? textArea : binaryArea.isFocusOwner() ? binaryArea : null;
	    if (activeArea != null) {
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        try {
	            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
	                String text = (String) clipboard.getData(DataFlavor.stringFlavor);
	                activeArea.setText(text);
	            }
	        } catch (UnsupportedFlavorException | IOException ex) {
	            Constants.LOGGER.error("Error pasting text", ex);
	        }
	    }
	}

	private void clear(ActionEvent e) {
		binaryArea.setText("");
	    textArea.setText("");
	    binaryArea.setText("");
	}

	private void binary(ActionEvent e) {
		currentFormat = "BINARY";
        String text = textArea.getText();
        String binary = TextBinaryConverter.textToBinary(text);
        binaryArea.setText(binary);
    }

    private void hexadecimal(ActionEvent e) {
    	currentFormat = "HEXADECIMAL";
        String text = textArea.getText();
        String hex = TextBinaryConverter.textToHexadecimal(text);
        binaryArea.setText(hex);
    }

    public void controls(ActionEvent e) {
        ControlsDialog dialog = new ControlsDialog(this);
        dialog.setVisible(true);
    }

	private void about(ActionEvent e) {
		AboutDialog dialog = new AboutDialog(this);
		dialog.setVisible(true);
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Educational license - José Manuel Amador Gallardo (José Manuel Amador)
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		openMenuItem = new JMenuItem();
		closeMenuItem = new JMenuItem();
		editMenu = new JMenu();
		copyMenuItem = new JMenuItem();
		cutMenuItem = new JMenuItem();
		pasteMenuItem = new JMenuItem();
		clearMenuItem = new JMenuItem();
		viewMenu = new JMenu();
		binaryMenuItem = new JMenuItem();
		hexadecimalMenuItem = new JMenuItem();
		helpMenu = new JMenu();
		controlsMenuItem = new JMenuItem();
		aboutMenuItem = new JMenuItem();
		mainPanel = new JPanel();
		textScrollPane = new JScrollPane();
		textArea = new JTextArea();
		binaryScrollPane = new JScrollPane();
		binaryArea = new JTextArea();

		//======== this ========
		setTitle("SAT");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(450, 300));
		setFont(new Font("FS Sinclair", Font.PLAIN, 16));
		var contentPane = getContentPane();
		contentPane.setLayout(new MigLayout(
			"hidemode 3",
			// columns
			"[grow,fill]",
			// rows
			"[grow,fill]"));

		//======== menuBar ========
		{

			//======== fileMenu ========
			{
				fileMenu.setText("File");
				fileMenu.setFont(new Font("FS Sinclair", Font.PLAIN, 20));

				//---- openMenuItem ----
				openMenuItem.setText("Open");
				openMenuItem.setIconTextGap(64);
				openMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/open.png")));
				openMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				openMenuItem.addActionListener(e -> open(e));
				fileMenu.add(openMenuItem);

				//---- closeMenuItem ----
				closeMenuItem.setText("Close");
				closeMenuItem.setIconTextGap(64);
				closeMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/close.png")));
				closeMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				closeMenuItem.addActionListener(e -> close(e));
				fileMenu.add(closeMenuItem);
			}
			menuBar.add(fileMenu);

			//======== editMenu ========
			{
				editMenu.setText("Edit");
				editMenu.setFont(new Font("FS Sinclair", Font.PLAIN, 20));

				//---- copyMenuItem ----
				copyMenuItem.setText("Copy");
				copyMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/copy.png")));
				copyMenuItem.setIconTextGap(64);
				copyMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				copyMenuItem.addActionListener(e -> copy(e));
				editMenu.add(copyMenuItem);

				//---- cutMenuItem ----
				cutMenuItem.setText("Cut");
				cutMenuItem.setIconTextGap(64);
				cutMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/cut.png")));
				cutMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				cutMenuItem.addActionListener(e -> cut(e));
				editMenu.add(cutMenuItem);

				//---- pasteMenuItem ----
				pasteMenuItem.setText("Paste");
				pasteMenuItem.setIconTextGap(64);
				pasteMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/paste.png")));
				pasteMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				pasteMenuItem.addActionListener(e -> paste(e));
				editMenu.add(pasteMenuItem);

				//---- clearMenuItem ----
				clearMenuItem.setText("Clear");
				clearMenuItem.setIconTextGap(64);
				clearMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/clear.png")));
				clearMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				clearMenuItem.addActionListener(e -> clear(e));
				editMenu.add(clearMenuItem);
			}
			menuBar.add(editMenu);

			//======== viewMenu ========
			{
				viewMenu.setText("View");
				viewMenu.setFont(new Font("FS Sinclair", Font.PLAIN, 20));

				//---- binaryMenuItem ----
				binaryMenuItem.setText("Binary");
				binaryMenuItem.setIconTextGap(64);
				binaryMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/binary.png")));
				binaryMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				binaryMenuItem.addActionListener(e -> binary(e));
				viewMenu.add(binaryMenuItem);

				//---- hexadecimalMenuItem ----
				hexadecimalMenuItem.setText("Hexadecimal");
				hexadecimalMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				hexadecimalMenuItem.setIconTextGap(64);
				hexadecimalMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/hexadecimal.png")));
				hexadecimalMenuItem.addActionListener(e -> hexadecimal(e));
				viewMenu.add(hexadecimalMenuItem);
			}
			menuBar.add(viewMenu);

			//======== helpMenu ========
			{
				helpMenu.setText("Help");
				helpMenu.setFont(new Font("FS Sinclair", Font.PLAIN, 20));

				//---- controlsMenuItem ----
				controlsMenuItem.setText("Controls");
				controlsMenuItem.setIconTextGap(64);
				controlsMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/controls.png")));
				controlsMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				controlsMenuItem.addActionListener(e -> controls(e));
				helpMenu.add(controlsMenuItem);

				//---- aboutMenuItem ----
				aboutMenuItem.setText("About");
				aboutMenuItem.setIconTextGap(64);
				aboutMenuItem.setIcon(new ImageIcon(getClass().getResource("/images/about.png")));
				aboutMenuItem.setFont(new Font("FS Sinclair", Font.PLAIN, 18));
				aboutMenuItem.addActionListener(e -> about(e));
				helpMenu.add(aboutMenuItem);
			}
			menuBar.add(helpMenu);
		}
		setJMenuBar(menuBar);

		//======== mainPanel ========
		{
			mainPanel.setLayout(new MigLayout(
				"insets 0,hidemode 3",
				// columns
				"[grow,fill]" +
				"[grow,fill]",
				// rows
				"[grow,fill]"));

			//======== textScrollPane ========
			{

				//---- textArea ----
				textArea.setFont(new Font("FS Sinclair", Font.PLAIN, 26));
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textScrollPane.setViewportView(textArea);
			}
			mainPanel.add(textScrollPane, "cell 0 0");

			//======== binaryScrollPane ========
			{

				//---- binaryArea ----
				binaryArea.setFont(new Font("FS Sinclair", Font.PLAIN, 26));
				binaryArea.setLineWrap(true);
				binaryArea.setWrapStyleWord(true);
				binaryScrollPane.setViewportView(binaryArea);
			}
			mainPanel.add(binaryScrollPane, "cell 1 0");
		}
		contentPane.add(mainPanel, "cell 0 0");
		setSize(900, 600);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Educational license - José Manuel Amador Gallardo (José Manuel Amador)
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem closeMenuItem;
	private JMenu editMenu;
	private JMenuItem copyMenuItem;
	private JMenuItem cutMenuItem;
	private JMenuItem pasteMenuItem;
	private JMenuItem clearMenuItem;
	private JMenu viewMenu;
	private JMenuItem binaryMenuItem;
	private JMenuItem hexadecimalMenuItem;
	private JMenu helpMenu;
	private JMenuItem controlsMenuItem;
	private JMenuItem aboutMenuItem;
	private JPanel mainPanel;
	private JScrollPane textScrollPane;
	public JTextArea textArea;
	private JScrollPane binaryScrollPane;
	public JTextArea binaryArea;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
