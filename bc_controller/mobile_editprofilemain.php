<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class mobile_editprofilemain extends CI_Controller {
 
 public function index()
	{
		$this->load->view('welcome_message');
	}
	public function get_usermaindata(){
	$user_name= $this->input->get('username');
	$sql = "
		select
			employee_id 
			, title
			, gender
			, born_dt
			, born_place
			, religion
			, married_status
			, married_since
		from 
			tb_m_employee
		 where
		 	 user_name = '$user_name'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_useraddress(){
	$user_name= $this->input->get('username');
	$sql = "
		select
			employee_id 
			, street
			, address
			, region
			, sub_district
			, province
			, country
			, postal_code
			, handphone1
			, handphone2
			, work_email
			, bank_account_number
			, closed_person_name
			, closed_person_phone 
		from 
			tb_m_employee
		 where
		 	 user_name = '$user_name'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_userfamily(){
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select 
			family_id
			, employee_id
			, relationship
			, gender
			, fullname
			, born_dt
			, born_place
			, nationality
		from 
			tb_m_family
		 where
		 	 employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_usereducation(){
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select
			education_id
			, employee_id
			, level
			, institution
			, faculty
			, graduated_dt
			, gpa 
		from 
			tb_m_education 
		where
			employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_useridcard(){
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select 
			card_id
			, employee_id
			, type_card
			, id_number
			, issued_dt
			, placed
			, expired_dt 
		from 
			tb_m_card
		 where
		 	 employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_usertax(){
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select 
			employee_id
			, user_name
			, npwp_number
			, npwp_dt
			, marital
			, bpjs_kesehatan
			, bpjs_ketenagakerjaan
		from 
			tb_m_employee
		 where
		 	employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_userchangepswd(){
	$user_name= $this->input->get('username');
	$user_password= $this->input->get('user_password');
	$password1= md5($this->input->get('password1'));
	$password2= md5($this->input->get('password2'));
	
	$sql = "
		select 
			 employee_id	
		from 
			tb_m_employee
		 where
		 	 user_name = '$user_name'
		 	 and user_password = '$user_password'";
		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
			if ($password1 != '' && $password2 != ''){
		   			if ($password1 == $password2){
						$sql = "
							update 
								tb_m_employee
							set
								user_password = '$password'
							where
								user_name = '$user_name'
		 						and user_password = '$user_password'";		
						$this->db->query($sql);
						 return $this->output
									->set_content_type('application/json')
									->set_output(json_encode(array(
										'msgType' => "info",
										'msgText' => "Password Telah Diupdate.."
								)));
						}else{
							 return $this->output
            			             ->set_content_type('application/json')
            			             ->set_output(json_encode(array(
            			                 'msgType' => "warning",
            			                 'msgText' => "Pasword Tidak Sama.."
            			        )));
						}
					}else{
						return $this->output
            			        ->set_content_type('application/json')
            			        ->set_output(json_encode(array(
            			            'msgType' => "warning",
            			            'msgText' => "Pasword Tidak Boleh Kosong.."
            			   )));
					}
		}
		else
		{
			return $this->output
                    ->set_content_type('application/json')
                    ->set_output(json_encode(array(
                        'msgType' => "warning",
                        'msgText' => "Pasword Salah.."
               )));
		}
	}
	
	public function get_userdata(){
	// $data = "";
		$user_name= $this->input->get('username');
		$sql ="";
		$sql.= "SELECT 
			e.employee_id
			,e.user_name
			,e.no_reg
			,e.employee_name
			,e.start_working_dt
			,UNIX_TIMESTAMP(e.start_working_dt) as unix_dt
			,e.born_place
			,e.born_dt
			,e.address
			,e.handphone1
			,e.handphone2
			,ed.level as education_step
			,ed.institution as education_institution
			,ed.graduated_dt as education_graduated_year
			,e.closed_person_name
			,e.closed_person_phone
			,e.marital
			,e.npwp_number
			,e.photo
			,e.bank_account_number
			,e.position_id
			,e.role_id
			,e.bpjs_ketenagakerjaan
			,e.bpjs_kesehatan
			,p.position_name
			,r.role_name
			,e.work_email
			,b.role_name as band
			";
		$sql.= " FROM tb_m_employee e";
		$sql.= " LEFT JOIN tb_m_position p ON e.position_id = p.position_id";
		$sql.= " LEFT JOIN tb_m_role r ON e.role_id = r.role_id";
		$sql.= " LEFT JOIN tb_m_role b ON e.band_id = b.role_id";
		$sql.= " LEFT JOIN tb_m_education ed ON e.employee_id = ed.employee_id";
		$sql.= " where e.user_name  = '$user_name'";
		$sql.= " order by ed.graduated_dt DESC";
		$sql.= " LIMIT 1";
		/*$this->db->join('tb_m_position p', 'e.position_id = p.position_id', 'LEFT');
		$this->db->join('tb_m_role r', 'e.role_id = r.role_id', 'LEFT');
		$this->db->join('tb_m_role b', 'e.band_id = b.role_id', 'LEFT');
		$this->db->join('tb_m_education ed', 'e.employee_id = ed.employee_id', 'LEFT');
		$this->db->where('user_name', $user_name);
		$this->db->order_by('ed.graduated_dt', 'desc');
		$this->db->limit(1);*/
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	
	}