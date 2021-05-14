package com.jlpay.qrcode.external.commons.util;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author qihuaiyuan
 * @since 2021/2/6
 */
public class NetworkUtil {

    private static String localHostIp;

    static {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            localHostIp = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            // swallow
            localHostIp = "127.0.0.1";
        }
    }

    private NetworkUtil() {
    }

    public static String getLocalHostIp() {
        return localHostIp;
    }
}
