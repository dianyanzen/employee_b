<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of ExcuseServiceMobilePortal
 *
 * @author wawan
 */
class complaintservicemobileportal extends CI_Controller {

    //put your code here
    public function index() {
        $this->load->view('welcome_message');
    }

    public function cek_complaint_approved_sts($complaint_id) {
        $sql = "SELECT 1 FROM tb_r_complaint 
		WHERE complaint_no = '$complaint_id' AND flg_confirm = 0";


        $data = $this->db->query($sql);
        if ($data->num_rows() > 0) {
            //$row=$data->row();
            return true;
        }

        return false;
    }

    public function delete_complaint() {
        $complaint_id = $this->input->get('complaint_id');
        $employee_id = $this->input->get('employee_id');

        try {
            if ($this->cek_complaint_approved_sts($complaint_id)) {

                $sql = "DELETE FROM tb_r_complaint
					WHERE complaint_no = '$complaint_id' and employee_cd='$employee_id'";
                $this->db->query($sql);

                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "Data Excuse telah dihapus.."
                )));
            } else {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => 'warning',
                                    'msgText' => "Delete gagal, Data Excuse sudah di approve.."
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

    public function save_complaint() {
        $complaint_id = $this->input->get('complaint_id');
        $complaint_dt = $this->input->get('complaint_dt');
        $complaint_type = $this->input->get('complaint_type');
        $complaint_description = $this->input->get('complaint_description');
		$complaint_clock_in = $this->input->get('complaint_clock_in');
		$complaint_clock_out = $this->input->get('complaint_clock_out');
        $username = $this->input->get('username');
        $employee_id = $this->input->get('employee_id');
        $created_dt = date('Y-m-d H:i:s');

        if ($complaint_id == "") {
            /* do insert */
            try {
				$sql = "INSERT INTO tb_r_complaint(
						complaint_no, 
						employee_cd,
						complaint_date,
						excuse_cd,
						clock_in_tm,
						clock_out_tm,
						reason,
						created_by,
						created_dt,
						changed_by)
					VALUES(
						UUID_SHORT(),
						'$employee_id',
						'$complaint_dt',
						'$complaint_type',
						'$complaint_clock_in',
						'$complaint_clock_out',
						'$complaint_description',
						'$username',
						'$created_dt',
						'$username')";
				
                $this->db->query($sql);
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "Data Excuse telah disimpan.."
                )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => 'warning',
                                    'msgText' => $e->getMessage()
                )));
            }
        } else {
            if ($this->cek_complaint_approved_sts($complaint_id)) {
                /* do update */

                try {
					$sql = "UPDATE tb_r_complaint
								SET complaint_date='$complaint_dt',
									excuse_cd='$complaint_type',
									clock_in_tm='$complaint_clock_in',
									clock_out_tm='$complaint_clock_out',
									reason='$complaint_description',								
									changed_by='$username',
									changed_dt='$created_dt'
								WHERE complaint_no = '$complaint_id'";
					
                    $this->db->query($sql);

                    return $this->output
                                    ->set_content_type('application/json')
                                    ->set_output(json_encode(array(
                                        'msgType' => "info",
                                        'msgText' => "Perubahan Data Excuse telah disimpan.."
                    )));
                } catch (Exception $e) {
                    return $this->output
                                    ->set_content_type('application/json')
                                    ->set_output(json_encode(array(
                                        'msgType' => 'warning',
                                        'msgText' => $e->getMessage()
                    )));
                }
            } else {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => 'warning',
                                    'msgText' => "Update gagal, sudah di approve.."
                )));
            }
        }
    }

    //public function checkLogin(){
    public function checklogin() {
        date_default_timezone_set('Asia/Jakarta');
        $username = $this->input->post('username', TRUE);
        $password = $this->input->post('password', TRUE);

        $sql = "select employee_id,employee_name FROM tb_m_employee 
		where user_name = '$username' and user_password = md5('$password')";



        $data = $this->db->query($sql);
        if ($data->num_rows() > 0) {
            $row = $data->row();
            return $this->output
                            ->set_content_type('application/json')
                            ->set_output(json_encode(array(
                                'msgType' => 'info',
                                'msgText' => 'Login Success',
                                'employee_id' => $row->employee_id,
                                'employee_name' => $row->employee_name
            )));
        }

        return $this->output
                        ->set_content_type('application/json')
                        ->set_output(json_encode(array(
                            'msgType' => 'warning',
                            'msgText' => 'Login Failed'
        )));
    }

    /* Complaint */

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

    public function get_complaint_by_id() {
        header("Content-Type: application/json");
        date_default_timezone_set('Asia/Jakarta');
        $complaint_id = $this->input->get('complaint_id', TRUE);

        $sql = "SELECT 
					DATE_FORMAT(com.complaint_date,'%d') as complaint_prod_date,
					DATE_FORMAT(com.complaint_date,'%Y-%m') as complaint_prod_month,
					com.complaint_no,
					DATE_FORMAT(com.complaint_date,'%Y-%m-%d') as complaint_dt,
					com.excuse_cd, 
					ex.excuse_nm,
					COALESCE((SUBSTR(com.clock_in_tm, 1, 5)), 'kosong', (SUBSTR(com.clock_in_tm, 1, 5))) as clock_in,
					COALESCE((SUBSTR(com.clock_out_tm, 1, 5)), 'kosong', (SUBSTR(com.clock_out_tm, 1, 5))) as clock_out,
					com.reason,
					com.flg_confirm,
					(
						CASE 
							WHEN com.flg_confirm = 0
							THEN ''
							ELSE 'Approved'
						END
					  ) AS complaint_status
				FROM 
					tb_r_complaint com left join tb_m_excuse ex
					on com.excuse_cd = ex.excuse_cd 
				WHERE com.complaint_no='$complaint_id'";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->row());
    }

    public function get_complaint_type_list() {
        header("Content-Type: application/json");
        $sql = "select excuse_cd, excuse_nm from tb_m_excuse";
        //echo($sql);
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    }
    function sent_notification($employee_id = NULL, $employee_name = NULL, $type = NULL) {
        $this->load->library('email');

        $cfg_email = $this->sm->get_data("config_others", 'email_settings')->value_txt;

        $r = json_decode($cfg_email);

        $config = Array(
            'protocol' => $r->protocol, //'smtp'
            'smtp_host' => $r->smtp_host, //'ssl://smtp.googlemail.com'/'ssl://smtp.gmail.com',
            'smtp_port' => $r->smtp_port, //465,
            'smtp_timeout' => $r->smtp_timeout, //'30',
            'smtp_user' => $r->smtp_user, //'misbachlennon90@gmail.com',
            'smtp_pass' => $r->smtp_pass, //'rahasia123!',
            'mailtype' => $r->mailtype, //'html',
            'charset' => $r->charset, //'iso-8859-1',
            'wordwrap' => $r->wordwrap, //TRUE
        );

        $subject = 'Excuse Claim';
        $emailFrom = $this->user_data->work_email;
        $sender = $this->user_data->employee_name;

        if ($type == 'notify_hrd') {
            $emailTo = $this->sm->get_data('config_others', 'hrd_email')->value_txt;
            $emailCc = $this->sm->get_data('config_others', 'hrd_email')->value_txt;

//            $message = $this->sm->get_data('notification_excuse', 'content_message_hrd')->value_txt;
        } else {
            $emailTo = $this->mdl->get_email($employee_id, 'spv');
            $emailCc = $this->mdl->get_email($employee_id, 'mgr');

            $msg_content = $this->sm->get_data('notification_excuse', 'content_message')->value_txt;
//            $message = str_replace("{employee_name}", $employee_name, $msg_content);
        }
        
        if (!empty($emailTo)) {
            $chk_emailTto = in_array("", $emailTo);
        }else{
            $chk_emailTto = 1;
        }
        
        if ($chk_emailTto == 0) {

            $data['type_request'] = "Excuse Claim Request";

            $this->email->initialize($config);
            $this->email->set_newline("\r\n");
            //$this->email->from($emailFrom, $sender);
            $this->email->from('no-reply@pt-pci.co.id', 'Admin PCI');
            $this->email->reply_to('no-reply@pt-pci.co.id', 'Admin PCI');
            $this->email->to($emailTo);
            $this->email->cc($emailCc);
            $this->email->subject($subject);
            //$this->email->message($message);

            $body = $this->load->view('template_email', $data, TRUE);
            $this->email->message($body);

            if ($this->email->send()) {
                return 1;
            } else {
//                return 
                $this->email->print_debugger();
                return 0;
            }
        } else {
            return 0;
        }
//        if ($emailTo) {
//            $this->email->initialize($config);
//            $this->email->set_newline("\r\n");
//            $this->email->from($emailFrom, $sender);
//            $this->email->to($emailTo);
//            $this->email->cc($emailCc);
//            $this->email->subject($subject);
//            $this->email->message($message);
//
//            if ($this->email->send()) {
//                return 1;
//            } else {
//                return $this->email->print_debugger();
//            }
//        } else {
//            return 1;
//        }
    }

    /* End Complaint */
}
