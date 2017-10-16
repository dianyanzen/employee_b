package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetExcuseGroup;
import pojo.GetExcuseList;
import pojo.GetExcuseShift;
import pojo.GetExcuseType;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by root on 09/10/17.
 */

public interface ApiExcuse {
    @GET("/excuseservicemobile/getexcusetype")
    void getExcuseType(
            Callback<List<GetExcuseType>> response
    );
    @GET("/excuseservicemobile/getexcusegroup")
    void getExcuseGroup(
            Callback<List<GetExcuseGroup>> response
    );
    @GET("/excuseservicemobile/getexcuseshift")
    void getExcuseShift(
            Callback<List<GetExcuseShift>> response
    );

    @GET("/excuseservicemobile/getexcuselist")
    void getExcuseList(
            @Query("employee_id") String employee_id, Callback<List<GetExcuseList>> response
    );

    @GET("/excuseservicemobile/getexcusebyid")
    void getExcuseById(
            @Query("excuse_id") String ExcuseId, Callback<GetExcuseList> response
    );

    @GET("/excuseservicemobile/deleteexcuse")
    void onDeleteExcuse(
            @Query("excuse_id") String excuse_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );

    //    @Multipart
    @POST("/excuseservicemobile/saveexcuse")
    void onSaveExcuse(
            @Query("employee_id") String employee_id,
            @Query("username") String username,
            @Query("excuse_id") String excuse_id,
            @Query("excuse_type") String excuse_type,
            @Query("excuse_reason") String excuse_reason,
            @Query("shift_type") String shift_type,
            @Query("shift") String shift,
            @Query("date_from") String date_from,
            @Query("date_to") String date_to,
            @Query("date_event") String date_event,
            @Query("group") String group,
            @Query("clock_in") String clock_in,
            @Query("clock_out") String clock_out,
            Callback<CheckLogin> response
    );


}
