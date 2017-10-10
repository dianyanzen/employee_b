package pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by roexcuse on 09/10/17.
 */

public class GetExcuseList {

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
