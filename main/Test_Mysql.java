package main;
import java.sql.*;
public class Test_Mysql {
	public static void main(String[] args) {
	String Node_ip="192.168.108.100";//主节点
	String ip="192.168.108.101";//slave1

    String download_url = null;

    try {
        Connection conn = null;
        PreparedStatement ps = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/blockchain?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String username = "root";
        String password = "123456";
        conn = DriverManager.getConnection(url, username, password);
        
        String sql = "select * from " + "哈尔滨银行" + " where time>='" + "2017-10-10" + "' and time<='" + "2019-10-10" + "' order by time ASC";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
        	String time1 = rs.getObject("title").toString();
        	System.out.println(time1);
        }
        rs.close();
        ps.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	}
}
