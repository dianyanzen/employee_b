package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetReimburseList;
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
 * Created by dawala on 3/6/2016.
 */
public interface APIReimburse {
    @GET("/cicoreimservicemobile/getreimburselist")
    void getReimburseList(
            @Query("employee_id") String employee_id, Callback<List<GetReimburseList>> response
    );

    @GET("/cicoreimservicemobile/getreimbursebyid")
    void getReimburseById(
            @Query("reimburse_id") String reimburse_id, Callback<GetReimburseList> response
    );

    @GET("/cicoreimservicemobile/deletereimburse")
    void onDeleteReimburse(
            @Query("reimburse_id") String reimburse_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );

    @Multipart
    @POST("/cicoreimservicemobile/savereimburse")
    void onSaveReimburse(@Part("userfile") TypedFile userfile,
                    @Query("reimburse_id") String reimburse_id,
                         @Query("reimburse_dt") String reimburse_dt,
                         @Query("reimburse_type") String reimburse_type,
                         @Query("reimburse_description") String reimburse_description,
                         @Query("reimburse_amount") String reimburse_amount,
                         @Query("username") String username,
                         @Query("old_reimburse_file") String old_reimburse_file,
                         @Query("reimburse_file") String reimburse_file,
                         @Query("employee_id") String employee_id,
                    Callback<CheckLogin> response
    );

    @GET("/cicoreimservicemobile/savereimbursewoimage")
    void onSaveReimburseWoImage(@Query("reimburse_id") String reimburse_id,
                         @Query("reimburse_dt") String reimburse_dt,
                         @Query("reimburse_type") String reimburse_type,
                         @Query("reimburse_description") String reimburse_description,
                         @Query("reimburse_amount") String reimburse_amount,
                         @Query("username") String username,
                         @Query("old_reimburse_file") String old_reimburse_file,
                         @Query("reimburse_file") String reimburse_file,
                         @Query("employee_id") String employee_id,
                         Callback<CheckLogin> response
    );

}
