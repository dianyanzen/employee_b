package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 16/10/17.
 */

public class GetProfileTax {
    @SerializedName("employee_id")
    @Expose
    private String EmployeeID;
    @SerializedName("user_name")
    @Expose
    private String UserName;
    @SerializedName("npwp_number")
    @Expose
    private String UserNpwpNumber;
    @SerializedName("npwp_dt")
    @Expose
    private String UserNpwpDt;
    @SerializedName("marital")
    @Expose
    private String UserMarital;
    @SerializedName("bpjs_kesehatan")
    @Expose
    private String UserBpjsSehat;
    @SerializedName("bpjs_ketenagakerjaan")
    @Expose
    private String UserBpjsKerja;


    /**
     *
     * @return
     * The employee_id
     */
    public String getEmployeeID() {
        return EmployeeID;
    }

    /**
     *
     * @param EmployeeID
     * The employee_id
     */
    public void setEmployeeID(String EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    /**
     *
     * @return
     * The user_name
     */
    public String getUserName() {
        return UserName;
    }

    /**
     *
     * @param UserName
     * The user_name
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    /**
     *
     * @return
     * The employee_id
     */
    public String getUserNpwpNumber() {
        return UserNpwpNumber;
    }

    /**
     *
     * @param UserNpwpNumber
     * The employee_id
     */
    public void setUserNpwpNumber(String UserNpwpNumber) {
        this.UserNpwpNumber = UserNpwpNumber;
    }

    /**
     *
     * @return
     * The user_name
     */
    public String getUserNpwpDt() {
        return UserNpwpDt;
    }

    /**
     *
     * @param UserNpwpDt
     * The user_name
     */
    public void setUserNpwpDt(String UserNpwpDt) {
        this.UserNpwpDt = UserNpwpDt;
    }

    /**
     *
     * @return
     * The employee_id
     */
    public String getUserMarital() {
        return UserMarital;
    }

    /**
     *
     * @param UserMarital
     * The employee_id
     */
    public void setUserMarital(String UserMarital) {
        this.UserMarital = UserMarital;
    }

    /**
     *
     * @return
     * The user_name
     */
    public String getUserBpjsSehat() {
        return UserBpjsSehat;
    }

    /**
     *
     * @param UserBpjsSehat
     * The user_name
     */
    public void setUserBpjsSehat(String UserBpjsSehat) {
        this.UserBpjsSehat = UserBpjsSehat;
    }

    /**
     *
     * @return
     * The user_name
     */
    public String getUserBpjsKerja() {
        return UserBpjsKerja;
    }

    /**
     *
     * @param UserBpjsKerja
     * The user_name
     */
    public void setUserBpjsKerja(String UserBpjsKerja) {
        this.UserBpjsKerja = UserBpjsKerja;
    }
}
