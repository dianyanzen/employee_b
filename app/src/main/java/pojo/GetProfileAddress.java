package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 16/10/17.
 */

public class GetProfileAddress {
    @SerializedName("employee_id")
    @Expose
    private String EmployeeID;
    @SerializedName("street")
    @Expose
    private String UserStreet;
    @SerializedName("address")
    @Expose
    private String UserAddreess;
    @SerializedName("region")
    @Expose
    private String UserRegion;
    @SerializedName("sub_district")
    @Expose
    private String UserSubDistrict;
    @SerializedName("province")
    @Expose
    private String UserProvince;
    @SerializedName("country")
    @Expose
    private String UserCountry;
    @SerializedName("postal_code")
    @Expose
    private String UserPostalCode;
    @SerializedName("handphone1")
    @Expose
    private String UserHandphone1;
    @SerializedName("handphone2")
    @Expose
    private String UserHandphone2;
    @SerializedName("work_email")
    @Expose
    private String UserWorkEmail;
    @SerializedName("bank_account_number")
    @Expose
    private String UserBankAccountNumber;
    @SerializedName("closed_person_name")
    @Expose
    private String UserClosedPersonName;
    @SerializedName("closed_person_phone")
    @Expose
    private String UserClosedPersonPhone;



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
     * The street
     */
    public String getUserStreet() {
        return UserStreet;
    }

    /**
     *
     * @param UserStreet
     * The street
     */
    public void setUserStreet(String UserStreet) {
        this.UserStreet = UserStreet;
    }


    /**
     *
     * @return
     * The address
     */
    public String getUserAddreess() {
        return UserAddreess;
    }

    /**
     *
     * @param UserAddreess
     * The address
     */
    public void setUserAddreess(String UserAddreess) {
        this.UserAddreess = UserAddreess;
    }


    /**
     *
     * @return
     * The region
     */
    public String getUserRegion() {
        return UserRegion;
    }

    /**
     *
     * @param UserRegion
     * The region
     */
    public void setUserRegion(String UserRegion) {
        this.UserRegion = UserRegion;
    }


    /**
     *
     * @return
     * The sub_district
     */
    public String getUserSubDistrict() {
        return UserSubDistrict;
    }

    /**
     *
     * @param UserSubDistrict
     * The sub_district
     */
    public void setUserSubDistrict(String UserSubDistrict) {
        this.UserSubDistrict = UserSubDistrict;
    }


    /**
     *
     * @return
     * The province
     */
    public String getUserProvince() {
        return UserProvince;
    }

    /**
     *
     * @param UserProvince
     * The province
     */
    public void setUserProvince(String UserProvince) {
        this.UserProvince = UserProvince;
    }


    /**
     *
     * @return
     * The country
     */
    public String getUserCountry() {
        return UserCountry;
    }

    /**
     *
     * @param UserCountry
     * The country
     */
    public void setUserCountry(String UserCountry) {
        this.UserCountry = UserCountry;
    }


    /**
     *
     * @return
     * The postal_code
     */
    public String getUserPostalCode() {
        return UserPostalCode;
    }

    /**
     *
     * @param UserPostalCode
     * The postal_code
     */
    public void setUserPostalCode(String UserPostalCode) {
        this.UserPostalCode = UserPostalCode;
    }


    /**
     *
     * @return
     * The handphone1
     */
    public String getUserHandphone1() {
        return UserHandphone1;
    }

    /**
     *
     * @param UserHandphone1
     * The handphone1
     */
    public void setUserHandphone1(String UserHandphone1) {
        this.UserHandphone1 = UserHandphone1;
    }


    /**
     *
     * @return
     * The handphone2
     */
    public String getUserHandphone2() {
        return UserHandphone2;
    }

    /**
     *
     * @param UserHandphone2
     * The handphone2
     */
    public void setUserHandphone2(String UserHandphone2) {
        this.UserHandphone2 = UserHandphone2;
    }


    /**
     *
     * @return
     * The work_email
     */
    public String getUserWorkEmail() {
        return UserWorkEmail;
    }

    /**
     *
     * @param UserWorkEmail
     * The work_email
     */
    public void setUserWorkEmail(String UserWorkEmail) {
        this.UserWorkEmail = UserWorkEmail;
    }


    /**
     *
     * @return
     * The bank_account_number
     */
    public String getUserBankAccountNumber() {
        return UserBankAccountNumber;
    }

    /**
     *
     * @param UserBankAccountNumber
     * The bank_account_number
     */
    public void setUserBankAccountNumber(String UserBankAccountNumber) {
        this.UserBankAccountNumber = UserBankAccountNumber;
    }


    /**
     *
     * @return
     * The closed_person_name
     */
    public String getUserClosedPersonName() {
        return UserClosedPersonName;
    }

    /**
     *
     * @param UserClosedPersonName
     * The closed_person_name
     */
    public void setUserClosedPersonName(String UserClosedPersonName) {
        this.UserClosedPersonName = UserClosedPersonName;
    }


    /**
     *
     * @return
     * The closed_person_phone
     */
    public String getUserClosedPersonPhone() {
        return UserClosedPersonPhone;
    }

    /**
     *
     * @param UserClosedPersonPhone
     * The closed_person_phone
     */
    public void setUserClosedPersonPhone(String UserClosedPersonPhone) {
        this.UserClosedPersonPhone = UserClosedPersonPhone;
    }
}
