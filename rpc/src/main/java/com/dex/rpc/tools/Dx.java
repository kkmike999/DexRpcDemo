package com.dex.rpc.tools;

import java.io.File;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/10/31.
 * <p>
 * Android dx工具
 */
public class Dx {

    String ANDROID_HOME = "";

    public Dx(String ANDROID_HOME) {
        this.ANDROID_HOME = ANDROID_HOME;
    }

    public Dx() {
        // 获取系统环境变量
        Map<String, String> map = System.getenv();

        checkEnv(map, "ANDROID_HOME");

        ANDROID_HOME = map.get("ANDROID_HOME").toString();
    }

    /**
     * 执行dx命令，jar-->dex
     *
     * @param jarPath jar路径
     * @param dexName dex文件名（建议使用.jar拓展名）
     *
     * @return dex路径
     */
    public String dx(String jarPath, String dexName) {
        String parent = new File(jarPath).getParent();

        // "$ANDROID_HOME/build-tools/26.0.2/dx --dex --output=dex.jar myjar.jar"
        File   dex = new File(parent, dexName);
        File   jar = new File(jarPath);
        String dx  = ANDROID_HOME + "/build-tools/26.0.2/dx --dex --output=" + dexName + " " + jar.getName();

        File dxFile = new File(ANDROID_HOME + "/build-tools/26.0.2/dx");

        if (!dxFile.exists()) {
            throw new RuntimeException(dxFile.getPath() + " 不存在");
        }

        Bash bash = new Bash();
        bash.cd(parent);
        bash.exec(dx);
        bash.commitAndExit();

        return dex.getPath();
    }

    private void checkEnv(Map<String, String> envMap, String key) {
        if (!envMap.containsKey(key)) {
            throw new RuntimeException("没有$" + key + "环境变量");
        }
    }
}
