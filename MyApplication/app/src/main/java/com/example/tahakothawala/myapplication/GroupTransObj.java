package com.example.tahakothawala.myapplication;

/**
 * Created by tahakothawala on 26/09/2017 AD.
 */

public class GroupTransObj  {
    private String DATE,TO,FROM,DESC;
    private int AMNT;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public GroupTransObj(String TO, String FROM, String DESC, int AMNT, String DATE) {
        this.TO = TO;
        this.FROM = FROM;
        this.AMNT = AMNT;
        this.DATE = DATE;
        this.DESC = DESC;

    }

    public String getTO() {
        return TO;
    }

    public void setTO(String TO) {
        this.TO = TO;
    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
    }

    public int getAMNT() {
        return AMNT;
    }

    public void setAMNT(int AMNT) {
        this.AMNT = AMNT;
    }
}
