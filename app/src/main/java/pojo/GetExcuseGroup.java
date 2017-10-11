package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 11/10/17.
 */

public class GetExcuseGroup {
    @SerializedName("excuse_group")
    @Expose
    private String excuse_group;

    /**
     * @return The excuseCd
     */
    public String getExcuseGroup() {
        return excuse_group;
    }

    /**
     * @param excuse_group The excuse_cd
     */
    public void setExcuseGroup(String excuse_group) {
        this.excuse_group = excuse_group;
    }
}
