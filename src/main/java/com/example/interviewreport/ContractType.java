package com.example.interviewreport;

public enum ContractType {
    OUTSOURCING("業務委託"),
    CONTRACT_EMPLOYEE("契約社員"),
    DISPATCH("派遣");

    private final String label;

    ContractType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
