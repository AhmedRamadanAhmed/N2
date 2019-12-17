/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP2;

/**
 *
 * @author TheSeeker
 */
import java.util.Random;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class MessageSender implements Runnable {

    public final static int PORT = 7331;
    private DatagramSocket sock;
    private String hostname;
    private int ClientIn;
    MessageSender(DatagramSocket s, String h,int ClientInd) {
        sock = s;
        hostname = h;
        ClientIn=ClientInd;
    }

    private void sendMessage(String s) throws Exception {
        s="Client "+ClientIn+"  :  "+ s;
        byte buf[] = s.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        sock.send(packet);
        
    }

    public void run() {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                while (!in.ready()) {
                    Thread.sleep(100);
                }
                
                sendMessage(in.readLine() );
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}

class MessageReceiver implements Runnable {

    DatagramSocket sock;
    byte buf[];

    MessageReceiver(DatagramSocket s) {
        sock = s;
        buf = new byte[1024];
    }

    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}

public class ChatClient {

    InetAddress inetAddress;

    DatagramSocket socket;
    MessageReceiver r;
    MessageSender s;
    Thread rt;
    Thread st;
    String host;
    int ClientN;

    public ChatClient() {

        try {
            Random rand = new Random();
            ClientN = rand.nextInt(50);
            inetAddress = InetAddress.getLocalHost();
            try {
                socket = new DatagramSocket();
            } catch (SocketException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            host = inetAddress.getHostAddress();
            r = new MessageReceiver(socket);
            s = new MessageSender(socket, host,this.ClientN);
            rt = new Thread(r);
            st = new Thread(s);
            rt.start();
            st.start();

        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String args[]) throws Exception {

        ChatClient c1 = new ChatClient();

        System.out.println("Client " + c1.ClientN);

    }
}
