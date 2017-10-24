package pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by roexcuse on 09/10/17.
 */

public class GetExcuseList {

    @SerializedName("employee_id")
    @Expose
    private String employee_id;
    @SerializedName("employee_name")
    @Expose
    private String employee_name;
    @SerializedName("excuse_prod_date")
    @Expose
    private String excuseProdDate;
    @SerializedName("excuse_prod_month")
    @Expose
    private String excuseProdMonth;
    @SerializedName("excuse_id")
    @Expose
    private String excuseId;
    @SerializedName("excuse_type")
    @Expose
    private String excuseType;
    @SerializedName("excuse_ci")
    @Expose
    private String excuseCi;

    @SerializedName("excuse_co")
    @Expose
    private String excuseCo;

    @SerializedName("excuse_shift_type")
    @Expose
    private String excuseShiftType;

    @SerializedName("excuse_shift")
    @Expose
    private String excuseShift;

    @SerializedName("excuse_group")
    @Expose
    private String excuseGroup;
    @SerializedName("excuse_dt")
    @Expose
    private String excuseDt;
    @SerializedName("excuse_todt")
    @Expose
    private String excuseTodt;
    @SerializedName("excuse_reason")
    @Expose
    private String excuseReason;
    @SerializedName("excuse_approved_dt")
    @Expose
    private String excuseApproveDt;
    @SerializedName("excuse_approved_by")
    @Expose
    private String excuseApproveBy;
    @SerializedName("excuse_status")
    @Expose
    private String excuseStatus;

    /**
     *
     * @return
     * The excuseProdDate
     */
    public String getEmployeeId() {
        return employee_id;
    }

    /**
     *
     * @param employee_id
     * The excuse_prod_date
     */
    public void setEmployeeId(String employee_id) {
        this.employee_id = employee_id;
    }
    /**
     *
     * @return
     * The excuseProdDate
     */
    public String getEmployeeName() {
        return employee_name;
    }

    /**
     *
     * @param employee_name
     * The excuse_prod_date
     */
    public void setEmployeeName(String employee_name) {
        this.employee_name = employee_name;
    }

    /**
     *
     * @return
     * The excuseProdDate
     */
    public String getExcuseProdDate() {
        return excuseProdDate;
    }

    /**
     *
     * @param excuseProdDate
     * The excuse_prod_date
     */
    public void setExcuseProdDate(String excuseProdDate) {
        this.excuseProdDate = excuseProdDate;
    }

    /**
     *
     * @return
     * The excuseProdMonth
     */
    public String getExcuseProdMonth() {
        return excuseProdMonth;
    }

    /**
     *
     * @param excuseProdMonth
     * The excuse_prod_month
     */
    public void setExcuseProdMonth(String excuseProdMonth) {
        this.excuseProdMonth = excuseProdMonth;
    }

    /**
     *
     * @return
     * The excuseId
     */
    public String getExcuseId() {
        return excuseId;
    }

    /**
     *
     * @param excuseId
     * The excuse_id
     */
    public void setExcuseId(String excuseId) {
        this.excuseId = excuseId;
    }

    /**
     *
     * @return
     * The excuseDt
     */
    public String getExcuseDt() {
        return excuseDt;
    }

    /**
     *
     * @param excuseDt
     * The excuse_dt
     */
    public void setExcuseDt(String excuseDt) {
        this.excuseDt = excuseDt;
    }

    /**
     *
     * @return
     * The excuseHour
     */
    public String getExcuseTodt() {
        return excuseTodt;
    }

    /**
     *
     * @param excuseTodt
     * The excuse_dt
     */
    public void setExcuseTodt(String excuseTodt) {
        this.excuseTodt = excuseTodt;
    }

    /**
     *
     * @return
     * The excuseHour
     */
    public String getExcuseType() {
        return excuseType;
    }

    /**
     *
     * @param excuseType
     * The excuse_dt
     */
    public void setExcuseType(String excuseType) {
        this.excuseType = excuseType;
    }

    /**
     *
     * @return
     * The excuseCi
     */
    public String getExcuseCi() {
        return excuseCi;
    }

    /**
     *
     * @param excuseCi
     * The excuse_ci
     */
    public void setExcuseCi(String excuseCi) {
        this.excuseCi = excuseCi;
    }

    /**
     *
     * @return
     * The excuseCo
     */
    public String getExcuseCo() {
        return excuseCo;
    }

    /**
     *
     * @param excuseCo
     * The excuse_co
     */
    public void setExcuseCo(String excuseCo) {
        this.excuseCo = excuseCo;
    }

    /**
     *
     * @return
     * The excuseShiftType
     */
    public String getExcuseShiftType() {
        return excuseShiftType;
    }

    /**
     *
     * @param excuseShiftType
     * The excuse_shift_type
     */
    public void setExcuseShiftType(String excuseShiftType) {
        this.excuseShiftType = excuseShiftType;
    }

    /**
     *
     * @return
     * The excuseShift
     */
    public String getExcuseShift() {
        return excuseShift;
    }

    /**
     *
     * @param excuseShift
     * The excuse_status
     */
    public void setExcuseShift(String excuseShift) {
        this.excuseShift = excuseShift;
    }

    /**
     *
     * @return
     * The excuseStatus
     */
    public String getExcuseGroup() {
        return excuseGroup;
    }

    /**
     *
     * @param excuseGroup
     * The excuse_status
     */
    public void setExcuseGroup(String excuseGroup) {
        this.excuseGroup = excuseGroup;
    }
    /**
     *
     * @return
     * The excuseHour
     */
    public String getExcuseDescription() {
        return excuseReason;
    }

    /**
     *
     * @param excuseReason
     * The excuse_description
     */
    public void setExcuseDescription(String excuseReason) {
        this.excuseReason = excuseReason;
    }

    /**
     *
     * @return
     * The excuseApproveDt
     */
    public String getExcuseApproveDt() {
        return excuseApproveDt;
    }

    /**
     *
     * @param excuseApproveDt
     * The excuse_approve_dt
     */
    public void setExcuseApproveDt(String excuseApproveDt) {
        this.excuseApproveDt = excuseApproveDt;
    }

    /**
     *
     * @return
     * The excuseApproveBy
     */
    public String getExcuseApproveBy() {
        return excuseApproveBy;
    }

    /**
     *
     * @param excuseApproveBy
     * The excuse_approve_by
     */
    public void setExcuseApproveBy(String excuseApproveBy) {
        this.excuseApproveBy = excuseApproveBy;
    }

    /**
     *
     * @return
     * The excuseStatus
     */
    public String getExcuseStatus() {
        return excuseStatus;
    }

    /**
     *
     * @param excuseStatus
     * The excuse_status
     */
    public void setExcuseStatus(String excuseStatus) {
        this.excuseStatus = excuseStatus;
    }


}
