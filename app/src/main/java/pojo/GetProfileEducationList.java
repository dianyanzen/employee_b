package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 19/10/17.
 */

public class GetProfileEducationList {
    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    @SerializedName("education_id")
    @Expose
    private String education_id;

    @SerializedName("level")
    @Expose
    private String level;

    @SerializedName("institution")
    @Expose
    private String institution;

    @SerializedName("faculty")
    @Expose
    private String faculty;

    @SerializedName("graduated_dt")
    @Expose
    private String graduated_dt;

    @SerializedName("gpa")
    @Expose
    private String gpa;

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
    public String getEducationId() {
        return education_id;
    }

    /**
     *
     * @param education_id
     *
     */
    public void setEducationId(String education_id) {
        this.education_id = education_id;
    }

    /**
     *
     * @return
     *
     */
    public String getLevel() {
        return level;
    }

    /**
     *
     * @param level
     *
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     *
     * @return
     *
     */
    public String getInstitution() {
        return institution;
    }

    /**
     *
     * @param institution
     *
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     *
     * @return
     *
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     *
     * @param faculty
     *
     */
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    /**
     *
     * @return
     *
     */
    public String getGraduatedDt() {
        return graduated_dt;
    }

    /**
     *
     * @param graduated_dt
     *
     */
    public void setGraduatedDt(String graduated_dt) {
        this.graduated_dt = graduated_dt;
    }

    /**
     *
     * @return
     *
     */
    public String getGPA() {
        return gpa;
    }

    /**
     *
     * @param gpa
     *
     */
    public void setGPA(String gpa) {
        this.gpa = gpa;
    }

}
