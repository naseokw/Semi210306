package semi.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import semi.vo.ChatVO;

public class ChatClient {

	public static void main(String[] args) {

		LoginFrame loginFrame = null;
		JoinFrame joinFrame = null;
		ChatFrame chatFrame = null;
		Socket socket = null;
		PrintWriter pw = null; // 서버에 메시지 송신
		Scanner sc = null; // 서버로부터 메시지 수신
		ObjectOutputStream oos = null;// 서버로 VO 보내는 데에 사용

		boolean isChatFrameOn = false;

		while (!isChatFrameOn) {
			try {
				socket = new Socket("121.139.85.156", 9234);
				sc = new Scanner(socket.getInputStream());
				pw = new PrintWriter(socket.getOutputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());

				if (socket != null) {
					// 서버와 연결됐을 때 로그인 창 활성화
					loginFrame = new LoginFrame();

					// 로그인 창에서 버튼을 눌러 activate가 false가 될 때까지 0.5초씩 sleep
					while (loginFrame.isActivate()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					String str = "";

					// 로그인 창에서 회원가입 버튼 눌렀을 때
					if (loginFrame.signal.equals("join")) {
						str = loginFrame.signal + "\n";
						pw.write(str);
						pw.flush();
						// join 메시지를 서버로 전달하고 로그인 창 dispose
						loginFrame.setActivate(false);

						while (!sc.nextLine().equals("join")) {
							// 서버에서 join 메시지를 받아 join을 전송할 때까지 루프
						}

						boolean isJoinAccept = false;

						while (!isJoinAccept) {

							// 회원가입 창 활성화
							joinFrame = new JoinFrame();

							// 회원가입 창에서 버튼을 눌러 activate가 false가 될 때까지 0.5초씩 sleep
							while (joinFrame.isActivate()) {
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							try {
								// 회원가입 창에서 입력된 정보를 VO에 넣어 oos를 통해 서버로 송신한다
								ChatVO cVO = new ChatVO(joinFrame.getId(), joinFrame.getNickName(),
										joinFrame.getPassword());
								if (!(cVO.getId().equals("") || cVO.getNickname().equals("")
										|| cVO.getPassword().equals(""))) {
									oos.writeObject(cVO);
									oos.flush();

									// 서버로부터 join_accept, join_refuse 메시지를 기다린다
									str = sc.nextLine().trim();
									if (str.equals("join_accept")) {
										JOptionPane.showMessageDialog(null, "회원가입 성공");
										joinFrame.setActivate(false);
										isJoinAccept = true;
									} else {
										JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.");
										// 아이디가 중복일 경우 회원가입 창을 닫고 새로 연다
										joinFrame.setActivate(false);
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					// 로그인 창에서 로그인 버튼 눌렀을 때
					else if (loginFrame.signal.equals("login")) {
						try {
							// 로그인 창에서 입력된 정보를 VO에 넣고, login 메시지 서버를 서버로 송신
							ChatVO cVO = new ChatVO(loginFrame.id, loginFrame.password);
							str = "login\n";
							pw.write(str);
							pw.flush();

							while (!sc.nextLine().equals("login")) {
								// 서버에서 login 메시지를 받아 login을 전송할 때까지 루프
							}
							// 로그인 창에서 입력한 정보를 넣은 VO를 oos를 통해 서버로 전송
							oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(cVO);
							oos.flush();

							// 서버로부터 login_accept, login_refuse, no_id 메시지를 기다린다
							str = sc.nextLine();

							// 로그인 성공
							if (str.equals("login_accept")) {
								loginFrame.setActivate(false);
								isChatFrameOn = true;
								chatFrame = new ChatFrame(socket);
								Thread thread = new Thread(chatFrame);

								// 채팅창을 데몬 스레드로 돌린다
								thread.setDaemon(true);
								thread.start();
							}
							// 비밀번호 틀림
							else if (str.equals("login_refuse")) {
								JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
								loginFrame.setActivate(false);
							}
							// 등록된 아이디 없음
							else if (str.equals("no_id")) {
								JOptionPane.showMessageDialog(null, "등록된 아이디가 없습니다.", "ERROR",
										JOptionPane.ERROR_MESSAGE);
								loginFrame.setActivate(false);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				//서버에 접속할 수 없을 경우 메시지 다이얼로그 띄우고 전체 루프 종료
				JOptionPane.showMessageDialog(null, "서버에 접속할 수 없습니다.");
				isChatFrameOn = true;
			}
		}
	}
}
