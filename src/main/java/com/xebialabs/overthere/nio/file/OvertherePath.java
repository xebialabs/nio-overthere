package com.xebialabs.overthere.nio.file;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

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

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

public class OvertherePath implements Path {

	private OverthereFileSystem fileSystem;

	private List<String> segments;
	
	private boolean absolute;

	OvertherePath(OverthereFileSystem fileSystem, String path) {
		this.fileSystem = fileSystem;
		String sep = fileSystem.getSeparator();
		this.absolute = path.startsWith(sep);
		this.segments = newArrayList(Splitter.on(sep).omitEmptyStrings().split(path));
	}

	OvertherePath(OverthereFileSystem fileSystem, List<String> segments, boolean absolute) {
		this.fileSystem = fileSystem;
		this.segments = segments;
		this.absolute = absolute;
    }

	@Override
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public Path getRoot() {
		return absolute ? fileSystem.getRoot() : null;
	}

	@Override
	public boolean isAbsolute() {
		return absolute;
	}

	@Override
	public Path getFileName() {
		int last = segments.size();
		return slicePath(last - 1, last, false);
	}

	@Override
	public Path getParent() {
		return slicePath(0, segments.size() - 1, absolute);
	}

	@Override
	public int getNameCount() {
		return segments.size();
	}

	@Override
	public Path getName(int index) {
		checkArgument(index >= 0 && index < segments.size(), "Cannot call getName with index = %d on path: %s", index, toString());
		return slicePath(index, index + 1, false);
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		checkArgument(beginIndex >= 0 && beginIndex < segments.size(), "Cannot call subpath with beginIndex = %s on path: %s", beginIndex, toString());
		checkArgument(endIndex > beginIndex && endIndex <= segments.size(), "Cannot call subpath with endIndex = %s on path: %s", endIndex, toString());
		return slicePath(beginIndex, endIndex, false);
	}

	private Path slicePath(int beginIndex, int endIndex, boolean absolute) {
		if(segments.isEmpty()) {
			return null;
		}

		return new OvertherePath(fileSystem, segments.subList(beginIndex, endIndex), absolute);
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
		return (absolute ? sep : "") + on(sep).join(segments);
	}

}
