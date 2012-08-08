package com.xebialabs.overthere.nio.file.provider;

import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.CIFS_PROTOCOL;

import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;

public class CifsFileSystemProvider extends OverthereFileSystemProvider {
    @Override
    public String getScheme() {
        return CIFS_PROTOCOL;
    }
}
