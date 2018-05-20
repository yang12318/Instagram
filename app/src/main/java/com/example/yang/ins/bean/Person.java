package com.example.yang.ins.bean;

/**
 * Created by youxihouzainali on 2018/5/13.
 */

public class Person {
    private int id;
    private String name;
    private String nickname;
    private String src;
    private boolean isFollowed;

    public Person() {
        isFollowed  = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }
}
