package com.xebialabs.overthere.nio.file;

import static java.util.Collections.singleton;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

import com.google.common.base.Joiner;
import com.xebialabs.overthere.OverthereConnection;

public class OverthereFileSystem extends FileSystem {

	private OverthereFileSystemProvider provider;

	private OverthereConnection connection;

	public OverthereFileSystem(OverthereFileSystemProvider provider, OverthereConnection connection) {
		this.provider = provider;
		this.connection = connection;
    }

	@Override
	public FileSystemProvider provider() {
		return provider;
	}

	@Override
	public void close() throws IOException {
		connection.close();
		connection = null;
	}

	@Override
	public boolean isOpen() {
		return connection != null;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public String getSeparator() {
		return connection.getHostOperatingSystem().getFileSeparator();
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		return singleton(getRoot());
	}

	protected Path getRoot() {
	    return getPath("/");
    }

	@Override
	public Iterable<FileStore> getFileStores() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getPath(String first, String... more) {
		String sep = getSeparator();
		StringBuilder path = new StringBuilder();
		path.append(first);
		if(more.length > 0) {
			path.append(sep);
			Joiner.on(sep).skipNulls().appendTo(path, more);
		}
		return new OvertherePath(this, path.toString());
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WatchService newWatchService() throws IOException {
		throw new UnsupportedOperationException();
	}

	OverthereConnection getConnection() {
	    return connection;
	}

}
