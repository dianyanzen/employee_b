package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetLeaveList;
import pojo.GetLeaveType;
import pojo.getEmployees;
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

    @GET("/leaveservicemobile/getleavetype")
    void getLeaveType(
            Callback<List<GetLeaveType>> response
    );

    @GET("/leaveservicemobile/getemployees")
    void getEmployees(
            Callback<List<getEmployees>> response
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
    void onSaveExcuse(
            @Query("employee_id") String employee_id,
            @Query("username") String username,
            @Query("time_off_id") String time_off_id,
            @Query("time_off_dt") String time_off_dt,
            @Query("time_off_type") String time_off_type,
            @Query("time_off_description") String time_off_description,
            @Query("date_from") String date_from,
            @Query("date_to") String date_to,
            Callback<CheckLogin> response
    );

}
