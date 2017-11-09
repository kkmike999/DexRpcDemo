package com.example.dex.socket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kkmike999 on 2017/11/09.
 */
public class SocketReceiver {

    ServerSocket mServer;
    int          mPort;

    public SocketReceiver(int port) throws IOException {
        mPort = port;

        mServer = new ServerSocket(port);
        mServer.setSoTimeout(0);// 无限时间
    }

    /**
     * 监听端口，并把接收到的文件，写入本地储存
     *
     * @param path 接收的文件，写入路径
     *
     * @return 写入路径
     */
    public String accept(String path) throws IOException {
        Socket socket = mServer.accept();

        writeFile(socket, path);

        socket.close();

        return path;
    }

    public void close() throws IOException {
        mServer.close();
    }

    /**
     * 从socket读取文件，写入本地
     *
     * @param socket
     *
     * @return 写入文件路径
     */
    private void writeFile(Socket socket, String path) throws IOException {
        // 写入文件
        File file = new File(path);

        if (file.exists()) {
            file.delete();
        }

        InputStream      is  = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[1024 * 1024];
        int    hasRead;

        while ((hasRead = is.read(buffer)) >= 0) {
            fos.write(buffer, 0, hasRead);
        }

        fos.flush();
        fos.close();
        is.close();
    }

}
