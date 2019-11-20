package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReadThread extends Thread {
	private Socket socket;
	private ChatClient client;

	public ReadThread(Socket socket, ChatClient client) {
		super();
		this.socket = socket;
		this.client = client;
	}

	public void run() {
		try {
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

			while (true) {
				String response = dataInputStream.readUTF();

				if (client.getUserName() != null)
					System.out.print("[" + client.getUserName() + "]: "+response);
			}
		} catch (IOException e) {
			System.out.println("Error input stream..." + e.getMessage());
			e.printStackTrace();
		}
	}
}
