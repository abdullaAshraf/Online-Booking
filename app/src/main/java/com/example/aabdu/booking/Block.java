package com.example.aabdu.booking;

import android.graphics.Color;

public class Block {
    private boolean reserved;
    private int order;

    public Block(int order){
        this.order = order;
        reserved = false;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public int getColor(){
        if(reserved)
            return Color.parseColor("#bbbbbb");
        else
            return Color.parseColor("#4150B5");
    }

    public String getTime() {
        int h = order/2;
        int m = (order%2)*30;
        String time = "";

        if(h < 10)
            time += "0"+h+":";
        else
            time += h+":";

        if(m < 10)
            time += "0"+m;
        else
            time += m;

        return time;
    }
}
