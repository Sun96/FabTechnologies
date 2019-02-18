package com.xonetsapps.fabtechnologies.invoice;

public class InvoiceMode {
    public String numbr;
    public String name;
    public String proPric;
    public String totalAmmount;
    public String proname;
    public String code;
    public String order;

    public String Description;
    public String title;

    public InvoiceMode(String proname,String title, String proPric, String totalAmmount){
        this.name=title;
        this.proPric=proPric;
        this.totalAmmount=totalAmmount;
        this.proname=proname;
    }

    public String getProName() {
        return proname;
    }

    public void setProName(String proname) {
        proname = proname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public String getProPric() {
        return proPric;
    }

    public void setProPric(String proPric) {
        this.proPric = proPric;
    }

    public String getAmmount() {
        return totalAmmount;
    }

    public void setAmmount(String totalAmmount) {
        totalAmmount = totalAmmount;
    }
}
