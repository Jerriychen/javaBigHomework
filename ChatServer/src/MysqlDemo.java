
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.sql.Connection;
 
public class MysqlDemo {
	
	static Connection conn;
	public MysqlDemo() {}
	
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://115.29.136.169:3306/test";
		String user = "user";
		String passwd = "usertest";
		return DriverManager.getConnection(url,user,passwd);
	}
	
	
	public boolean insert(String name,String passwd) throws Exception {
		String sql = "insert into ChatUser(username, passwd) values(?, ?)";
		try (Connection conn = getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, passwd);
			pstmt.executeUpdate();
			return true;			//若注册成功，则返回true
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean findByNameAndPasswd(String name,String passwd) throws Exception {
		String sql = "select username, passwd from ChatUser where username = ? and passwd = ?";
		try (Connection conn = getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, passwd);
			try (ResultSet rs = pstmt.executeQuery()) {
				System.out.println("登陆成功");
				System.out.println();
				 if (rs.next()) {
					 return true;
				 }
				 return false;
			}
		}
    }
}