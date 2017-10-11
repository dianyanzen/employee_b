package pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CicoPojo {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("clockInDt")
    @Expose
    private String clockInDt;
    @SerializedName("clockOutDt")
    @Expose
    private String clockOutDt;
    @SerializedName("workingHourTm")
    @Expose
    private String workingHourTm;
    @SerializedName("projectName")
    @Expose
    private String projectName;
    @SerializedName("projectLocation")
    @Expose
    private String projectLocation;
    @SerializedName("locationType")
    @Expose
    private String locationType;

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The clockInDt
     */
    public String getClockInDt() {
        return clockInDt;
    }

    /**
     *
     * @param clockInDt
     * The clockInDt
     */
    public void setClockInDt(String clockInDt) {
        this.clockInDt = clockInDt;
    }

    /**
     *
     * @return
     * The clockOutDt
     */
    public String getClockOutDt() {
        return clockOutDt;
    }

    /**
     *
     * @param clockOutDt
     * The clockOutDt
     */
    public void setClockOutDt(String clockOutDt) {
        this.clockOutDt = clockOutDt;
    }

    /**
     *
     * @return
     * The workingHourTm
     */
    public String getWorkingHourTm() {
        return workingHourTm;
    }

    /**
     *
     * @param workingHourTm
     * The workingHourTm
     */
    public void setWorkingHourTm(String workingHourTm) {
        this.workingHourTm = workingHourTm;
    }

    /**
     *
     * @return
     * The projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     *
     * @param projectName
     * The projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     *
     * @return
     * The projectLocation
     */
    public String getProjectLocation() {
        return projectLocation;
    }

    /**
     *
     * @param projectLocation
     * The projectLocation
     */
    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    /**
     *
     * @return
     * The locationType
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     *
     * @param locationType
     * The locationType
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

}
