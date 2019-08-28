/**
 * AuthCode.java
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
public class AuthCode {
    // 图片的格式，比如JPG类型的图片，就是JPG三个字母
    private String imgtype;
    // 图片的二进制内容，base64编码
    private String imgdata;
    // 图片会话id
    private String imgssid;

    public String getImgtype() {
        return imgtype;
    }

    public void setImgtype(String imgtype) {
        this.imgtype = imgtype;
    }

    public String getImgdata() {
        return imgdata;
    }

    public void setImgdata(String imgdata) {
        this.imgdata = imgdata;
    }

    public String getImgssid() {
        return imgssid;
    }

    public void setImgssid(String imgssid) {
        this.imgssid = imgssid;
    }

    @Override
    public String toString() {
        return "[imgtype=" + imgtype + ", imgdata=" + imgdata + ", imgssid=" + imgssid + "]";
    }
    
}
