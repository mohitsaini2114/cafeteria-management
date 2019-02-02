package com.msaini.cafetriamanagementsystem;

import java.io.Serializable;

public class FoodList implements Serializable {

    String description;
    String image;
    String price;
    String imageUrl;
    String key;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public FoodList() {
    }

    public FoodList(String description, String image, String price) {
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
