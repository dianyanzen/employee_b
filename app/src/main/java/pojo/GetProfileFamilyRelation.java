package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetProfileFamilyRelation {
    @SerializedName("relationship")
    @Expose
    private String relationship;

    /**
     * @return
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * @param relationship
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
