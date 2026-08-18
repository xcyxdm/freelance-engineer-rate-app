package com.example.interviewreport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "interview_reports")
public class InterviewReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120)
    private String company;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 80)
    private String role;

    @Column(nullable = false)
    private Integer unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkStyle workStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ContractType contractType;

    @Column(columnDefinition = "text")
    private String projectSummary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssignedRole assignedRole;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public WorkStyle getWorkStyle() {
        return workStyle;
    }

    public void setWorkStyle(WorkStyle workStyle) {
        this.workStyle = workStyle;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public String getProjectSummary() {
        return projectSummary;
    }

    public void setProjectSummary(String projectSummary) {
        this.projectSummary = projectSummary;
    }

    public AssignedRole getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(AssignedRole assignedRole) {
        this.assignedRole = assignedRole;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
