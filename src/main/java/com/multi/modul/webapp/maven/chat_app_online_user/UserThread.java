package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
 

public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
 
    public UserThread(Socket socket, ChatServer server) {
    	this.socket = socket;
    	this.server = server;
    }
    
    public Socket getSocket() {
		return socket;
	}

	public ChatServer getServer() {
		return server;
	}

	public void run() {
    	try {
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOutputStream = null;
			
			//Get online user
			showUsers();
			
			//Get client name
			String newUser = dataInputStream.readUTF();
			server.addUserName(newUser);
			System.out.println("User "+newUser+" connected to server.");
			
			//Send message new online user
			for (UserThread userThread : server.getListThread()) {
				if(userThread.getSocket().getPort() != this.getSocket().getPort()) {
					dataOutputStream = new DataOutputStream(userThread.getSocket().getOutputStream());
					dataOutputStream.writeUTF("User "+newUser+" connected to server.");
					
					dataOutputStream.flush();
				}
			}
			
			while(true) {
				//Get message from client
				String message = dataInputStream.readUTF();
				message = "["+newUser+"]: "+message;
				
				//Send message to other clients
				server.sendMessage(message, this, socket);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void showUsers() {
		if(server.getListUserName().isEmpty() /*&& server.getListThread().isEmpty()*/) {
			System.out.println("No users online.");
		}else {
			System.out.println("Online users "+server.getListUserName());
		}
	}
    
}