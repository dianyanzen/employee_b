<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class excuseservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}
/* excuse */
	
	public function cekexcuseapprovedsts($excuse_id){
		$sql = "select 1 from tb_m_excuse
		where excuse_id = '$excuse_id' and excuse_approved_by is null and excuse_approved_dt is null";
		

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   //$row=$data->row();
		   return true;
		}

		return false;
	}
	
	public function cekexcusedate($employee_id, $excuse_id){
		$sql = "select count(*) as cnt from tb_m_excuse where employee_id = '$employee_id' and excuse_id = '$excuse_id'";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt;
	}
	
    public function getexcuselist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $employee_id = $this->input->get('employee_id', true);
        $sql = "SELECT
        	 date_format(date_from,'%d') as excuse_prod_date
        	 , date_format(date_from,'%Y-%m') as excuse_prod_month
        	 , excuse_id, date_format(date_from,'%Y-%m-%d') as excuse_dt
        	 , excuse_type
        	 , CONCAT(SUBSTR(excuse_reason,1,12),'..') as excuse_reason
        	 , excuse_approved_dt, excuse_approved_by 
        	 , ( case when excuse_approved_by is null then '' else 'approved' end ) as excuse_status 
        	 from tb_m_excuse where
        	  employee_id='$employee_id'
				order by excuse_dt desc limit 30";
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
					a.excuse_cd, 
                    CONCAT(SUBSTR(a.reason,1,12),'..') as reason,
					a.flg_confirm ,
					(
				        CASE 
				            WHEN a.flg_confirm = 0
				            THEN ''
				            ELSE 'Approved'
				        END
				      ) AS complaint_status,
					b.excuse_nm
				FROM tb_r_complaint a INNER JOIN tb_m_excuse b
				on a.excuse_cd=b.excuse_cd
				where a.employee_cd='$employee_id'
				ORDER BY a.complaint_date DESC LIMIT 30";
        //echo($sql);
        $data = $this->db->query($sql);
        //print_r($data->result());
        echo json_encode($data->result());
        //return json_encode($data->result());
    }
    public function getexcusebyid() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $excuse_id = $this->input->get('excuse_id', true);

        $sql = "SELECT
        	 date_format(date_from,'%d') as excuse_prod_date
        	 , date_format(date_from,'%Y-%m') as excuse_prod_month
        	 , excuse_id, date_format(date_from,'%Y-%m-%d') as excuse_dt
        	 , excuse_id, date_format(date_to,'%Y-%m-%d') as excuse_todt
        	 , excuse_type
             , ( case when clock_in is null then '' else date_format(clock_in,'%H:%i') end ) as excuse_ci
             , ( case when clock_out is null then '' else date_format(clock_out,'%H:%i') end ) as excuse_co
             , ( case when shift_type is null then '' else shift_type end ) as excuse_shift_type
             , ( case when shift is null then '' else shift end ) as excuse_shift
             , ( case when tb_m_excuse.group is null then '' else tb_m_excuse.group end ) as excuse_group
        	 , CONCAT(SUBSTR(excuse_reason,1,12),'..') as excuse_reason
        	 , excuse_approved_dt
             , excuse_approved_by 
        	 , ( case when excuse_approved_by is null then '' else 'approved' end ) as excuse_status 
        	 from tb_m_excuse where
        	   excuse_id = '$excuse_id'
				order by excuse_dt desc limit 30";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->row());
    }
	
	public function saveexcuse()
	{
		$clock_in = '';
		$clock_out = NULL;
        $employee_id = $this->input->get('employee_id');
        $employee_name = $this->input->get('username');
        $excuse_id = $this->input->get('excuse_id');        
        $excuse_type = $this->input->get('excuse_type');
        $excuse_reason = $this->input->get('excuse_reason');        
        $shift_type = $this->input->get('shift_type');
        $shift = $this->input->get('shift');
        $date_from = $this->input->get('date_from');
        $date_to = $this->input->get('date_to');        
        $date_event = $this->input->get('date_event');        
        
        $group = $this->input->get('group');
//        echo $employee_id;
        if ($excuse_type == 'Change Clock-in'){
			$clock_in = $this->input->get('clock_in');
		}else{
			if ($excuse_type == 'Berita Acara Clock IN/OUT'){
				$clock_in = $this->input->get('clock_in');
				$clock_out = $this->input->get('clock_out');
			}
		}
        $data['employee_id']    = $employee_id;        
        $data['excuse_type']    = $excuse_type;
        $data['excuse_reason']  = $excuse_reason;        
        //$data['shift_type']   = $shift_type ? $shift_type : NULL;
        $data['shift']          = $shift ? $shift : NULL;
        
        $data['clock_in']       = $clock_in ? $clock_in : NULL;
        $data['clock_out']       = $clock_out ? $clock_out : NULL;
        $data['group']          = $group ? $group : NULL;
        if ($excuse_type == 'Berita Acara Clock IN/OUT'){
			$data['date_from']      = $date_event;
			$data['date_to']        = $date_event;    
		}else{
			$data['date_from']      = $date_from;
			$data['date_to']        = $date_to;    
		}		
        if ($excuse_type == 'Change Shift' && $shift_type == 'G2'){
            $data['shift'] = NULL;
            $data['group'] = $shift;
        }
        
        if ($excuse_type == 'Change Group'){
            $data['excuse_type'] = 'Change Shift';
            $data['shift_type'] = 'G2';
        }else{
            $data['shift_type'] = $shift_type ? $shift_type : NULL;
        }
        
        if ($excuse_id == "") {
            //insert 
            $data['created_by'] = $employee_name;
            $data['created_dt'] = date('Y-m-d H:i:s');

            $check_excuse = $this->check_excuse($employee_id, $data['excuse_type'], $date_from, $date_to);

            if ($check_excuse < 1) {
            	 try {
            	 $this->db->insert('tb_m_excuse', $data);
            	       return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Excuse telah disimpan.."
					)));
			} catch (Exception $e) {
               return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Data failed to save"
                                    )));
             
            }
            } else {
            	return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Data already exist"
                                    )));
                /*$ret['result'] = 0;
                $ret['message'] = "Data already exist";*/
            }
        } else {
            //update

            $data['changed_by'] = $employee_name;
            $data['changed_dt'] = date('Y-m-d H:i:s');

            $ret_ = $this->update($data, $excuse_id);

            if ($ret_) {
            	            	return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Data has been updated"
                                    )));
                /*$ret['result'] = 1;
                $ret['message'] = "Data has been updated";*/
            } else {
            	return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Update failed"
                                    )));
                /*$ret['result'] = 0;
                $ret['message'] = "Update failed";*/
            }
        }

        /*echo json_encode($ret);*/

	}
	
	public function deleteexcuse(){
		$excuse_id	= $this->input->get('excuse_id');
		$employee_id	= $this->input->get('employee_id');
		
		try {
			if($this->cekexcuseapprovedsts($excuse_id)){

				$sql= "delete from tb_m_excuse
					where excuse_id = '$excuse_id' and employee_id='$employee_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "excuse telah dihapus.."
	            )));
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "delete gagal, excuse sudah di approve.."
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
	 public function getexcusetype() {
        header("Content-Type: application/json");
        $sql = "SELECT system_code as excuse_type FROM tb_m_system where system_type = 'excuse_type'";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }

    public function getexcusegroup() {
        header("Content-Type: application/json");
        $sql = "SELECT system_code as excuse_group FROM tb_m_system  where system_type = 'work_group' order by system_code";
        /*echo($sql);
        die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }

    public function getexcuseshift() {
        header("Content-Type: application/json");
        $sql = "SELECT distinct shift_type FROM tb_m_excuse where shift_type is not null order by shift_type";
        /*echo($sql);
        die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
    function check_excuse($employee_id, $excuse_type, $date_from, $date_to) {
        
        //$st = "( (date_from BETWEEN '".$date_from."' and '".$date_to."') or (date_to BETWEEN '".$date_from."' and '".$date_to."') )";
        
        //if ($excuse_type != "Change Working Day") {
            $st = "( ('".$date_from."' BETWEEN date_from and date_to) or ('".$date_to."' BETWEEN date_from and date_to) )";
        //}
        
        $this->db->select('*');
        $this->db->from('tb_m_excuse');
        $this->db->where('employee_id', $employee_id);
        
        $this->db->where('excuse_type', $excuse_type);
        $this->db->where($st, NULL, FALSE); 
        $this->db->where('rejected_by is null',NULL,false);
        return $this->db->count_all_results();
//        $this->output->enable_profiler(TRUE);
    }
	function update($excuse, $excuse_id) {
        $this->db->where('excuse_id', $excuse_id);
        return $this->db->update('tb_m_excuse', $excuse);
    }
    /* End excuse */
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */