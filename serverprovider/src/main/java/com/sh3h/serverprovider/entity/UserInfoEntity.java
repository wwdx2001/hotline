/**
 * @author qiweiwei
 *
 */
package com.sh3h.serverprovider.entity;

import org.json.JSONObject;

public class UserInfoEntity {
	// / <summary>
	// / 用户编号
	// / </summary>
	private int UserId;

	// / <summary>
	// / 用户名
	// / </summary>
	private String UserName;

	// / <summary>
	// / 简单账号
	// / </summary>
	private String Account;

	// / <summary>
	// / 密码
	// / </summary>
	private String PWS;

	// / <summary>
	// / 手机
	// / </summary>
	private String CellPhone;

	// / <summary>
	// / 固定电话
	// / </summary>
	private String Phone;

	// / <summary>
	// / 地址
	// / </summary>
	private String Address;

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getPWS() {
		return PWS;
	}

	public void setPWS(String pWS) {
		PWS = pWS;
	}

	public String getCellPhone() {
		return CellPhone;
	}

	public void setCellPhone(String cellPhone) {
		CellPhone = cellPhone;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public static UserInfoEntity fromJSON(JSONObject resp) {
		UserInfoEntity userinfo = new UserInfoEntity();

		userinfo.setAccount(resp.optString("account"));
		userinfo.setAddress(resp.optString("address"));
		userinfo.setCellPhone(resp.optString("cellPhone"));
		userinfo.setPhone(resp.optString("phone"));
		userinfo.setPWS(resp.optString("pWS"));
		userinfo.setUserId(resp.optInt("userId"));
		userinfo.setUserName(resp.optString("userName"));

		return userinfo;
	}

}
