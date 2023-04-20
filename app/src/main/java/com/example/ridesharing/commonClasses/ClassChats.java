package com.example.ridesharing.commonClasses;

public class ClassChats {
    private int imageRes;
    private String userName;
    private String userMessage;
    private String time;
    private String countOfMessages;

    public ClassChats(int imageRes, String userName, String userMessage, String time, String countOfMessages) {
        this.imageRes = imageRes;
        this.userName = userName;
        this.userMessage = userMessage;
        this.time = time;
        this.countOfMessages = countOfMessages;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCountOfMessages() {
        return countOfMessages;
    }

    public void setCountOfMessages(String countOfMessages) {
        this.countOfMessages = countOfMessages;
    }
}
