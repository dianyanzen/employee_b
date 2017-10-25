package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.GetSalaryPdf;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by root on 25/10/17.
 */

public interface ApiSalary {
    @GET("/mobile_downloadsalary/getpdf")
    void getSlipGajiList(
            @Query("employee_id") String employee_id, Callback<List<GetSalaryPdf>> response
    );
}
