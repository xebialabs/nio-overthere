package com.xebialabs.overthere.nio.file;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.xebialabs.overcast.CloudHost;
import com.xebialabs.overcast.CloudHostFactory;

public class SshProtocolTest {

    private CloudHost overthere_ssh;
    private FileSystem fileSystem;
    private FileSystem localFileSystem;

    @BeforeClass
    public void setupHost() {
//        overthere_ssh = CloudHostFactory.getCloudHost("overthere_ssh");
//        overthere_ssh.setup();
    }

    @AfterClass
    public void teardownHost() {
//        overthere_ssh.teardown();
    }

    @BeforeMethod
    public void setup() {
        fileSystem = FileSystems.getFileSystem(URI.create("ssh+sftp://overthere@172.16.119.129/?os=UNIX&password=overhere"));
        localFileSystem = FileSystems.getDefault();
    }

    @Test
    public void shouldCopyToRemote() throws IOException {
        Path localPath = localFileSystem.getPath("/Users/ajvanerp/temp.txt");
        Path remotePath = fileSystem.getPath("/home/overthere/temp.txt");
        Files.copy(localPath, remotePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
