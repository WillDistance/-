/**
 * LoginTest.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.baidu.api.client.core.BASE64Decoder;
import com.baidu.api.client.core.login.DoLoginRequestImpl;
import com.baidu.api.client.core.login.DoLoginResponse;
import com.baidu.api.client.core.login.DoLogoutRequestImpl;
import com.baidu.api.client.core.login.DoLogoutResponse;
import com.baidu.api.client.core.login.PreLoginRequestImpl;
import com.baidu.api.client.core.login.PreLoginResponse;
import com.baidu.api.client.core.login.SendSmsRequest;
import com.baidu.api.client.core.login.SendSmsRequestImpl;
import com.baidu.api.client.core.login.SendSmsResponse;
import com.baidu.api.client.core.login.VerifyQuestionRequestImpl;
import com.baidu.api.client.core.login.VerifyQuestionResponse;
import com.baidu.api.client.core.login.VerifySmsRequest;
import com.baidu.api.client.core.login.VerifySmsRequestImpl;
import com.baidu.api.client.core.login.VerifySmsResponse;
import com.baidu.api.client.core.seclogin.SecLoginService;

/**
 * @author @author@ (@author-email@)
 * @version @version@, $Date: 2012-4-10$
 */
public class SecLoginTest {

    public final static String IMAGE_PATH = "D:\\";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //测试用户userName,tokenStr
        SecLoginService login = new SecLoginProxy("ahzm", "12345678");

        PreLoginResponse res = pre(login);

        DoLoginResponse dol = doLoginWithInput(login, res);

        if (dol.getQuestions() != null && dol.getQuestions().size() > 0)
            veri(login, dol);

        SendSmsResponse sendRes = sendSms(login, dol);

        if (sendRes != null && sendRes.getRetcode() != 0) {
            System.out.println(sendRes.getRetcode() + "," + sendRes.getRetmsg());
        }

        System.out.println("send success");

        VerifySmsResponse verifyRes = verifySms(login, dol);

        if (verifyRes != null && verifyRes.getRetcode() != 0) {
            System.out.println(verifyRes.getRetcode() + "," + verifyRes.getRetmsg());
        }
        System.out.println("verify success");

        doLogout(login, dol);

    }

    /**
     * @param login
     * @param dol
     */
    private static void doLogout(SecLoginService login, DoLoginResponse dol) {
        DoLogoutRequestImpl req = new DoLogoutRequestImpl();
        req.setSt(dol.getSt());
        req.setUcid(dol.getUcid());
        DoLogoutResponse r = login.doLogout(req);
        System.out.println(r);
    }

    /**
     * @param login
     * @param dol
     * @return
     */
    private static VerifyQuestionResponse veri(SecLoginService login,
                                               DoLoginResponse dol) {
        VerifyQuestionRequestImpl req = new VerifyQuestionRequestImpl();
        req.setAnswer("1234567890");
        req.setQid(dol.getQuestions().get(0).getQid());
        req.setSt(dol.getSt());
        req.setUcid(dol.getUcid());
        VerifyQuestionResponse r = login.verifyQuestion(req);
        System.out.println(r);
        return r;
    }

    private static PreLoginResponse pre(SecLoginService login) {
        PreLoginRequestImpl req = new PreLoginRequestImpl();
        req.setClientVersion("0.1");
        req.setDeviceType("cellphone");
        req.setOsVersion("Android");

        PreLoginResponse res = login.preLogin(req);
        System.out.println(res);
        // 保存验证码图片到本地
        if (res.getAuthCode() != null) {
            saveAuthImage(res);
        }
        return res;
    }

    private static void saveAuthImage(PreLoginResponse res) {
        if (res != null) {
            try {
                byte[] buff = BASE64Decoder.decodeReturnByte(res.getAuthCode()
                        .getImgdata());
                File file = new File(IMAGE_PATH + "out.".concat(res.getAuthCode()
                        .getImgtype()));
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(buff);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static DoLoginResponse doLoginWithInput(SecLoginService login,
                                                    PreLoginResponse res) {
        DoLoginRequestImpl req = new DoLoginRequestImpl();
        // req.setImageCode("1111");
        if (res.getAuthCode() != null) {
            Scanner in = new Scanner(System.in);
            String code = in.nextLine();
            req.setImageCode(code);
            req.setImageSsid(res.getAuthCode().getImgssid());
        }
        //线上白名单用户
        //req.setPassword("Colin128");
        //线上对照组用户
        //req.setPassword("Aa123456");
        //线下测试用户
        req.setPassword("Asd123");
        DoLoginResponse dol = login.doLogin(req);
        System.out.println(dol);
        return dol;
    }

    private static SendSmsResponse sendSms(SecLoginService login, DoLoginResponse dlr) {
        SendSmsRequestImpl req = new SendSmsRequestImpl();
        req.setRequest(new SendSmsRequest());
        req.setFunctionName("sendSms");
        req.setUcid(430);
        req.setToken("12345678");
        req.setUsername("86flower");
        req.setStrMobile("12345678900");
        req.setUuid("dr-api-test-123456");
        req.setSt(dlr.getSt());

        return login.sendSms(req);
    }

    private static VerifySmsResponse verifySms(SecLoginService login, DoLoginResponse dlr) {
        VerifySmsRequestImpl req = new VerifySmsRequestImpl();
        req.setRequest(new VerifySmsRequest());
        req.setCode("123456");
        req.setFunctionName("verifySms");
        req.setSt(dlr.getSt());
        req.setToken("12345678");
        req.setUcid(430);
        req.setUsername("86flower");
        req.setUuid("dr-api-test-123456");

        return login.verifySms(req);
    }

}
