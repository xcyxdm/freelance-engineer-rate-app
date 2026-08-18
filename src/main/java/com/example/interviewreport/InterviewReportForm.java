package com.example.interviewreport;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InterviewReportForm {

    @Size(max = 120, message = "会社は120文字以内で入力してください")
    private String company;

    @NotNull(message = "年齢を入力してください")
    @Min(value = 18, message = "年齢は18歳以上で入力してください")
    @Max(value = 80, message = "年齢は80歳以下で入力してください")
    private Integer age;

    @NotBlank(message = "職種を入力してください")
    @Size(max = 80, message = "職種は80文字以内で入力してください")
    private String role;

    @NotNull(message = "単価を入力してください")
    @Min(value = 5, message = "単価は5万円以上で入力してください")
    @Max(value = 999, message = "単価は999万円以下で入力してください")
    private Integer unitPrice;

    @NotNull(message = "勤務形態を選択してください")
    private WorkStyle workStyle;

    @NotNull(message = "契約形態を選択してください")
    private ContractType contractType;

    private String projectSummary;

    @NotNull(message = "担当役割を選択してください")
    private AssignedRole assignedRole;

    public static InterviewReportForm fromEntity(InterviewReport report) {
        InterviewReportForm form = new InterviewReportForm();
        form.setCompany(report.getCompany());
        form.setAge(report.getAge());
        form.setRole(report.getRole());
        form.setUnitPrice(report.getUnitPrice());
        form.setWorkStyle(report.getWorkStyle());
        form.setContractType(report.getContractType());
        form.setProjectSummary(report.getProjectSummary());
        form.setAssignedRole(report.getAssignedRole());
        return form;
    }

    public void copyTo(InterviewReport report) {
        report.setCompany(company);
        report.setAge(age);
        report.setRole(role);
        report.setUnitPrice(unitPrice);
        report.setWorkStyle(workStyle);
        report.setContractType(contractType);
        report.setProjectSummary(projectSummary);
        report.setAssignedRole(assignedRole);
    }

    @AssertTrue(message = "単価は5万円刻みで入力してください")
    public boolean isUnitPriceStepValid() {
        return unitPrice == null || unitPrice % 5 == 0;
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
}
