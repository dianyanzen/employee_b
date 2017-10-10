<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class cicoreimservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}

	public function cekreimburseapprovedSts($reimburse_id){
		$sql = "SELECT 1 FROM tb_r_reimburse 
		WHERE reimburse_id = '$reimburse_id' AND reimburse_approve_by IS NULL";
		

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   //$row=$data->row();
		   return true;
		}

		return false;
	}

	public function getreimbursefileName($reimburse_id,$employee_id){
		$sql= "SELECT reimburse_file FROM tb_r_reimburse
					WHERE reimburse_id = '$reimburse_id' and employee_id='$employee_id'";
        $row = $this->db->query($sql)->row();
        return $row->reimburse_file;
	}

	public function deletereimburse(){
		$reimburse_id	= $this->input->get('reimburse_id');
		$employee_id	= $this->input->get('employee_id');
		
		try {
			if($this->cekReimburseApprovedSts($reimburse_id)){

				/**Delete Image**/
				$this->delete_image($this->getReimburseFileName($reimburse_id,$employee_id));

				$sql= "DELETE FROM tb_r_reimburse
					WHERE reimburse_id = '$reimburse_id' and employee_id='$employee_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "Reimburse telah dihapus.."
	            )));
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "Delete gagal, Reimburse sudah di approve.."
			            )));
			}
				
		} catch (Exception $e) {
			return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => 'warning',
	                    'msgText' => $e->getMessage()
	            )));
		}
	}

	public function savereimbursewoimage()
	{
		$reimburse_id	= $this->input->get('reimburse_id');
		$reimburse_dt	= $this->input->get('reimburse_dt');
		$reimburse_type	= $this->input->get('reimburse_type');
		$reimburse_description	= $this->input->get('reimburse_description');
		$reimburse_amount	= $this->input->get('reimburse_amount');
		$username	= $this->input->get('username');
		$employee_id	= $this->input->get('employee_id');
		$created_dt= date('Y-m-d H:i:s');
		$reimburse_file= $this->input->get('reimburse_file');;
		$old_reimburse_file= $this->input->get('old_reimburse_file');;

		try {
			if($this->cekReimburseApprovedSts($reimburse_id)){
				$sql= "UPDATE tb_r_reimburse
					SET reimburse_dt='$reimburse_dt',
						reimburse_type='$reimburse_type',
						reimburse_description='$reimburse_description',
						reimburse_amount='$reimburse_amount',
						changed_by='$username',
						changed_dt='$created_dt'
					WHERE reimburse_id = '$reimburse_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "Perubahan Reimburse telah disimpan.."
	            )));
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "Update gagal, sudah di approve.."
			            )));
			}
				
		} catch (Exception $e) {
			return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => 'warning',
	                    'msgText' => $e->getMessage()
	            )));
		}
	}

	public function savereimburse()
	{
		$reimburse_id	= $this->input->get('reimburse_id');
		$reimburse_dt	= $this->input->get('reimburse_dt');
		$reimburse_type	= $this->input->get('reimburse_type');
		$reimburse_description	= $this->input->get('reimburse_description');
		$reimburse_amount	= $this->input->get('reimburse_amount');
		$username	= $this->input->get('username');
		$employee_id	= $this->input->get('employee_id');
		$created_dt= date('Y-m-d H:i:s');
		$reimburse_file= $this->input->get('reimburse_file');;
		$old_reimburse_file= $this->input->get('old_reimburse_file');;

		$this->load->helper('file');
		


		if($reimburse_id==""){
			/*do insert*/
			try {
				$reimburse_file = $this->do_upload('userfile',$username);
				$sql= "INSERT INTO tb_r_reimburse(employee_id,reimburse_dt,reimburse_type,reimburse_description,reimburse_amount,
							reimburse_file,created_by,created_dt,changed_by)
							VALUES('$employee_id','$reimburse_dt','$reimburse_type','$reimburse_description','$reimburse_amount',
							'$reimburse_file','$username','$created_dt','$username')";
				$this->db->query($sql);
			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "Reimburse telah disimpan.."
	            )));

			} catch (Exception $e) {
				return $this->output
		            ->set_content_type('application/json')
		            ->set_output(json_encode(array(
		                    'msgType' => 'warning',
		                    'msgText' => $e->getMessage()
		            )));
			}
		}else{
			if($this->cekReimburseApprovedSts($reimburse_id)){
				/*do update*/
				if($reimburse_file!=$old_reimburse_file){
					$reimburse_file = $this->do_upload('userfile',$username);
				}
				
				try {
					if ($old_reimburse_file != ''){
						$this->delete_image($old_reimburse_file);
					}

					$sql= "UPDATE tb_r_reimburse
							SET reimburse_dt='$reimburse_dt',
								reimburse_type='$reimburse_type',
								reimburse_description='$reimburse_description',
								reimburse_amount='$reimburse_amount',
								reimburse_file='$reimburse_file',
								changed_by='$username',
								changed_dt='$created_dt'
							WHERE reimburse_id = '$reimburse_id'";
					$this->db->query($sql);

				   	return $this->output
		            	->set_content_type('application/json')
		            	->set_output(json_encode(array(
		                    'msgType' => "info",
		                    'msgText' => "Perubahan Reimburse telah disimpan.."
		            )));

				} catch (Exception $e) {
					return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => $e->getMessage()
			            )));
				}
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "Update gagal, sudah di approve.."
			            )));
			}
			
		}
	}

	function do_upload($nama_file,$username) {
		
		$config['upload_path'] = './assets/files/reimburse';
		$config['allowed_types'] = '*';
		$config['max_size']	= '256';
		$config['file_name'] ="$username".'_'.date('YmdHis');

		$this->load->library('upload', $config);
		
		$this->upload->do_upload($nama_file);
		$data = $this->upload->data($nama_file);
		$image_data = $this->upload->data();
		
		$nama_filenya = $image_data['file_name'];
		
		return $nama_filenya;
		
	}



	function delete_image($image_name){
		try{
			unlink("./assets/files/reimburse/".$image_name);
		}catch(Exception $err){
			return true;
		}
	 	
	}

	/*Clock In Clock Out*/
	public function clockout(){
		date_default_timezone_set('Asia/Jakarta');
		$employee_id=$this->input->post('employee_id', TRUE);
		$clock_in=$this->input->post('clock_in', TRUE);
		$long_out=$this->input->post('long_out', TRUE);
        $lat_out=$this->input->post('lat_out', TRUE);

		$clock_out=date("Y-m-d H:i:s");

		$ci = new DateTime($clock_in);
		$co = new DateTime($clock_out);

		$interval = date_diff($ci,$co);

		$work_hour=$interval->format('%h:%i:%s');

		try {
			$sql= "UPDATE tb_r_attendance
					SET clock_out='$clock_out',
						long_out='$long_out',
						lat_out='$lat_out',
						work_hour='$work_hour'
					WHERE employee_id = '$employee_id'
					and (cast(clock_in as date)=cast('$clock_in' as date) OR (cast(clock_in as date)=cast(DATE_SUB('$clock_in',interval 1 day) as date) AND (clock_out IS NULL OR clock_out=clock_in)))";
			$this->db->query($sql);
		   	return $this->output
            	->set_content_type('application/json')
            	->set_output(json_encode(array(
                    'msgType' => "info",
                    'msgText' => "Hati2 di jalan keluarga menanti dirumah.."
            )));

		} catch (Exception $e) {
			return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => 'warning',
	                    'msgText' => $e->getMessage()
	            )));
		}

		
	}

	public function clockin(){
		date_default_timezone_set('Asia/Jakarta');
		$username=$this->input->post('username', TRUE);
        $employee_id=$this->input->post('employee_id', TRUE);
        $source=$this->input->post('source', TRUE);
        $long_in=$this->input->post('long_in', TRUE);
        $lat_in=$this->input->post('lat_in', TRUE);
        $schedule_type=$this->input->post('schedule_type', TRUE);
        $schedule_project=$this->input->post('schedule_project', TRUE);
        $schedule_description=$this->input->post('schedule_description', TRUE);
        $place =$this->input->post('place', TRUE);
        $address =$this->input->post('address', TRUE);

		// $username=$this->input->get('username', TRUE);
  //       $employee_id=$this->input->get('employee_id', TRUE);
  //       $source=$this->input->get('source', TRUE);
  //       $long_in=$this->input->get('long_in', TRUE);
  //       $lat_in=$this->input->get('lat_in', TRUE);
  //       $schedule_type=$this->input->get('schedule_type', TRUE);
  //       $schedule_project=$this->input->get('schedule_project', TRUE);
  //       $schedule_description=$this->input->get('schedule_description', TRUE);
  //       $place =$this->input->get('place', TRUE);



		$clock_in=date("Y/m/d H:i:s");
		try {
		    //$sql="insert into tb_r_attendance(employee_id,clock_in,source,long_in,lat_in,place)
			//	values('$employee_id','$clock_in','$source','$long_in','$lat_in','$place')";
			
			$sql="call SP_ClockIn('$username',
				'$employee_id',
				'$source',
				'$long_in',
				'$lat_in',
				'$schedule_type',
				'$schedule_project',
				'$schedule_description',
				'$place','$clock_in','$address'
				)";
			
			$data=$this->db->query($sql);
			if ($data->num_rows() > 0)
			{
			   $row=$data->row();
			   return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => $row->msgType,
	                    'msgText' => 'Selamat Bekerja'
	            )));

			}

			

		} catch (Exception $e) {
			return $this->output
	            ->set_content_type('application/json')
	            ->set_output(json_encode(array(
	                    'msgType' => 'warning',
	                    'msgText' => $e->getMessage()
	            )));
		}

		
	}

	//public function checkLogin(){
	public function checklogin(){
		date_default_timezone_set('Asia/Jakarta');
		$username=$this->input->post('username', TRUE);
		$password=$this->input->post('password', TRUE);
		 if ($username=='' && $username =='') {
		return $this->output
            ->set_content_type('application/json')
            ->set_output(json_encode(array(
                    'msgType' => 'warning',
                    'msgText' => 'Please Input Username Or Password'
            ))); 	
		 		 	
		 }else{
		$sql = "select employee_id,employee_name FROM tb_m_employee 
		where user_name = '$username' and user_password = md5('$password')";

		

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   $row=$data->row();
		   return $this->output
            ->set_content_type('application/json')
            ->set_output(json_encode(array(
                    'msgType' => 'info',
                    'msgText' => 'Login Success',
                    'employee_id' =>  $row->employee_id,
                    'employee_name'=> $row->employee_name
            )));
		}

		return $this->output
            ->set_content_type('application/json')
            ->set_output(json_encode(array(
                    'msgType' => 'warning',
                    'msgText' => 'Login Failed'
            ))); 	
		 }
		
	}

	public function getLastclockIn(){
		date_default_timezone_set('Asia/Jakarta');
		$employee_id=$this->input->get('employee_id', TRUE);
		$current_dt=date("Y/m/d H:i:s");

		$sql = "select COALESCE(DATE_FORMAT(att.clock_in,'%Y-%m-%d %T'),'00:00') as clock_in,
					(CASE COALESCE(att.clock_out,'') WHEN '' THEN '00:00' WHEN att.clock_in THEN '00:00' ELSE DATE_FORMAT(att.clock_out,'%Y-%m-%d %T')  END)  as clock_out,
					COALESCE(DATE_FORMAT(att.work_hour,'%T'),'00:00') as work_hour,
					COALESCE(att.place,'') as place, COALESCE(sch.schedule_type,'OFFICE') as schedule_type,COALESCE(sch.schedule_project,'') as schedule_project,
					COALESCE(sch.schedule_description,'') as schedule_description
				from tb_r_attendance att
				left join tb_r_schedule sch on(sch.employee_id=att.employee_id and cast(sch.schedule_dt as date)=cast(att.clock_in as date) ) 
				where att.employee_id='$employee_id' 
					and (cast(clock_in as date)=cast('$current_dt' as date) OR 
						(cast(clock_in as date)=cast(DATE_SUB('$current_dt',interval 1 day) as date) AND (clock_out IS NULL OR clock_out=clock_in )  ))
				order by att.clock_in desc LIMIT 1";

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   $row=$data->row();
		   return $this->output
            ->set_content_type('application/json')
            ->set_output(json_encode(array(
                    'msgType' => 'found',
                    'msgText' => 'sync. process successfully',
                    'clock_in'=>$row->clock_in,
                    'clock_out'=>$row->clock_out,
                    'work_hour'=>$row->work_hour,
                    'place'=>$row->place,
                    'schedule_type'=>$row->schedule_type,
                    'schedule_project'=>$row->schedule_project,
                    'schedule_description'=>$row->schedule_description
            )));
		}else{
			return $this->output
            ->set_content_type('application/json')
            ->set_output(json_encode(array(
                    'msgType' => 'notFound',
                    'msgText' => 'sync. process successfully'
            )));
		}

		return $this->output
            ->set_content_type('application/json')
            ->set_output(json_encode(array(
                    'msgType' => 'warning',
                    'msgText' => 'sync. process failed'
            )));
	}
	/*End Clock in Clock Out*/

	/*Reimburse*/
	public function getreimburseList(){
		header("Content-Type: application/json");
		date_default_timezone_set('Asia/Jakarta');
		$employee_id=$this->input->get('employee_id', TRUE);
		
		$sql = "SELECT 
					DATE_FORMAT(reimburse_dt,'%d') as reimburse_prod_date,
					DATE_FORMAT(reimburse_dt,'%Y-%m') as reimburse_prod_month,
					reimburse_id,
					DATE_FORMAT(reimburse_dt,'%Y-%m-%d') as reimburse_dt,
					reimburse_type, 
					CONCAT(SUBSTR(reimburse_description,1,12),'..') as reimburse_description,reimburse_amount, 
					reimburse_file,reimburse_approve_dt, reimburse_approve_by ,
					(
				        CASE 
				            WHEN reimburse_approve_by IS NULL
				            THEN ''
				            ELSE 'Approved'
				        END
				      ) AS reimburse_status
				FROM tb_r_reimburse where employee_id='$employee_id'
				ORDER BY reimburse_dt DESC LIMIT 30;";
		//echo($sql);
		$data = $this->db->query($sql);
		//print_r($data->result());
		echo json_encode($data->result());
		//return json_encode($data->result());

	}

	public function getReimburseById(){
		header("Content-Type: application/json");
		date_default_timezone_set('Asia/Jakarta');
		$reimburse_id=$this->input->get('reimburse_id', TRUE);
		
		$sql = "SELECT 
					DATE_FORMAT(reimburse_dt,'%d') as reimburse_prod_date,
					DATE_FORMAT(reimburse_dt,'%Y-%m') as reimburse_prod_month,
					reimburse_id,
					DATE_FORMAT(reimburse_dt,'%Y-%m-%d') as reimburse_dt,
					reimburse_type, 
					reimburse_description,reimburse_amount, 
					COALESCE(reimburse_file,'') as reimburse_file,reimburse_approve_dt, reimburse_approve_by ,

					(
				        CASE 
				            WHEN reimburse_approve_by IS NULL
				            THEN ''
				            ELSE 'Approved'
				        END
				      ) AS reimburse_status
				FROM tb_r_reimburse where reimburse_id='$reimburse_id'";
		//echo($sql);
		$data = $this->db->query($sql);
		echo json_encode($data->row());
	}
	

	/*End Reimburse*/
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */