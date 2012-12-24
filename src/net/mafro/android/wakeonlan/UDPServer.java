package net.mafro.android.wakeonlan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

//TODO: This should be a service!! Otherwise the OS may kill it. 
public class UDPServer implements Runnable{

	private final static String TAG = "WOLServer";
	private final static boolean D = true;
	
	private static final int MESSAGE_RECEIVED = 0;
	
	DatagramSocket serverSocket;
	
	Callback callback;
	
	UDPServer(int port, Callback callback) throws SocketException{
		this.callback = callback;
		
		serverSocket = new DatagramSocket(port);
		
	}
	
	@Override
	public void run() {
		
		byte[] buffer  = new byte[255];
		DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
		if(D) Log.i(TAG, "Server started on port: " + serverSocket.getLocalPort());
		while(true){
			
			try {
				serverSocket.receive(receivePacket);
				
				if(D) Log.i(TAG, (new Date().toString()) + ": " +"Received " + receivePacket.getLength() +  
		                " bytes from " + receivePacket.getAddress() + ":" + receivePacket.getPort() + "...");
				
				callback.packetReceived();
				
			} catch (IOException e) {
				Log.e(TAG, "receive() failed", e);
				break;
			}
			
		}
		
	}
	
	public void cancel() {
		serverSocket.close();
	}
	
	
	interface Callback {
		
		public void packetReceived();
		
	}

}
