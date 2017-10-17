package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.GetLeaveList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by root on 17/10/17.
 */

public interface ApiLeave {
    @GET("/leaveservicemobile/getleavelist")
    void getLeaveList(
            @Query("employee_id") String employee_id, Callback<List<GetLeaveList>> response
    );
}
