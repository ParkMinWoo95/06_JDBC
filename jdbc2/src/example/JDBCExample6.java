package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {
	public static void main(String[] args) throws ClassNotFoundException {
		
		// 아이디, 비밀번호를 입력 받아
		// 일치하는 사용자를 삭제
		
		// 단, 아이디 또는 비밀번호가 일치하지 않으면 
		// "아이디 또는 비밀번호가 일치하지 않습니다" 출력
		
		// 일치하면
		// "정말 삭제 하시겠습니까? (Y/N) " 출력 
		// -> 'Y' 입력 시 삭제(commit) -> "삭제 되었습니다"
		// -> 'N' 삭제 취소(rollback)  -> "삭제 취소됨"
		
		
		/* DB 연결을 위한 url, userNanme, password*/
		String url = "jdbc:oracle:thin:@112.221.156.34:12345:XE";
		String userName = "KH00_TEACHER"; 
		String password = "KH1234";
		
		/* JDBC Driver를 메모리에 load */
		Class.forName("oracle.jdbc.OracleDriver");
		
		/* SQL 작성 */
		String sql = """
				DELETE
				FROM TB_USER
				WHERE USER_ID = ?
				AND   USER_PW = ?
				""";
		
		try(
			Connection conn = DriverManager.getConnection(url,userName,password);
			PreparedStatement pstmt	= conn.prepareStatement(sql);
			Scanner sc = new Scanner(System.in);
				){
			
			conn.setAutoCommit(false); // 자동 커밋 끄기
			
			// 아이디, 비밀번호 입력 받기
			System.out.println("=== 사용자 삭제하기 ===");
			System.out.print("아이디 : ");
			String id = sc.next();
			
			System.out.print("비밀번호 : ");
			String pw = sc.next();
			
			// ? (placeholder)에 알맞은 값 세팅
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			// sql 수행 후 결과 반환 받기
			int result = pstmt.executeUpdate();
			
			if(result == 0) { // 0행 삭제
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다");
			
			}else {
				System.out.print("정말 삭제 하시겠습니까(Y/N) ? ");
				String check = sc.next().toUpperCase();
				
				if(check.equals("Y")) {
					conn.commit();
					System.out.println("삭제되었습니다");
			
				}else {
					conn.rollback();
					System.out.println("삭제 취소");
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
