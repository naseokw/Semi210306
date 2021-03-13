package semi.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatThread extends Thread {

	// PrintWriter 객체 담는 동기화된 리스트 초기화
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	//
	static ArrayList<String> member = new ArrayList<>();
	Socket socket = null;
	PrintWriter pw = null;
	String id = null;
	ChatDAO cDAO = new ChatDAO();

	public ChatThread() {
	}

	public ChatThread(Socket socket, String id) {
		this.socket = socket;
		this.id = id;
		try {
			pw = new PrintWriter(socket.getOutputStream()); // 소켓으로부터 출력스트림 받음
			list.add(pw); // 출력스트림을 리스트에 저장
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String name = ""; // 대화명을 저장해둘 변수
//		System.out.println("서버의 ChatThread 시작");
		try {
//			Scanner 대신 BufferedReader를 사용한다.
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			ChatDAO에서 id로 nickName을 얻어오는 메소드를 실행한다.
			name = cDAO.getNickname(id);
//			name = reader.readLine();	// 첫 줄을 읽어서 대화명으로 사용한다.
			for (String memberName : member) {
				if (name.equals(memberName)) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			member.add(name);
//			접속자 정보를 모든 클라이언트로 전송한다.
			for (PrintWriter writer : list) {
				writer.println(member);
				writer.flush();
				writer.println("#" + name + "님이 들어오셨습니다.\n");
				writer.flush();
			}
//				두번째 줄 부터는 채팅 메세지이므로 접속이 종료될 때까지 반복하며 채팅 메세지를 모든 클라이언트로 전송한다.
			while (true) {
				String str = reader.readLine();
//					채팅 내용이 더 이상 입력되지 않으면(채팅창을 닫으면) 무한루프 탈출
				if (str == null) {
					break;
				}
				for (PrintWriter writer : list) {
					writer.println(member);
					writer.flush();
					writer.println(name + "> " + str + "\n");
					writer.flush();
				}
			}
		} catch (IOException e) {
			System.out.println(id + " " + name + " 접속 종료");
		} finally {
//			채팅 창을 닫았으므로 채팅 목록에서 제거한다.
			list.remove(pw);
			member.remove(member.indexOf(name));
			for (PrintWriter writer : list) {
				writer.println("#" + name + "님이 나가셨습니다.\n");
				writer.flush();
			}

//			채팅창을 닫았으므로 통신 소켓을 제거한다.
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
