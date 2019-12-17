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
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread {

    public final static int PORT = 7331;

    private final static int BUFFER = 1024;
    private int choice = 0;

    private DatagramSocket socket;
    private ArrayList<InetAddress> clientAddresses;
    private ArrayList<Integer> clientPorts;

    private HashSet<String> existingClients;
    private Thread PointTPoint;
    private int index;

    public ChatServer() throws IOException {
        socket = new DatagramSocket(PORT);
        clientAddresses = new ArrayList();
        clientPorts = new ArrayList();
        existingClients = new HashSet();
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String content = new String(buf, buf.length);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String id = clientAddress.toString() + "," + clientPort;
                if (!existingClients.contains(id)) {
                    existingClients.add(id);
                    clientPorts.add(clientPort);
                    clientAddresses.add(clientAddress);
                }

                //  System.out.println(id + " : " + content);
                byte[] data = content.getBytes();

                InetAddress cl;
                int cp;
                PointTPoint = new Thread() {

                    public void run() {
                        Scanner sc2 = new Scanner(System.in);;
                        choice = -1;
                        System.out.println("Enter 1 for broadcast ");
                        System.out.println("Enter 0 for point to point \nChoose From Available port to which client you want to send\n");
                        choice = sc2.nextInt();
                        System.out.println("choice = " + choice);

                        while (choice == -1) {

                            System.out.println("Not Valid");
                            System.out.println("Enter 1 for broadcast ");
                            System.out.println("Enter 0 for point to point \nChoose From Available port to which client you want to send\n");
                            choice = sc2.nextInt();

                        }
                    }
                };
                PointTPoint.start();
                PointTPoint.join();

                if (choice == 0) {
                     int f;
                     f=packet.getPort();
                    for (int i = 0; i < clientAddresses.size(); i++) {

                        if ((clientPorts.get(i).equals(f))) {
                            continue;
                        } else {
                            System.out.println("Client " + i + " add " + clientAddresses.get(i) + " port " + clientPorts.get(i));

                        }

                    }
                    if (!(clientAddresses.size() <= 1)) {

                        System.out.println("enter the receiver");
                        Scanner sc3 = new Scanner(System.in);
                        index = sc3.nextInt();
                        cl = clientAddresses.get(index);
                        cp = clientPorts.get(index);
                        packet = new DatagramPacket(data, data.length, cl, cp);

                        socket.send(packet);

                    } else {
                        System.out.println("There's no other client to send message to it   ");

                    }
                } else if (choice == 1) {
                    if (!(clientAddresses.size() <= 1)) {
                            int f=packet.getPort();
                        for (int i = 0; i < clientAddresses.size(); i++) {

                            if ((clientPorts.get(i).equals(f))) {
                                                           continue;

                            }
                         
                                                  cl = clientAddresses.get(i);
                            cp = clientPorts.get(i);
                            packet = new DatagramPacket(data, data.length, cl, cp);

                            socket.send(packet);
                        
                        
                        }
                        

                        
                    } else {
                        System.out.println("There's one client \n broadcast not available ");
                    }
                    
                }
                else
                    System.out.println("Enter a Right choice");
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String args[]) throws Exception {

        ChatServer s = new ChatServer();
        s.start();
    }
}
