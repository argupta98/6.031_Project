package examples;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class HostnameExample {

    /**
     * Print the IPv4 hostnames that can be used to reach this machine.
     * Falls back to an IP address if it can't find a hostname.
     * Some of these addresses may not be usable from remote machines:
     * examples include localhost, 10.x.x.x, 172.x.x.x, 192.x.x.x.
     * 
     * @param args unused
     * @throws IOException if network problem
     */
    public static void main(String[] args) throws IOException {
        for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (InetAddress address: Collections.list(iface.getInetAddresses())) {
                if (address instanceof Inet4Address) {
                    System.out.println(address.getHostName());
                }
            }
        }
    }

}
