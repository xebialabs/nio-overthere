package com.xebialabs.overthere.nio.process;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereProcess;
import com.xebialabs.overthere.local.LocalConnection;
import com.xebialabs.overthere.nio.file.OverthereFileSystem;
import com.xebialabs.overthere.util.ConsoleOverthereProcessOutputHandler;

import static com.xebialabs.overthere.util.ConsoleOverthereProcessOutputHandler.consoleHandler;

/**
 * Implementation that abstracts processes over paths.
 */
public class Processes {

    public static int execute(Path path, String... arguments) {
        FileSystem fileSystem = path.getFileSystem();
        OverthereConnection connection = null;
        if (fileSystem instanceof OverthereFileSystem) {
            connection = ((OverthereFileSystem) fileSystem).getConnection();
        } else if (fileSystem.equals(FileSystems.getDefault())) {
            connection = LocalConnection.getLocalConnection();
        } else throw new IllegalArgumentException("FileSystem " + fileSystem + " is not supported for command execution.");

        final CmdLine commandLine = toCommandLine(path, arguments);
        return connection.execute(consoleHandler(), commandLine);
    }

    private static CmdLine toCommandLine(final Path path, final String[] arguments) {
        final CmdLine commandLine = CmdLine.build(path.toString());
        for (String argument : arguments) {
            commandLine.addArgument(argument);
        }
        return commandLine;
    }
}
