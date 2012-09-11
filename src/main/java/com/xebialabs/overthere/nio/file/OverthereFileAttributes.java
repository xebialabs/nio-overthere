package com.xebialabs.overthere.nio.file;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import com.xebialabs.overthere.OverthereFile;

public class OverthereFileAttributes implements BasicFileAttributes {
    private OverthereFile file;

    public OverthereFileAttributes(final OverthereFile file) {
        this.file = file;
    }

    @Override
    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    @Override
    public FileTime lastAccessTime() {
        return lastModifiedTime();
    }

    @Override
    public FileTime creationTime() {
        return lastModifiedTime();
    }

    @Override
    public boolean isRegularFile() {
        return file.isFile();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        // TODO
        return false;
    }

    @Override
    public boolean isOther() {
        return false;
    }

    @Override
    public long size() {
        return file.length();
    }

    @Override
    public Object fileKey() {
        return null;
    }
}
