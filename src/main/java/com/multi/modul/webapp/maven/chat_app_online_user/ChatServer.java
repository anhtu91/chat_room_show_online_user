package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
	private int port;
	private Set<String> listUserName = new HashSet<>();
	private Set<UserThread> listThread = new HashSet<>();

	public ChatServer(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

	public Set<String> getListUserName() {
		return listUserName;
	}

	public Set<UserThread> getListThread() {
		return listThread;
	}

	private void execute() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server start...");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected..." + socket);

				UserThread userThread = new UserThread(socket, this);
				listThread.add(userThread);
				userThread.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addUserName(String newUserName) {
		listUserName.add(newUserName);
	}

	public void sendMessage(String message, UserThread currentUserThread, Socket socket) {
		DataOutputStream dataOutputStream;
		try {
			for (UserThread userThread : listThread) {
				if (userThread.getSocket().getPort() != currentUserThread.getSocket().getPort() //&& 
						/*userThread.getSocket().getLocalPort() != currentUserThread.getSocket().getLocalPort() &&
						userThread.getSocket().getInetAddress() != currentUserThread.getSocket().getInetAddress()*/) {
					dataOutputStream = new DataOutputStream(userThread.getSocket().getOutputStream());
					dataOutputStream.writeUTF(message);
					dataOutputStream.flush();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		int port = 3333;

		ChatServer chatserver = new ChatServer(port);
		chatserver.execute();
	}
}