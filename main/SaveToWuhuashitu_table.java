package main;

/**
 * 实现逻辑：
 * 1.运行index.jsp文件打开查询界面
 * 2.输入查询属性： (1).要查询的企业名
 *              (2).查询起始时间
 * 				(3).查询截至时间 
 * 3.在MySQL数据库中建立一张"物化视图表"
 * 4.将以上三个属性以及开始点击查询的时间插入到物化视图表中
 * 5.将查到的记录也插入到物化视图表中
 * 6.新写一个页面，展示物化视图内容。(仅展示最近十次查询到的数据)
 */

public class SaveToWuhuashitu_table {
	
	public void createTable() {
    	String WuHuaShiTu = "物化视图表";
    	
    	String create_sql =  "Create Table If Not Exists " + WuHuaShiTu  + "(\n" +
                "`ID` Bigint(8) unsigned Primary key Auto_Increment,\n" +
                "`title` VARCHAR(255) NULL,\n" +
                "`time_dt` VARCHAR(50) NULL,\n"+
                "`time_qishi` VARCHAR(50) NULL,\n" +
                "`time_jiezhi` VARCHAR(50) NULL,\n"+
                "`time_query` VARCHAR(50) NULL"+
                "`connectCompany` VARCHAR(255) NULL,\n" +
                "`url` VARCHAR(255) NOT NULL,\n" +
                "`hash` VARCHAR(50) NULL,\n" +
                "`company` VARCHAR(30) NOT NULL,\n"+
                ")";
    	System.out.println(create_sql);
    	
    }

}
