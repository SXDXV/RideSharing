package com.example.ridesharing.commonClasses;

public class ClassMessage {
    private String chat_id;
    private String sender;
    private String receiver;
    private String message_text;
    private long time;

    public ClassMessage(String chat_id, String sender, String receiver, String message_text, long time) {
        this.chat_id = chat_id;
        this.sender = sender;
        this.receiver = receiver;
        this.message_text = message_text;
        this.time = time;
    }

    public ClassMessage() {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
