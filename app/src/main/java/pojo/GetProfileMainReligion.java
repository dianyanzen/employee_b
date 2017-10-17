package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetProfileMainReligion {
    @SerializedName("religions")
    @Expose
    private String religions;

    /**
     * @return
     */
    public String getReligions() {
        return religions;
    }

    /**
     * @param religions
     */
    public void setReligions(String religions) {
        this.religions = religions;
    }
}
