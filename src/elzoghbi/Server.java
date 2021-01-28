package elzoghbi;

import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) throws IOException {

		ServerSocket ss = new ServerSocket(1234);

		while (true) {
			Socket s = null;

			try {
				s = ss.accept();

				System.out.println("A new client is connected : " + s);

				System.out.println("Assigning new thread for this client");

				Thread t = new ClientHandler(s);

				t.start();

			} catch (Exception e) {
				s.close();
				ss.close();
				e.printStackTrace();
				// TODO: handle exception
			}
		}

	}
}

class ClientHandler extends Thread {

	final Socket s;

	public ClientHandler(Socket s) {
		this.s = s;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(1234);

			// int c=5;
			while (true) // instead of c i want to use true
			{
				byte[] receivebuffer = new byte[1024];
				byte[] sendbuffer = new byte[1024];
				DatagramPacket recvdpkt = new DatagramPacket(receivebuffer, receivebuffer.length);
				serverSocket.receive(recvdpkt);
				String clientdata  = new String (receivebuffer , 0 , receivebuffer.length);
				
				InetAddress IP = recvdpkt.getAddress();
				int portno = recvdpkt.getPort();

				// Kda Ana Gebt El-Client Data
				/*String clientdata = new String(recvdpkt.getData());*/
				clientdata = clientdata.trim();
				/*sendbuffer = clientdata.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);
				serverSocket.send(sendPacket);
				*/
				System.out.println("Server Received : "+ clientdata);

				if (clientdata.equals("quit")) {
					System.out.println("Client " + this.s + " sends exit...");
					System.out.println("Closing this connection.");
					this.s.close();
					System.out.println("Connection closed");
					break;
				}
				
				String toSend = getLocal(clientdata); 
					/*i += line.length();
					if (i >= local.length())
						break;*/
				//}
				//System.out.println(toSend);
				sendbuffer = toSend.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);
				serverSocket.send(sendPacket);
				// here the check condition for serverdata which must be bye
				/*
				 * if (serverdata.equalsIgnoreCase("bye")) {
				 * System.out.println("connection ended by server"); break; }
				 */

			}
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public String getLocal (String receive) throws IOException {
		String toSend = "notFound";
		RandomAccessFile local = new RandomAccessFile("local_dns_table.txt", "r");
		while (true) {
			String line = local.readLine();
			String[] split = (line.split(" "));
			
			if (line.equals(null)) {
				break;
			}else if (split[0].equals(receive)){
				toSend = "Reply from Server is : URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n" + "Server name = local DNS \n";
				
				System.out.println("Client Requested : " + split[0]+ "\n" +"URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n");
				break;
			}
		}
		local.close();
		return toSend;
	}

}
