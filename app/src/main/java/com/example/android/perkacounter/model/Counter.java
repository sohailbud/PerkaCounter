package com.example.android.perkacounter.model;

public class Counter {

    private String name;
    private Integer count = 0;

    public Counter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void increaseCount() {
        count++;
    }

    public void decreaseCount() {
        if (count >= 1) count--;
    }
}
