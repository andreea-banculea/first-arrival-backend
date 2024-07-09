package com.app.first_arrival.entities.enums;

public enum EmergencyTypeEnum {
    ACCIDENT("Accident"),
    CHEST_PAIN("Chest Pain"),
    BREATHLESSNESS("Breathlessness"),
    UNCONSCIOUSNESS("Unconsciousness"),
    PARALYSIS("Paralysis"),
    OTHER("Other");

    private final String displayName;

    EmergencyTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

