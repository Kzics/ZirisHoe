package fr.zirishoe.utils;

public enum CropsState {

    SMALL_GROWTH((byte) 1),
    MID_GROWTH((byte)2 ),
    FULLY_GROWTH((byte) 3);



    private byte state;

    CropsState(byte state){
        this.state = state;

    }


    public byte getState() {
        return state;
    }
}
