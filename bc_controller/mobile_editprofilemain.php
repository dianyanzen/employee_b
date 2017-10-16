<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class mobile_editprofilemain extends CI_Controller {
 
 public function index()
	{
		$this->load->view('welcome_message');
	}
	public function get_usermaindata(){
	header("content-type: application/json");
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select
			employee_id
			, ( case when user_name is null then '' else user_name end ) as user_name 
			, ( case when title is null then '' else title end ) as user_title
			, ( case when gender is null or gender = '' then 'Male' else gender end ) as user_gender 
			, ( case when born_dt is null then '' else born_dt end ) as user_born_dt
			, ( case when born_place is null then '' else born_place end ) as user_born_place
			, ( case when religion is null then '' else religion end ) as user_religion
			, ( case when married_status is null or married_status = '' then 'Single' else married_status  end ) as user_married_status 
			, ( case when married_since is null or married_status is null or married_status = '' or married_status = 'Single' then '' else married_since end ) as  user_married_since
		from 
			tb_m_employee
		 where
		 	 employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->row());	
	}
	public function get_useraddress(){
	header("content-type: application/json");
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select
			employee_id 
			, ( case when street is null then '' else street end ) as street
			, ( case when address is null then '' else address end ) as address
			, ( case when region is null then '' else region end ) as region
			, ( case when sub_district is null then '' else sub_district end ) as sub_district
			, ( case when province is null then '' else province end ) as province
			, ( case when country is null then '' else country end ) as country
			, ( case when postal_code is null then '' else postal_code end ) as postal_code
			, ( case when handphone1 is null then '' else handphone1 end ) as handphone1
			, ( case when handphone2 is null then '' else handphone2 end ) as handphone2
			, ( case when work_email is null then '' else work_email end ) as work_email
			, ( case when bank_account_number is null then '' else bank_account_number end ) as bank_account_number
			, ( case when closed_person_name is null then '' else closed_person_name end ) as closed_person_name
			, ( case when closed_person_phone is null then '' else closed_person_phone end ) as closed_person_phone 
		from 
			tb_m_employee
		 where
		 	 employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->row());	
	}
	public function get_userfamily(){
	header("content-type: application/json");
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
	header("content-type: application/json");
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
	header("content-type: application/json");
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select 
			card_id
			, employee_id
			, type_card
			, id_number
			, issued_dt
			, ( case when placed is null or placed = 'null' then '' else placed end ) as placed
			, expired_dt 
		from 
			tb_m_card
		 where
		 	 employee_id = '$employee_id'
		 order by card_id";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_usertax(){
	header("content-type: application/json");
	$employee_id= $this->input->get('employee_id');
	$sql = "
		select 
			employee_id
			, ( case when user_name is null then '' else user_name end ) as user_name
			, ( case when npwp_number is null then '' else npwp_number end ) as npwp_number
			, ( case when npwp_dt is null then '' else npwp_dt end ) as npwp_dt
			, ( case when marital is null then '' else marital end ) as marital
			, ( case when bpjs_kesehatan is null then '' else bpjs_kesehatan end ) as bpjs_kesehatan
			, ( case when bpjs_ketenagakerjaan is null then '' else bpjs_ketenagakerjaan end ) as bpjs_ketenagakerjaan
		from 
			tb_m_employee
		 where
		 	employee_id = '$employee_id'";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->row());	
	}
	public function get_userchangepswd(){
	$employee_id= $this->input->get('employee_id');
	$user_password= md5($this->input->get('user_password'));
	$password1= md5($this->input->get('password1'));
	$password2= md5($this->input->get('password2'));
	
	if ($this->input->get('user_password') == ''){
		return $this->output
                    ->set_content_type('application/json')
                    ->set_output(json_encode(array(
                        'msgType' => "warning",
                        'msgText' => "Pasword Salah Harus Di Isi Terlebih Dahulu"
               )));
	}else{
	$sql = "
		select 
			 employee_id	
		from 
			tb_m_employee
		 where
		 	 employee_id = '$employee_id'
		 	 and user_password = '$user_password'";
		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
			if ($this->input->get('password1') != '' && $this->input->get('password2') != ''){
		   			if ($this->input->get('password1') == $this->input->get('password2')){
						$sql = "
							update 
								tb_m_employee
							set
								user_password = '$password1'
							where
								employee_id = '$employee_id'
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