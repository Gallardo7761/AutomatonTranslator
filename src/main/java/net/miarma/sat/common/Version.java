package net.miarma.sat.common;

import java.util.Objects;

public record Version(Integer release, Integer major, Integer minor) implements Comparable<Version> {
    public Version {
        if (release == null || major == null || minor == null) {
            throw new IllegalArgumentException("Version numbers cannot be null");
        }
        if (release < 0 || major < 0 || minor < 0) {
            throw new IllegalArgumentException("Version numbers cannot be negative");
        }
    }

    public static Version of(Integer release, Integer major, Integer minor) {
        return new Version(release, major, minor);
    }

    @Override
    public int compareTo(Version other) {
        if (other == null) return 1;
        int result = release.compareTo(other.release);
        if (result != 0) return result;
        result = major.compareTo(other.major);
        if (result != 0) return result;
        return minor.compareTo(other.minor);
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d", release, major, minor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Version other = (Version) obj;
        return Objects.equals(release, other.release) && 
               Objects.equals(major, other.major) && 
               Objects.equals(minor, other.minor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(release, major, minor);
    }
}