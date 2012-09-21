package com.xebialabs.overthere.nio.file.provider;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.nio.file.OverthereFileSystemProvider;

import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.CIFS_PROTOCOL;

public class CifsTelnetFileSystemProvider extends OverthereFileSystemProvider {
    @Override
    public String getScheme() {
        return CIFS_PROTOCOL + "+telnet";
    }

    @Override
    protected OverthereConnection getConnection(final ConnectionOptions options) {
        options.set(CifsConnectionBuilder.CONNECTION_TYPE, CifsConnectionType.TELNET);
        options.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.WINDOWS);
        return Overthere.getConnection(CIFS_PROTOCOL, options);
    }
}
