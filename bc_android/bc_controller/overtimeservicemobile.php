<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class overtimeservicemobile extends CI_Controller {

	public function index()
	{
		$this->load->view('welcome_message');
	}
/* overtime */
	
	public function cekovertimeapprovedsts($ot_id){
		$sql = "select 1 from tb_r_overtime
		where ot_id = '$ot_id' and ot_approve_by is null and ot_approve_dt is null";
		

		$data=$this->db->query($sql);
		if ($data->num_rows() > 0)
		{
		   //$row=$data->row();
		   return true;
		}

		return false;
	}
	
	public function cekovertimedate($employee_id, $ot_dt){
		$sql = "select count(*) as cnt from tb_r_overtime where employee_id = '$employee_id' and ot_dt = '$ot_dt'";
		
		$data=$this->db->query($sql);
		return $data->row()->cnt;
	}
	
    public function getovertimelist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $employee_id = $this->input->get('employee_id', true);

        $sql = "select 
					date_format(ot_dt,'%d') as overtime_prod_date,
					date_format(ot_dt,'%Y-%m') as overtime_prod_month,
					ot_id,
					date_format(ot_dt,'%Y-%m-%d') as ot_dt,
					ot_hour, ot_calculate,
					CONCAT(SUBSTR(ot_description,1,12),'..') as ot_description, 
					ot_approve_dt, ot_approve_by ,
					(
						case 
							when ot_approve_by is null
							then ''
							else 'approved'
						end
					  ) as overtime_status
				from tb_r_overtime where employee_id='$employee_id'
				order by ot_dt desc limit 30";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }

    public function getovertimebyid() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $ot_id = $this->input->get('ot_id', true);

        $sql = "select 
					date_format(ot_dt,'%d') as overtime_prod_date,
					date_format(ot_dt,'%y-%m') as overtime_prod_month,
					ot_id,
					date_format(ot_dt,'%Y-%m-%d') as ot_dt,
					ot_hour, ot_calculate,
					substr(ot_description,1,255) as ot_description, 
					ot_approve_dt, ot_approve_by ,
					(
						case 
							when ot_approve_by is null
							then ''
							else 'approved'
						end
					  ) as overtime_status
				from tb_r_overtime where ot_id = '$ot_id'";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->row());
    }
	
	public function saveovertime()
	{
		$ot_id	= $this->input->get('ot_id');
		$ot_dt	= $this->input->get('ot_dt');
		$ot_description	= $this->input->get('ot_description');
		$ot_hour	= $this->input->get('ot_hour');
		$username	= $this->input->get('username');
		$ot_calculate = $this->calculate_ot($ot_hour, $ot_dt);
		$employee_id	= $this->input->get('employee_id');
		$created_dt= date('y-m-d h:i:s');
		$data_cnt = $this->cekovertimedate($employee_id, $ot_dt);
		
		if($ot_id==""){
			if($data_cnt == 0){
				/*do insert*/
				try {
					$sql= "
						insert into tb_r_overtime(
							employee_id,
							ot_dt,
							ot_description,
							ot_hour,
							ot_calculate,
							created_by,
							created_dt,
							changed_by
						)
						values(
							'$employee_id',
							'$ot_dt',
							'$ot_description',
							'$ot_hour',
							'$ot_calculate',
							'$username',
							'$created_dt',
							'$username'
						)";
						//echo $sql;
						//die;
					$this->db->query($sql);
					return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "overtime telah disimpan.."
					)));

				} catch (exception $e) {
					return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
								'msgType' => 'warning',
								'msgText' => $e->getmessage()
						)));
				}
			}else{
				return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "warning",
							'msgText' => "Data overtime tanggal : " .$ot_dt. " sudah ada"
					)));
			}
		}else{
			if($this->cekovertimeapprovedsts($ot_id)){
				/*do update*/
				try {

					$sql= "update tb_r_overtime
							set ot_dt='$ot_dt',
								ot_description='$ot_description',
								ot_hour='$ot_hour',
								changed_by='$username',
								changed_dt='$created_dt'
							where 
								ot_id = '$ot_id'";
					$this->db->query($sql);

				   	return $this->output
		            	->set_content_type('application/json')
		            	->set_output(json_encode(array(
		                    'msgType' => "info",
		                    'msgText' => "perubahan overtime telah disimpan.."
		            )));

				} catch (exception $e) {
					return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => $e->getmessage()
			            )));
				}
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "update gagal, sudah di approve.."
			            )));
			}
			
		}
	}
	
	function calculate_ot($hours, $dt){
		$query = "select count(*) as cnt from tb_m_holiday_cal where holiday_dt = '$dt'";
		$hot = $this->db->query($query)->row()->cnt;
	
		$day = date('l',strtotime($dt));
		//$hot = $this->mdl->check_holiday_date($dt);
		$ot_calc = 0;

		if ( ($hot!=0) || ( $day == "Sunday" || $day == "Saturday") ) { //jika lembur hari libur 
			$ot_calc = $hours * 2; 
			if ($hours > 8 && $hours <= 9) { 
				$ot_calc += 1 * 3; 
			} 
			if ($hours > 9){ 
				$hours = $hours - 9; 
				$ot_calc += 1 * 3; 
				$ot_calc += $hours * 4; 
			} 
			}else{ // jika lembur hari biasa 
				$ot_calc = 1 * 1.5 ; 
			if ($hours > 1) { 
				$hours = $hours - 1; 
				$ot_calc += $hours * 2;	
			} 
		}
	return $ot_calc;
	// echo $ot_calc;
}
	
	public function deleteovertime(){
		$ot_id	= $this->input->get('ot_id');
		$employee_id	= $this->input->get('employee_id');
		
		try {
			if($this->cekovertimeapprovedsts($ot_id)){

				$sql= "delete from tb_r_overtime
					where ot_id = '$ot_id' and employee_id='$employee_id'";
				$this->db->query($sql);

			   	return $this->output
	            	->set_content_type('application/json')
	            	->set_output(json_encode(array(
	                    'msgType' => "info",
	                    'msgText' => "overtime telah dihapus.."
	            )));
			}else{
				return $this->output
			            ->set_content_type('application/json')
			            ->set_output(json_encode(array(
			                    'msgType' => 'warning',
			                    'msgText' => "delete gagal, overtime sudah di approve.."
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

    /* End Overtime */
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */