package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.DashboardSummary;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by user on 4/21/2016.
 */
public interface APIDashboard {
    @GET("/dashboardservicemobile/get_summary")
    void getSummaryList(
            @Query("employee_id") String employee_id, Callback<List<DashboardSummary>> response
    );
}
