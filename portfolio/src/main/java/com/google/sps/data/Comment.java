package com.google.sps.data;

public final class Comment{

    private String body;
    private long timestamp;
    private double sentimentScore;

    public Comment(String body, long timestamp, double sentimentScore){
        this.body = body;
        this.timestamp = timestamp;
        this.sentimentScore = sentimentScore;
    }

}
