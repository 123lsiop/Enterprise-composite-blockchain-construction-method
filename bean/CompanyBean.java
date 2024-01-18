package bean;


public class CompanyBean {
    private String company;//公司名
    private String url; //新闻源
    private String images_src;//新闻标题
    private String time;//时间
    private String title;//新闻标题
    private String connectCompany;
    private String hash;

    public CompanyBean() {

    }
    public CompanyBean(String company, String url, String time, String title, String connectCompany) {
        this.company = company;
        this.url = url;
        this.time = time;
        this.title = title;
        this.connectCompany = connectCompany;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getImages_src() {
        return images_src;
    }

    public void setImages_src(String images_src) {
        this.images_src= images_src;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConnectCompany() {
        return connectCompany;
    }

    public void setConnectCompany(String connectCompany) {
        this.connectCompany = connectCompany;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    @Override
    public String toString() {
        return "CompanyBean{" +
                "company='" + company + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", connectCompany='" + connectCompany + '\'' +
                '}';
    }
}
