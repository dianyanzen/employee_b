package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetProfileEducationLevel {
    @SerializedName("edu_level")
    @Expose
    private String edu_level;

    /**
     * @return
     */
    public String getEduLevel() {
        return edu_level;
    }

    /**
     * @param edu_level
     */
    public void setEduLevel(String edu_level) {
        this.edu_level = edu_level;
    }
}
