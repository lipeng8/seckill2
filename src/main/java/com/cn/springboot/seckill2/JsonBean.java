package com.cn.springboot.seckill2;

public class JsonBean {
	
	public static final JsonBean success = new JsonBean();
	
	
	private String retCode  = "200";
	private String retMessage;
	
	private Object data;

	public JsonBean() {
	}

	public JsonBean(String code, String message) {
		this.retCode = code;
		this.retMessage = message;
	}


	public String getRetCode() {
		return retCode;
	}


	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMessage() {
		return retMessage;
	}


	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}




	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static JsonBean getSuccess() {
		return success;
	}

	/**
	 * 根据失败原因调用该方法
	 * @param reason
	 */
	public void fail(String reason){
		this.setRetCode("500");
		this.setRetMessage(reason);
		this.data = null;
	}
	
	/**
	 * 根据失败原因调用该方法
	 * @param reason
	 */
	public void success(String reason){
		this.setRetCode("200");
		this.setRetMessage(reason);
	}
	
	/**
	 * 根据失败原因调用该方法
	 */
	public void fail4hasLogout(){
		this.setRetCode("logout");
		this.data = null;
	}


	@Override
	public String toString() {
		return "JsonBean [retCode=" + retCode + ", retMessage=" + retMessage + ", data=" + data + "]";
	}
	
	
}
