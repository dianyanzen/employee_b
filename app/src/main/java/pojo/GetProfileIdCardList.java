package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 19/10/17.
 */

public class GetProfileIdCardList {
    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    @SerializedName("card_id")
    @Expose
    private String card_id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("type_card")
    @Expose
    private String type_card;

    @SerializedName("id_number")
    @Expose
    private String id_number;

    @SerializedName("issued_dt")
    @Expose
    private String issued_dt;

    @SerializedName("placed")
    @Expose
    private String placed;

    @SerializedName("expired_dt")
    @Expose
    private String expired_dt;

    /**
     *
     * @return
     *
     */
    public String getEmployeeId() {
        return employee_id;
    }

    /**
     *
     * @param employee_id
     *
     */
    public void setEmployeeId(String employee_id) {
        this.employee_id = employee_id;
    }

    /**
     *
     * @return
     *
     */
    public String getCardId() {
        return card_id;
    }

    /**
     *
     * @param card_id
     *
     */
    public void setCardId(String card_id) {
        this.card_id = card_id;
    }

    /**
     *
     * @return
     *
     */
    public String getTypeCard() {
        return type_card;
    }

    /**
     *
     * @param type_card
     *
     */
    public void setTypeCard(String type_card) {
        this.type_card = type_card;
    }

    /**
     *
     * @return
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     *
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     *
     * @return
     *
     */
    public String getIdNumber() {
        return id_number;
    }

    /**
     *
     * @param id_number
     *
     */
    public void setIdNumber(String id_number) {
        this.id_number = id_number;
    }

    /**
     *
     * @return
     *
     */
    public String getIssuedDt() {
        return issued_dt;
    }

    /**
     *
     * @param issued_dt
     *
     */
    public void setIssuedDt(String issued_dt) {
        this.issued_dt = issued_dt;
    }

    /**
     *
     * @return
     *
     */
    public String getPlaced() {
        return placed;
    }

    /**
     *
     * @param placed
     *
     */
    public void setPlaced(String placed) {
        this.placed = placed;
    }

    /**
     *
     * @return
     *
     */
    public String getExpiredDt() {
        return expired_dt;
    }

    /**
     *
     * @param expired_dt
     *
     */
    public void setExpiredDt(String expired_dt) {
        this.expired_dt = expired_dt;
    }
}
