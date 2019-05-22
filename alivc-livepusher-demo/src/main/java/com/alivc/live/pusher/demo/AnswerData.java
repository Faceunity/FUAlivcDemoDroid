package com.alivc.live.pusher.demo;

import java.util.List;

public class AnswerData {
    String type;
    List<AnswerInfo> contents;

    public String getmType() {
        return type;
    }

    public void setmType(String mType) {
        this.type = mType;
    }

    public List<AnswerInfo> getmContents() {
        return contents;
    }

    public void setmContents(List<AnswerInfo> mContents) {
        this.contents = mContents;
    }
}
