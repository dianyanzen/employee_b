<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class dailyreportservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}
	
	public function get_project_list(){
        date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					application_cd as id, application_nm as name
				from 
					tb_m_application";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
	}
	
	public function get_function_list(){
        date_default_timezone_set('asia/jakarta');
		$application_cd = $this->input->get('application_cd');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					function_cd as id, function_nm as name
				from 
					tb_m_function
				where
					application_cd = '$application_cd'";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
	}
	
	public function get_category_list(){
        date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					system_code as id, system_value_txt as name
				from 
					tb_m_system
				where
					system_type = 'TASK_CAT'";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
	}
	
	public function get_status_list(){
        date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					task_type_cd as id, task_type_nm as name
				from 
					tb_m_task_type";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
	}
	
	public function get_phase_list(){
        date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					system_code as id, system_value_txt as name
				from 
					tb_m_system
				where
					system_type = 'PHASE'";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
	}
	
	function getFunctionName($application_cd, $item_function){
		date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					function_nm
				from 
					tb_m_function
				where
					application_cd = '$application_cd'
					and function_cd = '$item_function'";
					
        $data = $arka_pms_db->query($sql);
		return $data->row()->function_nm;
	}
	
	function getNoReg($employee_id){
		date_default_timezone_set('asia/jakarta');
		
		$sql = "select 
					no_reg
				from 
					tb_m_employee
				where
					employee_id = '$employee_id'";
					
        $data = $this->db->query($sql);
		return $data->row()->no_reg;
	}
	
	function getEmail($employee_id){
		date_default_timezone_set('asia/jakarta');
		
		$sql = "select 
					work_email
				from 
					tb_m_employee
				where
					employee_id = '$employee_id'";
					
        $data = $this->db->query($sql);
		return $data->row()->work_email;
	}
	
	function getMemberName($no_reg){
		date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					member_cd,
					member_nm
				from 
					tb_m_member
				where
					employee_cd = '$no_reg'";
					
        $data = $arka_pms_db->query($sql);
		return $data->result();
	}
	
	function getStatusName($item_status){
		date_default_timezone_set('asia/jakarta');
		
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					task_type_nm
				from 
					tb_m_task_type
				where
					task_type_cd = '$item_status'";
					
        $data = $arka_pms_db->query($sql);
		return $data->row()->task_type_nm;
	}
	
	function getRegNo($item_project) {
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
	
        $sql = "call sp_autonumbering('" . $item_project . "' ,'" . $item_project . "' ,4,'D',@runner,@ratency);";
        $arka_pms_db->query($sql);
		
        $data = $arka_pms_db->query("select CONCAT('" . $item_project . "',@ratency,@runner) as reg_no")->row();
        return $data->reg_no;
    }
	
	function savedailyreporttasklist($tasklist){
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$arka_pms_db->insert('tb_r_tasklist', $tasklist);
	}
	
	function savedailyreporthandover($handover){
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$arka_pms_db->insert('tb_r_task_handover', $handover);
	}
	
	function updatedailyreporttasklist($tasklist, $regno, $item_project){
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$arka_pms_db->where('reg_no', $regno);
		$arka_pms_db->where('application_cd', $item_project);
		$arka_pms_db->update('tb_r_tasklist', $tasklist);
	}
	
	function deletedailyreporttasklist($regno, $item_project){
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$arka_pms_db->where('reg_no', $regno);
		$arka_pms_db->where('application_cd', $item_project);
		$arka_pms_db->delete('tb_r_tasklist');
	}
	
	function deletedailyreporthandover($regno){
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$arka_pms_db->where('reg_no', $regno);
		$arka_pms_db->delete('tb_r_task_handover');
	}
	
	function solvedailyreporttasklist($tasklist, $bugs_id, $item_project){
		$arka_pms_db= $this->load->database('arka_pms', TRUE);
		
		$arka_pms_db->where('reg_no', $bugs_id);
		$arka_pms_db->where('application_cd', $item_project);
		$arka_pms_db->update('tb_r_tasklist', $tasklist);
	}
	
	function check_data_solved($bugs_id, $item_project){
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					task_type_cd
				from 
					tb_r_tasklist
				where
					reg_no = '$bugs_id' and
					application_cd = '$item_project'";
					
        $data = $arka_pms_db->query($sql);
		return $data->row()->task_type_cd;
	}
	
	public function savedailyreport(){
        date_default_timezone_set('asia/jakarta');
		
		$bugs_id = $this->input->get('bugs_id');
		$task = $this->input->get('task');
		$created_date = $this->input->get('created_date');
		$due_date = $this->input->get('due_date');
		$item_project = $this->input->get('item_project');
		$item_function = $this->input->get('item_function');
		$item_phase = $this->input->get('item_phase');
		$item_category = $this->input->get('item_category');
		$item_status = $this->input->get('item_status');
		$mode = $this->input->get('mode');
		$employee_id	= $this->input->get('employee_id');
		$countermesure = $this->input->get('countermesure');
		
		$regno = $this->getRegNo($item_project);
		$function_nm = $this->getFunctionName($item_project, $item_function);
		$NoReg = $this->getNoReg($employee_id);
		$member_name = $this->getMemberName($NoReg);
		$name_member;
		$cd_member;
		foreach($member_name as $member){
			$name_member = $member->member_nm;
			$cd_member = $member->member_cd;
		}
		$task_type_nm = $this->getStatusName($item_status);
		
		$createdDate = strtotime($created_date);
		$dueDate = strtotime($due_date);
		if($mode != "Edit"){
			$tasklist = array(
				'reg_no' => $regno,
				'application_cd' => $item_project,
				'function_cd' => $item_function,
				'function_nm' => $function_nm,
				'issue_description' => $task,
				'due_date' => date("Y-m-d H:i:s", $dueDate),//$due_date,
				'member_cd' => $cd_member,
				'member_name' => $name_member,
				'task_type_cd' => $item_status,
				'task_type_nm' => $task_type_nm,
				'task_cat_cd' => $item_category,
				'phase_cd' => $item_phase,
				'remark' => $countermesure,
				'created_by' => $NoReg,
				'created_dt' => date("Y-m-d H:i:s", $createdDate),
				'changed_by' => $NoReg,
				'changed_dt' => date("Y-m-d H:i:s", $createdDate)
			);
			
			$this->savedailyreporttasklist($tasklist);
			$handover = array(
				'reg_no' => $regno,
				'due_date' => date("Y-m-d H:i:s", $dueDate),
				'member_cd' => $cd_member,
				'task_type_cd' => $item_status,
				'phase_cd' => $item_phase,
				'remark' => $task,
				'created_by' => $NoReg,
				'created_dt' => date("Y-m-d H:i:s"),
				'changed_by' => $NoReg,
				'changed_dt' => date("Y-m-d H:i:s")
			);
			
			$this->savedailyreporthandover($handover);
			
			return $this->output->set_content_type('application/json')->set_output(json_encode(array(
					'msgType' => "info", 'msgText' => "tasklist telah disimpan.."
			)));
		}else{
			$tasklist = array(
				'function_cd' => $item_function,
				'function_nm' => $function_nm,
				'issue_description' => $task,
				'due_date' => date("Y-m-d H:i:s", $dueDate),
				'member_cd' => $cd_member,
				'member_name' => $name_member,
				'task_type_cd' => $item_status,
				'task_type_nm' => $task_type_nm,
				'task_cat_cd' => $item_category,
				'phase_cd' => $item_phase,
				'changed_by' => $NoReg,
				'changed_dt' => date("Y-m-d H:i:s", $createdDate)
			);
			
			$this->updatedailyreporttasklist($tasklist, $bugs_id, $item_project);
			
			$handover = array(
				'reg_no' => $bugs_id,
				'due_date' => date("Y-m-d H:i:s", $dueDate),
				'member_cd' => $cd_member,
				'task_type_cd' => $item_status,
				'phase_cd' => $item_phase,
				'remark' => $task,
				'created_by' => $NoReg,
				'created_dt' => date("Y-m-d H:i:s"),
				'changed_by' => $NoReg,
				'changed_dt' => date("Y-m-d H:i:s")
			);
			
			$this->savedailyreporthandover($handover);
			
			return $this->output->set_content_type('application/json')->set_output(json_encode(array(
					'msgType' => "info", 'msgText' => "tasklist telah disimpan.."
			)));
		}
	}
	
	public function getdailyreportlist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		
        $employee_id = $this->input->get('employee_id', true);
		$NoReg = $this->getNoReg($employee_id);
		$member_name = $this->getMemberName($NoReg);
		$name_member;
		$cd_member;
		foreach($member_name as $member){
			$name_member = $member->member_nm;
			$cd_member = $member->member_cd;
		}
		
        $sql = "
			select 
				reg_no,
				application_cd, 
				function_cd,
				CONCAT(SUBSTR(function_nm,1,12),'..') as function_nm,
				issue_description,
				eviden,
				date_format(date_raised,'%Y-%m-%d') as date_raised,
				date_format(due_date,'%Y-%m-%d') as due_date,
				date_format(finish_date,'%Y-%m-%d') as finish_date,
				member_cd,
				member_name,
				task_type_cd,
				task_type_nm,
				task_cat_cd,
				priority_flg,
				remark,
				phase_cd,
				created_by,
				date_format(created_dt,'%Y-%m-%d') as created_dt,
				changed_by,
				date_format(changed_dt,'%Y-%m-%d') as changed_dt,
				date_format(created_dt,'%Y-%m') as month_dt,
				date_format(created_dt,'%d') as day_dt
			from 
				tb_r_tasklist 
			where
				created_by = $NoReg
				and task_type_cd in ('00', '01', '03')
			order by changed_dt desc
		";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
    }
	
	public function getdailyreportbyid(){
		header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		$bugs_id = $this->input->get('bugs_id', true);
        $employee_id = $this->input->get('employee_id', true);
		$NoReg = $this->getNoReg($employee_id);
		$member_name = $this->getMemberName($NoReg);
		$name_member;
		$cd_member;
		foreach($member_name as $member){
			$name_member = $member->member_nm;
			$cd_member = $member->member_cd;
		}
		
        $sql = "
			select 
				reg_no,
				tl.reg_no,
				tl.application_cd, 
				app.application_nm,
				tl.function_cd,
				tl.function_nm,
				tl.issue_description,
				tl.eviden,
				date_format(tl.date_raised,'%Y-%m-%d') as date_raised,
				date_format(tl.due_date,'%Y-%m-%d') as due_date,
				date_format(tl.finish_date,'%Y-%m-%d') as finish_date,
				tl.member_cd,
				tl.member_name,
				tl.task_type_cd,
				tl.task_type_nm,
				tl.task_cat_cd,
				sysc.system_value_txt as task_cat_nm,
				tl.priority_flg,
				tl.remark,
				tl.phase_cd,
				sysp.system_value_txt as phase_nm,
				tl.created_by,
				date_format(tl.created_dt,'%Y-%m-%d') as created_dt,
				tl.changed_by,
				date_format(tl.changed_dt,'%Y-%m-%d') as changed_dt,
				date_format(tl.created_dt,'%Y-%m') as month_dt,
				date_format(tl.created_dt,'%d') as day_dt
			from 
				tb_r_tasklist tl left join tb_m_application app
				on tl.application_cd = app.application_cd
				left join tb_m_system as sysp on
				tl.phase_cd = sysp.system_code
				left join tb_m_system as sysc on
				tl.task_cat_cd = sysc.system_code 
			where
				sysp.system_type = 'PHASE' and
				sysc.system_type = 'TASK_CAT' and	
				tl.created_by = $NoReg and
				tl.reg_no = '$bugs_id'
			order by tl.changed_dt desc
		";
        $data = $arka_pms_db->query($sql);
        echo json_encode($data->result());
	}
	
	public function deletedailyreport(){
		header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		$bugs_id = $this->input->get('bugs_id', true);
        $employee_id = $this->input->get('employee_id', true);
		$NoReg = $this->getNoReg($employee_id);
		$item_project = $this->input->get('item_project');
		
		$check_data_solved = $this->check_data_solved($bugs_id, $item_project);
		
		if($check_data_solved != "04"){
		
			$this->deletedailyreporttasklist($bugs_id, $item_project);
			$this->deletedailyreporthandover($bugs_id);
			
			return $this->output->set_content_type('application/json')->set_output(json_encode(array('msgType' => "info", 'msgText' => "Task list sudah dihapus"
			)));
			
		}else{
			return $this->output->set_content_type('application/json')->set_output(json_encode(array('msgType' => "info", 'msgText' => "Status task list : " . $check_data_solved . ", solved . Tidak boleh dihapus"
			)));
		}
		
	}
	
	public function solvedailyreport(){
		header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		$bugs_id = $this->input->get('bugs_id', true);
        $employee_id = $this->input->get('employee_id', true);
		$NoReg = $this->getNoReg($employee_id);
		$member_name = $this->getMemberName($NoReg);
		$name_member;
		$cd_member;
		foreach($member_name as $member){
			$name_member = $member->member_nm;
			$cd_member = $member->member_cd;
		}
		$item_project = $this->input->get('item_project');
		$item_phase = $this->input->get('item_phase');
		$dueDate = $this->input->get('dueDate');
		$countermesure = $this->input->get('countermesure');
		$task_type_nm = $this->getStatusName("04");
		
		$tasklist = array(
			'task_type_cd' => "04",
			'task_type_nm' => $task_type_nm,
			'finish_date' => date("Y-m-d H:i:s"),
			'remark' => $countermesure,
			'changed_by' => $NoReg,
			'changed_dt' => date("Y-m-d H:i:s")
		);
		
		$this->solvedailyreporttasklist($tasklist, $bugs_id, $item_project);
		
		$handover = array(
			'reg_no' => $bugs_id,
			'due_date' => date("Y-m-d H:i:s", $dueDate),
			'finish_date' => date("Y-m-d H:i:s"),
			'member_cd' => $cd_member,
			'task_type_cd' => "04",
			'phase_cd' => $item_phase,
			'remark' => $countermesure,
			'created_by' => $NoReg,
			'created_dt' => date("Y-m-d H:i:s"),
			'changed_by' => $NoReg,
			'changed_dt' => date("Y-m-d H:i:s")
		);
		
		$this->savedailyreporthandover($handover);
		
		return $this->output->set_content_type('application/json')->set_output(json_encode(array('msgType' => "info", 'msgText' => "Task list sudah solve"
		)));
	}
	
	public function getMsEmail(){
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
		
		$sql = "select 
					system_value_txt
				from 
					tb_m_system
				where
					system_type = 'EMAIL'";
					
        $data = $arka_pms_db->query($sql);
		return $data->row()->system_value_txt;
    }
	
	public function getdailyreport_send($NoReg, $type_already_done){
		header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
		$arka_pms_db = $this->load->database('arka_pms', TRUE);
	
		$sql = "
			select 
				reg_no,
				tl.reg_no,
				tl.application_cd, 
				app.application_nm,
				tl.function_cd,
				tl.function_nm,
				tl.issue_description,
				tl.eviden,
				date_format(tl.date_raised,'%Y-%m-%d') as date_raised,
				date_format(tl.due_date,'%Y-%m-%d') as due_date,
				date_format(tl.finish_date,'%Y-%m-%d') as finish_date,
				tl.member_cd,
				tl.member_name,
				tl.task_type_cd,
				tl.task_type_nm,
				tl.task_cat_cd,
				sysc.system_value_txt as task_cat_nm,
				tl.priority_flg,
				tl.remark,
				tl.phase_cd,
				sysp.system_value_txt as phase_nm,
				tl.created_by,
				date_format(tl.created_dt,'%Y-%m-%d') as created_dt,
				tl.changed_by,
				date_format(tl.changed_dt,'%Y-%m-%d') as changed_dt,
				date_format(tl.created_dt,'%Y-%m') as month_dt,
				date_format(tl.created_dt,'%d') as day_dt
			from 
				tb_r_tasklist tl left join tb_m_application app
				on tl.application_cd = app.application_cd
				left join tb_m_system as sysp on
				tl.phase_cd = sysp.system_code
				left join tb_m_system as sysc on
				tl.task_cat_cd = sysc.system_code 
			where
				sysp.system_type = 'PHASE' and
				sysc.system_type = 'TASK_CAT' and
				tl.created_by = '$NoReg' and
				(
					date_format(tl.created_dt,'%Y-%m-%d') = date_format(now(), '%Y-%m-%d') or 
					date_format(tl.changed_dt,'%Y-%m-%d') = date_format(now(), '%Y-%m-%d')
				)
				and tl.task_type_cd in ($type_already_done)
			order by tl.changed_dt desc
		";
        $data = $arka_pms_db->query($sql);
		return $data->result();
	}
	
	public function sent_report() {
        $this->load->library('email');
        $message = "";
        $cfg_email = $this->getMsEmail();
        $r = json_decode($cfg_email);
		
		//parameter untuk list data aleready done & next todo
		$send_to = $this->input->get('send_to');
		$send_date = $this->input->get('send_date');
		$send_cc = $this->input->get('CC');
		$send_note = $this->input->get('note');
		$employee_id = $this->input->get('employee_id', true);
		$NoReg = $this->getNoReg($employee_id);
		$member_name = $this->getMemberName($NoReg);
		$name_member;
		$cd_member;
		foreach($member_name as $member){
			$name_member = $member->member_nm;
			$cd_member = $member->member_cd;
		}
		
		$work_email = $this->getEmail($employee_id);
		
		$type_already_done = '"02","04"';
		$type_next_todo = '"00","01","03"';
        //end
		
        $dt_already_done = $this->getdailyreport_send($NoReg, $type_already_done);
        $dt_next_todo = $this->getdailyreport_send($NoReg, $type_next_todo);
        
        $data["done"] = $dt_already_done;
        $data["next"] = $dt_next_todo;
		
		$data["r"] = $r;
        $data["member_nm"] = $name_member;
        $data["cfg_email"] = $cfg_email;
        $data["now"] = $send_date; //date("Y-m-d H:i:s"); //$params["now"];
		$data["send_to"] = $send_to;
		$data["send_cc"] = $send_cc;
		$data["send_note"] = $send_note;
		$data["work_email"] = $work_email;
		
        $this->load->view('formatsenddailyreport', $data);
    }
	
	
}

?>