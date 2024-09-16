package com.project.sdpd.myapplication;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String language;
    public boolean duplicate;
    public boolean created;
    public int num;

    public ChatMessage(String messageText, String messageUser, String language, boolean created, int num) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.language = language;
        // Initialize to current time
        messageTime = new Date().getTime();
        duplicate = false;
        this.created = created;
        this.num = num;
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public String getLanguage() {
        return language;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public boolean getDuplicate(){
        return duplicate;
    }

    public void setDuplicate(){
        duplicate = true;
    }

    public boolean getCreated(){
        return created;
    }

    public void setCreated(){
        created = true;
    }

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}