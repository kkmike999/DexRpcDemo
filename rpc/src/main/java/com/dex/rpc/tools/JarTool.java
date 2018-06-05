package com.dex.rpc.tools;

import java.io.File;

/**
 * Created by kkmike999 on 2017/11/10.
 */
public class JarTool {

    String classDir = "";
    String jarName  = "";

    /**
     * @param classDir class文件目录
     * @param jarName  打包出来jar文件名
     */
    public JarTool(String classDir, String jarName) {
        this.classDir = classDir;
        this.jarName = jarName;
    }

    /**
     * 把class目录打包成jar文件
     *
     * @return jar文件路径
     */
    public String packageClass() {
        // 如果存在旧文件，先删掉
        File jar = new File(getJarPath());
        if (jar.exists()) {
            jar.delete();
        }

        // 打包com目录为jar
        Bash bash = new Bash();
        bash.cd(classDir);
        bash.changeDrive(classDir.substring(0, 1));
        bash.exec("jar -cvf " + jarName + " .");
        bash.commit();
        bash.close();

        return getJarPath();
    }

    public String getJarPath() {
        return new File(classDir, jarName).getPath();
    }
}
