package com.example.betaforall.model;
public class Lesnichestvo {
    public String id;
    public String naimenovanie;
    public int obem;

    // Конструктор без аргументов (обязательно для Firebase)
    public Lesnichestvo() {}

    // Конструктор с аргументами
    public Lesnichestvo(String id, String naimenovanie, int obem) {
        this.id = id;
        this.naimenovanie = naimenovanie;
        this.obem = obem;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public void setNaimenovanie(String naimenovanie) {
        this.naimenovanie = naimenovanie;
    }

    public int getObem() {
        return obem;
    }

    public void setObem(int obem) {
        this.obem = obem;
    }
}
