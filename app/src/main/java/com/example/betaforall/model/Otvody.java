package com.example.betaforall.model;
public class Otvody {
    public String id;
    public String delyankaId;
    public String lesnichestvoId;
    public String poroda;
    public String delovaya;
    public String drovyannaya;
    public String otkhody;
    public String otvetstvennyi;
    public String kommentariy;
    // Конструктор с аргументами
    public Otvody(String id, String delyankaId, String lesnichestvoId,
                  String poroda, String delovaya, String drovyannaya, String otkhody,
                  String otvetstvennyi, String kommentariy) {
        this.id = id;
        this.delyankaId = delyankaId;
        this.lesnichestvoId = lesnichestvoId;
        this.poroda = poroda;
        this.delovaya = delovaya;
        this.drovyannaya = drovyannaya;
        this.otkhody = otkhody;
        this.otvetstvennyi = otvetstvennyi;
        this.kommentariy = kommentariy;
    }


    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelyankaId() {
        return delyankaId;
    }

    public void setDelyankaId(String delyankaId) {
        this.delyankaId = delyankaId;
    }

    public String getLesnichestvoId() {
        return lesnichestvoId;
    }

    public void setLesnichestvoId(String lesnichestvoId) {
        this.lesnichestvoId = lesnichestvoId;
    }

    public String getPoroda() {
        return poroda;
    }

    public void setPoroda(String poroda) {
        this.poroda = poroda;
    }

    public String getDelovaya() {
        return delovaya;
    }

    public void setDelovaya(String delovaya) {
        this.delovaya = delovaya;
    }

    public String getDrovyannaya() {
        return drovyannaya;
    }

    public void setDrovyannaya(String drovyannaya) {
        this.drovyannaya = drovyannaya;
    }

    public String getOtkhody() {
        return otkhody;
    }

    public void setOtkhody(String otkhody) {
        this.otkhody = otkhody;
    }

    public String getOtvetstvennyi() {
        return otvetstvennyi;
    }

    public void setOtvetstvennyi(String otvetstvennyi) {
        this.otvetstvennyi = otvetstvennyi;
    }

    public String getKommentariy() {
        return kommentariy;
    }

    public void setKommentariy(String kommentariy) {
        this.kommentariy = kommentariy;
    }
}
