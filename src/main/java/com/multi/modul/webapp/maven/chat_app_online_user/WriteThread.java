package com.multi.modul.webapp.maven.chat_app_online_user;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
	private Socket socket;
	private ChatClient client;

	public WriteThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
	}

	public void run() {
		try {
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			Scanner sc = new Scanner(System.in);
			System.out.print("Enter username: ");
			String userName = sc.nextLine();

			client.setUserName(userName);
			dataOutputStream.writeUTF(userName);
			dataOutputStream.flush();
			
			String message = null;
			
			do {
				message = sc.nextLine();
				dataOutputStream.writeUTF(message);
				dataOutputStream.flush();
			} while (!message.equals("quit"));
			
			sc.close();
		} catch (IOException e) {
			System.out.println("Error output stream..." + e.getMessage());
			e.printStackTrace();
		}
	}
}