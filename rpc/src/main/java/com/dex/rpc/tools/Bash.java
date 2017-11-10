package com.dex.rpc.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by kkmike999 on 2017/11/01.
 * <p>
 * linux bash命令封装类
 */
public class Bash {

    public static boolean DEBUG = true;

    Runtime mRuntime;
    private Process        proc;
    private BufferedReader in;
    private PrintWriter    out;

    public Bash() {
        mRuntime = Runtime.getRuntime();

        try {
            bash();
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    /**
     * 定位到某目录（自动处理路径空格问题）
     *
     * @param path
     */
    public void cd(String path) {
        exec("cd " + path);
    }

    public void exec(String commend) {
        exec(new String[]{commend});
    }

    public void exec(String[] commends) {
        checkStatus();

        // 执行多行命令
        for (String commend : commends) {
            debug(commend);

            out.println(commend);
        }
    }

    private void checkStatus() {
        if (out == null) {
            throw new RuntimeException("Please call reset() method before exec()");
        }
    }

    public void bash() throws IOException {
        Properties props  = System.getProperties(); //获得系统属性集
        String     osName = props.getProperty("os.name"); //操作系统名称

        if (osName.startsWith("Windows")) {
            File wd = new File("C:\\Windows\\System32\\");
            proc = mRuntime.exec("C:\\Windows\\System32\\cmd", null, wd);
        } else {
            File wd = new File("/bin");
            proc = mRuntime.exec("/bin/bash", null, wd);
        }

        if (proc != null) {
            in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);

            debug("\n---- 执行/bin/bash:");
        }
    }

    /**
     * 退出bash。当执行exit命令后，java才能捕获输出
     */
    public void commitAndExit() {
        commit();
        close();
    }

    public void commit() {
        // 退出bash
        exit();
        debug("exit\n\n---- bash输出：");

        try {
            String line;
            while ((line = in.readLine()) != null) {
                debug(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        debug("\n--------commited--------\n");

        reset();
    }

    public void close() {

        exit();
        debug("\n--------close--------\n");

        try {
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();

            in = null;
            out = null;
            proc = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        if (out != null) {
            out.println("exit");
        }
    }

    public void reset() {
        try {
            {
                boolean debug = DEBUG;
                DEBUG = false;

                if (proc != null) {
                    close();
                }

                DEBUG = debug;
            }
            bash();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void debug(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

    public static String encodePath(String path) {
        return path.replace(" ", "\\ ");
    }
}
