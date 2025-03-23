package net.miarma.sat;

import net.miarma.sat.themes.SuperEarthLaf;
import net.miarma.sat.ui.TranslatorUI;

public class AutomatonTranslator {
	public static void main(String[] args) {
		SuperEarthLaf.setup();
		new TranslatorUI().setVisible(true);
	}
}
