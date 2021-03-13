package semi.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame implements ActionListener {

	JFrame frame = new JFrame("로그인 페이지");

	JPanel controlPanel = new JPanel(new BorderLayout()); // 전체패널
	JPanel dataPanel = new JPanel(); // 아이디와 패스워드를 배치할 패널
	JPanel fieldPanel = new JPanel(new GridLayout(2, 1)); // 텍스트필드를 배치할 패널
	JPanel buttonPanel = new JPanel(); // 버튼을 패치할 패널
	JPanel labelPanel = new JPanel(new GridLayout(2, 1)); // 라벨을 배치할 패널

	JTextField idField = new JTextField(12); // 아이디필드의 크기
	JPasswordField pwField = new JPasswordField(12); // 패스워드필드의 크기

	JLabel idLabel = new JLabel("ID:"); // 아이디 라벨
	JLabel pwLabel = new JLabel("PW:"); // 패스워드 라벨

	JButton loginBtn = new JButton("로그인"); // 로그인버튼
	JButton joinBtn = new JButton("회원가입"); // 회원가입 버튼

	Dimension fullSize = new Dimension();
	Dimension frameSize = new Dimension();

	String id = "";
	String password = "";
	String signal = "";
	private boolean activate;

	public LoginFrame() {
		fullSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(380, 170, 250, 220);
		frameSize.setSize(frame.getSize());
		frame.setLocation((fullSize.width - frameSize.width) / 2, (fullSize.height - frameSize.height) / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		labelPanel.setPreferredSize(new Dimension(30, 40));
		idLabel.setFont(new Font("Dialog", Font.BOLD, 15));
		pwLabel.setFont(new Font("Dialog", Font.BOLD, 15));

		labelPanel.add(idLabel);
		labelPanel.add(pwLabel);

		fieldPanel.add(idField);
		fieldPanel.add(pwField);

		dataPanel.setPreferredSize(new Dimension(30, 30));
		dataPanel.add(labelPanel, BorderLayout.WEST);
		dataPanel.add(fieldPanel);
		controlPanel.add(dataPanel);

		buttonPanel.add(loginBtn);
		buttonPanel.add(joinBtn);
		controlPanel.add(buttonPanel, BorderLayout.SOUTH);

		frame.add(controlPanel, BorderLayout.CENTER);

		loginBtn.setName("로그인");
		joinBtn.setName("회원가입");
		pwField.setName("로그인");

		pwField.addActionListener(this);
		loginBtn.addActionListener(this);
		joinBtn.addActionListener(this);

		frame.setResizable(false); // 프레임 크기 고정
		frame.setVisible(true);
		activate = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
		frame.dispose();
	}

//	public static void main(String[] args) {
//		LoginFrame lf = new LoginFrame();
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj == loginBtn) {
			login();
		} else if (obj == joinBtn) {
			signal = "join";
			setId("");
			setPassword("");
			activate = false;
		}
	}

	private void login() {
		signal = "login";
		id = idField.getText().trim();
		password = pwField.getText().trim();

		if (id.length() <= 0) {
			JOptionPane.showMessageDialog(null, "ID를 입력하십시오.");
		} else if (password.length() <= 0) {
			JOptionPane.showMessageDialog(null, "비밀번호를 입력하십시오.");
		} else {
			setId(idField.getText().trim());
			setPassword(pwField.getText().trim());
			activate = false;
		}
	}
}