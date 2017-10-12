package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetListDailyReport {

    @SerializedName("bugs_id")
    @Expose
    private String bugsId;
    @SerializedName("reg_no")
    @Expose
    private String regNo;
    @SerializedName("application_cd")
    @Expose
    private String applicationCd;
    @SerializedName("application_nm")
    @Expose
    private String applicationNm;
    @SerializedName("function_cd")
    @Expose
    private String functionCd;
    @SerializedName("function_nm")
    @Expose
    private String functionNm;
    @SerializedName("issue_description")
    @Expose
    private String issueDescription;
    @SerializedName("eviden")
    @Expose
    private Object eviden;
    @SerializedName("date_raised")
    @Expose
    private Object dateRaised;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("finish_date")
    @Expose
    private String finishDate;
    @SerializedName("member_cd")
    @Expose
    private String memberCd;
    @SerializedName("member_name")
    @Expose
    private String memberName;
    @SerializedName("task_type_cd")
    @Expose
    private String taskTypeCd;
    @SerializedName("task_type_nm")
    @Expose
    private String taskTypeNm;
    @SerializedName("task_cat_cd")
    @Expose
    private String taskCatCd;
    @SerializedName("task_cat_nm")
    @Expose
    private String taskCatNm;
    @SerializedName("priority_flg")
    @Expose
    private String priorityFlg;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("phase_cd")
    @Expose
    private String phaseCd;
    @SerializedName("phase_nm")
    @Expose
    private String phaseNm;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_dt")
    @Expose
    private String createdDt;
    @SerializedName("changed_by")
    @Expose
    private String changedBy;
    @SerializedName("changed_dt")
    @Expose
    private String changedDt;
    @SerializedName("month_dt")
    @Expose
    private String monthDt;
    @SerializedName("day_dt")
    @Expose
    private String dayDt;

    /**
     *
     * @return
     * The bugsId
     */
    public String getBugsId() {
        return bugsId;
    }

    /**
     *
     * @param bugsId
     * The bugs_id
     */
    public void setBugsId(String bugsId) {
        this.bugsId = bugsId;
    }

    /**
     *
     * @return
     * The regNo
     */
    public String getRegNo() {
        return regNo;
    }

    /**
     *
     * @param regNo
     * The reg_no
     */
    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    /**
     *
     * @return
     * The applicationCd
     */
    public String getApplicationCd() {
        return applicationCd;
    }

    /**
     *
     * @param applicationCd
     * The application_cd
     */
    public void setApplicationCd(String applicationCd) {
        this.applicationCd = applicationCd;
    }

    /**
     *
     * @return
     * The applicationNm
     */
    public String getApplicationNm() {
        return applicationNm;
    }

    /**
     *
     * @param applicationNm
     * The application_nm
     */
    public void setApplicationNm(String applicationNm) {
        this.applicationNm = applicationNm;
    }

    /**
     *
     * @return
     * The functionCd
     */
    public String getFunctionCd() {
        return functionCd;
    }

    /**
     *
     * @param functionCd
     * The function_cd
     */
    public void setFunctionCd(String functionCd) {
        this.functionCd = functionCd;
    }

    /**
     *
     * @return
     * The functionNm
     */
    public String getFunctionNm() {
        return functionNm;
    }

    /**
     *
     * @param functionNm
     * The function_nm
     */
    public void setFunctionNm(String functionNm) {
        this.functionNm = functionNm;
    }

    /**
     *
     * @return
     * The issueDescription
     */
    public String getIssueDescription() {
        return issueDescription;
    }

    /**
     *
     * @param issueDescription
     * The issue_description
     */
    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    /**
     *
     * @return
     * The eviden
     */
    public Object getEviden() {
        return eviden;
    }

    /**
     *
     * @param eviden
     * The eviden
     */
    public void setEviden(Object eviden) {
        this.eviden = eviden;
    }

    /**
     *
     * @return
     * The dateRaised
     */
    public Object getDateRaised() {
        return dateRaised;
    }

    /**
     *
     * @param dateRaised
     * The date_raised
     */
    public void setDateRaised(Object dateRaised) {
        this.dateRaised = dateRaised;
    }

    /**
     *
     * @return
     * The dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     *
     * @param dueDate
     * The due_date
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     *
     * @return
     * The finishDate
     */
    public String getFinishDate() {
        return finishDate;
    }

    /**
     *
     * @param finishDate
     * The finish_date
     */
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    /**
     *
     * @return
     * The memberCd
     */
    public String getMemberCd() {
        return memberCd;
    }

    /**
     *
     * @param memberCd
     * The member_cd
     */
    public void setMemberCd(String memberCd) {
        this.memberCd = memberCd;
    }

    /**
     *
     * @return
     * The memberName
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     *
     * @param memberName
     * The member_name
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    /**
     *
     * @return
     * The taskTypeCd
     */
    public String getTaskTypeCd() {
        return taskTypeCd;
    }

    /**
     *
     * @param taskTypeCd
     * The task_type_cd
     */
    public void setTaskTypeCd(String taskTypeCd) {
        this.taskTypeCd = taskTypeCd;
    }

    /**
     *
     * @return
     * The taskTypeNm
     */
    public String getTaskTypeNm() {
        return taskTypeNm;
    }

    /**
     *
     * @param taskTypeNm
     * The task_type_nm
     */
    public void setTaskTypeNm(String taskTypeNm) {
        this.taskTypeNm = taskTypeNm;
    }

    /**
     *
     * @return
     * The taskCatCd
     */
    public String getTaskCatCd() {
        return taskCatCd;
    }

    /**
     *
     * @param taskCatCd
     * The task_cat_cd
     */
    public void setTaskCatCd(String taskCatCd) {
        this.taskCatCd = taskCatCd;
    }

    /**
     *
     * @return
     * The taskCatNm
     */
    public String getTaskCatNm() {
        return taskCatNm;
    }

    /**
     *
     * @param taskCatNm
     * The task_cat_nm
     */
    public void setTaskCatNm(String taskCatNm) {
        this.taskCatNm = taskCatNm;
    }

    /**
     *
     * @return
     * The priorityFlg
     */
    public String getPriorityFlg() {
        return priorityFlg;
    }

    /**
     *
     * @param priorityFlg
     * The priority_flg
     */
    public void setPriorityFlg(String priorityFlg) {
        this.priorityFlg = priorityFlg;
    }

    /**
     *
     * @return
     * The remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     *
     * @param remark
     * The remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     *
     * @return
     * The phaseCd
     */
    public String getPhaseCd() {
        return phaseCd;
    }

    /**
     *
     * @param phaseCd
     * The phase_cd
     */
    public void setPhaseCd(String phaseCd) {
        this.phaseCd = phaseCd;
    }

    /**
     *
     * @return
     * The phaseNm
     */
    public String getPhaseNm() {
        return phaseNm;
    }

    /**
     *
     * @param phaseNm
     * The phase_nm
     */
    public void setPhaseNm(String phaseNm) {
        this.phaseNm = phaseNm;
    }

    /**
     *
     * @return
     * The createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     * The created_by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     * The createdDt
     */
    public String getCreatedDt() {
        return createdDt;
    }

    /**
     *
     * @param createdDt
     * The created_dt
     */
    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    /**
     *
     * @return
     * The changedBy
     */
    public String getChangedBy() {
        return changedBy;
    }

    /**
     *
     * @param changedBy
     * The changed_by
     */
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     *
     * @return
     * The changedDt
     */
    public String getChangedDt() {
        return changedDt;
    }

    /**
     *
     * @param changedDt
     * The changed_dt
     */
    public void setChangedDt(String changedDt) {
        this.changedDt = changedDt;
    }

    /**
     *
     * @return
     * The monthDt
     */
    public String getMonthDt() {
        return monthDt;
    }

    /**
     *
     * @param monthDt
     * The month_dt
     */
    public void setMonthDt(String monthDt) {
        this.monthDt = monthDt;
    }

    /**
     *
     * @return
     * The dayDt
     */
    public String getDayDt() {
        return dayDt;
    }

    /**
     *
     * @param dayDt
     * The day_dt
     */
    public void setDayDt(String dayDt) {
        this.dayDt = dayDt;
    }

}