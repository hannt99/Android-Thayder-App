package com.example.datingapp2.ui.matches;

public class MatchModel {
    private String profileImagesUrl;
    private String NickName;
    private String UserName;

    public MatchModel() {
    }

    public MatchModel(String profileImagesUrl, String NickName, String UserName) {
        this.profileImagesUrl = profileImagesUrl;
        this.NickName = NickName;
        this.UserName = UserName;
    }

    public String getProfileImagesUrl() {
        return profileImagesUrl;
    }

    public void setProfileImagesUrl(String profileImagesUrl) {
        this.profileImagesUrl = profileImagesUrl;
    }

    public String getNickName() {
        return this.NickName;
    }

    public void setNickName(String name) {
        this.NickName = name;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String name) {
        this.UserName = name;
    }
}
