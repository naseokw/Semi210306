package semi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiChatServer {

	public static void main(String[] args) {

		ServerSocket server = null;
		boolean isFlag = false;
		
		try {
			server = new ServerSocket(9234);
			while (!isFlag) {
				Socket client = server.accept();
				System.out.println(client.toString());

				/*
				 * 클라이언트 접속 시 각 클라이언트마다 LoginThraed 실행
				 * client 연결 정보를 LoginThread Constructor로 받음
				 * 로그인 완료되면 ChatThread 배정
				 */
				Thread loginThread = new LoginThread(client);
				loginThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
