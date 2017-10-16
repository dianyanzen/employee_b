package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 16/10/17.
 */

public class GetProfileMain {

    @SerializedName("employee_id")
    @Expose
    private String EmployeeID;
    @SerializedName("user_name")
    @Expose
    private String UserName;
    @SerializedName("user_title")
    @Expose
    private String Title;
    @SerializedName("user_gender")
    @Expose
    private String Gender;
    @SerializedName("user_born_dt")
    @Expose
    private String BornDt;
    @SerializedName("user_born_place")
    @Expose
    private String BornPlace;
    @SerializedName("user_religion")
    @Expose
    private String Religion;
    @SerializedName("user_married_status")
    @Expose
    private String MarriedStatus;
    @SerializedName("user_married_since")
    @Expose
    private String MarriedSince;


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
     * The user_title
     */
    public String getTitle() {
        return Title;
    }

    /**
     *
     * @param Title
     * The user_title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }
    /**
     *
     * @return
     * The user_gender
     */
    public String getGender() {
        return Gender;
    }

    /**
     *
     * @param Gender
     * The user_gender
     */
    public void setGender(String Gender) {
        this.Gender = Gender;
    }
    /**
     *
     * @return
     * The user_born_dt
     */
    public String getBornDt() {
        return BornDt;
    }

    /**
     *
     * @param BornDt
     * The user_born_dt
     */
    public void setBornDt(String BornDt) {
        this.BornDt = BornDt;
    }
    /**
     *
     * @return
     * The user_born_place
     */
    public String getBornPlace() {
        return BornPlace;
    }

    /**
     *
     * @param BornPlace
     * The user_born_place
     */
    public void setBornPlace(String BornPlace) {
        this.BornPlace = BornPlace;
    }
    /**
     *
     * @return
     * The user_religion
     */
    public String getReligion() {
        return Religion;
    }

    /**
     *
     * @param Religion
     * The user_religion
     */
    public void setReligion(String Religion) {
        this.Religion = Religion;
    }
    /**
     *
     * @return
     * The user_married_status
     */
    public String getMarriedStatus() {
        return MarriedStatus;
    }

    /**
     *
     * @param MarriedStatus
     * The user_married_status
     */
    public void setMarriedStatus(String MarriedStatus) {
        this.MarriedStatus = MarriedStatus;
    }
    /**
     *
     * @return
     * The user_married_since
     */
    public String getMarriedSince() {
        return MarriedSince;
    }

    /**
     *
     * @param MarriedSince
     * The user_married_since
     */
    public void setMarriedSince(String MarriedSince) {
        this.MarriedSince = MarriedSince;
    }
}
