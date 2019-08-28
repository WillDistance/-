/**
 * LoginProxy.java
 *
 * Copyright 2012 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.baidu.seclogin_test;

import java.security.Key;

import com.baidu.api.client.core.ClientInternalException;
import com.baidu.api.client.core.LoginConnection;
import com.baidu.api.client.core.RSAUtil;
import com.baidu.api.client.core.login.DoLoginRequestImpl;
import com.baidu.api.client.core.login.DoLoginResponse;
import com.baidu.api.client.core.login.DoLogoutRequestImpl;
import com.baidu.api.client.core.login.DoLogoutResponse;
import com.baidu.api.client.core.login.PreLoginRequestImpl;
import com.baidu.api.client.core.login.PreLoginResponse;
import com.baidu.api.client.core.login.SendSmsRequestImpl;
import com.baidu.api.client.core.login.SendSmsResponse;
import com.baidu.api.client.core.login.VerifyQuestionRequestImpl;
import com.baidu.api.client.core.login.VerifyQuestionResponse;
import com.baidu.api.client.core.login.VerifySmsRequestImpl;
import com.baidu.api.client.core.login.VerifySmsResponse;
import com.baidu.api.client.core.seclogin.SecLoginService;

/**
 * @author @author@ (@author-email@)
 * @version @version@, $Date: 2012-4-10$
 */
public class SecLoginProxy implements SecLoginService {

    //上线机器
    public static final String ADDRESS = "https://api.baidu.com/sem/common/SecLoginService";
    //        public static final String ADDRESS = "http://fctest.baidu.com:8080/gateway/sem/common/SecLoginService";
    public static final String KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdNjuYkrcNa3+QWjqL++ColDXcpzyZvvAQHsUpp3jQMWoe3GVdFxm6n8HlTcLmRS8u8Pb/fq8Ii3xofwbCyVAU6yZ+U3jb+Yq/Qul0iW1bScMjrQG41IOkaYxm1GTiT5sm83737TnoBl267cEJEnGCTeqfzGx15Zun66bjaPi4uwIDAQAB";
    public static final String UUID = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBg-1";
    private String username;
    private String token;

    /**
     * @param username
     * @param token
     */
    public SecLoginProxy(String username, String token) {
        super();
        this.username = username;
        this.token = token;
    }

    private LoginConnection makeConnection() {
        LoginConnection l;
        try {
            Key publicKey = RSAUtil.getPublicKey(KEY);
            l = new LoginConnection(ADDRESS);
            l.setPublicKey(publicKey);
            return l;
        } catch (Exception e) {
            throw new ClientInternalException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.baidu.api.client.core.login.LoginService#preLogin(com.baidu.api.client.core.login.PreLoginRequest)
     */
    public PreLoginResponse preLogin(PreLoginRequestImpl req) {
        LoginConnection l = makeConnection();
        req.setUsername(username);
        req.setUuid(UUID);
        req.setToken(token);
        req.setFunctionName("preLogin");
        l.sendRequest(req);
        return l.readSecClearResponse(PreLoginResponse.class);
    }

    /* (non-Javadoc)
     * @see com.baidu.api.client.core.login.LoginService#doLogin(com.baidu.api.client.core.login.DoLoginRequest)
     */
    public DoLoginResponse doLogin(DoLoginRequestImpl req) {
        LoginConnection l = makeConnection();
        req.setUsername(username);
        req.setUuid(UUID);
        req.setToken(token);
        req.setFunctionName("doLogin");
        l.sendRequest(req);
        return l.readSecClearResponse(DoLoginResponse.class);
    }

    /* (non-Javadoc)
     * @see com.baidu.api.client.core.login.LoginService#verifyQuestion(com.baidu.api.client.core.login.VerifyQuestionRequest)
     */
    public VerifyQuestionResponse verifyQuestion(VerifyQuestionRequestImpl req) {
        LoginConnection l = makeConnection();
        req.setUsername(username);
        req.setUuid(UUID);
        req.setToken(token);
        req.setFunctionName("verifyQuestion");
        l.sendRequest(req);
        return l.readSecClearResponse(VerifyQuestionResponse.class);
    }

    /* (non-Javadoc)
     * @see com.baidu.api.client.core.login.LoginService#doLogout(com.baidu.api.client.core.login.DoLogoutRequest)
     */
    public DoLogoutResponse doLogout(DoLogoutRequestImpl req) {
        LoginConnection l = makeConnection();
        req.setUsername(username);
        req.setUuid(UUID);
        req.setToken(token);
        req.setFunctionName("doLogout");
        l.sendRequest(req);
        return l.readSecClearResponse(DoLogoutResponse.class);
    }

    /* (non-Javadoc)
     * @see com.baidu.api.client.core.seclogin.SecLoginService#verifySms(com.baidu.drapi.iauth.pubsec.VerifySmsRequestImpl)
     */
    public VerifySmsResponse verifySms(VerifySmsRequestImpl req) {
        LoginConnection l = makeConnection();
        req.setUsername(username);
        req.setUuid(UUID);
        req.setToken(token);
        req.setFunctionName("verifySms");
        l.sendRequest(req);
        return l.readSecClearResponse(VerifySmsResponse.class);
    }

    /* (non-Javadoc)
     * @see com.baidu.api.client.core.seclogin.SecLoginService#sendSms(com.baidu.drapi.iauth.pubsec.SendSmsRequestImpl)
     */
    public SendSmsResponse sendSms(SendSmsRequestImpl req) {
        LoginConnection l = makeConnection();
        req.setUsername(username);
        req.setUuid(UUID);
        req.setToken(token);
        req.setFunctionName("sendSms");
        l.sendRequest(req);
        return l.readSecClearResponse(SendSmsResponse.class);
    }
}
