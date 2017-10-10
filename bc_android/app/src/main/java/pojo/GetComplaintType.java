package pojo;

/**
 * Created by wawan on 3/18/2016.
 */
//import javax.annotation.Generated;
//@Generated("org.jsonschema2pojo")
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetComplaintType {
    @SerializedName("excuse_cd")
    @Expose
    private String excuseCd;
    @SerializedName("excuse_nm")
    @Expose
    private String excuseNm;

    /**
     *
     * @return
     * The excuseCd
     */
    public String getExcuseCd() {
        return excuseCd;
    }

    /**
     *
     * @param excuseCd
     * The excuse_cd
     */
    public void setExcuseCd(String excuseCd) {
        this.excuseCd = excuseCd;
    }

    /**
     *
     * @return
     * The excuseNm
     */
    public String getExcuseNm() {
        return excuseNm;
    }

    /**
     *
     * @param excuseNm
     * The excuse_nm
     */
    public void setExcuseNm(String excuseNm) {
        this.excuseNm = excuseNm;
    }
}
