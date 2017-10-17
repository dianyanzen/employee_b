package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetProfileMainMarried {
    @SerializedName("married_status")
    @Expose
    private String married_status;

    /**
     * @return
     */
    public String getMarriedStatus() {
        return married_status;
    }

    /**
     * @param married_status
     */
    public void setMarriedStatus(String married_status) {
        this.married_status = married_status;
    }
}
