package com.uet.hoangtrinh.nguoibanhang;

import com.uet.hoangtrinh.GaApplication;

import java.util.Arrays;

public class Entity {
    private int content[];
    private int score;

    public Entity() {
    }

    public Entity(Entity entity) {
        this.content = entity.content.clone();
        this.score = entity.score;
    }

    public Entity(int[] content) {
        this.content = content;
        this.score = 0;
    }

    public Entity(int[] content, int score) {
        this.content = content;
        this.score = score;
    }

    public int[] getContent() {
        return content;
    }

    public void setContent(int[] content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return Arrays.toString(content) + ": " + score;
    }
}
