/**
 * PreLoginRequest.java
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
public class PreLoginRequest {
    // 客户端载体操作系统
    private String osVersion;
    // 客户端载体类型
    private String deviceType;
    // 客户端版本
    private String clientVersion;

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[OS ").append(osVersion).append(", Type ").append(deviceType).append(", Ver ")
                .append(clientVersion).append("]");
        return builder.toString();
    }

}
