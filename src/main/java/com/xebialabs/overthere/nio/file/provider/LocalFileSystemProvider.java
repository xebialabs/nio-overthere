package com.xebialabs.overthere.nio.file.provider;

import static com.xebialabs.overthere.local.LocalConnection.LOCAL_PROTOCOL;

import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;

public class LocalFileSystemProvider extends OverthereFileSystemProvider {
	@Override
	public String getScheme() {
		return LOCAL_PROTOCOL;
	}
}
