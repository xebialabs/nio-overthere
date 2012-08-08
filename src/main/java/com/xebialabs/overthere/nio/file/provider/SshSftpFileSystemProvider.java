package com.xebialabs.overthere.nio.file.provider;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;

import static com.xebialabs.overthere.ssh.SshConnectionBuilder.SSH_PROTOCOL;

public class SshSftpFileSystemProvider extends OverthereFileSystemProvider {
    @Override
    public String getScheme() {
        return SSH_PROTOCOL + "+sftp";
    }

    @Override
    protected OverthereConnection getConnection(final ConnectionOptions options) {
        options.set(SshConnectionBuilder.CONNECTION_TYPE, SshConnectionType.SFTP);
        return Overthere.getConnection(SSH_PROTOCOL, options);
    }
}
