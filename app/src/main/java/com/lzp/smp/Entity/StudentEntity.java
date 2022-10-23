package com.lzp.smp.Entity;

public class StudentEntity {
    private int rang;
    private String num;
    private String name;
    private float c;
    private float english;
    private float math;
    private float all;
    private float ave;

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }

    public float getAve() {
        return getAll() /3;
    }

    public void setAve(float ave) {
        this.ave = ave;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getEnglish() {
        return english;
    }

    public void setEnglish(float english) {
        this.english = english;
    }

    public float getMath() {
        return math;
    }

    public void setMath(float math) {
        this.math = math;
    }

    public float getAll() {
        return c + english + math;
    }

    public void setAll(float all) {
        this.all = all;
    }
}
