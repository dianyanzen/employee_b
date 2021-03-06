package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetProfileAddress;
import pojo.GetProfileEducationLevel;
import pojo.GetProfileEducationList;
import pojo.GetProfileFamilyList;
import pojo.GetProfileFamilyRelation;
import pojo.GetProfileIdCardList;
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
    // TODO User Data Fragment
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
    // TODO Get Combo Box Fragment
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
    // TODO User Password Fragment
    @GET("/mobile_editprofilemain/get_userchangepswd")
    void onUpdatePassword(
            @Query("employee_id") String employee_id,
            @Query("user_password") String user_password,
            @Query("password1") String password1,
            @Query("password2") String password2,
            Callback<CheckLogin> response
    );
    // TODO Main Data Fragment
    @POST("/mobile_editprofilemain/on_updatemain")
    void onUpdateMainData(
            @Query("employee_id") String employee_id,
            @Query("user") String user,
            @Query("user_title") String user_title,
            @Query("user_gender") String user_gender,
            @Query("user_born_dt") String user_born_dt,
            @Query("user_born_place") String user_born_place,
            @Query("user_religion") String user_religion,
            @Query("user_married_status") String user_married_status,
            @Query("user_married_since") String user_married_since,
            Callback<CheckLogin> response
    );
    // TODO Address Fragment
    @POST("/mobile_editprofilemain/on_updateaddress")
    void onUpdateAddress(
            @Query("employee_id") String employee_id,
            @Query("user_street") String user_street,
            @Query("user_address") String user_address,
            @Query("user_region") String user_region,
            @Query("user_sub_district") String user_sub_district,
            @Query("user_province") String user_province,
            @Query("user_country") String user_country,
            @Query("user_postal_code") String user_postal_code,
            @Query("user_phone1") String user_phone1,
            @Query("user_phone2") String user_phone2,
            @Query("user_work_email") String user_work_email,
            @Query("user_bank_account") String user_bank_account,
            @Query("user_contact_person") String user_contact_person,
            @Query("user_contact_person_phone") String user_contact_person_phone,
            Callback<CheckLogin> response
    );
    // TODO Tax Fragment
    @POST("/mobile_editprofilemain/on_updatetax")
    void onUpdateTax(
            @Query("employee_id") String employee_id,
            @Query("user_npwp") String user_npwp,
            @Query("user_npwp_dt") String user_npwp_dt,
            @Query("user_marital") String user_marital,
            @Query("user_bpjs_ketenagakerjaan") String user_bpjs_ketenagakerjaan,
            @Query("user_bpjs_kesehatan") String user_bpjs_kesehatan,
            Callback<CheckLogin> response
    );
    // TODO Family Activity
    @POST("/mobile_editprofilemain/on_addfamily")
    void onAddFamily(
            @Query("family_id") String family_id,
            @Query("employee_id") String employee_id,
            @Query("user_name") String user_name,
            @Query("family_relation") String family_relation,
            @Query("family_gender") String family_gender,
            @Query("family_full_name") String family_full_name,
            @Query("family_born_place") String family_born_place,
            @Query("family_born_date") String family_born_date,
            @Query("family_nationality") String family_nationality,
            Callback<CheckLogin> response
    );
    @GET("/mobile_editprofilemain/on_deletefamily")
    void onDeleteFamily(
            @Query("family_id") String family_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );
    @GET("/mobile_editprofilemain/get_userfamily")
    void getFamilyList(
            @Query("employee_id") String employee_id, Callback<List<GetProfileFamilyList>> response
    );
    @GET("/mobile_editprofilemain/get_userfamilybyid")
    void getFamilyListById(
            @Query("family_id") String FamilyId, Callback<GetProfileFamilyList> response
    );
    // TODO Education Activity
    @GET("/mobile_editprofilemain/get_usereducation")
    void getEducationList(
            @Query("employee_id") String employee_id, Callback<List<GetProfileEducationList>> response
    );
    @GET("/mobile_editprofilemain/on_deleteeducation")
    void onDeleteEducation(
            @Query("education_id") String EducationId,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );
    @GET("/mobile_editprofilemain/get_usereducationbyid")
    void getEducationListById(
            @Query("education_id") String EducationId, Callback<GetProfileEducationList> response
    );
    @POST("/mobile_editprofilemain/on_addeducation")
    void onAddEducation(
            @Query("education_id") String education_id,
            @Query("employee_id") String employee_id,
            @Query("user_name") String user_name,
            @Query("education_level") String education_level,
            @Query("education_institution") String education_institution,
            @Query("education_faculty") String education_faculty,
            @Query("education_graduate_date") String education_graduate_date,
            @Query("education_gpa") String education_gpa,
            Callback<CheckLogin> response
    );
    // TODO ID Card Activity
    @GET("/mobile_editprofilemain/get_useridcard")
    void getIdCardList(
            @Query("employee_id") String employee_id, Callback<List<GetProfileIdCardList>> response
    );
    @GET("/mobile_editprofilemain/on_deleteidcard")
    void onDeleteIdCard(
            @Query("card_id") String card_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );
    @GET("/mobile_editprofilemain/get_useridcardbyid")
    void getCardListById(
            @Query("card_id") String FamilyId, Callback<GetProfileIdCardList> response
    );
    @POST("/mobile_editprofilemain/on_addidcard")
    void onAddCard(
            @Query("card_id") String card_id,
            @Query("employee_id") String employee_id,
            @Query("user_name") String user_name,
            @Query("card_type") String card_type,
            @Query("card_description") String card_description,
            @Query("card_number") String card_number,
            @Query("card_issue_date") String card_issue_date,
            @Query("card_place") String card_place,
            @Query("card_expired_date") String card_expired_date,
            Callback<CheckLogin> response
    );
}
