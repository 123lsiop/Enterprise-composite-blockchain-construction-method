package servlet;

import bean.CompanyBean;
import bean.PageBean;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class EconomicDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String start_time = request.getParameter("start_time");
        String end_time = request.getParameter("end_time");
        String page_num = request.getParameter("page_num");
        String companyAjax = request.getParameter("companyAjax");
        String connectAjax = request.getParameter("connectAjax");

        String Node_ip="192.168.130.128";//主节
    	String ip="192.168.130.129";//slave1
        String download_url = null;
        String connect_name = null;
        int index_count = 0;
        int begin_index = 0;
        int end_index = 5;
        int con_index_count = 0;
        int con_begin_index = 0;
        int con_end_index = 5;

        List<CompanyBean> companyList = new ArrayList<>();
        List<PageBean> companyPageList = new ArrayList<>();
        List<CompanyBean> connectList = new ArrayList<>();
        List<PageBean> connectPageList = new ArrayList<>();

        if (page_num != null) {
            end_index = Integer.valueOf(page_num) * 6 - 1;
            begin_index = end_index - 5;
            con_end_index = Integer.valueOf(page_num) * 6 - 1;
            con_begin_index = con_end_index - 5;
        }

        try {
            Connection conn = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/blockchain?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            //String url = "jdbc:mysql://192.168.130.128:3306/blockchain?useSSL=false&useUnicode=true&characterEncoding=UTF8";
            String username = "root";
            String password = "123456";
            int wuhua_i=0;
            conn = DriverManager.getConnection(url, username, password);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String query_time = dateFormat.format(date);
            String sql = "select * from " + name + " where time>='" + start_time + "' and time<='" + end_time + "' order by time ASC";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
//            int num=rs.getRow(); //记录行数
            // 展开结果集数据库
            
            int Index_i=0;
            String Connectname=null;
//            String[] Connect_num=new String[100];
            while (rs.next()) {
            	
            	//获取查询结果存到物化试图表中
            	String title1 = rs.getString("title");
            	String time1 = rs.getString("time");
            	String connectCompany1 = rs.getString("connectCompany");
            	String url_1= rs.getString("url");
            	String hash_1 = rs.getString("hash");
            	//String insert_sql = "insert into 物化视图表 (company,title,`time`,start_time,end_time ,connectCompany , url, `hash`,query_time) values('"+name+"'"+","+"'"+title1+"'"+","+"'"+time1+"'"+","+"'"+start_time+"'"+","+"'"+end_time+"'"+","+"'"+connectCompany1+"'"+","+"'"+url_1+"'"+","+"'"+hash_1+"'"+","+"'"+query_time+"')"; 
            	//System.out.println(insert_sql);  
                //ps1 = conn.prepareStatement(insert_sql);
            	//ps1.executeUpdate(insert_sql);
            	
                if (index_count >= begin_index && index_count <= end_index) {
                    String filename = rs.getString("hash");
                    //download_url = "http://192.168.130.128:50070/webhdfs/v1/Blockchain/json/newdata0003b6527c1d0c5692b03ecd315e86aa92194b92.json?op=OPEN&namenoderpcaddress=192.168.130.128:9000&offset=0";
                    download_url="http://192.168.130.128:50070/webhdfs/v1/Blockchain/"+filename+".zip?op=OPEN&namenoderpcaddress="+Node_ip+":9000&offset=0";
                    companyList.add(new CompanyBean(name, download_url, rs.getString("time"), rs.getString("title"), rs.getString("connectCompany")));
                }
                
                index_count++;
                if(rs.getString("connectCompany")!=null) {
                	if(Index_i==0)
                	Connectname = rs.getString("connectCompany");
                	Index_i++;
                }
//                connect_name = rs.getString("connectCompany");
                System.out.println("关联公司名称"+connect_name);
            }
//            while (rs.next()) {
//                if (rs.getString("connectCompany")!=null) {
//                	connect_name = rs.getString("connectCompany");
//                	break;
//                }
//            }
//            &&"".equals(connect_name)
            connect_name = Connectname;
            if(connect_name!=null) {
                sql = "select * from " + connect_name + " where time>='" + start_time + "' and time<='" + end_time + "' order by time ASC";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery(sql);
                while (rs.next()) {
                    if (con_index_count >= con_begin_index && con_index_count <= con_end_index) {
                        String filename = rs.getString("hash");
                        //download_url = "http://192.168.130.128:50070/webhdfs/v1/Blockchain/json/newdata00051276b4b38a8a3db363607ae4a7259f3c87da.json?op=OPEN&namenoderpcaddress=192.168.130.128:9000&offset=0";
                        download_url="http://192.168.130.128:50070/webhdfs/v1/Blockchain/json/"+"newdata"+filename+".json?op=OPEN&namenoderpcaddress="+Node_ip+":9000&offset=0";
                        connectList.add(new CompanyBean(name, download_url, rs.getString("time"), rs.getString("title"), rs.getString("connectCompany")));
                        System.out.println("cuoshuwuhsf");
                    }
                    con_index_count++;
                }
                
                if(con_index_count==0) {
                	connectList=null;
                }
            }else 
            	connectList = null;




        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }

        int page_count = index_count % 6 == 0 ? index_count / 6 : index_count / 6 + 1;
        int con_page_count = con_index_count % 6 == 0 ? con_index_count / 6 : con_index_count / 6 + 1;

        if (page_count != 1) {
            companyPageList.add(new PageBean("companyBegin", "<<"));
        }
        for (int i = 1; i <= page_count; i++) {
            switch (i - Integer.valueOf(page_num)) {
                case -2:
                case -1:
                case 1:
                    ;
                case 2:
                    companyPageList.add(new PageBean("companyNormal", i + ""));
                    break;
                case 0:
                    companyPageList.add(new PageBean("companyNow", i + ""));
                    break;
                default:
                    ;
            }
        }
        if (page_count != 1) {
            companyPageList.add(new PageBean("companyEnd", ">>"));
        }


        if (con_page_count != 1) {
            connectPageList.add(new PageBean("connectBegin", "<<"));
        }
        for (int i = 1; i <= con_page_count; i++) {
            switch (i - Integer.valueOf(page_num)) {
                case -2:
                case -1:
                case 1:
                    ;
                case 2:
                    connectPageList.add(new PageBean("connectNormal", i + ""));
                    break;
                case 0:
                    connectPageList.add(new PageBean("connectNow", i + ""));
                    break;
                default:
                    ;
            }
        }
        if (con_page_count != 1) {
            connectPageList.add(new PageBean("connectEnd", ">>"));
        }

        if (companyAjax != null || connectAjax != null) {
            if ("1".equals(companyAjax)) {
                String result = JSON.toJSONString(companyList);
                out.print(result);
            } else if ("2".equals(companyAjax)) {
                String result = JSON.toJSONString(companyPageList);
                out.print(result);
            } else if ("1".equals(connectAjax)) {
                String result = JSON.toJSONString(connectList);
                out.print(result);
            } else if ("2".equals(connectAjax)) {
                String result = JSON.toJSONString(connectPageList);
                out.print(result);
            }
        } else {
            request.setAttribute("name", name);
            request.setAttribute("start", start_time);
            request.setAttribute("end", end_time);
            request.setAttribute("connect_name", connect_name);
            request.setAttribute("page_count",String.valueOf(page_count));
            request.setAttribute("con_page_count",String.valueOf(con_page_count));
            request.setAttribute("companyList", companyList);
            request.setAttribute("companyPageList", companyPageList);
            request.setAttribute("connectList", connectList);
            request.setAttribute("connectPageList", connectPageList);
//            if(companyList.size()==0&&connectList.size()==0) 
//            	request.getRequestDispatcher("next_text2.jsp").forward(
//                        request, response);
//            else 
            	request.getRequestDispatcher("index_later.jsp").forward(
                        request, response);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
