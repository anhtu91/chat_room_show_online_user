package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class UserThread extends Thread{
	private Socket socket;
	private ChatServer chatServer;
	
	public UserThread(Socket socket, ChatServer chatServer) {
		this.socket = socket;
		this.chatServer = chatServer;
	}
	
	public void run() {
		try {
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			String userName = dataInputStream.readUTF();
			chatServer.addUserName(userName);
			
			printUser(dataOutputStream);
			
			String serverMessage = "New user connected "+userName;
			chatServer.boardcast(serverMessage, this, dataOutputStream);
			
			String clientMessage = null;
			
			do {
				clientMessage = dataInputStream.readUTF();
				serverMessage = "[" + userName +"]"+clientMessage;
				chatServer.boardcast(serverMessage, this, dataOutputStream);
			}while(!clientMessage.equals("quit"));
			
			chatServer.removeUser(userName, this);
			serverMessage = userName + " quitted";
			chatServer.boardcast(serverMessage, this, dataOutputStream);
			
			dataInputStream.close();
			dataOutputStream.close();
			socket.close();
		
		} catch (IOException e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	void printUser(DataOutputStream dataOutputStream) throws IOException {
		if(chatServer.hasUser()) {
			dataOutputStream.writeUTF("Connected users: "+chatServer.getUserName());
		}else {
			dataOutputStream.writeUTF("No users connected");
		}
	}
	
	
	void sendMessage(String message, DataOutputStream dataOutputStream) {
		try {
			dataOutputStream.writeUTF(message);
		} catch (IOException e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
		}
	}
}
