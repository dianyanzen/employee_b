package id.co.arkamaya.bc_android;

import pojo.GetProfileAddress;
import pojo.GetProfileMain;
import pojo.GetProfileTax;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by root on 16/10/17.
 */

public interface APIEditProfile {
    @GET("/mobile_editprofilemain/get_usermaindata")
    void getProfileMain(
            @Query("employee_id") String Employee_id, Callback<GetProfileMain> response
    );
    @GET("/mobile_editprofilemain/get_useraddress")
    void getProfileAddress(
            @Query("employee_id") String Employee_id, Callback<GetProfileAddress> response
    );
    @GET("/mobile_editprofilemain/get_usertax")
    void getProfileTax(
            @Query("employee_id") String Employee_id, Callback<GetProfileTax> response
    );
}
