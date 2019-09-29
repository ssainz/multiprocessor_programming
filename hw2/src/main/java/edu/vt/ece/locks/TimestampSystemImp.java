package edu.vt.ece.locks;

public class TimestampSystemImp implements  TimestampSystem{

    private Timestamp[] stamps = null;


    public TimestampSystemImp(int n){
        stamps = new TimestampImp[n];
        for(int k = 0 ; k < stamps.length ; k++){
            stamps[k] = new TimestampImp(k);
        }
    }

    @Override
    public Timestamp[] scan() {
        return stamps;
    }

    @Override
    public void label(Timestamp ts, int i) {
        stamps[i] = ts;
    }
}
