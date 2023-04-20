package com.example.ridesharing.commonClasses;

/**
 * Класс описания публикации. Необходим для получения информации с сервера и получения
 * структурированных данных
 */
public class ClassPublication {
    private String author_id;
    private String from;
    private String to;
    private String peoples;
    private String date;
    private String time;
    private String price;
    private String comment;
    private boolean music;
    private boolean pets;
    private boolean talk;
    private boolean smoking;

    public ClassPublication(String author_id, String from, String to, String peoples,
                            String date, String time, String price, String comment,
                            boolean music, boolean pets, boolean talk,
                            boolean smoking) {
        this.author_id = author_id;
        this.from = from;
        this.to = to;
        this.peoples = peoples;
        this.date = date;
        this.time = time;
        this.price = price;
        this.comment = comment;
        this.music = music;
        this.pets = pets;
        this.talk = talk;
        this.smoking = smoking;
    }

    public ClassPublication() {
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isTalk() {
        return talk;
    }

    public void setTalk(boolean talk) {
        this.talk = talk;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }
}
