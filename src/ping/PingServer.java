package ping;
import CMPC3M06.AudioRecorder;
import java.io.*;
import java.net.*;
import uk.ac.uea.cmp.voip.*;

/*

 * Server to process ping requests over UDP.

 */
public class PingServer {
   public static void main(String[] args) throws Exception {

    int port = 55555;

    // Create a datagram socket for receiving and sending UDP packets
    // through the port specified on the command line.
    DatagramSocket socket = new DatagramSocket(port); 

    AudioRecorder recorder = null;
      try{
        recorder = new AudioRecorder();
      }
      catch(Exception e){
        System.out.println(e);
      }
    double overallStart, overallFinish, modStart, modFinish;
    // Processing loop.
    while (true) {
        overallStart = System.currentTimeMillis();
        modStart = overallStart;
        // Create a datagram packet to hold incomming UDP packet.
        byte[] buffer = new byte[512];
        DatagramPacket request = new DatagramPacket(buffer, 0, buffer.length);
        modFinish = System.currentTimeMillis();
        System.out.print("Create Empty: " + (modFinish - modStart));
        modStart = modFinish;
        // Block until the host receives a UDP packet.
        socket.receive(request);
        modFinish = System.currentTimeMillis();
        System.out.print("\tReceive request: " + (modFinish - modStart));
        modStart = modFinish;
        // Print the recieved data.
//        printData(request);
        // Send reply.
        InetAddress clientHost = request.getAddress();
        int clientPort = request.getPort();
        byte[] buf = request.getData();
        modFinish = System.currentTimeMillis();
        System.out.print("\tGet Data: " + (modFinish - modStart));
        modStart = modFinish;
        DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
        socket.send(reply);
        modFinish = System.currentTimeMillis();
        System.out.print("\tBuild and send: " + (modFinish - modStart));
        overallFinish = modFinish;
        System.out.println("\tOverall: " + (overallFinish - overallStart));
//        System.out.println("   Reply sent." + "Address: " + clientHost + " .Port: " + clientPort);
      }
   }
   /*

    * Print ping data to the standard output stream.

    */
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
      System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + buf.length );
   }
}