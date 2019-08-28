/**
 * 
 */
package com.baidu.api.client.core.login;

/**
 * @author baidu
 * @date 2012-10-16
 * @version V1.0
 */
public class DoLogoutRequestImpl extends AbstractLoginRequest {

	private DoLogoutRequest request;

	/**
	 * @return the request
	 */
	public DoLogoutRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(DoLogoutRequest request) {
		this.request = request;
	}

    public void setUcid(long ucid) {
    	initRequest();
        this.request.setUcid(ucid);
    }

    public void setSt(String st) {
    	initRequest();
        this.request.setSt(st);
    }
	
	private void initRequest(){
		if(request == null){
			this.request = new DoLogoutRequest();
		}
	}
}

