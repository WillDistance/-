/**
 * VerifyQuestionResponse.java
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
public class VerifyQuestionResponse {
    // 0：成功，1：该用户被锁定， 3：登陆IP被封禁， 8，回答错误  9：回答错误次数已超限制 10：会话无效 11：会话已经回答过密保问题
    private int retcode;
    // error具体信息
    private String retmsg;
    // 记录已经回答错了几次
    private int errortime;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public int getErrortime() {
        return errortime;
    }

    public void setErrortime(int errortime) {
        this.errortime = errortime;
    }

    @Override
    public String toString() {
        return "VerifyQuestionResponse [retcode=" + retcode + ", retmsg=" + retmsg + ", errortime=" + errortime + "]";
    }
    
}
