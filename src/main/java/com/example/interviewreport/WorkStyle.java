package com.example.interviewreport;

public enum WorkStyle {
    FULL_REMOTE("フルリモート"),
    PARTIAL_REMOTE("部分リモート"),
    FULL_OFFICE("フル出勤");

    private final String label;

    WorkStyle(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
