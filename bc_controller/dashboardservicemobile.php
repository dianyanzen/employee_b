<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class dashboardservicemobile extends CI_Controller {

 function __construct()
    {
        parent:: __construct();
        $this->load->model('hr_time_off_request_model', 'mdl');
        $this->load->model('shared_model', 'sm');

 
    }
	public function index()
	{
		$this->load->view('welcome_message');
	}
	
	public function get_summary(){
		header("Content-Type: application/json");
        date_default_timezone_set('Asia/Jakarta');
        $employee_id = $this->input->get('employee_id', TRUE);
		
		$work_day_start = $this->work_day_start();

		$work_day_end = $this->work_day_end();

		$num_of_day_period = $this->num_of_day_period();
		$noreg = $this->get_noreg($employee_id);
		
		//$cnt_of_task_assign = $this->cnt_of_task_assign($noreg);
		
		$cnt_of_day_work_period = $this->cnt_of_day_work_period($employee_id, $work_day_start, $work_day_end);

		$cnt_of_leave_day_work_period = $this->cnt_of_leave_day_work_period($employee_id, $work_day_start, $work_day_end);

		$cnt_of_sick_day_work_period = $this->cnt_of_sick_day_work_period($employee_id, $work_day_start, $work_day_end);

		$cnt_ot_hour_period = $this->cnt_ot_hour_period($employee_id, $work_day_start, $work_day_end);
		//$cnt_reimburse_amount_period = $this->cnt_reimburse_amount_period($employee_id, $work_day_start, $work_day_end);
		
		
		//$cnt_remaining_days_off = $this->cnt_remaining_days_off($employee_id, $work_day_start, $work_day_end);
		
		$cnt_less_work_hour = $this->remain($employee_id, $work_day_start, $work_day_end);
		
		$ot_hour_period = isset($cnt_ot_hour_period) ? $cnt_ot_hour_period : 0;
		//$reimburse_amount_period = isset($cnt_reimburse_amount_period) ? $cnt_reimburse_amount_period : 0;
		$remaining_days_off = isset($cnt_remaining_days_off) ? $cnt_remaining_days_off : 0;
		
		
		$sql = "
			select 
				$num_of_day_period as num_of_day_period,
				$cnt_of_day_work_period as cnt_of_day_work_period,
				$cnt_of_leave_day_work_period as cnt_of_leave_day_work_period,
				$cnt_of_sick_day_work_period as cnt_of_sick_day_work_period,
				$ot_hour_period as cnt_ot_hour_period,
				$cnt_less_work_hour as cnt_less_work_hour
				
		";
		
		//$sql = "select * from tb_m_system";
		// echo $sql;
		// die;
		$data = $this->db->query($sql);
        echo json_encode($data->result());
		
	}
	function remain($employee_id){
        //$employee_id = $this->input->post('employee_id');
        $end_dt = $this->sm->get_data('leave_request', 'end_month_date')->value_txt;
        $start_dt = $this->sm->get_data('leave_request', 'start_month_date')->value_txt;
        $num_of_month  = $this->mdl->total_month($employee_id, $end_dt);

        if ($num_of_month < 0){
            $limit = 0;
        }else if ( $num_of_month >= 12 ){
            $limit = $this->sm->get_data("time_off_request", "limit" )->value_num;
        }else{
            $limit = $num_of_month;
        }
       
        if ($employee_id != ""){
            $leave_count = $this->mdl->count_time_off( $employee_id, $start_dt );
        }else{
            $leave_count = $limit;
        }
       
        $ext_leave = $this->mdl->get_ext_leave($employee_id);
        
        $limit = $limit + $ext_leave;
        
        if ($limit > 0){
            $remain = $limit - $leave_count;
        }else{
            $remain = $limit;
        }
        return $remain;
    }
	
	public function work_day_start(){
		$sql = "select system_value_txt as system_value_txt from tb_m_system where system_code = 'work_day_start' and system_type = 'config_others'";
		
		$data=$this->db->query($sql);
		return $data->row()->system_value_txt;
	}
	
	public function work_day_end(){
		$sql = "select system_value_txt as system_value_txt from tb_m_system where system_code = 'work_day_end' and system_type = 'config_others'";
		
		$data=$this->db->query($sql);
		return $data->row()->system_value_txt;
	}
	
	public function num_of_day_period(){
		$sql = "select system_value_txt as system_value_txt from tb_m_system where system_type = 'num_of_day_period'";
		
		$data=$this->db->query($sql);
		return $data->row()->system_value_txt;
	}
	
	public function cnt_of_day_work_period($employee_id, $work_day_start, $work_day_end){
		$sql = "
			select 
				count(*) as 'cnt_of_day_work_period' 
			from tb_r_attendance 
			where
				employee_id = '$employee_id' and date_format(clock_in, '%Y-%m-%d') between 
				concat(date_format(now(), '%Y-%m'), '-', '$work_day_start') 
				and concat(date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m'), '-', '$work_day_end')";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt_of_day_work_period;
	}
	
	public function cnt_of_leave_day_work_period($employee_id, $work_day_start, $work_day_end){
		$sql = "
			select 
				count(*) as 'cnt_of_leave_day_work_period' 
			from tb_r_schedule 
			where
				employee_id = '$employee_id' and schedule_type = 'LEAVE' and date_format(schedule_dt, '%Y-%m-%d') between 
				concat(date_format(now(), '%Y-%m'), '-', '$work_day_start') 
				and concat(date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m'), '-', '$work_day_end')";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt_of_leave_day_work_period;
	}
	
	public function cnt_of_sick_day_work_period($employee_id, $work_day_start, $work_day_end){
		$sql = "
			select 
				count(*) as 'cnt_of_sick_day_work_period' 
			from tb_r_schedule 
			where
				employee_id = '$employee_id' and schedule_type = 'LEAVE' and date_format(schedule_dt, '%Y-%m-%d') between 
				concat(date_format(now(), '%Y-%m'), '-', '$work_day_start') 
				and concat(date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m'), '-', '$work_day_end')";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt_of_sick_day_work_period;
	}
	
	public function cnt_ot_hour_period($employee_id, $work_day_start, $work_day_end){
		$sql = "
			select 
				sum(ot_hour) as cnt_ot_hour_period 
			from tb_r_overtime 
			where 
				employee_id = '$employee_id' and date_format(ot_dt, '%Y-%m-%d') between 
				concat(date_format(now(), '%Y-%m'), '-', '$work_day_start') 
				and concat(date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m'), '-', '$work_day_end')";
		/*echo $sql;
		die;*/
		$data=$this->db->query($sql);
		return $data->row()->cnt_ot_hour_period;
	}
	
	public function cnt_reimburse_amount_period($employee_id, $work_day_start, $work_day_end){
		
		
		$sql = "
			select 
				sum(reimburse_amount) as cnt_reimburse_amount_period 
			from tb_r_reimburse 
			where 
				employee_id = '$employee_id' and date_format(reimburse_dt, '%Y-%m-%d') between concat(date_format(now(), '%Y-%m'), '-', '$work_day_start') and concat(date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m'), '-', '$work_day_end')";
				
		
		$data=$this->db->query($sql);
		return $data->row()->cnt_reimburse_amount_period;
	}
	
	public function cnt_remaining_days_off($employee_id, $work_day_start, $work_day_end){
		$sql = "
			select 
				((select system_value_num from tb_m_system where system_type = 'time_off_request') - count(*)) as cnt_remaining_days_off 
			from tb_r_time_off 
			where 
			employee_id = '$employee_id' and date_format(time_off_dt, '%Y') = date_format(now(), '%Y')";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt_remaining_days_off;
	}
	
	public function get_noreg($employee_id){
		$sql = "
			select 
				no_reg 
			from tb_m_employee 
			where 
				employee_id = '$employee_id'";
		
		$data=$this->db->query($sql);
		return $data->row()->no_reg;
	}
	
	public function cnt_of_task_assign($noreg){
		$arka_pms_db = $this->load->database('default', TRUE);
	
		$sql = "
			select 
				count(*) as cnt 
			from tb_r_tasklist 
			where 
				member_cd = (select member_cd from tb_m_member where employee_cd = '$noreg')
				and task_type_cd in ('00', '01', '03')";
		
		$data = $arka_pms_db->query($sql);
		return $data->row()->cnt;
	}
	
	
}
?>