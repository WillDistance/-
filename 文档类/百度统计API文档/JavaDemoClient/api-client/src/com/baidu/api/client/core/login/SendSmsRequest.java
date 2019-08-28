/**
 * 
 */
package com.baidu.api.client.core.login;


/**
 * @author baidu
 * @date 2013-1-8
 * @version V1.0
 */
public class SendSmsRequest {

	// mobile str the user input
	private String strMobile;
	// ucid get from former login request
	private long ucid;
	// session id
	private String st;
	/**
	 * @return the strMobile
	 */
	public String getStrMobile() {
		return strMobile;
	}
	/**
	 * @param strMobile the strMobile to set
	 */
	public void setStrMobile(String strMobile) {
		this.strMobile = strMobile;
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
		return "SendSmsRequest [strMobile=" + strMobile + ", ucid=" + ucid
				+ ", st=" + st + "]";
	}
}

