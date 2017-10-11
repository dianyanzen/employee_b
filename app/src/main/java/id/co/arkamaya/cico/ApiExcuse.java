package id.co.arkamaya.cico;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetExcuseGroup;
import pojo.GetExcuseList;
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
            @Query("excuse_id") String excuse_id,
            @Query("employee_id") String employee_id,
            @Query("username") String username,
            @Query("date_from") String date_from,
            @Query("date_to") String date_to,
            @Query("excuse_type") String excuse_type,
            @Query("excuse_reason") String excuse_reason,
            Callback<CheckLogin> response
    );


}
