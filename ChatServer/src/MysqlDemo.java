
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.sql.Connection;
 
public class MysqlDemo {
	
	static Connection conn;
	public MysqlDemo() {}
	
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
//		String url = "jdbc:mysql://localhost:3306/test?user=root&password=456789";
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String passwd = "456789";
		return DriverManager.getConnection(url,user,passwd);
	}
	
	
	public void insert(String name,String passwd) throws Exception {
		String sql = "insert into ChatUser(name, passwd) values(?, ?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, passwd);
			pstmt.executeUpdate();
		}
	}
	
	public boolean findByNameAndPasswd(String name,String passwd) throws Exception {
		String sql = "select name, passwd from ChatUser where name = ? and passwd = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, passwd);
			try (ResultSet rs = pstmt.executeQuery()) {
				 if (rs.next()) {
//					 System.out.println("登陆成功");
				   return true;
				 }
				 return false;
			}
		}
    }
}