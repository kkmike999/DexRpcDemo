# 开发指南

## 代码

`RPCTest`为打包发送dex单元测试：
```
public class RPCTest {

    @Test
    public void rpc() throws Exception {
        Bash.DEBUG = false;

        // "192.168.1.154"修改为app当前ip
        RPC rpc = new RPC("com.example.dex", "192.168.1.154", 10086);
        rpc.remoteRun();
    }
}
```

`DexTask.run()`方法里，写调试代码:
```
public class DexTask implements Runnable {
    @Override
    public void run() {
        ...
    }
}
```

## 调试

#### 1.修改Working Directory

> `Run -> Edit Configurations -> Defaults -> Android Junit -> Working Directory` 配置成 `$MODULE_DIR$`

已存在在junit配置，都要修改`Working Directory`。

#### 2.运行单元测试

运行app。

执行`RPCTest.rpc()`单元测试，程序会编译、打包整个单元测试为dex文件，发送给app。app 10240端口接受到dex文件，自动查找`com.example.dex.DexTask`类，并执行`run()`。

执行效果：

![](demo.gif)