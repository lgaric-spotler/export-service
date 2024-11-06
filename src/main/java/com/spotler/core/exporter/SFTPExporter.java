package com.spotler.core.exporter;

import com.google.inject.Inject;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

public class SFTPExporter {

    private final String host;
    private final String username;
    private final String password;

    @Inject
    public SFTPExporter(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public void uploadFile(String data, String remoteFilePath) throws Exception {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try (InputStream inputStreamData = new ByteArrayInputStream(data.getBytes())) {
            session = jsch.getSession(this.username, this.host, 22);
            session.setPassword(this.password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.put(inputStreamData, remoteFilePath);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
