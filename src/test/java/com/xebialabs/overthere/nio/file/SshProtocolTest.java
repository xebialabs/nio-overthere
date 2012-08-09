package com.xebialabs.overthere.nio.file;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import org.testng.annotations.*;

import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

import com.xebialabs.overcast.CloudHost;
import com.xebialabs.overcast.CloudHostFactory;

import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.MatcherAssert.assertThat;

public class SshProtocolTest {

    private CloudHost overthere_ssh;
    private FileSystem fileSystem;
    private FileSystem localFileSystem;

//    @BeforeClass
    public void setupHost() {
        overthere_ssh = CloudHostFactory.getCloudHost("overthere_ssh");
        overthere_ssh.setup();
    }

//    @AfterClass
    public void teardownHost() {
        overthere_ssh.teardown();
    }

//    @BeforeMethod
    public void setup() throws IOException {
        fileSystem = FileSystems.newFileSystem(URI.create("ssh+sftp://overthere@" + overthere_ssh.getHostName() + "/?os=UNIX&password=overhere"), Maps.<String, Object>newHashMap());
        localFileSystem = FileSystems.getDefault();
    }

//    @AfterMethod
    public void closeAll() {
        Closeables.closeQuietly(fileSystem);
    }

//    @Test
    public void shouldCopyToRemote() throws IOException {
        Path localPath = localFileSystem.getPath("/Users/ajvanerp/temp.txt");
        Path remotePath = fileSystem.getPath("/home/overthere/temp.txt");
        Files.copy(localPath, remotePath, StandardCopyOption.REPLACE_EXISTING);
        assertThat("temp.txt should exist after copy", Files.exists(remotePath));
    }
}
