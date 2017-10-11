package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by user on 4/21/2016.
 */
public class DashboardSummary {

    @SerializedName("num_of_day_period")
    @Expose
    private String numOfDayPeriod;
    @SerializedName("cnt_of_day_work_period")
    @Expose
    private String cntOfDayWorkPeriod;
    @SerializedName("cnt_of_leave_day_work_period")
    @Expose
    private String cntOfLeaveDayWorkPeriod;
    @SerializedName("cnt_of_sick_day_work_period")
    @Expose
    private String cntOfSickDayWorkPeriod;
    @SerializedName("cnt_ot_hour_period")
    @Expose
    private String cntOtHourPeriod;
    @SerializedName("cnt_reimburse_amount_period")
    @Expose
    private String cntReimburseAmountPeriod;
    @SerializedName("cnt_remaining_days_off")
    @Expose
    private String cntRemainingDaysOff;
    @SerializedName("cnt_less_work_hour")
    @Expose
    private String cntLessWorkHour;
    @SerializedName("cnt_of_task_assign")
    @Expose
    private String cntOfTaskAssign;

    /**
     *
     * @return
     * The numOfDayPeriod
     */
    public String getNumOfDayPeriod() {
        return numOfDayPeriod;
    }

    /**
     *
     * @param numOfDayPeriod
     * The num_of_day_period
     */
    public void setNumOfDayPeriod(String numOfDayPeriod) {
        this.numOfDayPeriod = numOfDayPeriod;
    }

    /**
     *
     * @return
     * The cntOfDayWorkPeriod
     */
    public String getCntOfDayWorkPeriod() {
        return cntOfDayWorkPeriod;
    }

    /**
     *
     * @param cntOfDayWorkPeriod
     * The cnt_of_day_work_period
     */
    public void setCntOfDayWorkPeriod(String cntOfDayWorkPeriod) {
        this.cntOfDayWorkPeriod = cntOfDayWorkPeriod;
    }

    /**
     *
     * @return
     * The cntOfLeaveDayWorkPeriod
     */
    public String getCntOfLeaveDayWorkPeriod() {
        return cntOfLeaveDayWorkPeriod;
    }

    /**
     *
     * @param cntOfLeaveDayWorkPeriod
     * The cnt_of_leave_day_work_period
     */
    public void setCntOfLeaveDayWorkPeriod(String cntOfLeaveDayWorkPeriod) {
        this.cntOfLeaveDayWorkPeriod = cntOfLeaveDayWorkPeriod;
    }

    /**
     *
     * @return
     * The cntOfSickDayWorkPeriod
     */
    public String getCntOfSickDayWorkPeriod() {
        return cntOfSickDayWorkPeriod;
    }

    /**
     *
     * @param cntOfSickDayWorkPeriod
     * The cnt_of_sick_day_work_period
     */
    public void setCntOfSickDayWorkPeriod(String cntOfSickDayWorkPeriod) {
        this.cntOfSickDayWorkPeriod = cntOfSickDayWorkPeriod;
    }

    /**
     *
     * @return
     * The cntOtHourPeriod
     */
    public String getCntOtHourPeriod() {
        return cntOtHourPeriod;
    }

    /**
     *
     * @param cntOtHourPeriod
     * The cnt_ot_hour_period
     */
    public void setCntOtHourPeriod(String cntOtHourPeriod) {
        this.cntOtHourPeriod = cntOtHourPeriod;
    }

    /**
     *
     * @return
     * The cntReimburseAmountPeriod
     */
    public String getCntReimburseAmountPeriod() {
        return cntReimburseAmountPeriod;
    }

    /**
     *
     * @param cntReimburseAmountPeriod
     * The cnt_reimburse_amount_period
     */
    public void setCntReimburseAmountPeriod(String cntReimburseAmountPeriod) {
        this.cntReimburseAmountPeriod = cntReimburseAmountPeriod;
    }

    /**
     *
     * @return
     * The cntRemainingDaysOff
     */
    public String getCntRemainingDaysOff() {
        return cntRemainingDaysOff;
    }

    /**
     *
     * @param cntRemainingDaysOff
     * The cnt_remaining_days_off
     */
    public void setCntRemainingDaysOff(String cntRemainingDaysOff) {
        this.cntRemainingDaysOff = cntRemainingDaysOff;
    }

    /**
     *
     * @return
     * The cntLessWorkHour
     */
    public String getCntLessWorkHour() {
        return cntLessWorkHour;
    }

    /**
     *
     * @param cntLessWorkHour
     * The cnt_less_work_hour
     */
    public void setCntLessWorkHour(String cntLessWorkHour) {
        this.cntLessWorkHour = cntLessWorkHour;
    }

    /**
     *
     * @return
     * The cntOfTaskAssign
     */
    public String getCntOfTaskAssign() {
        return cntOfTaskAssign;
    }

    /**
     *
     * @param cntOfTaskAssign
     * The cnt_of_task_assign
     */
    public void setCntOfTaskAssign(String cntOfTaskAssign) {
        this.cntOfTaskAssign = cntOfTaskAssign;
    }

}