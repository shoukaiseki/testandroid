package com.example.shoukaiseki.databuilding;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by shoukaiseki on 2017/12/25.
 */

public class PingUtils {

    public static boolean ping(String host,  StringBuffer stringBuffer) {
        int pingCount=4;
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
        BufferedReader errorReader = null;
        String command = "ping -c " + pingCount + " -w 5 " + host;
//        command = "ping -c " + pingCount + " " + host;
        command="/system/bin/ping  -c 4 -s 64 -i 1 " + host;
        boolean isSuccess = false;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                LogUtil.e("ping fail:process is null.");
                append(stringBuffer, "ping fail:process is null.");
                return false;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader( process.getErrorStream()));
            while ((line = successReader.readLine()) != null) {
                LogUtil.i(line);
                append(stringBuffer, line);
            }
            int status = process.waitFor();
            if (status == 0) {
                LogUtil.i("exec cmd success:" + command);
                append(stringBuffer, "exec cmd success:" + command);
                isSuccess = true;
            } else {
                LogUtil.e("exec cmd fail.");
                append(stringBuffer, "exec cmd fail.");
                isSuccess = false;
            }
            LogUtil.i("exec finished.");
            append(stringBuffer, "exec finished.");
            while ((line = errorReader.readLine()) != null) {
                append(stringBuffer,line);
            }
        } catch (IOException e) {
            LogUtil.e(e);
        } catch (InterruptedException e) {
            LogUtil.e(e);
        } finally {
            LogUtil.i("ping exit.");
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        }
        return isSuccess;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }
}
