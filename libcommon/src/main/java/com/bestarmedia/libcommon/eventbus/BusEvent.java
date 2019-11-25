package com.bestarmedia.libcommon.eventbus;

public class BusEvent {

    public int id;
    public Object data;


    public static BusEvent getEvent(int id) {
        return new BusEvent(id);
    }

    public static BusEvent getEvent(int id, Object data) {
        return new BusEvent(id, data);
    }

    private BusEvent() {
    }

    private BusEvent(int id) {
        this.id = id;
    }

    private BusEvent(int id, Object data) {
        this.id = id;
        this.data = data;
    }

}
