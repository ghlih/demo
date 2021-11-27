package cn.hwali.ex.entity;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    //为了配合zTree所需要添加的属性
    private Integer id;
    private Integer pid;            //找到父节点
    private String name;            //作为节点名称
    private String url;             //点击节点时跳转的位置
    private String icon;            //节点图标样式
    private List<Menu> children = new ArrayList<>();    //存储子节点的集合，初始化是为了避免空指针异常
    private Boolean open = true;    //控制节点是否默认为打开，true表示默认打开

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", open=" + open +
                '}';
    }
}