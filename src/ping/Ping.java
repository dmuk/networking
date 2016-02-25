package ping;
import CMPC3M06.AudioRecorder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import uk.ac.uea.cmp.voip.*;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Ping {
    
    public static void main(String[] args) throws Exception {

        String ServerName = "CMPLEWIN-09";
        int port = 55555;

        DatagramSocket socket = new DatagramSocket3(port);
        InetAddress IPAddress =InetAddress.getByName(ServerName);
        
        AudioRecorder recorder = null;
        try{
          recorder = new AudioRecorder();
        }
        catch(Exception e){
          System.out.println(e);
        }
            
        double overallStart, overallFinish, modStart, modFinish;
        byte[] message;
        for(int i=0;i<20;i++){
            Thread.sleep(32);
            overallStart = System.currentTimeMillis();
            modStart = overallStart;
            message = recorder.getBlock();
            modFinish = System.currentTimeMillis();
            System.out.print("Record Time: " + (modFinish - modStart));
            modStart = modFinish;
            DatagramPacket request = new DatagramPacket(message, message.length,IPAddress,port );
            socket.send(request);
            modFinish = System.currentTimeMillis();
            System.out.print("\tPacket Creation: " + (modFinish - modStart));
            modStart = modFinish;
            byte[] buffer = new byte[512];
            DatagramPacket reply = new DatagramPacket(buffer, 0, buffer.length);
            socket.setSoTimeout(1000);
            try {
                modFinish = System.currentTimeMillis();
                System.out.print("\tCreated Reply: " + (modFinish - modStart));
                modStart = modFinish;
                socket.receive(reply);
                modFinish = System.currentTimeMillis();
                System.out.print("\tReceived: " + (modFinish - modStart));
                modStart = modFinish;
                overallFinish = modFinish;
                double rtt = overallFinish - overallStart;
                System.out.println("\t RTT: " + rtt + "ms" + ". Start: " + overallStart + ". Receive: " + overallFinish);
                double delay = rtt/2;
                System.out.println("Delay: " + delay + "ms");
            } catch(SocketTimeoutException e){
                System.out.println("\tPacket Timeout");
            }
        }
    }

    private static void printData(DatagramPacket request) throws Exception {
        // Obtain references to the packet's array of bytes.
        byte[] buf = request.getData();
        // Wrap the bytes in a byte array input stream,
        // so that you can read the data as a stream of bytes.
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        // Wrap the byte array output stream in an input stream reader,
        // so you can read the data as a stream of characters.
        InputStreamReader isr = new InputStreamReader(bais);
        // Wrap the input stream reader in a bufferred reader,
        // so you can read the character data a line at a time.
        // (A line is a sequence of chars terminated by any combination of \r and \n.)
        BufferedReader br = new BufferedReader(isr);
        // The message data is contained in a single line, so read this line.
        String line = br.readLine();
        // Print host address and data received from it.
        System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + line );
    }
}