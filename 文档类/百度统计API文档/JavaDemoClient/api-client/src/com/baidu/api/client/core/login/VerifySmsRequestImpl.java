/**
 * Baidu.com Inc.
 * Copyright (c) 2000-2013 All Rights Reserved.
 */
package com.baidu.api.client.core.login;

/**
 * @title VerifySmsRequestImpl
 * @description TODO 
 * @author lyb1985031
 * @date 2013-4-12
 * @version 1.0
 */
public class VerifySmsRequestImpl extends AbstractLoginRequest{

    private VerifySmsRequest request;
    
    
    
    public final void setRequest(VerifySmsRequest request) {
        this.request = request;
    }

    @Override
    public VerifySmsRequest getRequest() {
        return request;
    }

    public void setUcid(long ucid) {
        initRequest();
        this.request.setUcid(ucid);
    }

    public void setSt(String st) {
        initRequest();
        this.request.setSt(st);
    }
    
    public void setCode(String code)
    {
        initRequest();
        this.request.setCode(code);
    }
    
    private void initRequest()
    {
        if(request==null)
            request = new VerifySmsRequest();
    }
}
