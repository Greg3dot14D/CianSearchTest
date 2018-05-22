package ru.greg3d.ciansearchtest.helpers;

public class IndexHolder {
    private int index = 0;

    public int getIndex(){
        return this.index;
    }

    public IndexHolder incrementIndex(){
        this.index ++;
        return this;
    }
}
