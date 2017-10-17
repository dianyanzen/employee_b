package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 17/10/17.
 */

public class GetProfileIdcardType {
    @SerializedName("id_card_type")
    @Expose
    private String id_card_type;

    /**
     * @return
     */
    public String getIdcardType() {
        return id_card_type;
    }

    /**
     * @param id_card_type
     */
    public void setIdcardType(String id_card_type) {
        this.id_card_type = id_card_type;
    }
}
