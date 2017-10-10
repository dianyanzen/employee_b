package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTotaLWHWorkingReport {

    @SerializedName("total_mv_time")
    @Expose
    private String totalMvTime;

    /**
     *
     * @return
     * The totalMvTime
     */
    public String getTotalMvTime() {
        return totalMvTime;
    }

    /**
     *
     * @param totalMvTime
     * The total_mv_time
     */
    public void setTotalMvTime(String totalMvTime) {
        this.totalMvTime = totalMvTime;
    }

}