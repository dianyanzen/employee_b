package id.co.arkamaya.cico;

import pojo.CheckLogin;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by dawala on 2/20/2016.
 */
public interface APILogin {
    @FormUrlEncoded
    @POST("/cicoreimservicemobile/checklogin")
    void checkLogin(
            @Field("username") String UserName,
            @Field("password") String Password, Callback<CheckLogin> response);
}
