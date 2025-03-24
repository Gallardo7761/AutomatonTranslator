package net.miarma.sat.common;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import net.miarma.sat.AutomatonTranslator;

public class Constants {
	public static final String APP_NAME = "Super Automaton Translator";
	public static final String APP_SHORT_NAME = "SAT";
	public static final Version APP_VERSION = Version.of(1,0,0);
	public static final Logger LOGGER = LoggerFactory.getLogger(AutomatonTranslator.class);
}
