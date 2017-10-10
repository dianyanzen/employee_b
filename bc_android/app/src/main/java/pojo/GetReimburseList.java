package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetReimburseList {

    @SerializedName("reimburse_prod_date")
    @Expose
    private String reimburseProdDate;
    @SerializedName("reimburse_prod_month")
    @Expose
    private String reimburseProdMonth;
    @SerializedName("reimburse_id")
    @Expose
    private String reimburseId;
    @SerializedName("reimburse_dt")
    @Expose
    private String reimburseDt;
    @SerializedName("reimburse_type")
    @Expose
    private String reimburseType;
    @SerializedName("reimburse_description")
    @Expose
    private String reimburseDescription;
    @SerializedName("reimburse_amount")
    @Expose
    private String reimburseAmount;
    @SerializedName("reimburse_file")
    @Expose
    private String reimburseFile;
    @SerializedName("reimburse_approve_dt")
    @Expose
    private Object reimburseApproveDt;
    @SerializedName("reimburse_approve_by")
    @Expose
    private Object reimburseApproveBy;
    @SerializedName("reimburse_status")
    @Expose
    private String reimburseStatus;

    /**
     *
     * @return
     * The reimburseProdDate
     */
    public String getReimburseProdDate() {
        return reimburseProdDate;
    }

    /**
     *
     * @param reimburseProdDate
     * The reimburse_prod_date
     */
    public void setReimburseProdDate(String reimburseProdDate) {
        this.reimburseProdDate = reimburseProdDate;
    }

    /**
     *
     * @return
     * The reimburseProdMonth
     */
    public String getReimburseProdMonth() {
        return reimburseProdMonth;
    }

    /**
     *
     * @param reimburseProdMonth
     * The reimburse_prod_month
     */
    public void setReimburseProdMonth(String reimburseProdMonth) {
        this.reimburseProdMonth = reimburseProdMonth;
    }

    /**
     *
     * @return
     * The reimburseId
     */
    public String getReimburseId() {
        return reimburseId;
    }

    /**
     *
     * @param reimburseId
     * The reimburse_id
     */
    public void setReimburseId(String reimburseId) {
        this.reimburseId = reimburseId;
    }

    /**
     *
     * @return
     * The reimburseDt
     */
    public String getReimburseDt() {
        return reimburseDt;
    }

    /**
     *
     * @param reimburseDt
     * The reimburse_dt
     */
    public void setReimburseDt(String reimburseDt) {
        this.reimburseDt = reimburseDt;
    }

    /**
     *
     * @return
     * The reimburseType
     */
    public String getReimburseType() {
        return reimburseType;
    }

    /**
     *
     * @param reimburseType
     * The reimburse_type
     */
    public void setReimburseType(String reimburseType) {
        this.reimburseType = reimburseType;
    }

    /**
     *
     * @return
     * The reimburseDescription
     */
    public String getReimburseDescription() {
        return reimburseDescription;
    }

    /**
     *
     * @param reimburseDescription
     * The reimburse_description
     */
    public void setReimburseDescription(String reimburseDescription) {
        this.reimburseDescription = reimburseDescription;
    }

    /**
     *
     * @return
     * The reimburseAmount
     */
    public String getReimburseAmount() {
        return reimburseAmount;
    }

    /**
     *
     * @param reimburseAmount
     * The reimburse_amount
     */
    public void setReimburseAmount(String reimburseAmount) {
        this.reimburseAmount = reimburseAmount;
    }

    /**
     *
     * @return
     * The reimburseFile
     */
    public String getReimburseFile() {
        return reimburseFile;
    }

    /**
     *
     * @param reimburseFile
     * The reimburse_file
     */
    public void setReimburseFile(String reimburseFile) {
        this.reimburseFile = reimburseFile;
    }

    /**
     *
     * @return
     * The reimburseApproveDt
     */
    public Object getReimburseApproveDt() {
        return reimburseApproveDt;
    }

    /**
     *
     * @param reimburseApproveDt
     * The reimburse_approve_dt
     */
    public void setReimburseApproveDt(Object reimburseApproveDt) {
        this.reimburseApproveDt = reimburseApproveDt;
    }

    /**
     *
     * @return
     * The reimburseApproveBy
     */
    public Object getReimburseApproveBy() {
        return reimburseApproveBy;
    }

    /**
     *
     * @param reimburseApproveBy
     * The reimburse_approve_by
     */
    public void setReimburseApproveBy(Object reimburseApproveBy) {
        this.reimburseApproveBy = reimburseApproveBy;
    }

    /**
     *
     * @return
     * The reimburseStatus
     */
    public String getReimburseStatus() {
        return reimburseStatus;
    }

    /**
     *
     * @param reimburseStatus
     * The reimburse_status
     */
    public void setReimburseStatus(String reimburseStatus) {
        this.reimburseStatus = reimburseStatus;
    }

}