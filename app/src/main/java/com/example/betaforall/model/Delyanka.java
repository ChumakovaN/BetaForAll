package com.example.betaforall.model;
public class Delyanka {
    public String id;
    public String lesnichestvoId;
    public String naimenovanie;
    public String hozyaistvo;
    public String sposob;
    public String tekhnologiya;
    public String preobladayushchaya;

    // Конструктор без аргументов
    public Delyanka() {}

    // Конструктор с аргументами
    public Delyanka(String id, String lesnichestvoId, String naimenovanie,
                    String hozyaistvo, String sposob, String tekhnologiya, String preobladayushchaya) {
        this.id = id;
        this.lesnichestvoId = lesnichestvoId;
        this.naimenovanie = naimenovanie;
        this.hozyaistvo = hozyaistvo;
        this.sposob = sposob;
        this.tekhnologiya = tekhnologiya;
        this.preobladayushchaya = preobladayushchaya;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLesnichestvoId() {
        return lesnichestvoId;
    }

    public void setLesnichestvoId(String lesnichestvoId) {
        this.lesnichestvoId = lesnichestvoId;
    }

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public void setNaimenovanie(String naimenovanie) {
        this.naimenovanie = naimenovanie;
    }

    public String getHozyaistvo() {
        return hozyaistvo;
    }

    public void setHozyaistvo(String hozyaistvo) {
        this.hozyaistvo = hozyaistvo;
    }

    public String getSposob() {
        return sposob;
    }

    public void setSposob(String sposob) {
        this.sposob = sposob;
    }

    public String getTekhnologiya() {
        return tekhnologiya;
    }

    public void setTekhnologiya(String tekhnologiya) {
        this.tekhnologiya = tekhnologiya;
    }

    public String getPreobladayushchaya() {
        return preobladayushchaya;
    }

    public void setPreobladayushchaya(String preobladayushchaya) {
        this.preobladayushchaya = preobladayushchaya;
    }
}
