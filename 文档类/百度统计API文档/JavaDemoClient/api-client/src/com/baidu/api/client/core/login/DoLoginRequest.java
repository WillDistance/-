/**
 * DoLoginRequest.java
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
public class DoLoginRequest {
    // 用户输入密码
    private String password;
    // 验证码
    private String imageCode;
    // 验证码会话id
    private String imageSsid;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getImageSsid() {
        return imageSsid;
    }

    public void setImageSsid(String imageSsid) {
        this.imageSsid = imageSsid;
    }

    @Override
    public String toString() {
        return "DoLoginRequest [imageCode=" + imageCode + ", imageSsid=" + imageSsid + "]";
    }

}
