package com.example.betaforall.model;
public class Equipment {
    public String id;
    public String equipmentName;

    // Конструктор по умолчанию (необходим для Firebase)
    public Equipment() {
    }

    public Equipment(String id, String equipmentName) {
        this.id = id;
        this.equipmentName = equipmentName;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
