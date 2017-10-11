package id.co.arkamaya.cico;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetOvertimeList;
import pojo.GetTotaLWHWorkingReport;
import pojo.GetWorkingReport;
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
 * Created by user on 3/14/2016.
 */
public interface APIWorkingReport {
    @GET("/workingreportservicemobile/get_working_report_list")
    void getWorkingReportList(
            @Query("employee_id") String employee_id,
            @Query("data_month") String data,
            Callback<List<GetWorkingReport>> response
    );

    @GET("/workingreportservicemobile/get_total_wh_month")
    void getTotalWH_Month(
            @Query("employee_id") String employee_id,
            @Query("data_month") String data,
            Callback<List<GetTotaLWHWorkingReport>> response
    );
}
