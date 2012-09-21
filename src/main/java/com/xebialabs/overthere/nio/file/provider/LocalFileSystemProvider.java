package com.xebialabs.overthere.nio.file.provider;

import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;

import static com.xebialabs.overthere.local.LocalConnection.LOCAL_PROTOCOL;

public class LocalFileSystemProvider extends OverthereFileSystemProvider {
    @Override
    public String getScheme() {
        return LOCAL_PROTOCOL;
    }
}
