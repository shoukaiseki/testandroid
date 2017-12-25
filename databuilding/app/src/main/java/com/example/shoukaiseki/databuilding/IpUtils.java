package com.example.shoukaiseki.databuilding;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shoukaiseki on 2017/12/25.
 */

public class IpUtils {

    public static void getHostIp(String ip,StringBuffer sb){
        try {
            InetAddress[] addresses = InetAddress.getAllByName(ip);
            for (int i = 0; i < addresses.length; i++) {

                InetAddress address=addresses[i];
                    append(sb,ip+"["+i+"]: "+address.getHostAddress()); //args[0]是执行程序时写的参数，
            }
            InetAddress localhost=null;
            localhost = InetAddress.getLocalHost(); //本地地址
            localhost =InetAddress.getLoopbackAddress();
            append(sb,"localhost.localIP: "+getHostIP());
            append(sb,"localhost.netIP: "+getNetIp());
            append(sb,"localhost.Attribution: "+getIpAttribution());

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e);
            sb.append("IpUtils error");
        }
    }

    private static void append(StringBuffer sb, String text) {
        if (sb != null) {
            sb.append(text + "\n");
        }
    }


    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }


    /**
     * 获取外网的IP
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://2017.ip138.com/ic.asp");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "gb2312"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");

                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");

                LogUtil.d(strber.toString());
                Matcher matcher = pattern.matcher(strber.toString());
                while (matcher.find()) {
                    ipLine = ipLine+matcher.group();
                }
                if(ipLine.isEmpty()){
                    pattern = Pattern .compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
                    matcher = pattern.matcher(strber.toString());
                    while (matcher.find()) {
                        ipLine = ipLine+matcher.group();
                    }
                }
                if(ipLine.isEmpty()){
                    pattern = Pattern.compile("(?<=\\[)(.*?)(?=\\])");
                    matcher = pattern.matcher(strber.toString());
                    while (matcher.find()) {
                        ipLine = ipLine+"\n"+matcher.group();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ipLine="getNetIp.error";
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ipLine;
    }


    public static String getIpAttribution(){
        String str="";

        try {
            Document document = Jsoup.connect("http://2017.ip138.com/ic.asp").get();
            str=document.text();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e);
        }

        return str;
    }
}
