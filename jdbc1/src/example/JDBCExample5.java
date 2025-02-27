package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample5 {
	public static void main(String[] args) {
		// 부서명을 입력 받아
		// 해당 부서의 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 오름차순으로 조회
		
		Scanner sc = new Scanner(System.in);
		Connection conn = null;	// DB 연결 정보 저장, 연겨랗는 객체
		Statement stmt = null;	// SQL 수행, 결과 반환받는 객체
		ResultSet rs = null;		// SELECT 수행 결과 저장 객체
		
		try {
		/* 2. DriverManager 객체를 이용해 Connection 객체 생성하기 */
			
			// Oracle JDBC Driver를 메모리에 로드(적재) == 객체로 만듦
			Class.forName("oracle.jdbc.OracleDriver");
			
			String type = "jdbc:oracle:thin:@";
			String host = "112.221.156.34";
			String port = ":12345";
			String dbName = ":XE";
			String userName = "KH10_PMW";
			String password = "KH1234";
			
			// Connection 객체를 생성해서 얻어오기
			conn = DriverManager.getConnection(
					type + host + port + dbName, // DB URL 
					userName,
					password
					);
			
			// 범위 입력 받기
			System.out.println("=== 부서명으로 조회 ==");
			System.out.print("부서명 입력 : ");
			String deptTitle = sc.next();
			
		/* 3. SQL 작성 */
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT E.EMP_ID, E.EMP_NAME, D.DEPT_TITLE, J.JOB_NAME \n");
			sb.append("FROM EMPLOYEE E ");
			sb.append("JOIN JOB J ON (J.JOB_CODE = E.JOB_CODE) ");
			sb.append("JOIN DEPARTMENT D ON (D.DEPT_ID = E.DEPT_CODE) ");
			sb.append("WHERE D.DEPT_TITLE = '");
			sb.append(deptTitle);
			sb.append("'");
			sb.append(" ORDER BY E.JOB_CODE ASC ");
			
			String sql = sb.toString();
			
		/* 4. sql을 전달하고 결과를 받아올 Statement 객체 생성 */
			stmt = conn.createStatement();
			
			
			
		/* 5. Statement 객체를 이용해서 SQL을 DB로 전달 후 수행 
		  1) SELECT문 : executeQuery() -> ResultSet으로 반환
		  2) DML문 : executeUpdate() -> 결과 행의 개수(int) 반환
		*/
			rs = stmt.executeQuery(sql);


		/* (5번 SQL이 SELECT인 경우에만)
		 * 6. 조회 결과가 저장된 ResultSet을 1행씩 접근하여 각 행에 기록된 컬럼 값 얻어오기
		   - 1행씩 접근 할때 자동으로 Cursor를 이용함
		 */
			
			while(rs.next()) {
				// rs.next() : ResultSet의 Cursor를 다음 행으로 이동
				// 다음 행이 있으면 true, 없으면 false
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String jopName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n", empId, empName, deptTitle, jopName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 7. 사용 완료된 JDBC 객체 자원 반환
			try {
				// 생성 역순으로 close 하는 것이 좋다
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
