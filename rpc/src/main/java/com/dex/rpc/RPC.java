package com.dex.rpc;

import com.dex.rpc.tools.Dx;
import com.dex.rpc.tools.JarTool;
import com.dex.rpc.tools.SocketSender;

import java.io.File;

/**
 * Created by kkmike999 on 2017/11/01.
 */
public class RPC {

    String hostname;
    int    port;

    public RPC(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * 打包dex，并发送到客户端执行
     */
    public void remoteRun() {
        String dir = new File("build/intermediates/classes/test/debug").getAbsolutePath();

        JarTool jarTool = new JarTool(dir, "myjar.jar");

        // 打包jar文件
        String jarPath = jarTool.packageClass();
        File   jar     = new File(jarPath);

        if (jar.exists()) {
            // dx
            Dx     dx      = new Dx();
            String dexPath = dx.dx(jarPath, "dex.jar");
            File   dex     = new File(dexPath);

            jar.delete();

            if (dex.exists()) {
                SocketSender sender = new SocketSender(hostname, port);
                sender.send(dexPath);

                dex.delete();
            }
        } else {
            throw new RuntimeException("没有配置Work Directory为$MODULE_DIR$ 或者 目录不对： " + dir);
        }
    }
}
