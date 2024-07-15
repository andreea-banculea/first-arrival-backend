package com.app.first_arrival.entities.enums;

public enum EmergencyTypeEnum {
    ACCIDENT("Accident"),
    CHEST_PAIN("Chest Pain"),
    BREATHLESSNESS("Breathlessness"),
    UNCONSCIOUSNESS("Unconsciousness"),
    SUDDEN_PARALYSIS_OR_WEAKNESS("Sudden paralysis or weakness"),
    OTHER("Other");

    private final String displayName;

    EmergencyTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

