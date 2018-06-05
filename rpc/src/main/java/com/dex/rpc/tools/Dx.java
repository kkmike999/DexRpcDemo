package com.dex.rpc.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

        // 例子，"$ANDROID_HOME/build-tools/27.0.1/dx --dex --output=dex.jar myjar.jar"
        File dex = new File(parent, dexName);
        File jar = new File(jarPath);

        String buildToolsVersion = getLastBuildToolsVersion();
        File   dxFile            = new File(ANDROID_HOME + "/build-tools/" + buildToolsVersion + "/dx");
        String dx                = dxFile.getPath() + " --dex --output=" + dexName + " " + jar.getName();

        Bash bash = new Bash();
        bash.cd(parent);
        bash.changeDrive(parent.substring(0,1));
        bash.exec(dx);
        bash.commitAndExit();

        if (!dex.exists()) {
            throw new RuntimeException(dex.getPath() + " 不存在");
        }

        return dex.getPath();
    }

    private void checkEnv(Map<String, String> envMap, String key) {
        if (!envMap.containsKey(key)) {
            throw new RuntimeException("没有$" + key + "环境变量");
        }
    }

    /**
     * 获取sdk build-tools 最新版本
     *
     * @return
     */
    private String getLastBuildToolsVersion() {
        File buildToolFile = new File(ANDROID_HOME + "/build-tools/");

        if (!buildToolFile.exists()) {
            throw new RuntimeException(buildToolFile.getPath() + " not exist.");
        }

        List<String> names = new ArrayList<>(Arrays.asList(buildToolFile.list()));
        names.remove(".DS_Store");

        if (names.size() > 0) {
            // 按build tools version 大到小排序
            Collections.sort(names, new Comparator<String>() {
                @Override
                public int compare(String name1, String name2) {
                    name1 = name1.replace(".", "");
                    name2 = name2.replace(".", "");
                    return -(Integer.valueOf(name1) - Integer.valueOf(name2));
                }
            });

            String buildToolsVersion = names.get(0);

            return buildToolsVersion;
        } else {
            throw new RuntimeException(buildToolFile.getPath() + " has no build tool");
        }
    }
}
