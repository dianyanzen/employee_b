package id.co.arkamaya.cico;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetComplaintList;
import pojo.GetComplaintType;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by wawan on 3/11/2016.
 */
public interface APIComplaint {
    @GET("/complaintservicemobileportal/get_complaint_type_list")
    void getComplaintType(
            Callback<List<GetComplaintType>> response
    );

    @GET("/complaintservicemobileportal/get_complaint_list")
    void getComplaintList(
            @Query("employee_id") String employee_id, Callback<List<GetComplaintList>> response
    );

    @GET("/complaintservicemobileportal/get_complaint_by_id")
    void getComplaintById(
            @Query("complaint_id") String complaint_id, Callback<GetComplaintList> response
    );

    @GET("/complaintservicemobileportal/delete_complaint")
    void onDeleteComplaint(
            @Query("complaint_id") String complaint_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );

//    @Multipart
    @POST("/complaintservicemobileportal/save_complaint")
    void onSaveComplaint(
            @Query("complaint_id") String complaint_id,
            @Query("employee_id") String employee_id,
            @Query("complaint_dt") String complaint_dt,
            @Query("complaint_type") String complaint_type,
            @Query("complaint_description") String complaint_description,
            @Query("complaint_clock_in") String complaint_clock_in,
            @Query("complaint_clock_out") String complaint_clock_out,
            Callback<CheckLogin> response
    );


}
