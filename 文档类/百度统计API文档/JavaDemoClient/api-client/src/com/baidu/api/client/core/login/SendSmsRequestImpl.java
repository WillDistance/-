/**
 * Baidu.com Inc.
 * Copyright (c) 2000-2013 All Rights Reserved.
 */
package com.baidu.api.client.core.login;

/**
 * @author lyb1985031
 * @version 1.0
 * @title SendSmsRequestImpl
 * @description TODO
 * @date 2013-4-11
 */
public class SendSmsRequestImpl extends AbstractLoginRequest {

    private SendSmsRequest request;

    @Override
    public SendSmsRequest getRequest() {
        return request;
    }

    public void setRequest(SendSmsRequest request) {
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

    public void setStrMobile(String strMobile) {
        initRequest();
        this.request.setStrMobile(strMobile);
    }

    private void initRequest() {
        if (request == null)
            request = new SendSmsRequest();
    }

}
