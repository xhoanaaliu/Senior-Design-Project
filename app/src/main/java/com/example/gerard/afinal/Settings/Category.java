package com.example.gerard.afinal.Settings;


public class Category {

    private String categoryName1;
    private String getCategoryName;
    private int categoryPicture, categoryPicture1;

    public Category(String categoryName1, String getCategoryName, int categoryPicture, int categoryPicture1){
        this.categoryName1 = categoryName1;
        this.categoryPicture = categoryPicture;
        this.categoryPicture1 = categoryPicture1;
        this.getCategoryName = getCategoryName;
    }

    public String getCategoryName1() {
        return categoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    public String getGetCategoryName() {
        return getCategoryName;
    }

    public void setGetCategoryName(String getCategoryName) {
        this.getCategoryName = getCategoryName;
    }

    public int getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryPicture(int categoryPicture) {
        this.categoryPicture = categoryPicture;
    }

    public int getCategoryPicture1() {
        return categoryPicture1;
    }

    public void setCategoryPicture1(int categoryPicture1) {
        this.categoryPicture1 = categoryPicture1;
    }
}

