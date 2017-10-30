package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/10/17.
 */

public class GetScheduleActivityList {


    @SerializedName("employee_id")
    @Expose
    private String employee_id;
    @SerializedName("employee_name")
    @Expose
    private String employee_name;
    @SerializedName("schedule_id")
    @Expose
    private String schedule_id;
    @SerializedName("schedule_prod_date")
    @Expose
    private String schedule_prod_date;

    @SerializedName("schedule_prod_month")
    @Expose
    private String schedule_prod_month;

    @SerializedName("schedule_dt")
    @Expose
    private String schedule_dt;

    @SerializedName("approval_due_dt")
    @Expose
    private String approval_due_dt;

    @SerializedName("schedule_type")
    @Expose
    private String schedule_type;

    @SerializedName("schedule_description")
    @Expose
    private String schedule_description;

    @SerializedName("schedule_status")
    @Expose
    private String schedule_status;

    /**
     * @return
     */
    public String getScheduleId() {
        return schedule_id;
    }

    /**
     * @param schedule_id
     */
    public void setScheduleId(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    /**
     * @return
     */
    public String getEmployeeId() {
        return employee_id;
    }

    /**
     * @param employee_id
     */
    public void setEmployeeId(String employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * @return
     */
    public String getEmployeeName() {
        return employee_name;
    }

    /**
     * @param employee_name
     */
    public void setEmployeeName(String employee_name) {
        this.employee_name = employee_name;
    }

    /**
     * @return
     */
    public String getScheduleProdDate() {
        return schedule_prod_date;
    }

    /**
     * @param schedule_prod_date
     */
    public void setScheduleProdDate(String schedule_prod_date) {
        this.schedule_prod_date = schedule_prod_date;
    }

    /**
     * @return
     */
    public String getScheduleProdMonth() {
        return schedule_prod_month;
    }

    /**
     * @param schedule_prod_month
     */
    public void setScheduleProdMonth(String schedule_prod_month) {
        this.schedule_prod_month = schedule_prod_month;
    }

    /**
     * @return
     */
    public String getScheduleDt() {
        return schedule_dt;
    }

    /**
     * @param schedule_dt
     */
    public void setScheduleDt(String schedule_dt) {
        this.schedule_dt = schedule_dt;
    }

    /**
     * @return
     */
    public String getApprovalDueDt() {
        return approval_due_dt;
    }

    /**
     * @param approval_due_dt
     */
    public void setApprovalDueDt(String approval_due_dt) {
        this.approval_due_dt = approval_due_dt;
    }

    /**
     * @return
     */
    public String getScheduleType() {
        return schedule_type;
    }

    /**
     * @param schedule_type
     */
    public void setScheduleType(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    /**
     * @return
     */
    public String getScheduleDescription() {
        return schedule_description;
    }

    /**
     * @param schedule_description
     */
    public void setScheduleDescription(String schedule_description) {
        this.schedule_description = schedule_description;
    }

    /**
     * @return
     */
    public String getScheduleStatus() {
        return schedule_status;
    }

    /**
     * @param schedule_status
     */
    public void setScheduleStatus(String schedule_status) {
        this.schedule_status = schedule_status;
    }
}
