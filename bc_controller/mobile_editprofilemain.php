<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class mobile_editprofilemain extends CI_Controller {
 
 function __construct() {
        parent:: __construct();
        $this->load->model('shared_model', 'sm');
    }

public function index()
	{
		$this->load->view('welcome_message');
	}

public function get_usermaindata()
	{
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

public function get_main_religion() 
	{
		header("Content-Type: application/json");
       	$row = array();
        $result = $this->sm->get_data('config_others', 'religions') ? $this->sm->get_data('config_others', 'religions')->value_txt : "";
        $result = strtoupper($result);
        if ($result != "") {
            $data = explode(";", $result);
            for ($i = 0; $i < count($data); $i++) {
                $row[] = array(
                    'religions' => $data[$i]);
            }
        }
        echo json_encode($row);
	}
	

public function get_main_marriedstat() 
	{
		header("Content-Type: application/json");
       	$row = array();
        $result = $this->sm->get_data('config_others', 'married_status') ? $this->sm->get_data('config_others', 'married_status')->value_txt : "";
        // $result = strtoupper($result);
        if ($result != "") {
            $data = explode(";", $result);
            for ($i = 0; $i < count($data); $i++) {
                $row[] = array(
                    'married_status' => $data[$i]);
            }
        }
        echo json_encode($row);
	}

public function get_useraddress()
	{
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

public function on_addfamily()
	{
		$employee_id = $this->input->get('employee_id');
        $user_name = $this->input->get('user_name');
        $family_id = $this->input->get('family_id');        
        $family_relation = $this->input->get('family_relation');
        $family_gender = $this->input->get('family_gender');        
        $family_full_name = strtoupper($this->input->get('family_full_name'));
        $family_born_place = $this->input->get('family_born_place');
        $family_born_date = $this->input->get('family_born_date');
        $family_nationality = $this->input->get('family_nationality');        
if ($this->input->get('employee_id') != '' && 
	$this->input->get('user_name') != '' && 
	$this->input->get('family_relation') != '' && 
	$this->input->get('family_gender') != '' && 
	$this->input->get('family_full_name') != '' && 
	$this->input->get('family_born_place') != '' && 
	$this->input->get('family_born_date') != '' && 
	$this->input->get('family_nationality') != ''){
        if ($family_id == "") {
            //insert 
            $created_by = $user_name;
            $created_dt = date('Y-m-d H:i:s');
            	 try {
            	 	$sql = "
            	 	INSERT INTO
            	 		tb_m_family (
            	 		employee_id
            	 		, relationship
            	 		, gender
            	 		, fullname
            	 		, born_dt
            	 		, born_place
            	 		, nationality
            	 		, created_by
            	 		,created_dt
            	 		)
					VALUES (
					'$employee_id'
					, '$family_relation'
					, '$family_gender'
					, '$family_full_name'
					, '$family_born_date'
					, '$family_born_place'
					, '$family_nationality'
					, '$created_by'
					, '$created_dt'
					 )";
            	 
			$this->db->query($sql);
            	       return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Data Keluarga Telah Disimpan.."
					)));
			} catch (Exception $e) {
               return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Data Gagal Disimpan.."
                                    )));
             
            }
        } else {
            //update

            $changed_by = $user_name;
            $changed_dt = date('Y-m-d H:i:s');

             try {
             	$sql = "
				update 
					tb_m_family
				set
					relationship = '$family_relation'
					, gender = '$family_gender'
					, fullname = '$family_full_name'
					, born_place = '$family_born_place'
					, born_dt = '$family_born_date'
					, nationality = '$family_nationality'
					, changed_by = '$changed_by'
					, changed_dt = '$changed_dt'
				where
					employee_id = '$employee_id'
					and family_id = '$family_id'
					";		
			$this->db->query($sql);
            	            	return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "Data has been updated"
                                    )));
            } catch (Exception $e) {
            	return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Update failed"
                                    )));
            }
        } 
    	}
        else{
        	return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Terjadi Kesalahan Input"
                                    )));
        }   
	}

public function on_deletefamily()
	{
		$family_id	= $this->input->get('family_id');
		$employee_id	= $this->input->get('employee_id');
		
		try {
			
				$sql= "delete from tb_m_family
					where family_id = '$family_id' and employee_id='$employee_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "Data Keluarga Telah Dihapus.."
	            )));
		} catch (exception $e) {
			return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => 'warning',
	                    'msgText' => $e->getmessage()
	            )));
		}
	}

public function get_userfamily()
	{
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
		 	 employee_id = '$employee_id'
		 order by born_dt";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->result());	
	}
	public function get_userfamilybyid()
	{
		header("content-type: application/json");
		$family_id= $this->input->get('family_id');
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
		 	 family_id = '$family_id'
		 order by born_dt";
		$data=$this->db->query($sql);
		// return $data;
		echo json_encode($data->row());	
	}
	public function get_family_relation() 
	{
        header("Content-Type: application/json");
        $sql = "select 
        		distinct 
        			relationship 
        		from 
        			tb_m_family 
        		where 
        			relationship <> '' and 
        			relationship is not null 
        		order by 
        			relationship";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }

public function get_usereducation()
	{
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

public function get_education_level() 
	{
        header("Content-Type: application/json");
       	$row = array();
        $result = $this->sm->get_data('config_others', 'education') ? $this->sm->get_data('config_others', 'education')->value_txt : "";
        if ($result != "") {
            $data = explode(";", $result);
            for ($i = 0; $i < count($data); $i++) {
               $row[] = array(
                    'edu_level' => $data[$i]);
            }
        }
        echo json_encode($row);
    }
public function get_useridcard()
	{
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

public function get_idcard_type() 
	{
        header("Content-Type: application/json");
       	$row = array();
        $result = $this->sm->get_data('config_others', 'card_type') ? $this->sm->get_data('config_others', 'card_type')->value_txt : "";
        if ($result != "") {
            $data = explode(";", $result);
            for ($i = 0; $i < count($data); $i++) {
                $row[] = array(
                    'id_card_type' => $data[$i]);
            }
        }
        echo json_encode($row);
    }

public function get_usertax()
	{
		header("content-type: application/json");
		$employee_id= $this->input->get('employee_id');
		$sql = "
		select 
			employee_id
			, ( case when user_name is null then '' else user_name end ) as user_name
			, ( case when npwp_number is null or 
				replace(format(npwp_number,0),',','') = 0 
				then '' else replace(format(npwp_number,0),',','') end ) as npwp_number
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

public function get_tax_marital() 
	{
		header("Content-Type: application/json");
       	$row = array();
        $result = $this->sm->get_data('config_others', 'marital') ? $this->sm->get_data('config_others', 'marital')->value_txt : "";
        // $result = strtoupper($result);
        if ($result != "") {
            $data = explode(";", $result);
            for ($i = 0; $i < count($data); $i++) {
                $row[] = array(
                    'marital' => $data[$i]);
            }
        }
        echo json_encode($row);
	}

public function get_userchangepswd()
	{
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
	}
	else
	{
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
			if ($this->input->get('password1') != '' && $this->input->get('password2') != '')
				{
		   			if ($this->input->get('password1') == $this->input->get('password2'))
		   				{
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
						}
						else
						{
							 return $this->output
            			             ->set_content_type('application/json')
            			             ->set_output(json_encode(array(
            			                 'msgType' => "warning",
            			                 'msgText' => "Pasword Tidak Sama.."
            			        )));
						}
				}
				else
				{
				
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

public function on_updatemain()
	{
		$employee_id= $this->input->get('employee_id');
		$user_title= $this->input->get('user_title');
		$user_gender= $this->input->get('user_gender');
		$user_born_dt= $this->input->get('user_born_dt');
		$user_born_place= $this->input->get('user_born_place');
		$user_religion= $this->input->get('user_religion');
		$user_married_status= $this->input->get('user_married_status');
		$user_married_since= $this->input->get('user_married_since');
		if ($this->input->get('employee_id') != '')
		{
		if ($this->input->get('user_married_since') == '' && 
			$this->input->get('user_married_status') != 'Single'){
			
			return $this->output
                    ->set_content_type('application/json')
                    ->set_output(json_encode(array(
                        'msgType' => "warning",
                        'msgText' => "Married Since Tidak Boleh  Kosong"
               )));
		}else{
		
			$sql = "
				update 
					tb_m_employee
				set
					title = '$user_title'
					, gender = '$user_gender'
					, born_dt = '$user_born_dt'
					, born_place = '$user_born_place'
					, religion = '$user_religion'";
			if ($this->input->get('user_married_since') != '')
			{
				$sql .=" , married_since = '$user_married_since'";
			}		
				$sql .="
					, married_status = '$user_married_status'
					
				where
					employee_id = '$employee_id'
					";		
			$this->db->query($sql);
			 return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Data Telah Diupdate.."
					)));
		}
		}
		else
		{
				return $this->output
                    ->set_content_type('application/json')
                    ->set_output(json_encode(array(
                        'msgType' => "warning",
                        'msgText' => "Terjadi Kesalahan "
               )));
		}

	}


public function on_updateaddress()
	{
		$employee_id= $this->input->get('employee_id');
		$user_street= $this->input->get('user_street');
		$user_address= $this->input->get('user_address');
		$user_region= $this->input->get('user_region');
		$user_sub_district= $this->input->get('user_sub_district');
		$user_province= $this->input->get('user_province');
		$user_country= $this->input->get('user_country');
		$user_postal_code= $this->input->get('user_postal_code');
		$user_phone1= $this->input->get('user_phone1');
		$user_phone2= $this->input->get('user_phone2');
		$user_work_email= $this->input->get('user_work_email');
		$user_bank_account= $this->input->get('user_bank_account');
		$user_contact_person= $this->input->get('user_contact_person');
		$user_contact_person_phone= $this->input->get('user_contact_person_phone');
		if ($this->input->get('employee_id') != '')
		{
		
			$sql = "
				update 
					tb_m_employee
				set
					street = '$user_street'
					, address = '$user_address'
					, region = '$user_region'
					, sub_district = '$user_sub_district'
					, province = '$user_province'
					, country = '$user_country'
					, postal_code = '$user_postal_code'
					, handphone1 = '$user_phone1'
					, handphone2 = '$user_phone2'
					, work_email = '$user_work_email'
					, bank_account_number = '$user_bank_account'
					, closed_person_name = '$user_contact_person'
					, closed_person_phone = '$user_contact_person_phone'
					
				where
					employee_id = '$employee_id'
					";		
			$this->db->query($sql);
			 return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Alamat Telah Diupdate.."
					)));
		
		}
		else
		{
				return $this->output
                    ->set_content_type('application/json')
                    ->set_output(json_encode(array(
                        'msgType' => "warning",
                        'msgText' => "Terjadi Kesalahan "
               )));
		}

	}

	public function on_updatetax()
	{   
		
		$employee_id= $this->input->get('employee_id');
		$user_npwp= $this->input->get('user_npwp');
/*		$user_npwp = substr($user_npwp,0,1).'.'.substr($user_npwp,2);
		echo $user_npwp;
		die;*/
		$user_npwp_dt= $this->input->get('user_npwp_dt');
		$user_marital= $this->input->get('user_marital');
		$user_bpjs_ketenagakerjaan= $this->input->get('user_bpjs_ketenagakerjaan');
		$user_bpjs_kesehatan= $this->input->get('user_bpjs_kesehatan');
		
		if ($this->input->get('employee_id') != '')
		{
		
			$sql = "
				update 
					tb_m_employee
				set
					npwp_number = '$user_npwp'";
					if ($this->input->get('user_npwp_dt') != '')
					{
					$sql .= " , npwp_dt = '$user_npwp_dt'";
					}
					$sql .= " , marital = '$user_marital'
					, bpjs_ketenagakerjaan = '$user_bpjs_ketenagakerjaan'
					, bpjs_kesehatan = '$user_bpjs_kesehatan'
				where
					employee_id = '$employee_id'
					";		
			$this->db->query($sql);
			 return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Pajak Dan Bpjs Telah Diupdate.."
					)));
		
		}
		else
		{
				return $this->output
                    ->set_content_type('application/json')
                    ->set_output(json_encode(array(
                        'msgType' => "warning",
                        'msgText' => "Terjadi Kesalahan "
               )));
		}

	}


public function get_userdata()
	{
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