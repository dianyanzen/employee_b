package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 11/10/17.
 */

public class GetExcuseShift {
    @SerializedName("shift_type")
    @Expose
    private String shift_type;

    /**
     * @return The excuseCd
     */
    public String getExcuseshift() {
        return shift_type;
    }

    /**
     * @param shift_type The excuse_cd
     */
    public void setExcuseshift(String shift_type) {
        this.shift_type = shift_type;
    }

}