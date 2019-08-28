/**
 * 
 */
package com.baidu.api.client.core.login;


/**
 * @author baidu
 * @date 2013-1-8
 * @version V1.0
 */
public class VerifySmsRequest {

	// sms code which user inputs.
	private String code;
	// ucid which get from former login.
	private long ucid;
	// session id.
	private String st;
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the ucid
	 */
	public long getUcid() {
		return ucid;
	}
	/**
	 * @param ucid the ucid to set
	 */
	public void setUcid(long ucid) {
		this.ucid = ucid;
	}
	/**
	 * @return the st
	 */
	public String getSt() {
		return st;
	}
	/**
	 * @param st the st to set
	 */
	public void setSt(String st) {
		this.st = st;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VerifySmsRequest [code=" + code + ", ucid=" + ucid + ", st="
				+ st + "]";
	}
	
}

