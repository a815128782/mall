package com.changgou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name="tb_center")
public class Center implements Serializable {
    @Id
    private String username;
    private String nick_name;
    private String sex;
    private String city;
    private String province;
    private String area;
    private String occupation;
    private String head_pic;
    private List<Cities> citiesList;
    private List<Provinces> provincesList;
    private List<Areas> areasList;
    private List<Occupations> occupationsList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public List<Cities> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(List<Cities> citiesList) {
        this.citiesList = citiesList;
    }

    public List<Provinces> getProvincesList() {
        return provincesList;
    }

    public void setProvincesList(List<Provinces> provincesList) {
        this.provincesList = provincesList;
    }

    public List<Areas> getAreasList() {
        return areasList;
    }

    public void setAreasList(List<Areas> areasList) {
        this.areasList = areasList;
    }

    public List<Occupations> getOccupationsList() {
        return occupationsList;
    }

    public void setOccupationsList(List<Occupations> occupationsList) {
        this.occupationsList = occupationsList;
    }
}
