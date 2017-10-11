package id.co.arkamaya.cico;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetOvertimeList;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by user on 3/11/2016.
 */
public interface APIOvertime {
    @GET("/overtimeservicemobile/getovertimelist")
    void getOvertimeList(
            @Query("employee_id") String employee_id, Callback<List<GetOvertimeList>> response
    );

    @GET("/overtimeservicemobile/getovertimebyid")
    void getOvertimeById(
            @Query("ot_id") String ot_id, Callback<GetOvertimeList> response
    );

    @GET("/overtimeservicemobile/deleteovertime")
    void onDeleteOvertime(
            @Query("ot_id") String ot_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );

    @POST("/overtimeservicemobile/saveovertime")
    void onSaveOvertime(
            @Query("ot_id") String ot_id,
            @Query("ot_dt") String ot_dt,
            @Query("ot_description") String ot_description,
            @Query("ot_hour") String ot_hour,
            @Query("username") String username,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );
}
