package com.tafarri.tafarri.Models;

public class BookModel {

    private static final long serialVersionUID = 1L;
    private long book_id;
    private String book_sys_id;
    private String book_title;
    private String book_author;
    private String book_ratings;
    private String book_description_short;
    private String book_description_long;
    private String book_pages;
    private String book_cover_photo;
    private String book_pdf;
    private String book_summary_pdf;
    private String book_audio;
    private String book_summary_audio;
    private String book_cost;
    private String book_summary_cost;
    private String book_full_purchased;
    private String book_summary_purchased;
    private String book_reference_url;
    private String created_at;
    private String updated_at;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getBook_sys_id() {
        return book_sys_id;
    }

    public void setBook_sys_id(String book_sys_id) {
        this.book_sys_id = book_sys_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_ratings() {
        return book_ratings;
    }

    public void setBook_ratings(String book_ratings) {
        this.book_ratings = book_ratings;
    }

    public String getBook_description_short() {
        return book_description_short;
    }

    public void setBook_description_short(String book_description_short) {
        this.book_description_short = book_description_short;
    }

    public String getBook_description_long() {
        return book_description_long;
    }

    public void setBook_description_long(String book_description_long) {
        this.book_description_long = book_description_long;
    }

    public String getBook_pages() {
        return book_pages;
    }

    public void setBook_pages(String book_pages) {
        this.book_pages = book_pages;
    }

    public String getBook_cover_photo() {
        return book_cover_photo;
    }

    public void setBook_cover_photo(String book_cover_photo) {
        this.book_cover_photo = book_cover_photo;
    }

    public String getBook_pdf() {
        return book_pdf;
    }

    public void setBook_pdf(String book_pdf) {
        this.book_pdf = book_pdf;
    }

    public String getBook_summary_pdf() {
        return book_summary_pdf;
    }

    public void setBook_summary_pdf(String book_summary_pdf) {
        this.book_summary_pdf = book_summary_pdf;
    }

    public String getBook_audio() {
        return book_audio;
    }

    public void setBook_audio(String book_audio) {
        this.book_audio = book_audio;
    }

    public String getBook_summary_audio() {
        return book_summary_audio;
    }

    public void setBook_summary_audio(String book_summary_audio) {
        this.book_summary_audio = book_summary_audio;
    }

    public String getBook_cost() {
        return book_cost;
    }

    public void setBook_cost(String book_cost) {
        this.book_cost = book_cost;
    }

    public String getBook_summary_cost() {
        return book_summary_cost;
    }

    public void setBook_summary_cost(String book_summary_cost) {
        this.book_summary_cost = book_summary_cost;
    }

    public String getBook_full_purchased() {
        return book_full_purchased;
    }

    public void setBook_full_purchased(String book_full_purchased) {
        this.book_full_purchased = book_full_purchased;
    }

    public String getBook_summary_purchased() {
        return book_summary_purchased;
    }

    public void setBook_summary_purchased(String book_summary_purchased) {
        this.book_summary_purchased = book_summary_purchased;
    }

    public String getBook_reference_url() {
        return book_reference_url;
    }

    public void setBook_reference_url(String book_reference_url) {
        this.book_reference_url = book_reference_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
