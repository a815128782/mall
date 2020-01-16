package com.changgou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * address实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_address")
public class Address implements Serializable {

	@Id
	private Integer id;//id


	
	private String username;//用户名
	private String province;//省
	private String city;//市
	private String area;//县/区
	private String phone;//电话
	private String address;//详细地址
	private String contact;//联系人
	private String isDefault;//是否是默认 1默认 0否
	private String alias;//别名
	private String totalAddress;//详细地址

	public String getTotalAddress() {
		return totalAddress;
	}

	public void setTotalAddress(String totalAddress) {
		this.totalAddress = totalAddress;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}



}
