/**
 * 
 */
package com.baidu.api.client.core.login;


/**
 * @author baodi
 * @date 2013-1-8
 * @version V1.0
 */
public class VerifySmsResponse {

	// 0: success, 195: wrong sms code, 196: sms verify wrong time exceed limitation, 194: not login, 502: wrong parameter
	private int retcode;
	// error message
	private String retmsg;
	// error times
	private int errortime;
	/**
	 * @return the retcode
	 */
	public int getRetcode() {
		return retcode;
	}
	/**
	 * @param retcode the retcode to set
	 */
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	/**
	 * @return the retmsg
	 */
	public String getRetmsg() {
		return retmsg;
	}
	/**
	 * @param retmsg the retmsg to set
	 */
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	/**
	 * @return the errortime
	 */
	public int getErrortime() {
		return errortime;
	}
	/**
	 * @param errortime the errortime to set
	 */
	public void setErrortime(int errortime) {
		this.errortime = errortime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VerifySmsResponse [retcode=" + retcode + ", retmsg=" + retmsg
				+ ", errortime=" + errortime + "]";
	}
	
}

