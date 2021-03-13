package semi.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatDAO {

	DBConnection dbCon = DBConnection.getInstance();

	/********************************************************************************
	 * 회원가입 메서드 INSERT INTO chat(id, nickname, password) VALUES (?, ?, ?)
	 * 
	 * @param id       사용자가 입력한 아이디
	 * @param nickname 사용자가 입력한 닉네임
	 * @param password 사용자가 입력한 비밀번호
	 ********************************************************************************/
	public void join(String id, String nickname, String password) {
		Connection con = dbCon.getConnection();
		try {
			String sql = "INSERT INTO chat (id, nickname, password) VALUES (?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, nickname);
			pstmt.setString(3, password);
			pstmt.executeUpdate();
			dbCon.freeConnection(con, pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/********************************************************************************
	 * 회원가입 아이디 중복확인 메서드 SELECT id FROM chat
	 * 
	 * @param id 사용자가 입력한 아이디
	 * @return 중복이면 true, 아니면 false
	 ********************************************************************************/
	public boolean idCompare(String id) {
		boolean isExist = false;
		Connection con = dbCon.getConnection();
		try {
			String sql = "SELECT id FROM chat";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				do {
					if (rs.getString("id").equals(id)) {
						isExist = true;
						break;
					}
				} while (rs.next());
			}
			dbCon.freeConnection(con, pstmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	/********************************************************************************
	 * 로그인 메서드 SELECT * FROM chat WHERE id = ?
	 * 
	 * @param id       사용자가 입력한 아이디
	 * @param password 사용자가 입력한 비밀번호
	 * @return 1:로그인 성공, 2:비밀번호 틀림, 3:아이디 없음
	 ********************************************************************************/
	public int login(String id, String password) {
		int result = 0;
		Connection con = dbCon.getConnection();
		try {
			String sql = "SELECT * FROM chat WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("password").equals(password)) {
					System.out.println("정상 로그인");
					result = 1;
				} else {
					System.out.println("비밀번호 틀림");
					result = 2;
				}
			} else {
				System.out.println("존재하지 않는 아이디");
				result = 3;
			}
			dbCon.freeConnection(con, pstmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/********************************************************************************
	 * 입력한 아이디로 닉네임 찾는 메서드 SELECT nickname FROM chat WHERE id = ?
	 * 
	 * @param id 사용자가 입력한 아이디
	 * @return 입력한 아이디의 닉네임
	 ********************************************************************************/
	public String getNickname(String id) {
		String nickname = "";
		Connection con = dbCon.getConnection();
		try {
			String sql = "SELECT nickname FROM chat WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				nickname = rs.getString("nickname");
			}
			dbCon.freeConnection(con, pstmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nickname;
	}
}
