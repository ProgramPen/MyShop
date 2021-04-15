package com.quchaoqun.order.domain;

import lombok.Data;

@Data
public class Order {
    private String oid;
    private int uid;
    private int aid;
    private int ocount;
    private String time;
    private int state;
}
