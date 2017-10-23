package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetEmployees;
import pojo.GetLeaveList;
import pojo.GetLeaveType;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by root on 17/10/17.
 */

public interface ApiLeave {

    @GET("/leaveservicemobile/getleavelist")
    void getLeaveList(
            @Query("employee_id") String employee_id, Callback<List<GetLeaveList>> response
    );

    @GET("/leaveservicemobile/gettypevalue")
    void getLeaveType(
            Callback<List<GetLeaveType>> response
    );

    @GET("/leaveservicemobile/getemployees")
    void getEmployees(
            Callback<List<GetEmployees>> response
    );

    @GET("/leaveservicemobile/getleavebyid")
    void getLeaveById(
            @Query("time_off_id") String time_off_id, Callback<GetLeaveList> response
    );

    @GET("/leaveservicemobile/deleteleave")
    void onDeleteLeave(
            @Query("time_off_id") String time_off_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );

    //    @Multipart
    @POST("/leaveservicemobile/saveleave")
    void onSaveLeave(
            @Query("leave_id") String leave_id,
            @Query("employee_id") String employee_id,
            @Query("username") String username,
            @Query("leave_type") String leave_type,
            @Query("leave_descript") String leave_descript,
            @Query("leave_from_date") String leave_from_date,
            @Query("leave_to_date") String leave_to_date,
            Callback<CheckLogin> response
    );

}
