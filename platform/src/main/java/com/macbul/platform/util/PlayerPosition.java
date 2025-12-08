package com.macbul.platform.util;

public enum PlayerPosition {

    // Genel roller
    GK("Kaleci"),
    DF("Defans"),
    MF("Orta Saha"),
    FW("Forvet"),

    // Bekler
    RB("Sağ Bek"),
    LB("Sol Bek"),

    // Stoper
    CB("Stoper"),

    // Orta saha roller
    DM("Defansif Orta Saha"),
    CM("Merkez Orta Saha"),
    AM("Ofansif Orta Saha"),

    // Kanatlar
    RW("Sağ Kanat"),
    LW("Sol Kanat"),

    // Hücum
    ST("Santrafor");

    private final String label;

    PlayerPosition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // İstersen: string → enum convert metodu
    public static PlayerPosition from(String value) {
        if (value == null) return null;
        try {
            return PlayerPosition.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null; // invalid string
        }
    }
}
