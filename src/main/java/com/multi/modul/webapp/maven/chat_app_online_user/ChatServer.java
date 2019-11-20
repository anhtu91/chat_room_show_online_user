package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
	private int port;
	private Set<String> userNames = new HashSet<String>();
	private Set<UserThread> userThreads = new HashSet<UserThread>();
	
	public ChatServer(int port) {
		this.port = port;
	}

	public void execute() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server start....");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected..." + socket);
				
				UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				newUser.start();
			}
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	void boardcast(String message, UserThread excludeUser, DataOutputStream dataOutputStream) {
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				//System.out.println(aUser);
				aUser.sendMessage(message, dataOutputStream);
			}
		}
	}

	void addUserName(String userName) {
		userNames.add(userName);
	}

	void removeUser(String userName, UserThread aUser) {
		boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("User " + userName + " quitted");
		}
	}

	Set<String> getUserName() {
		return this.userNames;
	}

	boolean hasUser() {
		return !this.userNames.isEmpty();
	}

	public static void main(String[] args) {
		int port = 3333;
		ChatServer server = new ChatServer(port);
		server.execute();
	}
}
