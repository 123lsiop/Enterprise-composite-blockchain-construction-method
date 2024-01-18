package main;
import java.sql.*;

public class webdemo {
// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/blockchain?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    //static final String DB_URL = "jdbc:mysql://192.168.130.128:3306/blockchain?useSSL=false&useUnicode=true&characterEncoding=UTF8";
    //"jdbc:mysql://192.168.130.128:3306/blockchain1?useSSL=false&useUnicode=true&characterEncoding=UTF8"
//    Class.forName("com.mysql.jdbc.Driver");
//    String url = "jdbc:mysql://192.168.130.128:3306/blockchain1?useSSL=false&useUnicode=true&characterEncoding=UTF8";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT time, url FROM 哈尔滨银行";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
//                int id  = rs.getInt("name");
                String name = rs.getString("time");
                String url1 = rs.getString("url");

                // 输出数据
//                System.out.print("ID: " + id);
                System.out.println(", 站点名称: " + name);
                System.out.println(", 站点 URL: " + url1);
                System.out.println(", 站点名称: " + name);
                System.out.println(", 站点 URL: " + url1);
                System.out.println("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
