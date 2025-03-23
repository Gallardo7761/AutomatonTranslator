package net.miarma.sat.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class SuperEarthLaf extends FlatDarkLaf {
	private static final long serialVersionUID = 8302337235385942498L;
	
	public static boolean setup() {
        return setup( new SuperEarthLaf() );
    }

    @Override
    public String getName() {
        return "SuperEarthLaf";
    }
}
