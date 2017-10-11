package pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLastClockIn {

    @SerializedName("msgType")
    @Expose
    private String msgType;
    @SerializedName("msgText")
    @Expose
    private String msgText;
    @SerializedName("clock_in")
    @Expose
    private String clockIn;
    @SerializedName("clock_out")
    @Expose
    private String clockOut;
    @SerializedName("work_hour")
    @Expose
    private String workHour;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("schedule_type")
    @Expose
    private String scheduleType;
    @SerializedName("schedule_project")
    @Expose
    private String scheduleProject;
    @SerializedName("schedule_description")
    @Expose
    private String scheduleDescription;

    /**
     *
     * @return
     * The msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     *
     * @param msgType
     * The msgType
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     *
     * @return
     * The msgText
     */
    public String getMsgText() {
        return msgText;
    }

    /**
     *
     * @param msgText
     * The msgText
     */
    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    /**
     *
     * @return
     * The clockIn
     */
    public String getClockIn() {
        return clockIn;
    }

    /**
     *
     * @param clockIn
     * The clock_in
     */
    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    /**
     *
     * @return
     * The clockOut
     */
    public String getClockOut() {
        return clockOut;
    }

    /**
     *
     * @param clockOut
     * The clock_out
     */
    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }

    /**
     *
     * @return
     * The workHour
     */
    public String getWorkHour() {
        return workHour;
    }

    /**
     *
     * @param workHour
     * The work_hour
     */
    public void setWorkHour(String workHour) {
        this.workHour = workHour;
    }

    /**
     *
     * @return
     * The place
     */
    public String getPlace() {
        return place;
    }

    /**
     *
     * @param place
     * The place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     *
     * @return
     * The scheduleType
     */
    public String getScheduleType() {
        return scheduleType;
    }

    /**
     *
     * @param scheduleType
     * The schedule_type
     */
    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    /**
     *
     * @return
     * The scheduleProject
     */
    public String getScheduleProject() {
        return scheduleProject;
    }

    /**
     *
     * @param scheduleProject
     * The schedule_project
     */
    public void setScheduleProject(String scheduleProject) {
        this.scheduleProject = scheduleProject;
    }

    /**
     *
     * @return
     * The scheduleDescription
     */
    public String getScheduleDescription() {
        return scheduleDescription;
    }

    /**
     *
     * @param scheduleDescription
     * The schedule_description
     */
    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

}