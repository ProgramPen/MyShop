package com.quchaoqun.shoppingcart.domain;

import lombok.Data;

@Data
public class Cart {
    private int cid;
    private int uid;
    private int pid;
    private double cCount;
    private int cNum;

}
