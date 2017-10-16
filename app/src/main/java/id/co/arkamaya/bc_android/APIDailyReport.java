package id.co.arkamaya.bc_android;

import java.util.List;

import pojo.CheckLogin;
import pojo.ComboboxCommon;
import pojo.GetListDailyReport;
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
 * Created by user on 4/26/2016.
 */
public interface APIDailyReport {
    @GET("/PMS03/PMS03101/get_project_list")
    void getProject_list(
            @Query("employee_id") String employee_id, Callback<List<ComboboxCommon>> response
    );

    @GET("/PMS03/PMS03101/get_function_list")
    void getFunction_list(
            @Query("application_cd") String application_Cd, Callback<List<ComboboxCommon>> response
    );

    @GET("/PMS03/PMS03101/get_category_list")
    void getCategory_list(
            @Query("employee_id") String employee_id, Callback<List<ComboboxCommon>> response
    );

    @GET("/PMS03/PMS03101/get_status_list")
    void getStatus_list(
            @Query("employee_id") String employee_id, Callback<List<ComboboxCommon>> response
    );

    @GET("/PMS03/PMS03101/get_phase_list")
    void getPhase_list(
            @Query("employee_id") String employee_id, Callback<List<ComboboxCommon>> response
    );

    @POST("/PMS03/PMS03101/savedailyreport")
    void onSaveDailyReport(
            @Query("bugs_id") String bugs_id,
            @Query("task") String task,
            @Query("created_date") String created_date,
            @Query("due_date") String due_date,
            @Query("item_project") String item_project,
            @Query("item_function") String item_function,
            @Query("item_phase") String item_phase,
            @Query("item_category") String item_category,
            @Query("item_status") String item_status,
            @Query("mode") String mode,
            @Query("countermesure") String countermesure,
            @Query("employee_id") String EmployeeId,
            Callback<CheckLogin> response
    );

    @GET("/PMS03/PMS03101/getdailyreportlist")
    void getDailyReport_list(
            @Query("employee_id") String employee_id, Callback<List<GetListDailyReport>> response
    );

    @GET("/PMS03/PMS03101/getdailyreport_already_done")
    void getDailyReport_AlreadyDone_list(
            @Query("employee_id") String employee_id, Callback<List<GetListDailyReport>> response
    );

    @GET("/PMS03/PMS03101/getdailyreport_next_todo")
    void getDailyReport_NextTodo_list(
            @Query("employee_id") String employee_id, Callback<List<GetListDailyReport>> response
    );

    @GET("/PMS03/PMS03101/getdailyreportbyid")
    void getDailyReportbyId(
            @Query("bugs_id") String bugs_id,
            @Query("employee_id") String employee_id,
            Callback<List<GetListDailyReport>> response
    );

    @POST("/PMS03/PMS03101/deletedailyreport")
    void onDeleteDailyReport(
            @Query("bugs_id") String bugs_id,
            @Query("item_project") String item_project,
            @Query("employee_id") String EmployeeId,
            Callback<CheckLogin> response
    );

    @POST("/PMS03/PMS03101/solvedailyreport")
    void onSolveDailyReport(
            @Query("bugs_id") String bugs_id,
            @Query("item_project") String item_project,
            @Query("item_phase") String item_phase,
            @Query("due_date") String due_date,
            @Query("countermesure") String countermesure,
            @Query("employee_id") String EmployeeId,
            Callback<CheckLogin> response
    );

    @POST("/PMS03/PMS03101/sent_report")
    void onSendDailyReport(
            @Query("send_to") String task,
            @Query("send_date") String created_date,
            @Query("mode") String mode,
            @Query("CC") String CC,
            @Query("note") String Note,
            @Query("employee_id") String EmployeeId,
            Callback<CheckLogin> response
    );

}
