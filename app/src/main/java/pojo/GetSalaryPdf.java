package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 25/10/17.
 */

public class GetSalaryPdf {

    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    @SerializedName("employee_name")
    @Expose
    private String employee_name;

    @SerializedName("attachment_id")
    @Expose
    private String attachment_id;

    @SerializedName("period")
    @Expose
    private String period;

    @SerializedName("file_name")
    @Expose
    private String file_name;


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
    public String getEmployeeName() {
        return employee_name;
    }

    /**
     *
     * @param employee_name
     *
     */
    public void setEmployeeName(String employee_name) {
        this.employee_name = employee_name;
    }
    /**
     *
     * @return
     *
     */
    public String getAttachmentId() {
        return attachment_id;
    }

    /**
     *
     * @param attachment_id
     *
     */
    public void setAttachmentId(String attachment_id) {
        this.attachment_id = attachment_id;
    }
    /**
     *
     * @return
     *
     */
    public String getPeriod() {
        return period;
    }

    /**
     *
     * @param period
     *
     */
    public void setPeriod(String period) {
        this.period = period;
    }
    /**
     *
     * @return
     *
     */
    public String getFileName() {
        return file_name;
    }

    /**
     *
     * @param file_name
     *
     */
    public void setFileName(String file_name) {
        this.file_name = file_name;
    }

}
