/**
 * DoLoginResponse.java
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

import java.util.List;

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2012-3-22$
 * 
 */
public class DoLoginResponse {
    // 0：成功，1：该用户被锁定，2：该用户需要回答密保问题，3：登陆IP被封禁，4：用户不存在，5：密码错误 6：参数错误 7：验证码错误
    private int retcode;
    // 错误信息
    private String retmsg;
    // 用户ucid
    private long ucid;
    // 会话ID
    private String st;
    // 是否是token登陆用户
    private int istoken;
    // 是否需要设置Pin码
    private int setpin;
    // 安全問題列表
    private List<Question> questions;

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

    public long getUcid() {
        return ucid;
    }

    public void setUcid(long ucid) {
        this.ucid = ucid;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getIstoken() {
        return istoken;
    }

    public void setIstoken(int istoken) {
        this.istoken = istoken;
    }

    public int getSetpin() {
        return setpin;
    }

    public void setSetpin(int setpin) {
        this.setpin = setpin;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DoLoginResponse [retcode=").append(retcode).append(", retmsg=").append(retmsg)
                .append(", ucid=").append(ucid).append(", st=").append(st).append(", istoken=").append(istoken)
                .append(", setpin=").append(setpin).append(", questions=").append(questions).append("]");
        return builder.toString();
    }

}
