/**
 * 
 */
package com.baidu.api.client.core.login;


/**
 * @author baidu
 * @date 2012-10-15
 * @version V1.0
 */
public interface LoginRequest {

	String getUsername();
	
	String getToken();
	
	String getUuid();
	
	String getFunctionName();
	
	Object getRequest();
}

