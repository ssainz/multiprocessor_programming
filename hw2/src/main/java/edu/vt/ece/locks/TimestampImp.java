package edu.vt.ece.locks;

import java.sql.Time;

public class TimestampImp implements Timestamp {
    private long time;
    int i = -1;

    TimestampImp(int i ){
        time = System.currentTimeMillis();
        this.i = i;
    }

    @Override
    public boolean compare(Timestamp t) {

        TimestampImp timestampImp = (TimestampImp) t;
        if (this.time < timestampImp.time){
            return true;
        }else if(this.time == timestampImp.time){
            if(i < timestampImp.i){
                return true;
            }
        }

        return false;
    }
}
