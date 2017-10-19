package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by arkamaya on 10/18/2017.
 */

public class GetLeaveType {
    @SerializedName("leave_type")
    @Expose
    private String leave_type;

    @SerializedName("leave_id")
    @Expose
    private String leave_id;

    /**
     *
     * @return
     *
     */
    public String getLeaveID() {
        return leave_id;
    }

    /**
     *
     * @param leave_id
     *
     */
    public void setLeaveCd(String leave_id) {
        this.leave_id = leave_id;
    }

    /**
     *
     * @return
     *
     */
    public String getLeaveName() {
        return leave_type;
    }

    /**
     *
     * @param leave_type
     *
     */
    public void setLeaveName(String leave_type) {
        this.leave_id = leave_type;
    }
}
