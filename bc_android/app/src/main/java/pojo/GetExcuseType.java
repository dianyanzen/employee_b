package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 10/10/17.
 */

public class GetExcuseType {
    @SerializedName("excuse_type")
    @Expose
    private String excuse_type;

    /**
     * @return The excuseCd
     */
    public String getExcuseType() {
        return excuse_type;
    }

    /**
     * @param excuse_type The excuse_cd
     */
    public void setExcuseCd(String excuse_type) {
        this.excuse_type = excuse_type;
    }

}
