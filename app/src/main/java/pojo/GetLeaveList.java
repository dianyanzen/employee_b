package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetLeaveList {

    @SerializedName("leave_prod_date")
    @Expose
    private String leaveProdDate;
    @SerializedName("leave_prod_month")
    @Expose
    private String leaveProdMonth;
    @SerializedName("time_off_id")
    @Expose
    private String leaveId;
    @SerializedName("time_off_type")
    @Expose
    private String leaveType;
    @SerializedName("leave_dt")
    @Expose
    private String leaveDt;
    @SerializedName("leave_dt")
    @Expose
    private String leaveTodt;
    @SerializedName("leave_reason")
    @Expose
    private String leaveReason;
    @SerializedName("time_off_approve_dt")
    @Expose
    private String leaveApproveDt;
    @SerializedName("time_off_approve_by")
    @Expose
    private String leaveApproveBy;
    @SerializedName("leave_status")
    @Expose
    private String leaveStatus;


    /**
     *
     * @return
     * The leaveProdDate
     */
    public String getLeaveProdDate() {
        return leaveProdDate;
    }

    /**
     *
     * @param leaveProdDate
     * The leave_prod_date
     */
    public void setLeaveProdDate(String leaveProdDate) {
        this.leaveProdDate = leaveProdDate;
    }

    /**
     *
     * @return
     * The leaveProdMonth
     */
    public String getLeaveProdMonth() {
        return leaveProdMonth;
    }

    /**
     *
     * @param leaveProdMonth
     * The leave_prod_month
     */
    public void setLeaveProdMonth(String leaveProdMonth) {
        this.leaveProdMonth = leaveProdMonth;
    }

    /**
     *
     * @return
     * The leaveId
     */
    public String getLeaveId() {
        return leaveId;
    }

    /**
     *
     * @param leaveId
     * The leave_id
     */
    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    /**
     *
     * @return
     * The leaveDt
     */
    public String getLeaveDt() {
        return leaveDt;
    }

    /**
     *
     * @param leaveDt
     * The leave_dt
     */
    public void setLeaveDt(String leaveDt) {
        this.leaveDt = leaveDt;
    }

    /**
     *
     * @return
     * The leaveHour
     */
    public String getLeaveTodt() {
        return leaveTodt;
    }

    /**
     *
     * @param leaveTodt
     * The leave_dt
     */
    public void setLeaveTodt(String leaveTodt) {
        this.leaveTodt = leaveTodt;
    }

    /**
     *
     * @return
     * The leaveHour
     */
    public String getLeaveType() {
        return leaveType;
    }

    /**
     *
     * @param leaveType
     * The leave_dt
     */
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    /**
     *
     * @return
     * The leaveHour
     */
    public String getLeaveDescription() {
        return leaveReason;
    }

    /**
     *
     * @param leaveReason
     * The leave_description
     */
    public void setLeaveDescription(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    /**
     *
     * @return
     * The leaveApproveDt
     */
    public String getLeaveApproveDt() {
        return leaveApproveDt;
    }

    /**
     *
     * @param leaveApproveDt
     * The leave_approve_dt
     */
    public void setLeaveApproveDt(String leaveApproveDt) {
        this.leaveApproveDt = leaveApproveDt;
    }

    /**
     *
     * @return
     * The leaveApproveBy
     */
    public String getLeaveApproveBy() {
        return leaveApproveBy;
    }

    /**
     *
     * @param leaveApproveBy
     * The leave_approve_by
     */
    public void setLeaveApproveBy(String leaveApproveBy) {
        this.leaveApproveBy = leaveApproveBy;
    }

    /**
     *
     * @return
     * The leaveStatus
     */
    public String getLeaveStatus() {
        return leaveStatus;
    }

    /**
     *
     * @param leaveStatus
     * The leave_status
     */
    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

}
