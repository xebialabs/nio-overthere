package com.xebialabs.overthere.nio.file;

import static com.xebialabs.overthere.ssh.SshConnectionBuilder.SSH_PROTOCOL;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;

public class OverthereFileSystemProvider extends FileSystemProvider {

	@Override
	public String getScheme() {
		return "overthere";
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		ConnectionOptions options = new ConnectionOptions();

		copyHostAndPort(uri, options);
		copyUserInfo(uri, options);
		copyQuery(uri, options);
		copyEnvironment(env, options);

		OverthereConnection connection = Overthere.getConnection(SSH_PROTOCOL, options);
		return new OverthereFileSystem(this, connection);
	}

	protected void copyHostAndPort(URI uri, ConnectionOptions options) {
	    String host = uri.getHost();
		if(host != null) {
			options.set(ConnectionOptions.ADDRESS, host);
		}
		int port = uri.getPort();
		if(port != -1) {
			options.set(ConnectionOptions.PORT, port);
		}
    }

	protected void copyUserInfo(URI uri, ConnectionOptions options) {
	    String userInfo = uri.getUserInfo();
		if(userInfo != null) {
			String[] userInfoParts = userInfo.split(":", 2);
			if(userInfoParts.length >= 1) {
				options.set(ConnectionOptions.USERNAME, userInfoParts[0]);
				if(userInfoParts.length >= 2) {
					options.set(ConnectionOptions.PASSWORD, userInfoParts[1]);
				}
			}
		}
    }

	protected void copyQuery(URI uri, ConnectionOptions options) {
		List<NameValuePair> parsedQuery = URLEncodedUtils.parse(uri, null);
		for(NameValuePair queryEntry: parsedQuery) {
			options.set(queryEntry.getName(), queryEntry.getValue());
		}
    }

	protected void copyEnvironment(Map<String, ?> env, ConnectionOptions options) {
	    for(Map.Entry<String,?> envEntry : env.entrySet()) {
			options.set(envEntry.getKey(), envEntry.getValue());
		}
    }

	@Override
	public FileSystem getFileSystem(URI uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getPath(URI uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Path path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

}
