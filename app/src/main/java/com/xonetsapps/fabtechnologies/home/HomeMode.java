package com.xonetsapps.fabtechnologies.home;

public class HomeMode {

        String Description;
        String title,pId;
        int imgResId;
    String[] sublist,price,vendorId ;
    public HomeMode(String title, String[] sublist, String[] price,String[] vendorID, String pId){
       this.sublist=sublist;
        this.title=title;
        this.price=price;
        this.vendorId=vendorID;
        this.pId=pId;

    }


        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String[] getSublist() {
            return sublist;
        }

        public void setSublist(String[] sublist) {
            this.sublist = sublist;
        }
    public String[] getPrice() {
        return price;
    }

    public void setPrice(String[] price) {
        this.price = price;
    }

    public String[] getVendorId() {
        return vendorId;
    }

    public void setVendorId(String[] vendorId) {
        this.vendorId = vendorId;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }


}