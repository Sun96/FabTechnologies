package com.xonetsapps.fabtechnologies.home;

import java.util.ArrayList;

public class SubMode {
    String subprice;
    String subtitle,vId,pId;
    int imgResId;
    private boolean isSelected;
    ArrayList<String> sublist = new ArrayList<String>();
    SubMode(String title, String price,String vId,String pId){
        this.subtitle=title;
        this.subprice=price;
        this.vId=vId;
        this.pId=pId;

    }


    public String getSubprice() {
        return subprice;
    }

    public void setSubprice(String subprice) {
        subprice = subprice;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getVid() {
        return vId;
    }

    public void setVid(String vId) {
        this.vId = vId;
    }
    public String getPid() {
        return pId;
    }

    public void setPid(String pId) {
        this.pId = pId;
    }



    public ArrayList<String> getSublist() {
        return sublist;
    }

    public void setSublist(ArrayList<String> sublist) {
        this.sublist = sublist;
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
