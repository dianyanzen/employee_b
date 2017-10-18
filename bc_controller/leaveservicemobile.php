<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class leaveservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}

	public function getleavelist() {
        //header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $employee_id = $this->input->get('employee_id', true);
        $sql = "SELECT
        	 date_format(time_off_dt,'%d') as leave_prod_date
        	 , date_format(time_off_dt,'%Y-%m') as leave_prod_month
        	 , time_off_id, date_format(time_off_dt,'%Y-%m-%d') as leave_dt
        	 , time_off_type
        	 , CONCAT(SUBSTR(time_off_description,1,12),'..') as leave_reason
        	 , time_off_approve_dt, time_off_approve_by 
        	 , ( case when time_off_approve_by is null then '' else 'approved' end ) as leave_status 
        	 from tb_r_time_off where
        	  employee_id='$employee_id'
				order by time_off_dt desc limit 30";
		
		$data = $this->db->query($sql);
        echo json_encode($data->result());
    }
	
	public function getleavebyid() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $time_off_id = $this->input->get('time_off_id', true);
        $sql = "SELECT
        	 date_format(time_off_dt,'%d') as leave_prod_date
        	 , date_format(time_off_dt,'%Y-%m') as leave_prod_month
        	 , time_off_id, date_format(time_off_dt,'%Y-%m-%d') as leave_dt
        	 , time_off_type
        	 , CONCAT(SUBSTR(time_off_description,1,12),'..') as leave_reason
        	 , time_off_approve_dt, time_off_approve_by 
        	 , ( case when time_off_approve_by is null then '' else 'approved' end ) as leave_status 
        	 from tb_r_time_off where
        	  time_off_id='$time_off_id'
				order by time_off_dt desc limit 30";
		
		$data = $this->db->query($sql);
        echo json_encode($data->result());
    }
	
	public function getleavetype() {
        header("Content-Type: application/json");
        $sql = "SELECT leave_type_cd as leave_id, leave_name as leave_type, quota FROM tb_m_leave_type order by leave_name asc ";
     
		$data = $this->db->query($sql);
        echo json_encode($data->result());
    }
	
	public function getemployees() {
        header("Content-Type: application/json");
        $sql = "SELECT 
						employee_id, user_name, working_unit, CONCAT_WS(' - ', no_reg, employee_name ) as employee_name,
						no_reg, gender FROM tb_m_employee where user_group != 'admin_department' and is_active = 1 order by employee_name asc ";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
	
	public function deleteleave(){
		$time_off_id	= $this->input->get('time_off_id');
		$employee_id	= $this->input->get('employee_id');
		
		try {
			if($this->cekleaveapprovedsts($time_off_id)){

				$sql= "delete from tb_r_time_off
					where time_off_id = '$time_off_id' and employee_id='$employee_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "leave telah dihapus.."
	            )));
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "delete gagal, leave sudah di approve.."
			            )));
			}
				
		} catch (exception $e) {
			return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => 'warning',
	                    'msgText' => $e->getmessage()
	            )));
		}
	}
	
	public function cekleaveapprovedsts($time_off_id){
		$sql = "select 1 from tb_r_time_off
		where time_off_id = '$time_off_id' and time_off_approve_by is null and time_off_approve_dt is null";
		

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   return true;
		}

		return false;
	}
	
	function saveleave()
    {
        $time_off_id = $this->input->post('time_off_id');        
        $date_from = $this->input->post('date_from');        
        $date_to = $this->input->post('date_to');        
        $employee_id = $this->input->post('employee_id');
        $employee_name = $this->input->post('username');
        $app_due_dt = $this->input->post('approval_due_dt');
        $time_off_type = $this->input->post('time_off_type');  
        $time_off_description = $this->input->post('time_off_description');
       
        $time_off = array
        (
            'employee_id'				=> $employee_id
            , 'time_off_type' 			=> $time_off_type
            , 'time_off_description' 	=> $time_off_description
            , 'changed_by'				=> $employee_name
            , 'changed_dt'				=> date('Y-m-d H:i:s')
            
        );

        if ($time_off_id == '' || $time_off_id == '0')
        {
            $cnt_workday = $this->count_workday($employee_id, date('Y-m-d'));
			
            $due_dt = date_create( date("Y-m-d H:i:s") );
            date_add($due_dt, date_interval_create_from_date_string( "7 days" ));
            $approval_due_dt = date_format($due_dt, "Y-m-d");
            
            $time_off['approval_due_dt'] = $approval_due_dt;
            
            $start_dt = $this>get_data('leave_request', 'start_month_date')->value_txt;
            $longDate = $this->db->query("SELECT CONVERT(SUBSTRING_INDEX(DATEDIFF('$dateTo', DATE_ADD('$dateFrom', INTERVAL -1 DAY)),'-',-1),UNSIGNED INTEGER) as total")->row()->total;
            $limit = $this->get_data("time_off_request", "limit" )->value_num;
            $cnt_time_off = $this->count_time_off($time_off["employee_id"], $start_dt );
            $ext_leave = $this->get_ext_leave($time_off["employee_id"]);
            
            $remain = $this->remain($employee_id);
            
            if ($remain < $longDate && $time_off['time_off_type'] == 'C'){
               
              return $this->output
                          ->set_content_type('application/json')
                          ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Saving failed, total leave request more then ".$limit. " day(s)")));
                
            }else{
                $check_data = $this->check_data($time_off['employee_id'], $dateFrom, $dateTo);

                if ($check_data==0){
                    $data = array();
                    $i = 1;
                    while ($i <= $longDate){
                        
                        $dt = $this->db->query("SELECT DATE_ADD(DATE_ADD('$dateFrom', INTERVAL -1 DAY), INTERVAL $i DAY) as addDate")->row()->addDate;
						
                        $check_schedule = $this->check_schedule($employee_id, $dt);
              	 
                            $time_off['created_by'] = $employee_name;
                            $time_off['created_dt'] = date('Y-m-d H:i:s');
                            $time_off['time_off_dt'] = $dt;
                            
                            $data[] = $time_off;
             			$i++;
                    }
			        
                    if ( count($data) > 0 ){
                        $result = $this->insert($data);
                    }else{
                        $result = 0;
                    }
                    
                    if ( $result == 1 ){
                        
                  		return $this->output
									->set_content_type('application/json')
									->set_output(json_encode(array(
										'msgType' => "info",
										'msgText' => "Leave request has been saved..")));
					
                    }else{
                        return $this->output
									->set_content_type('application/json')
									->set_output(json_encode(array(
												'msgType' => "warning",
												'msgText' => "Data failed to save")));
                    }
                }else{
                    return $this->output
									->set_content_type('application/json')
									->set_output(json_encode(array(
												'msgType' => "warning",
												'msgText' => "Data already exist")));
                }
            }
        } else {
            
           $this->update($time_off, $time_off_id);
           return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
												'msgType' => "warning",
												'msgText' => "Data has been updated")));
        }
        echo json_encode($ret);
    }
	
	function update($data, $id) {
        $this->db->where('time_off_id', $id);
        $this->db->update('tb_r_time_off', $data);
    }
	
	function insert($data) {
        return $this->db->insert_batch('tb_r_time_off', $data);
    }

	
	function check_schedule($employee_id, $date) {
        $sql = "select fn_emp_schedule('" . $employee_id . "','" . $date . "') as res";
        $ret = $this->db->query($sql)->row()->res;

        if ($ret == '-') {
            $sql = "call sp_emp_schedule('" . $employee_id . "', '" . $date . "')";
            $res = $this->db->query($sql);
            $_res = $res->result_array();

            $ret = $_res[0]['result'];

            $res->next_result();
            $res->free_result();
        }

        return $ret;
    }

	
	function check_data($employee_id, $dateFrom, $dateTo) {
        return $this->db->select('COUNT(*) as cnt')->from('tb_r_time_off')
                        ->where("employee_id = '" . $employee_id . "' AND time_off_dt >= '" . $dateFrom . "' AND time_off_dt <= '" . $dateTo . "'")
                        ->where('rejected_by IS NULL', null, false)
                        ->get()->row()->cnt;
    }
	
	function total_month($employee_id, $end_dt) {
        
        $end_period = ( date('Y') - 1 ).'-'.$end_dt;
        
        $sql = "select IFNULL ((SELECT 12 * (YEAR( '".$end_period."' ) - YEAR(start_working_dt)) + (MONTH( '".$end_period."' ) - MONTH(start_working_dt)) AS months 
                from tb_m_employee
                where employee_id = '" . $employee_id . "' ),0) as months";
        
        $result = $this->db->query($sql)->row()->months;
        
        return ($result ? $result : 0);
    }
	
	function remain($employee_id){
        $end_dt = $this->get_data('leave_request', 'end_month_date')->value_txt;
        $start_dt = $this->get_data('leave_request', 'start_month_date')->value_txt;
        $num_of_month  = $this->total_month($employee_id, $end_dt);

        if ($num_of_month < 0){
            $limit = 0;
        }else if ( $num_of_month >= 12 ){
            $limit = $this->get_data("time_off_request", "limit" )->value_num;
        }else{
            $limit = $num_of_month;
        }
       
        if ($employee_id != ""){
            $leave_count = $this->count_time_off( $employee_id, $start_dt );
        }else{
            $leave_count = $limit;
        }
       
        $ext_leave = $this->get_ext_leave($employee_id);
        
        $limit = $limit + $ext_leave;
        
        if ($limit > 0){
            $remain = $limit - $leave_count;
        }else{
            $remain = $limit;
        }
        return $remain;
    }
	
	function count_workday($employee_id, $date) {

        $sql = "call sp_count_workday('" . $employee_id . "', '" . $date . "')";
        $res = $this->db->query($sql);
        $_res = $res->result_array();

        $ret = $_res[0]['result'];

        $res->next_result();
        $res->free_result();

        return $ret;
    }
	
	 function get_data($system_type, $system_code) {
        $this->db->select('system_value_txt as value_txt, system_value_num as value_num, system_value_time as value_time');
        $this->db->from('tb_m_system');
        if ($system_type != "") {
            $this->db->where('system_type', $system_type);
        }
        if ($system_code != "") {
            $this->db->where('system_code', $system_code);
        }
        $query = $this->db->get();

        if ($query->num_rows() > 0) {
            return $query->row();
        } else {
            $row = new stdClass();
            $row->value_txt = '';
            $row->value_num = '';
            $row->value_time = '';
            return $row;
        }
    }
	
	function count_time_off($id, $start_dt) {

      $sql = "SELECT 
                    sum( 
                        case when time_off_type = 'C' or time_off_type = 'C1' then 1
                            when time_off_type = 'C2' then 0.5
                            else 0
                        end 
                    )as cnt 
                FROM tb_r_time_off  
                where employee_id = '" . $id . "' 
                and ( rejected_by is null or rejected_by = '')
                and time_off_type IN ('C', 'C1',  'C2')
                and time_off_dt 
                BETWEEN DATE_ADD( CONCAT( CAST(year(CURDATE()) as char(4)), '-','" . $start_dt . "'), INTERVAL -1 year) 
                AND CONCAT( CAST(year(CURDATE()) as char(4)), '-', '" . $start_dt . "') ";
		
	
        return $this->db->query($sql)->row()->cnt;
    }
	
	
	function get_ext_leave($id) {
        $this->db->select('ext_leave');
        $this->db->from('tb_m_employee');
        $this->db->where('employee_id', $id);
        
        $query = $this->db->get();

        if ($query->num_rows() > 0) {
			$result = $query->row()->ext_leave ? $query->row()->ext_leave : 0;
            return $result;
        } else {
            $row = new stdClass();
            $row->ext_leave = 100;
            return $row;
        }
    }
	
	
	
}