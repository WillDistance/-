/**
 * PreLoginResponse.java
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
 * 
 * @version @version@, $Date: 2012-3-22$
 * 
 */
public class PreLoginResponse {
    // 是否需要验证码
    private boolean needAuthCode;
    // 验证码详细内容
    private AuthCode authCode;

    public boolean isNeedAuthCode() {
        return needAuthCode;
    }

    public void setNeedAuthCode(boolean needAuthCode) {
        this.needAuthCode = needAuthCode;
    }

    public AuthCode getAuthCode() {
        return authCode;
    }

    public void setAuthCode(AuthCode authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "PreLoginResponse [needAuthCode=" + needAuthCode + ", authCode=" + authCode + "]";
    }

}
