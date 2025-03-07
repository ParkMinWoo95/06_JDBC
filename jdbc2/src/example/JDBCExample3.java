package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample3 {
	public static void main(String[] args) {
		
		// 아이디, 비밀번호, 이름를 입력 받아
		// 아이디, 비밀번호가 일치하는 사용자의 이름을 수정(UPDATE)
		
		
		/* 1. JDBC 객체 참조 변수 선언 */
		Connection conn = null;
		PreparedStatement pstmt = null; // ? 값을 대입할 준비가 되어있다!
		
		// UPDATE는 수정된 행의 개수가 반환될 예정
		// -> ResultSet 불필요

		try {
			/* 2. DriverManager 객체를 이용해 Connection 객체 생성하기 */
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@112.221.156.34:12345:XE";
			String userName = "KH00_TEACHER"; // 사용자 계정명
			String password = "KH1234"; // 계정 비밀번호
			conn = DriverManager.getConnection(url, userName, password);
			
			// 자동 커밋 끄기!!!
			conn.setAutoCommit(false);
			
			/* 3. SQL 작성 */
			String sql = """
					UPDATE TB_USER SET
						USER_NAME = ?
					WHERE
					  USER_ID = ?
					AND
						USER_PW = ?
					""";
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.next();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.next();
			
			System.out.print("수정할 이름 입력 : ");
			String name = sc.next();
			
			
			/* 4. sql을 전달하고 결과를 받아올 
			 * PreparedStatement 객체 생성 + ? 값 세팅 */
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			
			/* 5. PreaparedStatement 객체를 이용해서 SQL을 DB로 전달 후 수행 
			 1) SELECT문 : executeQuery() -> ResultSet으로 반환 
			 2) DML문    : executeUpdate() -> 결과 행의 개수(int) 반환
			*/
			int result = pstmt.executeUpdate();
			
			/* (DML 수행 시)
			 * 6. SQL 수행 결과에 따른 처리 + 트랜잭션 제어 */
			
			if(result > 0) { // 1행 수정 
				System.out.println("수정 성공!!");
				conn.commit();
			} else {
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다");
				conn.rollback();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			/* 7. 사용 완료된 JDBC 객체 자원 반환 */
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null)  conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
