package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.GetScheduleActivityList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by root on 20/10/17.
 */

public interface ApiScheduleActivity {
    @GET("/scheduleservicemobile/getschedulelist")
    void getScheduleActivityList(
            @Query("employee_id") String employee_id, Callback<List<GetScheduleActivityList>> response
    );

    @GET("/scheduleservicemobile/getschedulebyid")
    void getScheduleActivityById(
            @Query("schedule_id") String ScheduleActivityId, Callback<GetScheduleActivityList> response
    );

    @GET("/scheduleservicemobile/deleteschedule")
    void onDeleteScheduleActivity(
            @Query("schedule_id") String schedule_id,
            @Query("employee_id") String employee_id,
            Callback<CheckLogin> response
    );

    //    @Multipart
    @POST("/scheduleservicemobile/saveschedule")
    void onSaveScheduleActivity(
            @Query("schedule_id") String schedule_id,
            @Query("employee_id") String employee_id,
            @Query("username") String username,
            @Query("schedule_type") String schedule_type,
            @Query("schedule_descript") String schedule_descript,
            @Query("schedule_from_date") String schedule_from_date,
            @Query("schedule_to_date") String schedule_to_date,
            @Query("schedule_due_date") String schedule_due_date,
            Callback<CheckLogin> response
    );
}
