package com.xebialabs.overthere.nio.file.provider;

import static com.xebialabs.overthere.ssh.SshConnectionBuilder.SSH_PROTOCOL;

import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;

public class SshFileSystemProvider extends OverthereFileSystemProvider {
	@Override
	public String getScheme() {
		return SSH_PROTOCOL;
	}
}
