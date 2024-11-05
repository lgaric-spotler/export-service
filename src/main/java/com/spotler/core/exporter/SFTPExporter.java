package com.spotler.core.exporter;

import com.google.inject.Inject;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.spotler.ExportServiceConfiguration;
import com.spotler.jobs.config.DataLakeConfiguration;

import java.io.InputStream;
import java.util.Properties;

public class SFTPExporter {

    private final DataLakeConfiguration config;

    @Inject
    public SFTPExporter(ExportServiceConfiguration exportServiceConfiguration) {
        this.config = exportServiceConfiguration.getDataLake();
    }

    public void uploadFile(InputStream data, String remoteFilePath) throws Exception {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(config.getSftpUsername(), config.getSftpHost(), 22);
            session.setPassword(config.getSftpPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.put(data, remoteFilePath);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
            data.close();
        }
    }
}
