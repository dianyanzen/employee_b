package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 19/10/17.
 */

public class GetEmployees {
    @SerializedName("employee_id")
    @Expose
    private String employee_id;

    @SerializedName("employee_name")
    @Expose
    private String employee_name;


    /**
     *
     * @return
     *
     */
    public String getEmployeeID() {
        return employee_id;
    }

    /**
     *
     * @param employee_id
     *
     */
    public void setEmployeeID(String employee_id) {
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
}
