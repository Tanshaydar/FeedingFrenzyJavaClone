package com.bilkent.feedingbobby.model;

import java.io.Serializable;

public class HighScore implements Serializable, Comparable<HighScore> {

    private String name;
    private int score;
    private String date;

    public HighScore() {
    }

    public HighScore(String name, int score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore( int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date) {
        this.date = date;
    }

    @Override
    public int compareTo( HighScore o) {
        if (o.getScore() > getScore()) {
            return -1;
        } else if (o.getScore() < getScore()) {
            return 1;
        } else {
            return 0;
        }
    }

}
