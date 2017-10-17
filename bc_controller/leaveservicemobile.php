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
        	 date_format(date_from,'%d') as leave_prod_date
        	 , date_format(date_from,'%Y-%m') as leave_prod_month
        	 , leave_id, date_format(date_from,'%Y-%m-%d') as leave_dt
        	 , leave_type
        	 , CONCAT(SUBSTR(leave_reason,1,12),'..') as leave_reason
        	 , leave_approved_dt, leave_approved_by 
        	 , ( case when leave_approved_by is null then '' else 'approved' end ) as leave_status 
        	 from tb_m_leave where
        	  employee_id='$employee_id'
				order by leave_dt desc limit 30";
				echo $sql;
				die;
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
}