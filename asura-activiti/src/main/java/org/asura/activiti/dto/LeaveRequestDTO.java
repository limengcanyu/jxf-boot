package org.asura.activiti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

/**
 * 请假申请DTO
 * 
 * @author Auto Generated
 * @version 1.0
 */
public class LeaveRequestDTO {

    @NotBlank(message = "申请人不能为空")
    private String applicant;

    @NotBlank(message = "请假类型不能为空")
    private String leaveType;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @NotNull(message = "请假天数不能为空")
    @Min(value = 1, message = "请假天数至少为1天")
    private Long days;

    @NotBlank(message = "请假原因不能为空")
    private String reason;

    // 构造方法
    public LeaveRequestDTO() {
    }

    public LeaveRequestDTO(String applicant, String leaveType, LocalDate startDate, LocalDate endDate, Long days, String reason) {
        this.applicant = applicant;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.reason = reason;
    }

    // Getter和Setter方法
    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LeaveRequestDTO{" +
                "applicant='" + applicant + '\'' +
                ", leaveType='" + leaveType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", days=" + days +
                ", reason='" + reason + '\'' +
                '}';
    }
}