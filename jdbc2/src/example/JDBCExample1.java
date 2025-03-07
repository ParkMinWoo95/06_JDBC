package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample1 {
	public static void main(String[] args) {
		
		/* 입력 받은 아이디가 포함된 사용자의
		 * 사용자 번호, 아이디, 이름, 가입일을
		 * 회원 번호 오름차순으로 조회(SELECT)
		 */
		
		/* 1. JDBC 객체 참조 변수 선언 */
		Connection conn = null; // DB 연결 정보를 가지고 연결하는 객체 
		Statement stmt = null;  // SQL 수행, 결과 반환 받는 객체
		ResultSet rs = null; // SELECT 결과를 저장하고 1행씩 접근하는 객체
		
		try {
			/* 2. DriverManager 객체를 이용해 Connection 객체 생성하기 */
			
			Class.forName("oracle.jdbc.OracleDriver");
			
			// 내 컴퓨터 DB 연결 시
			// jdbc:oracle:thin:@localhost:1521:XE
			
			// 학원 DB 서버 URL
			// - jdbc 드라이버가 어떤 데이터베이스에 연결할지 지정
			String url = "jdbc:oracle:thin:@112.221.156.34:12345:XE";
			String userName = "KH00_TEACHER"; // 사용자 계정명
			String password = "KH1234"; // 계정 비밀번호
			
			conn = DriverManager.getConnection(url, userName, password);
			
			/* 3. SQL 작성 */
			Scanner sc = new Scanner(System.in);
			System.out.print("검색할 아이디 입력 : ");
			String input = sc.next();
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT USER_NO, USER_ID, USER_NAME, ENROLL_DATE ");
			sb.append("FROM TB_USER ");
		  sb.append("WHERE USER_ID LIKE '%" + input + "%' ");
		  sb.append("ORDER BY USER_NO ASC ");
			
		  /* 4. sql을 전달하고 결과를 받아올 Statement 객체 생성 */
		  stmt = conn.createStatement();
		  
		  /* 5. Statement 객체를 이용해서 SQL을 DB로 전달 후 수행 
		  1)  문 : executeQuery() -> ResultSet으로 반환 
		  2) DML문    : executeUpdate() -> 결과 행의 개수(int) 반환
		  */
		  String sql = sb.toString();
			rs = stmt.executeQuery(sql);
			
			/* (5번 SQL이 SELECT인 경우에만)
			 * 6. 조회 결과가 저장된 ResultSet을 
			 * 1행씩 접근하여 각 행에 기록된 컬럼 값 얻어오기 */
			
			while(rs.next()) { // 커서를 다음행으로 이동, 행 있으면 true
				int    no   = rs.getInt("USER_NO");
				String id   = rs.getString("USER_ID");
				String name = rs.getString("USER_NAME");
				java.sql.Date enrollDate = rs.getDate("ENROLL_DATE");
				
				// java.sql.Date : DB의 Date 타입을 저장하는 클래스
				
				System.out.printf("%d / %s / %s / %s \n",
						no, id, name, enrollDate.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
