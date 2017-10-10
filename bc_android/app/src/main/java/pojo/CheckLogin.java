
package pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckLogin {

    @SerializedName("msgType")
    @Expose
    private String msgType;
    @SerializedName("msgText")
    @Expose
    private String msgText;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;

    /**
     *
     * @return
     * The msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     *
     * @param msgType
     * The msgType
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     *
     * @return
     * The msgText
     */
    public String getMsgText() {
        return msgText;
    }

    /**
     *
     * @param msgText
     * The msgText
     */
    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    /**
     *
     * @return
     * The employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     *
     * @param employeeId
     * The employee_id
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     *
     * @return
     * The employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     *
     * @param employeeName
     * The employee_name
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}