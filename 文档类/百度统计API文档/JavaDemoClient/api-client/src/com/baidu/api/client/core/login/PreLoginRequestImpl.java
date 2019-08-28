/**
 *
 */
package com.baidu.api.client.core.login;


/**
 * @author baidu
 * @version V1.0
 * @date 2012-10-16
 */
public class PreLoginRequestImpl extends AbstractLoginRequest {

    private PreLoginRequest request;

    /**
     * @return the request
     */
    public PreLoginRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(PreLoginRequest request) {
        this.request = request;
    }

    public void setOsVersion(String osVersion) {
        initRequest();
        this.request.setOsVersion(osVersion);
    }

    public void setDeviceType(String deviceType) {
        initRequest();
        this.request.setDeviceType(deviceType);
    }

    public void setClientVersion(String clientVersion) {
        initRequest();
        this.request.setClientVersion(clientVersion);
    }

    private void initRequest() {
        if (request == null) {
            request = new PreLoginRequest();
        }
    }
}

