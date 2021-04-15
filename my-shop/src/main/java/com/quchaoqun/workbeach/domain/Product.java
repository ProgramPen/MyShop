package com.quchaoqun.workbeach.domain;

import lombok.Data;

@Data
public class Product {
    private int pid;
    private int tid;
    private String name;
    private String time;
    private String image;
    private double price;
    private int state;
    private String info;

}
