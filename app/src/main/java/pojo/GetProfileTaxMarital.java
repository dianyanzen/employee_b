package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetProfileTaxMarital {
    @SerializedName("marital")
    @Expose
    private String marital;

    /**
     * @return
     */
    public String getTaxMarital() {
        return marital;
    }

    /**
     * @param marital
     */
    public void setTaxMarital(String marital) {
        this.marital = marital;
    }
}
