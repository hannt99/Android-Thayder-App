package com.example.datingapp2.ui.message;

public class ChatMessage {
    private String messageUser;
    private String messageText;
    private String messageTime;

    public ChatMessage() {
    }

    public ChatMessage(String messageUser, String messageText, String messageTime) {
        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageTime = messageTime;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return this.messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}



