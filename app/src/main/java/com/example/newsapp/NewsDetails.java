package com.example.newsapp;

public class NewsDetails {

    String mTitle;
    String mSection;
    String mUrl;
    String mDate;
    String mAuthor;

    public NewsDetails(String mTitle, String mSection, String mUrl, String mDate, String mAuthor) {
        this.mTitle = mTitle;
        this.mSection = mSection;
        this.mUrl = mUrl;
        this.mDate = mDate;
        this.mAuthor = mAuthor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmAuthor() {
        if(mAuthor == null)
            return "Author Unavailable";
        return mAuthor;
    }
}
