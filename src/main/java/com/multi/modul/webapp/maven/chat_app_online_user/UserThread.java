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
			System.out.println(serverMessage);
			chatServer.boardcast(serverMessage, this, dataOutputStream);
			
			String clientMessage = null;
			
			do {
				clientMessage = dataInputStream.readUTF();
				serverMessage = "[" + userName +"]"+clientMessage;
				System.out.println(serverMessage);
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
			System.out.println("printUser "+chatServer.getUserName());
			dataOutputStream.writeUTF("Connected users: "+chatServer.getUserName());
			dataOutputStream.flush();
		}else {
			System.out.println("No user "+chatServer.getUserName());
			dataOutputStream.writeUTF("No users connected");
			dataOutputStream.flush();
		}
	}
	
	
	void sendMessage(String message, DataOutputStream dataOutputStream) {
		try {
			dataOutputStream.writeUTF(message);
			dataOutputStream.flush();
		} catch (IOException e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
		}
	}
}
