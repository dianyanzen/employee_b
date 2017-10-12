<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class workingreportservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}

/* Working Report */
	public function time_off(){
		$sql = "select COALESCE(system_value_num,0) as system_value_num from tb_m_system where system_type = 'mobile_config' and system_code = 'time_off'";
		
		$data=$this->db->query($sql);
		if($data->num_rows()>0){
			return $data->row()->system_value_num;	
		}else{
			return 0;
		}

		
	}

	public function get_working_report_list() {
        header("Content-Type: application/json");
        date_default_timezone_set('Asia/Jakarta');
        $employee_id = $this->input->get('employee_id', TRUE);
		$data_month = $this->input->get('data_month', TRUE);
		$time_off = $this->time_off();
		/*$curr_dt = date('Y-m-d H:i:s');*/
		/*
        $sql = "SELECT 
					att.attendance_id,
					att.employee_id,
					date_format(att.clock_out, '%H:%i:%S') as clock_out,
					date_format(att.clock_in, '%H:%i:%S') as clock_in,
					att.work_hour,
					att.source,
					att.long_in,
					att.lat_in,
					att.reason,
					att.address,
					att.place,
					sch.schedule_dt,
					sch.schedule_type,
					sch.schedule_description,
					sch.schedule_project,
					sch.schedule_approve_by,
					sch.schedule_approve_dt
				FROM 
					tb_r_attendance as att left join tb_r_schedule as sch
					on att.employee_id = sch.employee_id
				WHERE 
					att.employee_id='8'
				ORDER BY 
					sch.schedule_dt DESC LIMIT 30";
		*/
		$sql = "
			SELECT 
				att.attendance_id,
				att.employee_id,
				date_format(att.clock_out, '%H:%i:%S') as clock_out,
				date_format(att.clock_in, '%H:%i:%S') as clock_in,
				att.work_hour,
				att.source,
				att.long_in,
				att.lat_in,
				att.reason,
				att.address,
				att.place,
				date_format(att.clock_out, '%Y-%m-%d') as schedule_dt,
				COALESCE(SEC_TO_TIME(((TIMESTAMPDIFF(minute, clock_in, clock_out) - $time_off) * 60)),0) AS Diff_Hour,
				COALESCE(CAST(SUBSTRING(SEC_TO_TIME(((TIMESTAMPDIFF(minute, clock_in, clock_out)) - $time_off) * 60), 1, 2) as UNSIGNED),0) AS Count_Hour
			FROM 
				tb_r_attendance as att
			WHERE 
				att.employee_id='$employee_id' ";
				
		if($data_month != null){
			$dataMonth_split = explode("-",$data_month); //$data_month.split("-");
			$dataYear = $dataMonth_split[0];
			
			if($dataMonth_split[1] < 10){
				$dataMonth = "0" . $dataMonth_split[1];
			}else{
				$dataMonth = $dataMonth_split[1];
			}
			
			$Month_Year = $dataYear . "-" . $dataMonth;
			
			$sql = $sql . " 
				AND date_format(att.clock_in, '%Y-%m-%d') between concat('$Month_Year', '-', '10') and date_add((concat('$Month_Year', '-', '10')), INTERVAL 1 month)
			";
		}else{
			$sql = $sql . "
				AND date_format(att.clock_in, '%Y-%m-%d') between concat((date_format(now(), '%Y-%m')), '-', '10') and concat((date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m')), '-', '11')
			";
		}
		
		$sql = $sql . "
			ORDER BY 
				date_format(att.clock_in, '%Y-%M-%d') DESC LIMIT 30
		";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
	
	public function get_total_wh_month() {
        header("Content-Type: application/json");
        date_default_timezone_set('Asia/Jakarta');
        $employee_id = $this->input->get('employee_id', TRUE);
		$data_month = $this->input->get('data_month', TRUE);
		
		$sql = "
			SELECT sec_to_time(sum(
				extract(hour from att.work_hour) * 3600
			  + extract(minute from att.work_hour) * 60
			  + extract(second from att.work_hour)
			  )) AS total_mv_time
			from tb_r_attendance att
			WHERE 
				att.employee_id = '$employee_id' and
			";
			
		if($data_month != null){
			$dataMonth_split = explode("-",$data_month); //$data_month.Split("-");
			$dataYear = $dataMonth_split[0];
			
			if($dataMonth_split[1] < 10){
				$dataMonth = "0" . $dataMonth_split[1];
			}else{
				$dataMonth = $dataMonth_split[1];
			}
			
			$Month_Year = $dataYear . "-" . $dataMonth;
			
			$sql = $sql . " 
				date_format(att.clock_in, '%Y-%m-%d') between concat('$Month_Year', '-', '10') and date_add((concat('$Month_Year', '-', '10')), INTERVAL 1 month)
			";
			
		}else{
			$sql = $sql . "
				date_format(att.clock_in, '%Y-%m-%d') between concat((date_format(now(), '%Y-%m')), '-', '10') and concat((date_format((date_add(now(), INTERVAL 1 month)), '%Y-%m')), '-', '11')
			";
		}
		
		$sql = $sql . "
			ORDER BY 
				date_format(att.clock_in, '%Y-%m-%d') DESC LIMIT 30;
		";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
	
	/* End Working Report */
}