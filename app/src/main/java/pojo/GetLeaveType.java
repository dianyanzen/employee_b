package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by arkamaya on 10/18/2017.
 */

public class GetLeaveType {
    @SerializedName("leave_name")
    @Expose
    private String leave_name;

    @SerializedName("leave_type_cd")
    @Expose
    private String leave_type_cd;

    /**
     *
     * @return
     *
     */
    public String getLeaveNm() {
        return leave_name;
    }

    /**
     *
     * @param leave_name
     *
     */
    public void setLeaveNm(String leave_name) {
        this.leave_name = leave_name;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveCd() {
        return leave_type_cd;
    }

    /**
     *
     * @param leave_type_cd
     *
     */
    public void setLeaveCd(String leave_type_cd) {
        this.leave_type_cd = leave_type_cd;
    }
}
