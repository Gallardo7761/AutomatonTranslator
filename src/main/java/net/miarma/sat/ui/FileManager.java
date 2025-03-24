package net.miarma.sat.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileManager {
	private File file;
	
	public FileManager(File file) {
		this.file = file;
	}
	
	public FileManager(String path) {
		this.file = new File(path);
	}
	
	public File getFile() {
		return this.file;
	}
	
	public String getFileName() {
		return this.file.getName();
	}
	
	public String getFilePath() {
		return this.file.getAbsolutePath();
	}
	
	public String getFileParent() {
		return this.file.getParent();
	}
	
	public boolean isFile() {
		return this.file.isFile();
	}
	
	public boolean isDirectory() {
		return this.file.isDirectory();
	}
	
	public boolean exists() {
		return this.file.exists();
	}
	
	public Stream<String> read() throws IOException {
		return Files.readAllLines(this.file.toPath()).stream();
	}
	
	public String readAsString() throws IOException {
		return Files.readString(this.file.toPath());
	}
	
	public void write(String content) throws IOException {
		Files.write(this.file.toPath(), content.getBytes());
	}
}
