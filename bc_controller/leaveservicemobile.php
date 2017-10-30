<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class leaveservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}

	public function getleavelist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT a.time_off_id as leave_id
             , a.employee_id
             , c.employee_name
             , date_format(a.time_off_dt,'%d') as leave_prod_date
             , date_format(a.time_off_dt,'%Y-%m') as leave_prod_month
             , date_format(a.time_off_dt,'%Y-%m-%d') as leave_dt
             , b.leave_name as time_off_type
             , ( case when a.time_off_description is null then '..' when a.time_off_description = '' then '..' else a.time_off_description end ) as leave_reason
             , a.approval_due_dt
             , a.time_off_approve_dt
             , a.time_off_approve_by 
             , ( case when a.time_off_approve_by is not null then 'approved' when a.rejected_dt is not null then 'rejected' else '' end ) as leave_status 
            from tb_r_time_off a 
            left join
                tb_m_leave_type b 
                on a.time_off_type = b.leave_type_cd
            left join 
            tb_m_employee c
                on a.employee_id = c.employee_id
            where
              a.created_dt >= '$pastdate' and 
             a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY)
            order by a.time_off_dt desc";
		/*echo $sql;
        die;*/
		$data = $this->db->query($sql);
        echo json_encode($data->result());
        }else{
             if ($user_name != '' ||  $user_name != null){
        $sql = "SELECT a.time_off_id as leave_id
             , a.employee_id
             , c.employee_name
             , date_format(a.time_off_dt,'%d') as leave_prod_date
             , date_format(a.time_off_dt,'%Y-%m') as leave_prod_month
             , date_format(a.time_off_dt,'%Y-%m-%d') as leave_dt
             , b.leave_name as time_off_type
             , ( case when a.time_off_description is null then '..' when a.time_off_description = '' then '..' else a.time_off_description end ) as leave_reason
             , a.approval_due_dt
             , a.time_off_approve_dt
             , a.time_off_approve_by 
             , ( case when a.time_off_approve_by is not null then 'approved' when a.rejected_dt is not null then 'rejected' else '' end ) as leave_status 
            from tb_r_time_off a 
            left join
                tb_m_leave_type b 
                on a.time_off_type = b.leave_type_cd
            left join 
            tb_m_employee c
                on a.employee_id = c.employee_id
            where
            c.user_name = '$user_name' or c.supervisor1 = '$user_name' or c.supervisor2 = '$user_name' 
            and
            a.created_dt >= '$pastdate' and 
            a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
            order by a.time_off_dt desc";
            // echo $sql;
            // die;
        $data = $this->db->query($sql);
       
        echo json_encode($data->result());
        }
        }
    }
    public function setaprove(){
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $time_off_id = $this->input->get('time_off_id');
        $employee_id = $this->input->get('employee_id');
        
        $sql ="select user_name
                , user_group 
            from tb_m_employee 
            where employee_id = '$employee_id'";
        
        $data = $this->db->query($sql);
        $approved_by = $data->row()->user_name;
        
        $sql ="select 
                a.supervisor1
                , a.supervisor2
                , b.spv_approved_dt
                , b.spv_approved_by 
                , b.mgr_approved_dt
                , b.mgr_approved_by
            from tb_m_employee a 
            inner join tb_r_time_off b 
            on a.employee_id = b.employee_id 
            where b.time_off_id = '$time_off_id'";
        
        $data = $this->db->query($sql);
        $aprove_mgr = $data->row()->mgr_approved_by;
        $aprove_spv = $data->row()->spv_approved_by;
        $approved_dt = date('Y-m-d H:i:s');
        if($aprove_spv == null || $aprove_spv == ''){
            $supervisorone = $data->row()->supervisor1;    
            $aprove_spv_dt = date('Y-m-d H:i:s');
            
        }else{
            $supervisorone = $aprove_spv;
            $aprove_spv_dt = $data->row()->spv_approved_dt;    
        }
        if($aprove_mgr == null || $aprove_mgr == ''){
            $supervisortwo = $data->row()->supervisor2;
            $aprove_mgr_dt = date('Y-m-d H:i:s');
        }else{
            $supervisortwo = $aprove_mgr;
            $aprove_mgr_dt = $data->row()->mgr_approved_dt;    
        }
        
        if ($approved_by == $supervisortwo){
            try {
                $sql = "
                UPDATE
                    tb_r_time_off
                set
                    spv_approved_dt = '$aprove_spv_dt'
                    , spv_approved_by = '$supervisorone'
                    , mgr_approved_dt = '$aprove_mgr_dt'
                    , mgr_approved_by = '$supervisortwo'
                    , time_off_approve_dt = '$approved_dt'
                    , time_off_approve_by = '$approved_by'
                where
                    time_off_id = '$time_off_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "leave has been Aproved"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to aprove leave"
                                    )));
            }
        }else  if ($approved_by == $supervisorone){
            try {
                $sql = "
                UPDATE
                    tb_r_time_off
                set
                     spv_approved_dt = '$aprove_spv_dt'
                    , spv_approved_by = '$supervisorone'
                where
                    time_off_id = '$time_off_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "leave has been Aproved"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to aprove leave"
                                    )));
            }  
        }else{
            try {
                $sql = "
                UPDATE
                    tb_r_time_off
                set
                     spv_approved_dt = '$aprove_spv_dt'
                    , spv_approved_by = '$supervisorone'
                    , mgr_approved_dt = '$aprove_mgr_dt'
                    , mgr_approved_by = '$supervisortwo'
                    , time_off_approve_dt = '$approved_dt'
                    , time_off_approve_by = '$approved_by'
                where
                    time_off_id = '$time_off_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "leave has been Aproved"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to aprove leave"
                                    )));
            }
        }
     }
     public function setreject(){
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $time_off_id = $this->input->get('time_off_id');
        $employee_id = $this->input->get('employee_id');
        $sql ="select user_name, user_group from tb_m_employee where employee_id = '$employee_id'";
        $data = $this->db->query($sql);
        $approved_by = $data->row()->user_name;
        $approved_dt = date('Y-m-d H:i:s');
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'HRD' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if (($user_group == 'employee' ||  $user_group == 'employee_app') && $position_name == 'hrd') {
            $position_name = 'employee';
        }
        $position_name = strtoupper($position_name);
             try {
                $sql = "
                UPDATE
                    tb_r_time_off
                set
                    rejected_dt = '$approved_dt'
                    , rejected_by = '$approved_by'
                    , rejected_position = '$position_name'
                where
                    time_off_id = '$time_off_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "leave has been Rejected"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to Rejected leave"
                                    )));
            }   
     }
    public function gettypevalue(){
            header("content-type: application/json");
            $sql ="select leave_type_cd,leave_name from tb_m_leave_type order by leave_type_cd;
            ";
            $data = $this->db->query($sql);
            echo json_encode($data->result());
          
    }
    public function getleavetypedescript(){
        //header("content-type: application/json");
            $sql ="
                SELECT 
                    system_value_txt as leave_type
                from
                    tb_m_system
                where
                    system_type ='time_off_request' and 
                    system_code = 'type'
            ";
            $data = $this->db->query($sql);
            $code = $data->row()->leave_type;
            $splitcode = explode(';',$code);
            $i =-1;
            $data = array();
            foreach ($splitcode as $rowsp) {
                $i++;    
                $data[$i] = $splitcode[$i];
            }
            $x=-1;
            foreach ($data as $rowdt) {
            $x++;
            list($first[$x], $last[$x]) = explode('~',$data[$x]);
            }
            return $last;
            //return $first;
    }
    public function getleavetype(){
        //header("content-type: application/json");
            $sql ="
                SELECT 
                    system_value_txt as leave_type
                from
                    tb_m_system
                where
                    system_type ='time_off_request' and 
                    system_code = 'type'
            ";
            $data = $this->db->query($sql);
            $code = $data->row()->leave_type;
            $splitcode = explode(';',$code);
            $i =-1;
            $data = array();
            foreach ($splitcode as $rowsp) {
                $i++;    
                $data[$i] = $splitcode[$i];
            }
            $x=-1;
            foreach ($data as $rowdt) {
            $x++;
            list($first[$x], $last[$x]) = explode('~',$data[$x]);
            }
            //return $last;
            return $first;
    }
    public function leavetype(){
        header("content-type: application/json");
        $last = $this->getleavetypedescript();
        echo json_encode($last);   
    }
    public function leavecd(){
        header("content-type: application/json");
         $first = $this->getleavetype();
        echo json_encode($first);   
    }
    public function convert_type($type){
        $last = $this->getleavetypedescript();
        $first = $this->getleavetype();
        $x=-1;
        foreach ($last as $lst) {
        $x++;
        if ($type == $last[$x]){
            $type_leave = $first[$x];
        }
        }
        return $type_leave;
    }

    public function reverse_convert_type($type){
     $last = $this->getleavetypedescript();
        $first = $this->getleavetype();
        $x=-1;
        foreach ($first as $frs) {
        $x++;
        if ($type == $first[$x]){
            $type_leave_descript = $last[$x];
        }
        }
        return $type_leave_descript;   
    }
	
	public function getleavebyid() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $time_off_id = $this->input->get('time_off_id', true);
        $sql = "SELECT a.time_off_id as leave_id
             , a.employee_id
             , c.employee_name
             , date_format(a.time_off_dt,'%d') as leave_prod_date
             , date_format(a.time_off_dt,'%Y-%m') as leave_prod_month
             , date_format(a.time_off_dt,'%Y-%m-%d') as leave_dt
             , b.leave_name as time_off_type
             , ( case when a.time_off_description is null then '..' when a.time_off_description = '' then '..' else a.time_off_description end ) as leave_reason
             , a.approval_due_dt
             , a.time_off_approve_dt
             , a.time_off_approve_by 
             , ( case when a.time_off_approve_by is not null then 'approved' when a.rejected_dt is not null then 'rejected' else '' end ) as leave_status 
            from tb_r_time_off a 
            left join
                tb_m_leave_type b 
                on a.time_off_type = b.leave_type_cd
            left join 
            tb_m_employee c
                on a.employee_id = c.employee_id
            where
              a.time_off_id='$time_off_id'
            order by a.time_off_dt desc";
		
		$data = $this->db->query($sql);
        echo json_encode($data->row());
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
        $time_off_id = $this->input->get('leave_id');        
        $date_from = $this->input->get('leave_from_date');        
        $date_to = $this->input->get('leave_to_date');        
        $employee_id = $this->input->get('employee_id');
        $employee_name = $this->input->get('username');
        $app_due_dt = $this->input->get('approval_due_dt');
        $time_off_type = $this->input->get('leave_type');
        if ($time_off_type != '' || $time_off_type == null){
            $sql ="SELECT 
                    leave_type_cd as type_cd 
                from
                    tb_m_leave_type
                where leave_name = '$time_off_type'
            ";

        $data = $this->db->query($sql);
        $time_off_type = $data->row()->type_cd;
        }
        $time_off_description = $this->input->get('leave_descript');
       
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
            
            $start_dt = $this->get_data('leave_request', 'start_month_date')->value_txt;
            $longDate = $this->db->query("SELECT CONVERT(SUBSTRING_INDEX(DATEDIFF('$date_to', DATE_ADD('$date_from', INTERVAL -1 DAY)),'-',-1),UNSIGNED INTEGER) as total")->row()->total;
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
                $check_data = $this->check_data($time_off['employee_id'], $date_from, $date_to);

                if ($check_data==0){
                    $data = array();
                    $i = 1;
                    while ($i <= $longDate){
                        
                        $dt = $this->db->query("SELECT DATE_ADD(DATE_ADD('$date_from', INTERVAL -1 DAY), INTERVAL $i DAY) as addDate")->row()->addDate;
						
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

	
	function check_data($employee_id, $date_from, $date_to) {
        return $this->db->select('COUNT(*) as cnt')->from('tb_r_time_off')
                        ->where("employee_id = '" . $employee_id . "' AND time_off_dt >= '" . $date_from . "' AND time_off_dt <= '" . $date_to . "'")
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