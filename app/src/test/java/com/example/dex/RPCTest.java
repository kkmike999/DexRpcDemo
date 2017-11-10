package com.example.dex;

import com.dex.rpc.RPC;
import com.dex.rpc.tools.Bash;

import org.junit.Test;

/**
 * Created by kkmike999 on 2017/11/01.
 */
public class RPCTest {

    @Test
    public void rpc() throws Exception {
        Bash.DEBUG = false;

        RPC rpc = new RPC("192.168.1.154", 10086);
        rpc.remoteRun();
    }
}
