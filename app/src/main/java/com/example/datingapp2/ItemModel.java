package com.example.datingapp2;

public class ItemModel {
    private String imageLink, nickname, username,age, city, khoangcach;

    public ItemModel() {
        this.imageLink = "https://firebasestorage.googleapis.com/v0/b/datingapp-135e3.appspot.com/o/profileImages%2Fz9y3i7RKJkXAwBZDtvg3q9RBios2%2F25?alt=media&token=cabc15be-702b-447f-b7e4-0b5fa8af9d80";
        this.nickname = "Henry";
        this.username = "huy";
        this.age = "30";
        this.city = "Ho Chi Minh";
        this.khoangcach = "10";
    }

    public ItemModel(String imageLink, String nickname, String username, String age, String city, String khoangcach) {
        this.imageLink = imageLink;
        this.nickname = nickname;
        this.username = username;
        this.age = age;
        this.city = city;
        this.khoangcach = khoangcach;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String name) {
        this.nickname = name;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getAge() {
        return age;
    }


    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(String khoangcach) {
        this.khoangcach = khoangcach;
    }
}
