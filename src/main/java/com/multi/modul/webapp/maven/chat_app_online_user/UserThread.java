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

			// Get client name
			String newUser = dataInputStream.readUTF();
			server.addUserName(newUser);
			System.out.println("User " + newUser + " connected to server.");

			DataOutputStream currentDataOutputStream = new DataOutputStream(socket.getOutputStream());
			if (!server.getListUserName().isEmpty()) {
				currentDataOutputStream.writeUTF("Online users " + server.getListUserName());
			}

			DataOutputStream dataOutputStream = null;
			// Send message new online user
			for (UserThread userThread : server.getListThread()) {
				if (userThread.getSocket().getPort() != this.getSocket().getPort()) {
					dataOutputStream = new DataOutputStream(userThread.getSocket().getOutputStream());
					dataOutputStream.writeUTF("User " + newUser + " connected to server.");
					if (!server.getListUserName().isEmpty()) {
						dataOutputStream.writeUTF("Online users " + server.getListUserName());
					}
					dataOutputStream.flush();
				}
			}

			while (true) {
				// Get message from client
				String message = dataInputStream.readUTF();
				String messageWithUser = "[" + newUser + "]: " + message;
				System.out.println(messageWithUser);
				// Send message to other clients
				server.sendMessage(messageWithUser, this, socket);

				if (message.equals("quit")) {
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}