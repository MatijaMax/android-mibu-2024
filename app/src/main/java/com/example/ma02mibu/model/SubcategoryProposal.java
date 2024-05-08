package com.example.ma02mibu.model;

public class SubcategoryProposal {
    private Subcategory subcategory;
    //ID servisa ili usluge
    private String itemId;
    public SubcategoryProposal(){}

    public SubcategoryProposal(Subcategory subcategory, String itemId) {
        this.subcategory = subcategory;
        this.itemId = itemId;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
