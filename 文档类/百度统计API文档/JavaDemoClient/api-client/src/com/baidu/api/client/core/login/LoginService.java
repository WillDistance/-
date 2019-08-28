/**
 * LoginService.java
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
package com.baidu.api.client.core.login;


/**
 * @author @author@ (@author-email@)
 * @version @version@, $Date: 2012-4-10$
 */
public interface LoginService {
    public PreLoginResponse preLogin(PreLoginRequest req);

    public DoLoginResponse doLogin(DoLoginRequest req);

    public VerifyQuestionResponse verifyQuestion(VerifyQuestionRequest req);

    public DoLogoutResponse doLogout(DoLogoutRequest req);

    public VerifySmsResponse verifySms(VerifySmsRequest req);

    public SendSmsResponse sendSms(SendSmsRequest req);
}
