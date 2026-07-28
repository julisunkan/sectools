package com.netsec.toolkit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtils {

    public static boolean isReachable(String host, int timeoutMs) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            return addr.isReachable(timeoutMs);
        } catch (Exception e) {
            return false;
        }
    }

    public static long ping(String host) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            long start = System.currentTimeMillis();
            boolean reached = addr.isReachable(3000);
            long end = System.currentTimeMillis();
            return reached ? (end - start) : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isTcpPortOpen(String host, int port, int timeoutMs) {
        try (Socket s = new Socket()) {
            s.connect(new java.net.InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> getLocalIpAddresses() {
        List<String> result = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(ifaces)) {
                if (!iface.isUp() || iface.isLoopback()) continue;
                for (java.net.InterfaceAddress ia : iface.getInterfaceAddresses()) {
                    InetAddress addr = ia.getAddress();
                    if (addr.isLoopbackAddress()) continue;
                    if (addr instanceof java.net.Inet4Address) {
                        result.add(iface.getName() + ": " + addr.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            result.add("Error: " + e.getMessage());
        }
        return result;
    }

    public static String getSubnet(Context ctx) {
        try {
            WifiManager wm = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wm == null) return null;
            WifiInfo wi = wm.getConnectionInfo();
            int ip = wi.getIpAddress();
            if (ip == 0) return null;
            return String.format("%d.%d.%d", ip & 0xFF, (ip >> 8) & 0xFF, (ip >> 16) & 0xFF);
        } catch (Exception e) {
            return null;
        }
    }

    public static String ipIntToString(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    public static String readArpTable() {
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader("/proc/net/arp"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            return "Error reading ARP table: " + e.getMessage();
        }
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
}
