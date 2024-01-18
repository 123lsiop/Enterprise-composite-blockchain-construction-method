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
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: 保存记录至本地，mysql，hdfs，hbase
 */
public class SaveTest {

    /**
     * 将一条记录保存到一个本地的JSON文件，并命名为Sha1.json
     *
     * @param path
     * @param CompanyName
     * @param record
     * @return 文件的Sha1
     * @throws IOException
     */
    public String[] saveToLocalFile(String path, String CompanyName, String record, CompanyBean bean, String imgname) throws IOException {
        String childpath="/newData/";
        File dir = new File(path + childpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        String new_path = path+childpath + CompanyName + ".json";
        System.out.println(new_path);
        File newFile = new File(new_path);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "utf-8"));
        bw.write(record);
        bw.flush();
        bw.close();
        String sha1 = Sha1Utils.getFileSha1(newFile);
       // System.out.println("文件sha1："+sha1);
        String path1 = path + "/newData/" + sha1 + ".json";
        newFile.renameTo(new File(path1));
        System.out.println(sha1);
        File dir1 = new File("/usr/local/test/hdfsdata/"+sha1+"/");
        if (!dir1.exists()) {// 判断目录是否存在
            dir1.mkdir();
        }
        System.out.println("文件sha1："+sha1);
        File source = new File(path1);
        File dest = new File("/usr/local/test/hdfsdata/"+sha1+"/"+sha1+".json");
        Files.copy(source.toPath(), dest.toPath());
        String Combine=new String(bean.getImages_src());
        String path2=null;
        if(Combine.equals("[]")) {
        	System.out.println("img is null"); 
            String src1="/usr/local/test/hdfsdata/"+sha1;
            File srcFile1=new File(src1);
            File[] srcFiles1=srcFile1.listFiles();
            File zipFile = new File(src1+"/"+sha1+".zip");
            path2=src1+"/"+sha1+".zip";
            zipFiles(srcFiles1, zipFile);
        }
        else {
        	String Combine1=Combine.substring(1,Combine.length()-1);
        	String[] split = Combine1.split(",");
        	String[] split1=new String[split.length];
        	for(int i=0;i<split.length;i++) {
        		String Buf=split[i].substring(split[i].lastIndexOf("/")+1,split[i].length()-1);
        		split1[i]=new String(Buf);
        		System.out.println(split1[i]);
        		System.out.println("ok");
        		//copy 分类的 file 去 目的地
                File source1 = new File("/usr/local/test/imgdata/"+imgname+"/"+split1[i]);
                File dest1 = new File("/usr/local/test/hdfsdata/"+sha1+"/"+split1[i]);
                Files.copy(source1.toPath(), dest1.toPath());
        	}
            String src1="/usr/local/test/hdfsdata/"+sha1;
            File srcFile1=new File(src1);
            File[] srcFiles1=srcFile1.listFiles();
            File zipFile = new File(src1+"/"+sha1+".zip");
            path2=src1+"/"+sha1+".zip";
            zipFiles(srcFiles1, zipFile);
        }


        System.out.println("本地保存完成，文件为 "+path2);
        String[] fileInfo = new String[2];
        fileInfo[0] = path2;
        fileInfo[1] = sha1;
        return fileInfo;
    }
    public void save(String local_path,String hdfs_path) throws IOException, URISyntaxException {
    	System.out.println("123");
        File file = new File(local_path);
        File[] fs = file.listFiles(); //得到文件夹下的所有JSON文件
        for (File f : fs) { // 遍历每个Json文件
            if (!f.isDirectory()) {
                System.out.println(f.getName());
                //Img文件夹地址
                String ImgName=f.getName().substring(0,f.getName().length()-5);
                System.out.println(ImgName);
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
                            continue;//                            String newpath = fileInfo[0];
                        }
                        value=object.toString();

                        switch (key) {
                            case "company":
                                bean.setCompany(value);
                                break;
                            case "url":
                                bean.setUrl(value);
                                break;
                            case "images_src":
                                bean.setImages_src(value);
                                break;
                            case "time":
                                bean.setTime(value);
                                break;
                            case "title"://                            String newpath = fileInfo[0];
                                bean.setTitle(value);
                                break;
                            case "connectCompany":
                                bean.setConnectCompany(value);
                                break;
                            default:
                                break;
                        }
                    }
                    String Show=bean.getImages_src();
                    String Show1=Show.substring(1,Show.length()-1);
                    System.out.println(Show1);
                    
//                    String Show2=bean.getTime();
//                    String Show3=Show2.substring(3,Show2.length());
//                    System.out.println("a"+Show3+"a");
                    //System.out.println("公司名称"+f.getName());
                    if (bean.getCompany() != null && bean.getUrl() != null && bean.getTime() != null && bean.getTitle() != null) {
                        String time=bean.getTime().trim().replaceAll("\u00a0", ""); //去除时间字段里的空格与 &nbsp
                        //判断一条记录中时间的格式是否为YYYY/MM/DD或YYYY.MM.DD 或YYYY-MM-DD格式
                        //if(DateUtils.isValidDate(time)){
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
                            //System.out.println("123");
                            String[] fileInfo = saveToLocalFile(local_path, bean.getCompany(), line, bean, ImgName);
                            String newpath = fileInfo[0];
                            String sha1 = fileInfo[1];
                            //步骤2:将记录上传到hdfs
                            saveToHdfs(newpath,hdfs_path);
                            //步骤3:将记录保存到mysql
                            saveToMysql(bean, sha1);

                        //}
                    }
                    reader.endObject();
                }


            }
        }

    }
    public static void zipFiles(File[] srcFiles, File zipFile) {
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;

        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;
            // 遍历源文件数组
            for (int i = 0; i < srcFiles.length; i++) {
                // 将源文件数组中的当前文件读入 FileInputStream 流中
                fileInputStream = new FileInputStream(srcFiles[i]);
                // 实例化 ZipEntry 对象，源文件数组中的当前文件
                zipEntry = new ZipEntry(srcFiles[i].getName());
                zipOutputStream.putNextEntry(zipEntry);
                // 该变量记录每次真正读的字节个数
                int len;
                // 定义每次读取的字节数组
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                "`images_src` VARCHAR(1024) NULL,\n" +
                "`connectCompany` VARCHAR(255) NULL,\n" +
                "`url` VARCHAR(255) NOT NULL,\n" +
                "`hash` VARCHAR(50) NULL\n" +
                ")";
        template.update(sql_CreateTable);
        //向mysql数据库的表中插入数据
        if(bean.getConnectCompany()==null){
            bean.setConnectCompany("");
        }
        String Show2=bean.getTime();
        String Show3=Show2.substring(3,Show2.length());
        String sql_InsertData = "INSERT INTO `" + bean.getCompany() + "` (`title`,`time`,`images_src`,`connectCompany`,`url`,`hash`) VALUES ('" + bean.getTitle() + "','" + Show3 + "','" + bean.getImages_src() + "','" + bean.getConnectCompany() + "','" + bean.getUrl() + "', '" + sha1 + "')";
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
        // 读取hadoop文件系统的配置
        Configuration conf = new Configuration();
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

    public static void main(String[] args) throws Exception {
    	String local_path = "/usr/local/test/data"; //JSON文件目录
        String hdfs_path="/Blockchain";
        SaveTest saveData = new SaveTest();
        saveData.save(local_path,hdfs_path);
        System.out.println("所有文件处理成功，程序正常结束");
    }
}
