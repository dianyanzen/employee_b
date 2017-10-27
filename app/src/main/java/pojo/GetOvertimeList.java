package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOvertimeList {

    @SerializedName("employee_id")
    @Expose
    private String employee_id;
    @SerializedName("employee_name")
    @Expose
    private String employee_name;
    @SerializedName("overtime_prod_date")
    @Expose
    private String overtimeProdDate;
    @SerializedName("overtime_prod_month")
    @Expose
    private String overtimeProdMonth;
    @SerializedName("ot_id")
    @Expose
    private String otId;
    @SerializedName("ot_dt")
    @Expose
    private String otDt;
    @SerializedName("ot_hour")
    @Expose
    private String otHour;
    @SerializedName("ot_from")
    @Expose
    private String otFrom;
    @SerializedName("ot_to")
    @Expose
    private String otTo;
    @SerializedName("ot_calculate")
    @Expose
    private String otCalculate;
    @SerializedName("ot_description")
    @Expose
    private String otDescription;
    @SerializedName("ot_approve_dt")
    @Expose
    private String otApproveDt;
    @SerializedName("ot_approve_by")
    @Expose
    private String otApproveBy;
    @SerializedName("overtime_status")
    @Expose
    private String overtimeStatus;

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
    public String getEmployeeName() {
        return employee_name;
    }

    /**
     *
     * @param employee_name
     *
     */
    public void setEmployeeName(String employee_name) {
        this.employee_name = employee_name;
    }

    /**
     *
     * @return
     *
     */
    public String getOvertimeProdDate() {
        return overtimeProdDate;
    }

    /**
     *
     * @param overtimeProdDate
     * The overtime_prod_date
     */
    public void setOvertimeProdDate(String overtimeProdDate) {
        this.overtimeProdDate = overtimeProdDate;
    }

    /**
     *
     * @return
     * The overtimeProdMonth
     */
    public String getOvertimeProdMonth() {
        return overtimeProdMonth;
    }

    /**
     *
     * @param overtimeProdMonth
     * The overtime_prod_month
     */
    public void setOvertimeProdMonth(String overtimeProdMonth) {
        this.overtimeProdMonth = overtimeProdMonth;
    }

    /**
     *
     * @return
     * The otId
     */
    public String getOtId() {
        return otId;
    }

    /**
     *
     * @param otId
     * The ot_id
     */
    public void setOtId(String otId) {
        this.otId = otId;
    }

    /**
     *
     * @return
     * The otDt
     */
    public String getOtDt() {
        return otDt;
    }

    /**
     *
     * @param otDt
     * The ot_dt
     */
    public void setOtDt(String otDt) {
        this.otDt = otDt;
    }

    /**
     *
     * @return
     * The otHour
     */
    public String getOtHour() {
        return otHour;
    }

    /**
     *
     * @param otHour
     * The ot_hour
     */
    public void setOtHour(String otHour) {
        this.otHour = otHour;
    }

    /**
     *
     * @return
     * The otHour
     */
    public String getOtFrom() {
        return otFrom;
    }

    /**
     *
     * @param otFrom
     * The ot_hour
     */
    public void setOtFrom(String otFrom) {
        this.otFrom = otFrom;
    }

    /**
     *
     * @return
     * The otHour
     */
    public String getOtTo() {
        return otTo;
    }

    /**
     *
     * @param otTo
     * The ot_hour
     */
    public void setOtTo(String otTo) {
        this.otTo = otTo;
    }

    /**
     *
     * @return
     * The otCalculate
     */
    public String getOtCalculate() {
        return otCalculate;
    }

    /**
     *
     * @param otCalculate
     * The ot_calculate
     */
    public void setOtCalculate(String otCalculate) {
        this.otCalculate = otCalculate;
    }

    /**
     *
     * @return
     * The otDescription
     */
    public String getOtDescription() {
        return otDescription;
    }

    /**
     *
     * @param otDescription
     * The ot_description
     */
    public void setOtDescription(String otDescription) {
        this.otDescription = otDescription;
    }

    /**
     *
     * @return
     * The otApproveDt
     */
    public String getOtApproveDt() {
        return otApproveDt;
    }

    /**
     *
     * @param otApproveDt
     * The ot_approve_dt
     */
    public void setOtApproveDt(String otApproveDt) {
        this.otApproveDt = otApproveDt;
    }

    /**
     *
     * @return
     * The otApproveBy
     */
    public String getOtApproveBy() {
        return otApproveBy;
    }

    /**
     *
     * @param otApproveBy
     * The ot_approve_by
     */
    public void setOtApproveBy(String otApproveBy) {
        this.otApproveBy = otApproveBy;
    }

    /**
     *
     * @return
     * The overtimeStatus
     */
    public String getOvertimeStatus() {
        return overtimeStatus;
    }

    /**
     *
     * @param overtimeStatus
     * The overtime_status
     */
    public void setOvertimeStatus(String overtimeStatus) {
        this.overtimeStatus = overtimeStatus;
    }

}