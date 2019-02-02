package com.msaini.cafetriamanagementsystem;

public class VendorDetails {

    public String vendorId;
    public String password;
    public String name;
    public String key;

    public VendorDetails() {

    }

    public VendorDetails(String vendorId, String password, String name) {
        this.vendorId = vendorId;
        this.password = password;
        this.name = name;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
