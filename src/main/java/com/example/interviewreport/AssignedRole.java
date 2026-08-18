package com.example.interviewreport;

public enum AssignedRole {
    PM("PM"),
    PMO("PMO"),
    LEADER("リーダー"),
    SE("SE"),
    PG("PG");

    private final String label;

    AssignedRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
