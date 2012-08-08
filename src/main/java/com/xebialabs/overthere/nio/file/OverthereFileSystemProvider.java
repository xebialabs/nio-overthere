package com.xebialabs.overthere.nio.file;

import static com.google.common.collect.Maps.newHashMap;
import static com.xebialabs.overthere.ssh.SshConnectionBuilder.SSH_PROTOCOL;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.google.common.collect.Maps;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;

public abstract class OverthereFileSystemProvider extends FileSystemProvider {
    Map<URI, OverthereFileSystem> cache = newHashMap();

    @Override
    public synchronized FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        if (cache.containsKey(uri)) {
            throw new FileSystemAlreadyExistsException(uri.toString());
        }
        ConnectionOptions options = buildOptionsFromUri(uri);
        copyEnvironment(env, options);

        OverthereConnection connection = getConnection(options);
        OverthereFileSystem overthereFileSystem = new OverthereFileSystem(this, connection, uri);
        cache.put(uri, overthereFileSystem);
        return overthereFileSystem;
    }

    private ConnectionOptions buildOptionsFromUri(final URI uri) {
        ConnectionOptions options = new ConnectionOptions();
        copyHostAndPort(uri, options);
        copyUserInfo(uri, options);
        copyQuery(uri, options);
        return options;
    }

    protected OverthereConnection getConnection(final ConnectionOptions options) {
        return Overthere.getConnection(getScheme(), options);
    }


    protected void copyHostAndPort(URI uri, ConnectionOptions options) {
        String host = uri.getHost();
        if (host != null) {
            options.set(ConnectionOptions.ADDRESS, host);
        }
        int port = uri.getPort();
        if (port != -1) {
            options.set(ConnectionOptions.PORT, port);
        }
    }

    protected void copyUserInfo(URI uri, ConnectionOptions options) {
        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            String[] userInfoParts = userInfo.split(":", 2);
            if (userInfoParts.length >= 1) {
                options.set(ConnectionOptions.USERNAME, userInfoParts[0]);
                if (userInfoParts.length >= 2) {
                    options.set(ConnectionOptions.PASSWORD, userInfoParts[1]);
                }
            }
        }
    }

    protected void copyQuery(URI uri, ConnectionOptions options) {
        List<NameValuePair> parsedQuery = URLEncodedUtils.parse(uri, null);
        for (NameValuePair queryEntry : parsedQuery) {
            options.set(queryEntry.getName(), queryEntry.getValue());
        }
    }

    protected void copyEnvironment(Map<String, ?> env, ConnectionOptions options) {
        for (Map.Entry<String, ?> envEntry : env.entrySet()) {
            options.set(envEntry.getKey(), envEntry.getValue());
        }
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        if (!cache.containsKey(uri)) {
            throw new FileSystemNotFoundException(uri.toString());
        }
        return cache.get(uri);
    }

    @Override
    public Path getPath(URI uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) throws IOException {
        OverthereFile ofile = ((OvertherePath) path).getOverthereFile();
        final InputStream in = ofile.getInputStream();
        return new SeekableByteChannel() {
            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public void close() throws IOException {
                in.close();
            }

            @Override
            public int write(ByteBuffer src) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public SeekableByteChannel truncate(long size) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public long size() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read(ByteBuffer dst) throws IOException {
                byte[] buf = new byte[dst.remaining()];
                int bytesRead = in.read(buf);
                dst.put(buf, 0, bytesRead);
                return bytesRead;
            }

            @Override
            public SeekableByteChannel position(long newPosition) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public long position() throws IOException {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createDirectory(final Path dir, final FileAttribute<?>... attrs) throws IOException {
        ((OvertherePath) dir).getOverthereFile().mkdir();
    }

    @Override
    public void delete(Path path) throws IOException {
        ((OvertherePath) path).getOverthereFile().delete();
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
