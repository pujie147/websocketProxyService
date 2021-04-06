package com.vdegree.february.im.common.utils;

import lombok.extern.log4j.Log4j2;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * ip工具
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/6 16:16
 */
@Log4j2
public class IPUtils {
    /**
     * 获取本机的局域网IP地址,兼容Linux
     * @return String
     */
    public static String getLocalHostIP(){
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            String localHostAddress = "";
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> address = networkInterface.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress inetAddress = address.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        localHostAddress = inetAddress.getHostAddress();
                    }
                }
            }
            return localHostAddress;
        }catch (Exception e){
            log.error(e);
        }
        return null;
    }
}
