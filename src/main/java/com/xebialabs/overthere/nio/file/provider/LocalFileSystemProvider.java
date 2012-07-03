package com.xebialabs.overthere.nio.file.provider;

import com.xebialabs.overthere.local.LocalConnection;
import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;

public class LocalFileSystemProvider extends OverthereFileSystemProvider {
	@Override
	public String getScheme() {
		return LocalConnection.LOCAL_PROTOCOL;
	}
}
