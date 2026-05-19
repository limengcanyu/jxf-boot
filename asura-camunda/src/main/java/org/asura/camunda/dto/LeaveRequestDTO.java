package org.asura.camunda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 请假申请DTO
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public class LeaveRequestDTO {
    
    /**
     * 申请人
     */
    @NotBlank(message = "申请人不能为空")
    private String applicant;
    
    /**
     * 请假类型
     */
    @NotBlank(message = "请假类型不能为空")
    private String leaveType;
    
    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    
    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    
    /**
     * 请假天数
     */
    @NotNull(message = "请假天数不能为空")
    @Min(value = 1, message = "请假天数必须大于0")
    private Integer days;
    
    /**
     * 请假原因
     */
    @NotBlank(message = "请假原因不能为空")
    private String reason;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 紧急程度（1-低，2-中，3-高）
     */
    private Integer urgency;

    // Constructors
    public LeaveRequestDTO() {}

    public LeaveRequestDTO(String applicant, String leaveType, Date startDate, Date endDate, Integer days, String reason) {
        this.applicant = applicant;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.reason = reason;
    }

    // Getter and Setter methods
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUrgency() {
        return urgency;
    }

    public void setUrgency(Integer urgency) {
        this.urgency = urgency;
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
                ", phone='" + phone + '\'' +
                ", urgency=" + urgency +
                '}';
    }
}