package com.anjlab.android.iab.v3;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "PayRecordBean")
public class PayRecordBean {
	@Column(name = "id")
	private int id;
	// 表id
	@Column(name = "appId")
	private String appId;
	// 游戏区服
	@Column(name = "coo_server")
	private String coo_server;
	// 游戏角色id
	@Column(name = "coo_uid")
	private String coo_uid;
	// 用户id
	@Column(name = "uid")
	private String uid;
	// 充值成功返回
	@Column(name = "receipt")
	private String receipt;
	// google官冲签名
	@Column(name = "signature")
	private String signature;
	// 是否成功
	@Column(name = "isSuccess")
	private int isSuccess;
	// 是否成功
	@Column(name = "extra")
	private String extra;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCoo_server() {
		return coo_server;
	}

	public void setCoo_server(String coo_server) {
		this.coo_server = coo_server;
	}

	public String getCoo_uid() {
		return coo_uid;
	}

	public void setCoo_uid(String coo_uid) {
		this.coo_uid = coo_uid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return "PayRecordBean [id=" + id + ", appId=" + appId + ", coo_server="
				+ coo_server + ", coo_uid=" + coo_uid + ", uid=" + uid
				+ ", receipt=" + receipt + ", signature=" + signature
				+ ", isSuccess=" + isSuccess + ", extra=" + extra + "]";
	}

	

}
