package com.dex.rpc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by kkmike999 on 2017/10/31.
 * <p>
 * socket发送文件工具类
 */
public class SocketSender {

    String hostname;
    int    port;

    public SocketSender(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void send(String path) {
        try {
            System.out.println("sending");

            Socket socket = new Socket();
            socket.setSoTimeout(10 * 1000);
            socket.connect(new InetSocketAddress(hostname, port));

            OutputStream os = socket.getOutputStream();

            FileInputStream fis = new FileInputStream(path);

            byte[] buffer = new byte[1024 * 1024];
            int    hasRead;

            while ((hasRead = fis.read(buffer)) >= 0) {
                os.write(buffer, 0, hasRead);
            }

            fis.close();
            os.flush();
            os.close();
            socket.close();

            System.out.println("send finish. length=" + new File(path).length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
