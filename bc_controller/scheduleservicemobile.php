<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class scheduleservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}
/* schedule */
	
	public function cekscheduleapprovedsts($schedule_id){
		$sql = "select 1 from tb_r_schedule
		where schedule_id = '$schedule_id' and schedule_approved_by is null and schedule_approved_dt is null";
		

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   //$row=$data->row();
		   return true;
		}

		return false;
	}
	
	public function cekscheduledate($employee_id, $schedule_id){
		$sql = "select count(*) as cnt from tb_r_schedule where employee_id = '$employee_id' and schedule_id = '$schedule_id'";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt;
	}
	
    public function getschedulelist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $employee_id = $this->input->get('employee_id', true);
        $sql = "select 
                schedule_id
                , employee_id
                , date_format(schedule_dt,'%d') as schedule_prod_date
                , date_format(schedule_dt,'%Y-%m') as schedule_prod_month
                , date_format(schedule_dt,'%Y-%m-%d') as schedule_dt
                , ( case when schedule_type = 'ONSITE' then 'DINAS LUAR' when schedule_type = 'VISIT' then 'DINAS LUAR' else schedule_type end ) as schedule_type
                , schedule_description
                , ( case when schedule_approved_dt is not null  then 'approved' when rejected_dt is not null then 'rejected' else '' end ) as schedule_status
                , date_format(approval_due_dt,'%Y-%m-%d') as approval_due_dt
                from tb_r_schedule where
        	  employee_id='$employee_id'
				order by schedule_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
 
 public function get_complaint_list() {
        header("Content-Type: application/json");
        date_default_timezone_set('Asia/Jakarta');
        $employee_id = $this->input->get('employee_id', TRUE);

        $sql = "SELECT 
					DATE_FORMAT(a.complaint_date,'%d') as complaint_prod_date,
					DATE_FORMAT(a.complaint_date,'%Y-%m') as complaint_prod_month,
					a.complaint_no,
					DATE_FORMAT(a.complaint_date,'%Y-%m-%d') as complaint_dt,
					a.schedule_cd, 
                    CONCAT(SUBSTR(a.reason,1,12),'..') as reason,
					a.flg_confirm ,
					(
				        CASE 
				            WHEN a.flg_confirm = 0
				            THEN ''
				            ELSE 'Approved'
				        END
				      ) AS complaint_status,
					b.schedule_nm
				FROM tb_r_complaint a INNER JOIN tb_r_schedule b
				on a.schedule_cd=b.schedule_cd
				where a.employee_cd='$employee_id'
				ORDER BY a.complaint_date DESC LIMIT 30";
        //echo($sql);
        $data = $this->db->query($sql);
        //print_r($data->result());
        echo json_encode($data->result());
        //return json_encode($data->result());
    }
    public function getschedulebyid() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $schedule_id = $this->input->get('schedule_id', true);

        $sql = "select 
                schedule_id
                , employee_id
                , date_format(schedule_dt,'%d') as schedule_prod_date
                , date_format(schedule_dt,'%Y-%m') as schedule_prod_month
                , date_format(schedule_dt,'%Y-%m-%d') as schedule_dt
                , ( case when schedule_type = 'ONSITE' then 'DINAS LUAR' when schedule_type = 'VISIT' then 'DINAS LUAR' else schedule_type end ) as schedule_type
                , schedule_description
                , ( case when schedule_approved_dt is not null  then 'approved' when rejected_dt is not null then 'rejected' else '' end ) as schedule_status
                , date_format(approval_due_dt,'%Y-%m-%d') as approval_due_dt
                from tb_r_schedule where
        	    schedule_id = '$schedule_id'
				order by schedule_dt desc";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->row());
    }
	
	public function saveschedule()
	{
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $this->load->model('hr_schedule_model', 'mdl');
		$schedule_id = $this->input->get('schedule_id');
        $employee_id = $this->input->get('employee_id');
        $employee_name = $this->input->get('username');
        $schedule_type = strtoupper($this->input->get('schedule_type'));
        if ($schedule_type == "DINAS LUAR"){
            $schedule_type = "ONSITE";
        }
        $schedule_description = $this->input->get('schedule_descript');
        $dateFrom = $this->input->get('schedule_from_date');
        $dateTo = $this->input->get('schedule_to_date');
        $app_due_dt = $this->input->get('schedule_due_date');
        $approval_due_dt = "";
        $cnt_workday = $this->count_workday($employee_id, date('Y-m-d'));
        if ($app_due_dt != "") {
            $approval_due_dt = $app_due_dt;
        } else {
            $due_dt = date_create(date("Y-m-d H:i:s"));
            date_add($due_dt, date_interval_create_from_date_string( "7 days" ));
            $approval_due_dt = date_format($due_dt, "Y-m-d");
        }

        $data['approval_due_dt'] = $approval_due_dt;

        $ret = array();

        if ($schedule_id == '' || $schedule_id == '0') {

            $longDate = "";
            $longDate = $this->db->query("SELECT CONVERT(SUBSTRING_INDEX(DATEDIFF('$dateTo', DATE_ADD('$dateFrom', INTERVAL -1 DAY)),'-',-1),UNSIGNED INTEGER) as total")->row()->total;

            $a = 1;

            while ($a <= $longDate) {
                $addDate = $this->db->query("SELECT DATE_ADD(DATE_ADD('$dateFrom', INTERVAL -1 DAY), INTERVAL $a DAY) as addDate")->row()->addDate;

                $schedule = array
                    (
                    'schedule_dt' => $addDate
                    , 'employee_id' => $employee_id
                    , 'schedule_type' => $schedule_type
                    , 'schedule_description' => $schedule_description
                    , 'schedule_dt' => $addDate
                    , 'approval_due_dt' => $approval_due_dt
                    , 'created_by' => $employee_name
                    , 'created_dt' => date('Y-m-d H:i:s')
                    , 'changed_by' => $employee_name
                );

                $check_schedule_dt = $this->check_activity($employee_id, $addDate);

                if ($check_schedule_dt == 0) {
                    $result = $this->insert($schedule);

                    if ($result == null) {
                        // sent email ke atasan
                        return $this->output
                            ->set_content_type('application/json')
                            ->set_output(json_encode(array(
                            'msgType' => "info",
                            'msgText' => "Schedule telah disimpan.."
                    )));
                            
                       
                    } else {
                        return $this->output
                            ->set_content_type('application/json')
                            ->set_output(json_encode(array(
                            'msgType' => "warning",
                            'msgText' => "Schedule gagal disimpan.."
                    )));
                    }
                } else {
                        return $this->output
                            ->set_content_type('application/json')
                            ->set_output(json_encode(array(
                            'msgType' => "warning",
                            'msgText' => "Schedule sudah ada.."
                    )));
                    break;
                }
                $a++;
            }
        } else {
            $schedule = array
                (
                'schedule_dt' => $dateFrom
                , 'schedule_type' => $schedule_type
                , 'schedule_description' => $schedule_description
                , 'approval_due_dt' => $approval_due_dt
                , 'changed_by' => $employee_name
            );

            $res = $this->update($schedule, $schedule_id);

            if ($res == null) {
                return $this->output
                        ->set_content_type('application/json')
                        ->set_output(json_encode(array(
                            'msgType' => "warning",
                            'msgText' => "Schedule gagal diupdate.."
                    )));
            } else {
               return $this->output
                        ->set_content_type('application/json')
                        ->set_output(json_encode(array(
                               'msgType' => "info",
                            'msgText' => "Schedule telah diupdate.."
                    )));
            }
        }


	}
	
	public function deleteschedule(){
		$schedule_id	= $this->input->get('schedule_id');
		$employee_id	= $this->input->get('employee_id');
		
		try {
			if($this->cekscheduleapprovedsts($schedule_id)){

				$sql= "delete from tb_r_schedule
					where schedule_id = '$schedule_id' and employee_id='$employee_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "schedule telah dihapus.."
	            )));
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "delete gagal, schedule sudah di approve.."
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
	 public function getscheduletype() {
        header("Content-Type: application/json");
        $sql = "SELECT system_code as schedule_type FROM tb_m_system where system_type = 'schedule_type'";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }

    public function getschedulegroup() {
        header("Content-Type: application/json");
        $sql = "SELECT system_code as schedule_group FROM tb_m_system  where system_type = 'work_group' order by system_code";
        /*echo($sql);
        die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }

    public function getscheduleshift() {
        header("Content-Type: application/json");
        $sql = "SELECT distinct shift_type FROM tb_r_schedule where shift_type is not null order by shift_type";
        /*echo($sql);
        die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
    function check_schedule($employee_id, $schedule_type, $date_from, $date_to) {
        
        //$st = "( (date_from BETWEEN '".$date_from."' and '".$date_to."') or (date_to BETWEEN '".$date_from."' and '".$date_to."') )";
        
        //if ($schedule_type != "Change Working Day") {
            $st = "( ('".$date_from."' BETWEEN date_from and date_to) or ('".$date_to."' BETWEEN date_from and date_to) )";
        //}
        
        $this->db->select('*');
        $this->db->from('tb_r_schedule');
        $this->db->where('employee_id', $employee_id);
        
        $this->db->where('schedule_type', $schedule_type);
        $this->db->where($st, NULL, FALSE); 
        $this->db->where('rejected_by is null',NULL,false);
        return $this->db->count_all_results();
//        $this->output->enable_profiler(TRUE);
    }
	function update($schedule, $schedule_id) {
        $this->db->where('schedule_id', $schedule_id);
        return $this->db->update('tb_r_schedule', $schedule);
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
    function insert($schedule) {
        $this->db->insert('tb_r_schedule', $schedule);
    }
    function check_activity($employee_id, $dt) {
        $this->db->select('count(schedule_id) as cnt');
        $this->db->from('tb_r_schedule');
        $this->db->where('employee_id', $employee_id);
        $this->db->where('schedule_dt', $dt);

        return $this->db->get()->row()->cnt;
    }

    /* End schedule */
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */