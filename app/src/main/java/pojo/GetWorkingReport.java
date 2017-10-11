package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWorkingReport {

    @SerializedName("attendance_id")
    @Expose
    private String attendanceId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("clock_out")
    @Expose
    private String clockOut;
    @SerializedName("clock_in")
    @Expose
    private String clockIn;
    @SerializedName("work_hour")
    @Expose
    private String workHour;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("long_in")
    @Expose
    private String longIn;
    @SerializedName("lat_in")
    @Expose
    private String latIn;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("schedule_dt")
    @Expose
    private String scheduleDt;
    @SerializedName("Diff_Hour")
    @Expose
    private String DiffHour;
    @SerializedName("Count_Hour")
    @Expose
    private Integer CountHour;

    /**
     *
     * @return
     * The attendanceId
     */
    public String getAttendanceId() {
        return attendanceId;
    }

    /**
     *
     * @param attendanceId
     * The attendance_id
     */
    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    /**
     *
     * @return
     * The employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     *
     * @param employeeId
     * The employee_id
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     * The longIn
     */
    public String getLongIn() {
        return longIn;
    }

    /**
     *
     * @param longIn
     * The long_in
     */
    public void setLongIn(String longIn) {
        this.longIn = longIn;
    }

    /**
     *
     * @return
     * The latIn
     */
    public String getLatIn() {
        return latIn;
    }

    /**
     *
     * @param latIn
     * The lat_in
     */
    public void setLatIn(String latIn) {
        this.latIn = latIn;
    }

    /**
     *
     * @return
     * The reason
     */
    public Object getReason() {
        return reason;
    }

    /**
     *
     * @param reason
     * The reason
     */
    public void setReason(Object reason) {
        this.reason = reason;
    }

    /**
     *
     * @return
     * The address
     */
    public Object getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(Object address) {
        this.address = address;
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
     * The scheduleDt
     */
    public String getScheduleDt() {
        return scheduleDt;
    }

    /**
     *
     * @param scheduleDt
     * The schedule_dt
     */
    public void setScheduleDt(String scheduleDt) {
        this.scheduleDt = scheduleDt;
    }

    /**
     *
     * @return
     * The DiffHour
     */
    public String getDiffHour() {
        return DiffHour;
    }

    /**
     *
     * @param DiffHour
     * The Diff_Hour
     */
    public void setDiffHour(String DiffHour) {
        this.DiffHour = DiffHour;
    }

    /**
     *
     * @return
     * The CountHour
     */
    public Integer getCountHour() {
        return CountHour;
    }

    /**
     *
     * @param CountHour
     * The Count_Hour
     */
    public void setCountHour(Integer CountHour) {
        this.CountHour = CountHour;
    }

}