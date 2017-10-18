package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18/10/17.
 */

public class GetFamilyList {
    @SerializedName("family_id")
    @Expose
    private String family_id;

    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    @SerializedName("relationship")
    @Expose
    private String relationship;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("born_dt")
    @Expose
    private String born_dt;

    @SerializedName("born_place")
    @Expose
    private String born_place;

    @SerializedName("nationality")
    @Expose
    private String nationality;

    /**
     *
     * @return
     *
     */
    public String getFamilyId() {
        return family_id;
    }

    /**
     *
     * @param family_id
     *
     */
    public void setFamilyId(String family_id) {
        this.family_id = family_id;
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
    public String getRelationship() {
        return relationship;
    }

    /**
     *
     * @param relationship
     *
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    /**
     *
     * @return
     *
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     *
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     *
     */
    public String getFullName() {
        return fullname;
    }

    /**
     *
     * @param fullname
     *
     */
    public void setFullNamce(String fullname) {
        this.fullname = fullname;
    }

    /**
     *
     * @return
     *
     */
    public String getBornDate() {
        return born_dt;
    }

    /**
     *
     * @param born_dt
     *
     */
    public void setBornDate(String born_dt) {
        this.born_dt = born_dt;
    }

    /**
     *
     * @return
     *
     */
    public String getBornPlace() {
        return born_place;
    }

    /**
     *
     * @param born_place
     *
     */
    public void setBornPlace(String born_place) {
        this.born_place = born_place;
    }

    /**
     *
     * @return
     *
     */
    public String getNationality() {
        return nationality;
    }

    /**
     *
     * @param nationality
     *
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
