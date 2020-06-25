package com.david0926.drop.model;

public class ArticleModel {

    private String id, user_name, user_email, user_profile, type, upload_time, product_name,
            product_image, product_desc, product_time, product_place, product_addinfo;
    private Boolean solve;
    private CommentModel[] comments;

    public ArticleModel() {
    }

    public ArticleModel(String id, String user_name, String user_email, String user_profile, String type, String upload_time, String product_name, String product_image, String product_desc, String product_time, String product_place, String product_addinfo, Boolean solve, CommentModel[] comments) {
        this.id = id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_profile = user_profile;
        this.type = type;
        this.upload_time = upload_time;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_desc = product_desc;
        this.product_time = product_time;
        this.product_place = product_place;
        this.product_addinfo = product_addinfo;
        this.solve = solve;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_time() {
        return product_time;
    }

    public void setProduct_time(String product_time) {
        this.product_time = product_time;
    }

    public String getProduct_place() {
        return product_place;
    }

    public void setProduct_place(String product_place) {
        this.product_place = product_place;
    }

    public String getProduct_addinfo() {
        return product_addinfo;
    }

    public void setProduct_addinfo(String product_addinfo) {
        this.product_addinfo = product_addinfo;
    }

    public Boolean getSolve() {
        return solve;
    }

    public void setSolve(Boolean solve) {
        this.solve = solve;
    }

    public CommentModel[] getComments() {
        return comments;
    }

    public void setComments(CommentModel[] comments) {
        this.comments = comments;
    }
}
