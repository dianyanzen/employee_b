package id.co.arkamaya.bc_android;


import pojo.CheckLogin;
import pojo.GetLastClockIn;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by dawala on 2/16/2016.
 */
/*public interface CicoAPI {
    @GET("bins/2z9bn")
    Call<CicoPojo> cicoGet();
}*/

public interface APICico {
    @FormUrlEncoded
    @POST("/cicoreimservicemobile/clockin")
    void clockIn(
            @Field("username") String username,
            @Field("employee_id") String employee_id,
            @Field("source") String source,
            @Field("long_in") String long_in,
            @Field("lat_in") String lat_in,
            @Field("schedule_type") String schedule_type,
            @Field("schedule_project") String schedule_project,
            @Field("schedule_description") String schedule_description,
            @Field("place") String place,
            @Field("address") String address,
            Callback<CheckLogin> response);

    @FormUrlEncoded
    @POST("/cicoreimservicemobile/clockout")
    void clockOut(
            @Field("employee_id") String username,
            @Field("clock_in") String clock_in,
            @Field("long_out") String long_out,
            @Field("lat_out") String lat_out,Callback<CheckLogin> response);

    @GET("/cicoreimservicemobile/getlastclockIn")
    void getLastClockIn(
            @Query("employee_id") String employee_id, Callback<GetLastClockIn> response
    );
}
