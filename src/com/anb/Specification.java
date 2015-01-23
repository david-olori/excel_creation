package com.anb;

/**
 * Created with IntelliJ IDEA.
 * User: davidolori
 * Date: 1/21/15
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Specification {

    private String trans;
    private String carrier;
    private String engine;
    private String hp;

    Specification(){

        trans="";
        carrier="";
        engine="";
        hp="";
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }
}
