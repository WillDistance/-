/**

 * SecLoginService.java
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
package com.baidu.api.client.core.seclogin;

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

/**
 * @author @author@ (@author-email@)
 * @version @version@, $Date: 2012-4-10$
 */
public interface SecLoginService {
    public PreLoginResponse preLogin(PreLoginRequestImpl req);

    public DoLoginResponse doLogin(DoLoginRequestImpl req);

    public VerifyQuestionResponse verifyQuestion(VerifyQuestionRequestImpl req);

    public DoLogoutResponse doLogout(DoLogoutRequestImpl req);

    public VerifySmsResponse verifySms(VerifySmsRequestImpl req);

    public SendSmsResponse sendSms(SendSmsRequestImpl req);
}
