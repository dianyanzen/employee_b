package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetLeaveList {

    @SerializedName("leave_id")
    @Expose
    private String leave_id;

    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    @SerializedName("leave_prod_date")
    @Expose
    private String leave_prod_date;

    @SerializedName("leave_prod_month")
    @Expose
    private String leave_prod_month;

    @SerializedName("leave_dt")
    @Expose
    private String leave_dt;

    @SerializedName("time_off_type")
    @Expose
    private String time_off_type;

    @SerializedName("leave_reason")
    @Expose
    private String leave_reason;

    @SerializedName("approval_due_dt")
    @Expose
    private String approval_due_dt;

    @SerializedName("time_off_approve_dt")
    @Expose
    private String time_off_approve_dt;

    @SerializedName("time_off_approve_by")
    @Expose
    private String time_off_approve_by;

    @SerializedName("leave_status")
    @Expose
    private String leave_status;



    /**
     *
     * @return
     *
     */
    public String getLeaveId() {
        return leave_id;
    }

    /**
     *
     * @param leave_id
     *
     */
    public void setLeaveId(String leave_id) {
        this.leave_id = leave_id;
    }

    /**
     *
     * @return
     *
     */
    public String getEmployeeId() {
        return employee_id;
    }

    /**
     *
     * @param employee_id
     *
     */
    public void setEmployeeId(String employee_id) {
        this.employee_id = employee_id;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveProdDate() {
        return leave_prod_date;
    }

    /**
     *
     * @param leave_prod_date
     *
     */
    public void setLeaveProdDate(String leave_prod_date) {
        this.leave_prod_date = leave_prod_date;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveProdMonth() {
        return leave_prod_month;
    }

    /**
     *
     * @param leave_prod_month
     *
     */
    public void setLeaveProdMonth(String leave_prod_month) {
        this.leave_prod_month = leave_prod_month;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveDt() {
        return leave_dt;
    }

    /**
     *
     * @param leave_dt
     *
     */
    public void setLeaveDt(String leave_dt) {
        this.leave_dt = leave_dt;
    }

    /**
     *
     * @return
     *
     */
    public String getTimeOffType() {
        return time_off_type;
    }

    /**
     *
     * @param time_off_type
     *
     */
    public void setTimeOffType(String time_off_type) {
        this.time_off_type = time_off_type;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveReason() {
        return leave_reason;
    }

    /**
     *
     * @param leave_reason
     *
     */
    public void setLeaveReason(String leave_reason) {
        this.leave_reason = leave_reason;
    }

    /**
     *
     * @return
     *
     */
    public String getApprovalDueDt() {
        return approval_due_dt;
    }

    /**
     *
     * @param approval_due_dt
     *
     */
    public void setApprovalDueDt(String approval_due_dt) {
        this.approval_due_dt = approval_due_dt;
    }

    /**
     *
     * @return
     *
     */
    public String getTimeOffApproveDt() {
        return time_off_approve_dt;
    }

    /**
     *
     * @param time_off_approve_dt
     *
     */
    public void setTimeOffApproveDt(String time_off_approve_dt) {
        this.time_off_approve_dt = time_off_approve_dt;
    }

    /**
     *
     * @return
     *
     */
    public String getTimeOffApproveBy() {
        return time_off_approve_by;
    }

    /**
     *
     * @param time_off_approve_by
     *
     */
    public void setTimeOffApproveBy(String time_off_approve_by) {
        this.time_off_approve_by = time_off_approve_by;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveStatus() {
        return leave_status;
    }

    /**
     *
     * @param leave_status
     *
     */
    public void setLeaveStatus(String leave_status) {
        this.leave_status = leave_status;
    }


}
