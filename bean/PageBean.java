package bean;

public class PageBean {
    private String pageClass;
    private String pageNum;

    public PageBean() {
    }

    public PageBean(String pageClass, String pageNum) {
        this.pageClass = pageClass;
        this.pageNum = pageNum;
    }

    public String getPageClass() {
        return pageClass;
    }

    public void setPageClass(String pageClass) {
        this.pageClass = pageClass;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "pageClass='" + pageClass + '\'' +
                ", pageNum='" + pageNum + '\'' +
                '}';
    }
}
