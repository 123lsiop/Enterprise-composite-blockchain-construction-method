package main;
import bean.CompanyBean;
import com.alibaba.fastjson.JSONReader;
import utils.JDBCUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.Sha1Utils;
import utils.DateUtils;

import java.io.*;
import java.net.URISyntaxException;

/**
 * @Description: 保存记录至本地，mysql，hdfs，hbase
 */
public class SaveData {

    /**x-special/nautilus-clipboard
copy
file:///media/hadoop/2.6.7-28-i6/share/hadoop/hdfs/lib
file:///media/hadoop/2.6.7-28-i6/share/hadoop/hdfs/hadoop-hdfs-2.7.5.jar
file:///media/hadoop/2.6.7-28-i6/share/hadoop/hdfs/hadoop-hdfs-2.7.5-tests.jar
file:///media/hadoop/2.6.7-28-i6/share/hadoop/hdfs/hadoop-hdfs-nfs-2.7.5.jar

     * 将一条记录保存到一个本地的JSON文件，并命名为Sha1.json
     *
     * @param path
     * @param CompanyName
     * @param record
     * @return 文件的Sha1
     * @throws IOException
     */
    public String[] saveToLocalFile(String path, String CompanyName, String record) throws IOException {
        //将一条记录保存到一个本地的JSON文件，并命名为Sha1.json
        String childpath="/newdata";
        File dir = new File(path + childpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        String new_path = path+childpath + CompanyName + ".json";
        //System.out.println(new_path);
        File newFile = new File(new_path);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "utf-8"));
        bw.write(record);
        bw.flush();
        bw.close();
        String sha1 = Sha1Utils.getFileSha1(newFile);
       // System.out.println("文件sha1："+sha1);
        String path1 = path + "/newdata" + sha1 + ".json";
        newFile.renameTo(new File(path1));

        System.out.println("本地保存完成，文件为 "+path1);
        String[] fileInfo = new String[2];
        fileInfo[0] = path1;
        fileInfo[1] = sha1;//sha1作为hashcode存到MySQL中 --hyz
        return fileInfo;
    }

    /**
     * 保存到mysql
     *
     * @param bean
     * @param sha1
     */
    public void saveToMysql(CompanyBean bean, String sha1) {
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        //若公司名不存在，则在mysql数据库中创建一个以公司名称为表名的表
        String sql_CreateTable = "Create Table If Not Exists " + bean.getCompany() + "(\n" +
                "`ID` Bigint(8) unsigned Primary key Auto_Increment,\n" +
                "`title` VARCHAR(255) NULL,\n" +
                "`time` VARCHAR(50) NULL,\n" +
                "`connectCompany` VARCHAR(255) NULL,\n" +
                "`url` VARCHAR(255) NOT NULL,\n" +
                "`hash` VARCHAR(50) NULL\n" +
                ")";
        template.update(sql_CreateTable);
        //向mysql数据库的表中插入数据
        if(bean.getConnectCompany()==null){
            bean.setConnectCompany("");
        }
        String sql_InsertData = "INSERT INTO `" + bean.getCompany() + "` (`title`,`time`,`connectCompany`,`url`,`hash`) VALUES ('" + bean.getTitle() + "','" + bean.getTime() + "','" + bean.getConnectCompany() + "','" + bean.getUrl() + "', '" + sha1 + "')";
        template.update(sql_InsertData);
        System.out.println("Mysql保存完成，记录插入到 "+bean.getCompany()+" 表");
    }

    /**
     * @function 本地文件上传至 HDFS
     * @param source 原文件路径
     * @param dest  目的文件路径
     * @throws IOException
     * @throws URISyntaxException
     */
    public void saveToHdfs(String source, String dest) throws URISyntaxException, IOException {
        //在window运行，需配置用户名-DHADOOP_USER_NAME=hadoop
        System.setProperty("HADOOP_USER_NAME","hadoop");
        // 读取hadoop文件2.7.5的配置
        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://192.168.152.100:9000/");
        conf.set("fs.defaultFS", "hdfs://192.168.130.128:9000/");
        // FileSystem是用户操作HDFS的核心类，它获得URI对应的HDFS文件系统
        FileSystem fileSystem = FileSystem.get(conf);
        // 源文件路径
        Path srcPath = new Path(source);
        // 目的路径
        Path dstPath = new Path(dest);
        // 查看目的路径是否存在
        if (!(fileSystem.exists(dstPath))) {
            // 如果路径不存在，即刻创建
            fileSystem.mkdirs(dstPath);
        }

        try {
            // 将本地文件上传到HDFS
            fileSystem.copyFromLocalFile(srcPath, dstPath);
            System.out.println("HDFS保存完成，位于 " + dest+" 目录下");
        } catch (Exception e) {
            System.err.println("Exception caught! :" + e);

        } finally {
            fileSystem.close();
        }

    }


//    public void saveToHbase(CompanyBean bean, String sha1) {
//
//    }
    /**
     * 
     * @param local_path :关于多个公司的json文件的本地保存路径
     * @param hdfs_path：json文件在hdfs中的存储位置
     * @throws IOException
     * @throws URISyntaxException
     */
    public void save(String local_path,String hdfs_path) throws IOException, URISyntaxException {

        File file = new File(local_path);
        File[] fs = file.listFiles(); //得到文件夹下的所有JSON文件

        for (File f : fs) { // 遍历每个Json文件
            if (!f.isDirectory()) {
                System.out.println(f.getName());
                InputStreamReader inputReader = new InputStreamReader(new FileInputStream(f), "utf-8");
                BufferedReader bf = new BufferedReader(inputReader);
                // 按行读取字符串
                String line;
                while ((line = bf.readLine()) != null) {
                    JSONReader reader = new JSONReader(new StringReader(line));
                    reader.startObject();//解析数据
                    CompanyBean bean = new CompanyBean();
                    String key =null;
                    String value =null;
                    while (reader.hasNext()) { //遍历一行记录
                        key = reader.readString();
                        Object object = reader.readObject();
                        //采集的数据里，有的key对应的value为null（如bankofhaerbinnn.json文件里有的字段,），所以通过if判断value是否为null
                        if(object==null){
                            continue;
                        }
                        value=object.toString();
                        switch (key) {
                            case "company":
                                bean.setCompany(value);
                                break;
                            case "url":
                                bean.setUrl(value);
                                break;
                            case "time":
                                bean.setTime(value);
                                break;
                            case "title":
                                bean.setTitle(value);
                                break;
                            case "connectCompany":
                                bean.setConnectCompany(value);
                                break;
                            default:
                                break;
                        }
                    }
                    //System.out.println("公司名称"+bean);
                    //System.out.println("公司名称"+f.getName());
                    if (bean.getCompany() != null && bean.getUrl() != null && bean.getTime() != null && bean.getTitle() != null) {
                        String time=bean.getTime().trim().replaceAll("\u00a0", ""); //去除时间字段里的空格与 &nbsp
                        //判断一条记录中时间的格式是否为YYYY/MM/DD或YYYY.MM.DD 或YYYY-MM-DD格式
                        if(DateUtils.isValidDate(time)){
                            //若记录中时间的格式为YYYY.MM.DD，则转换为YYYY-MM-DD
                            if(DateUtils.isValidDate2(time)){
                                DateUtils.formateDateStr2(time);
                            }
                            //若记录中时间的格式为YYYY/MM/DD，则转换为YYYY-MM-DD
                            else if(DateUtils.isValidDate3(time)){
                                DateUtils.formateDateStr3(time);
                            }
                           //统一时间格式为YYYY-MM-DD
                           bean.setTime(time);
                           //步骤1:将记录保存到本地
                           String[] fileInfo = saveToLocalFile(local_path, bean.getCompany(), line);
                           String newpath = fileInfo[0];
                           String sha1 = fileInfo[1];
                            //步骤2:将记录上传到hdfs
                           saveToHdfs(newpath,hdfs_path);
                            //步骤3:将记录保存到mysql
                           saveToMysql(bean, sha1);
                        }
                    }
                    reader.endObject();
                }
            }
        }
    }
    
    

    public static void main(String[] args) throws Exception {
//        String local_path = "E:/MyCode/Java/QMT/data"; //JSON文件目录
        String local_path = "/usr/local/jsondata"; //JSON文件目录
        String hdfs_path="/blockchain/json";
        SaveData saveData = new SaveData();
        saveData.save(local_path,hdfs_path);
        System.out.println("所有文件处理成功，程序正常结束");
        
//        saveData.savetoWuHuaShiTu();
//        System.out.println();
    }
}
