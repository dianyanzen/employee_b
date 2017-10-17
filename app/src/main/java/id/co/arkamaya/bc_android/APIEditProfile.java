package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetProfileAddress;
import pojo.GetProfileEducationLevel;
import pojo.GetProfileFamilyRelation;
import pojo.GetProfileIdcardType;
import pojo.GetProfileMain;
import pojo.GetProfileMainMarried;
import pojo.GetProfileMainReligion;
import pojo.GetProfileTax;
import pojo.GetProfileTaxMarital;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
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
    @GET("/mobile_editprofilemain/get_main_religion")
    void getProfileMainReligion(
            Callback<List<GetProfileMainReligion>> response
    );
    @GET("/mobile_editprofilemain/get_main_marriedstat")
    void getProfileMainMarried(
            Callback<List<GetProfileMainMarried>> response
    );
    @GET("/mobile_editprofilemain/get_family_relation")
    void getProfileFamilyRelation(
            Callback<List<GetProfileFamilyRelation>> response
    );
    @GET("/mobile_editprofilemain/get_education_level")
    void getProfileEducationLevel(
            Callback<List<GetProfileEducationLevel>> response
    );
    @GET("/mobile_editprofilemain/get_idcard_type")
    void getProfileIdcardType(
            Callback<List<GetProfileIdcardType>> response
    );
    @GET("/mobile_editprofilemain/get_tax_marital")
    void getProfileTaxMarital(
            Callback<List<GetProfileTaxMarital>> response
    );

    @GET("/mobile_editprofilemain/get_userchangepswd")
    void onUpdatePassword(
            @Query("employee_id") String employee_id,
            @Query("user_password") String user_password,
            @Query("password1") String password1,
            @Query("password2") String password2,
            Callback<CheckLogin> response
    );
    @POST("/mobile_editprofilemain/get_updatemain")
    void onUpdateMainData(
            @Query("employee_id") String employee_id,
            @Query("user_title") String user_title,
            @Query("user_gender") String user_gender,
            @Query("user_born_dt") String user_born_dt,
            @Query("user_born_place") String user_born_place,
            @Query("user_religion") String user_religion,
            @Query("user_married_status") String user_married_status,
            @Query("user_married_since") String user_married_since,
            Callback<CheckLogin> response
    );

}
