package com.dex.rpc;

import com.dex.rpc.tools.Bash;
import com.dex.rpc.tools.Dx;
import com.dex.rpc.tools.SocketSender;

import java.io.File;

/**
 * Created by kkmike999 on 2017/11/01.
 */
public class RPC {

    String hostname;
    int    port;
    String packageName;

    public RPC(String packageName, String hostname, int port) {
        this.packageName = packageName;
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * 打包dex，并发送到客户端执行
     */
    public void remoteRun() {
        String dir = new File("build/intermediates/classes/test/debug").getAbsolutePath();
        File   jar = new File(dir, "myjar.jar");
        jar.delete();

        String packageDir = packageName.split("\\.")[0];

        // 打包com目录为jar
        Bash bash = new Bash();
        bash.cd(dir);
        bash.exec("jar -cvf myjar.jar " + packageDir);// jar目录下的co文件夹
        bash.commit();
        bash.close();

        if (jar.exists()) {
            // dx
            Dx     dx      = new Dx();
            String dexPath = dx.dx(jar.getPath(), "dex.jar");
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
