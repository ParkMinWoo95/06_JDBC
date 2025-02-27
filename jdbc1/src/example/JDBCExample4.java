package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {
	public static void main(String[] args) {
		
		// "최소", "최대" 급여 범위를 입력 받아
		
		// EMPLOYEE 테이블에서
		// 급여 "최소" 이상 "최대 이하로 받는 사원의
		// 사번, 이름, 부서코드, 급여를
		// 급여 내림차순으로 출력
		
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
			System.out.println("=== 범위 내 급여 받는 직원 조회 ==");
			System.out.println("최소 값 입력 : ");
			int min = sc.nextInt();
			
			System.out.println("최대값 입력 : ");
			int max = sc.nextInt();
			
		/* 3. SQL 작성 */
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT EMP_ID, EMP_NAME, DEPT_CODE, SALARY\n");
			sb.append("FROM EMPLOYEE ");
			sb.append("WHERE SALARY BETWEEN");
			sb.append(min);
			sb.append(" AND ");
			sb.append(max);
			sb.append(" ORDER BY SALARY DESC  ");
			
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
				String deptCode = rs.getString("DEPT_CODE");
				int		 salary = rs.getInt("Salary");
				
				System.out.printf("%s / %s / %s / %d \n", empId, empName, deptCode, salary);
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
