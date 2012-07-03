package com.xebialabs.overthere.nio.file;

import static com.google.common.base.Joiner.on;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;

public class OvertherePath implements Path {

	private OverthereFileSystem fileSystem;

	private List<String> segments;

	public OvertherePath(OverthereFileSystem fileSystem, List<String> segments) {
		this.fileSystem = fileSystem;
		this.segments = segments;
    }

	@Override
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public boolean isAbsolute() {
		return true;
	}

	@Override
	public Path getRoot() {
		return fileSystem.getRoot();
	}

	@Override
	public Path getFileName() {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Path getParent() {
		if(segments.isEmpty()) {
			return null;
		}

		return new OvertherePath(fileSystem, segments.subList(0, segments.size() - 1));
	}

	@Override
	public int getNameCount() {
		return segments.size();
	}

	@Override
	public Path getName(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean startsWith(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean startsWith(String other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean endsWith(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean endsWith(String other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path normalize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path resolve(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path resolve(String other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path resolveSibling(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path resolveSibling(String other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path relativize(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URI toUri() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path toAbsolutePath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public File toFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Path> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(Path other) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		String sep = getFileSystem().getSeparator();
		return sep + on(sep).join(segments);
	}

}
