package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
	private String hostName;
	private int port;
	private String userName;
	
	public ChatClient(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}
	
	public void execute() {
		try {
			Socket socket = new Socket(hostName, port);
			System.out.println("Connected to chat server...");
			
			new ReadThread(socket, this).start();
			new WriteThread(socket, this).start();
		} catch (UnknownHostException e) {
			System.out.println("Not found server...."+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O Error..."+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	void setUserName(String userName) {
		this.userName = userName;
	}
	
	String getUserName() {
		return this.userName;
	}
	
	public static void main(String[] args) {
		String hostname = "localhost";
		
		int userport = 3333;
		
		ChatClient client = new ChatClient(hostname, userport);
		client.execute();
	}
}
