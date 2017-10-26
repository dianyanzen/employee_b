<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class mobile_downloadsalary extends CI_Controller {
 
 function __construct() {
        parent:: __construct();
        $this->load->model('shared_model', 'sm');
    }

	public function index()
		{
			$this->load->view('welcome_message');
		}
	public function getpdf()
	{
		header("content-type: application/json");
		$employee_id= $this->input->get('employee_id');
		$sql = "
			SELECT
			 	b.employee_name
			 	,a.*
			 FROM 
			 	tb_m_salary_attachment a 
			 left join
			 	tb_m_employee b 
			 on
			 	a.employee_id = b.employee_id
			 where 
			 	a.employee_id ='$employee_id' 
			 order by 
			 	a.period desc;
		";
		/*$sql = "
			SELECT
			 	b.employee_name
			 	,a.*
			 FROM 
			 	tb_m_salary_attachment a 
			 left join
			 	tb_m_employee b 
			 on
			 	a.employee_id = b.employee_id
			 order by 
			 	a.period desc;
		";*/
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}

}
?>