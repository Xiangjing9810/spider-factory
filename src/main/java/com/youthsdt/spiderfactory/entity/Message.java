package com.youthsdt.spiderfactory.entity;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/3 14:17
 */
public class Message {
    private String id;
    private String msg;
    private long sendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
