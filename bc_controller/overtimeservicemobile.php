<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class overtimeservicemobile extends CI_Controller {


	function __construct() 
	{
        parent:: __construct();
        $this->load->model('hr_overtime_model', 'mdl');
        $this->load->model('shared_model', 'sm');
    }

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
         $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT
					b.employee_name
					, a.employee_id
					, date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
					, date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
					, date_format(a.ot_time_from,'%H:%i') as ot_from
					, date_format(a.ot_time_to,'%H:%i') as ot_to
					, a.ot_hour
                    , a.ot_calculate
					, a.ot_description
					, a.ot_approve_dt
                    , a.ot_approve_by 
                    ,( case
                	when a.rejected_by is not null then 'rejected'
                	when a.ot_approve_by is not null then 'approved'
                  	else '' end ) as overtime_status 
					from tb_r_overtime a inner join tb_m_employee b
					on a.employee_id = b.employee_id where 
					a.created_dt >= '$pastdate' and 
             		a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
					order by a.ot_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    	}else{
    		$sql = "SELECT
					b.employee_name
					, a.employee_id
					, date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
					, date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
					, date_format(a.ot_time_from,'%H:%i') as ot_from
					, date_format(a.ot_time_to,'%H:%i') as ot_to
					, a.ot_hour
                    , a.ot_calculate
					, a.ot_description
					, a.ot_approve_dt
                    , a.ot_approve_by 
                    , ( case
                			when a.rejected_by is not null then 'rejected'
                			when a.ot_approve_by is not null then 'approved'
                            when a.spv_approved_by = '$user_name' then 'approved' 
                            when a.mgr_approved_by = '$user_name' then 'approved' 
                  			else '' end ) as overtime_status 
					from tb_r_overtime a inner join tb_m_employee b 
					on a.employee_id = b.employee_id where 
				 	(b.user_name = '$user_name' or b.supervisor1 = '$user_name' or b.supervisor2 = '$user_name') 
				 	and a.created_dt >= '$pastdate' 
				 	and a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
					order by a.ot_dt desc";
				/*echo $sql;
				die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
    	}
    }
    public function getovertimespvlist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
         $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    ,( case
                    when a.rejected_by is not null then 'rejected'
                    when a.ot_approve_by is not null then 'approved'
                    else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b
                    on a.employee_id = b.employee_id where 
                    a.rejected_by is null and
                    a.spv_approved_by is not null and
                    a.mgr_approved_by is null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and 
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }else{
            $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    , ( case
                            when a.rejected_by is not null then 'rejected'
                            when a.ot_approve_by is not null then 'approved'
                            when a.spv_approved_by = '$user_name' then 'approved' 
                            when a.mgr_approved_by = '$user_name' then 'approved' 
                            else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b 
                    on a.employee_id = b.employee_id where 
                    (b.user_name = '$user_name' or b.supervisor1 = '$user_name' or b.supervisor2 = '$user_name') and
                    a.rejected_by is null and
                    a.spv_approved_by is not null and
                    a.mgr_approved_by is null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
                /*echo $sql;
                die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }
    }
    public function getovertimemgrlist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
         $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    ,( case
                    when a.rejected_by is not null then 'rejected'
                    when a.ot_approve_by is not null then 'approved'
                    else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b
                    on a.employee_id = b.employee_id where
                    a.rejected_by is null and
                    a.spv_approved_by is not null and
                    a.mgr_approved_by is not null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and 
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }else{
            $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    , ( case
                            when a.rejected_by is not null then 'rejected'
                            when a.ot_approve_by is not null then 'approved'
                            when a.spv_approved_by = '$user_name' then 'approved' 
                            when a.mgr_approved_by = '$user_name' then 'approved' 
                            else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b 
                    on a.employee_id = b.employee_id where 
                    (b.user_name = '$user_name' or b.supervisor1 = '$user_name' or b.supervisor2 = '$user_name') and
                    a.rejected_by is null and
                    a.spv_approved_by is not null and
                    a.mgr_approved_by is not null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and 
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
                /*echo $sql;
                die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }
    }
    public function getovertimehrdlist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
         $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    ,( case
                    when a.rejected_by is not null then 'rejected'
                    when a.ot_approve_by is not null then 'approved'
                    else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b
                    on a.employee_id = b.employee_id where 
                    a.rejected_by is null and
                    a.spv_approved_by is not null and
                    a.mgr_approved_by is not null and
                    a.ot_approve_by is not null and
                    a.created_dt >= '$pastdate' and 
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }else{
            $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    , ( case
                            when a.rejected_by is not null then 'rejected'
                            when a.ot_approve_by is not null then 'approved'
                            when a.spv_approved_by = '$user_name' then 'approved' 
                            when a.mgr_approved_by = '$user_name' then 'approved' 
                            else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b 
                    on a.employee_id = b.employee_id where 
                    (b.user_name = '$user_name' or b.supervisor1 = '$user_name' or b.supervisor2 = '$user_name') and 
                    a.rejected_by is null and
                    a.spv_approved_by is not null and
                    a.mgr_approved_by is not null and
                    a.ot_approve_by is not null and
                    a.created_dt >= '$pastdate' and
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
                /*echo $sql;
                die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }
    }
    public function getovertimerejectedlist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
         $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    ,( case
                    when a.rejected_by is not null then 'rejected'
                    when a.ot_approve_by is not null then 'approved'
                    else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b
                    on a.employee_id = b.employee_id where 
                    a.rejected_by is not null and
                    a.spv_approved_by is null and
                    a.mgr_approved_by is null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }else{
            $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    , ( case
                            when a.rejected_by is not null then 'rejected'
                            when a.ot_approve_by is not null then 'approved'
                            when a.spv_approved_by = '$user_name' then 'approved' 
                            when a.mgr_approved_by = '$user_name' then 'approved' 
                            else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b 
                    on a.employee_id = b.employee_id where 
                    (b.user_name = '$user_name' or b.supervisor1 = '$user_name' or b.supervisor2 = '$user_name') 
                    and 
                    a.rejected_by is not null and
                    a.spv_approved_by is null and
                    a.mgr_approved_by is null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
                /*echo $sql;
                die;*/
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }
    }
    public function getovertimenotyetlist() {
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
         $nowdate = date('Y-m-d');
        /* UBAH HARI PADA  */
        $pastdate = date("Y-m-d", strtotime("- 30 days")); 
        $employee_id = $this->input->get('employee_id', true);
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        
        if (strpos($position_name, 'admin') != false || $position_name == 'admin' ){
        $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    ,( case
                    when a.rejected_by is not null then 'rejected'
                    when a.ot_approve_by is not null then 'approved'
                    else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b
                    on a.employee_id = b.employee_id where 
                    a.rejected_by is null and
                    a.spv_approved_by is null and
                    a.mgr_approved_by is null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }else{
            $sql = "SELECT
                    b.employee_name
                    , a.employee_id
                    , date_format(a.ot_dt,'%d') as overtime_prod_date
                    , date_format(a.ot_dt,'%Y-%m') as overtime_prod_month
                    , a.ot_id
                    , date_format(a.ot_dt,'%Y-%m-%d') as ot_dt
                    , date_format(a.ot_time_from,'%H:%i') as ot_from
                    , date_format(a.ot_time_to,'%H:%i') as ot_to
                    , a.ot_hour
                    , a.ot_calculate
                    , a.ot_description
                    , a.ot_approve_dt
                    , a.ot_approve_by 
                    , ( case
                            when a.rejected_by is not null then 'rejected'
                            when a.ot_approve_by is not null then 'approved'
                            when a.spv_approved_by = '$user_name' then 'approved' 
                            when a.mgr_approved_by = '$user_name' then 'approved' 
                            else '' end ) as overtime_status 
                    from tb_r_overtime a inner join tb_m_employee b 
                    on a.employee_id = b.employee_id where 
                    (b.user_name = '$user_name' or b.supervisor1 = '$user_name' or b.supervisor2 = '$user_name') 
                    and 
                    a.rejected_by is null and
                    a.spv_approved_by is null and
                    a.mgr_approved_by is null and
                    a.ot_approve_by is null and
                    a.created_dt >= '$pastdate' and
                    a.created_dt < DATE_ADD('$nowdate',INTERVAL 1 DAY) 
                    order by a.ot_dt desc";
                
        $data = $this->db->query($sql);
        echo json_encode($data->result());
        }
    }
    public function setaprove(){
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $ot_id	= $this->input->get('ot_id');
        $employee_id = $this->input->get('employee_id');
        
        $sql ="select user_name
                , user_group 
            from tb_m_employee 
            where employee_id = '$employee_id'";
        
        $data = $this->db->query($sql);
        $approved_by = $data->row()->user_name;
        
        $sql ="select 
                a.supervisor1
                , a.supervisor2
                , b.spv_approved_dt
                , b.spv_approved_by 
                , b.mgr_approved_dt
                , b.mgr_approved_by
            from tb_m_employee a 
            inner join tb_r_overtime b 
            on a.employee_id = b.employee_id 
            where b.ot_id = '$ot_id'";
        $data = $this->db->query($sql);
        $aprove_mgr = $data->row()->mgr_approved_by;
        $aprove_spv = $data->row()->spv_approved_by;
        $approved_dt = date('Y-m-d H:i:s');
        if($aprove_spv == null || $aprove_spv == ''){
            $supervisorone = $data->row()->supervisor1;    
            $aprove_spv_dt = date('Y-m-d H:i:s');
            
        }else{
            $supervisorone = $aprove_spv;
            $aprove_spv_dt = $data->row()->spv_approved_dt;    
        }
        if($aprove_mgr == null || $aprove_mgr == ''){
            $supervisortwo = $data->row()->supervisor2;
            $aprove_mgr_dt = date('Y-m-d H:i:s');
        }else{
            $supervisortwo = $aprove_mgr;
            $aprove_mgr_dt = $data->row()->mgr_approved_dt;    
        }
        
        if ($approved_by == $supervisortwo){
            try {
                $sql = "
                UPDATE
                    tb_r_overtime
                set
                    spv_approved_dt = '$aprove_spv_dt'
                    , spv_approved_by = '$supervisorone'
                    , mgr_approved_dt = '$aprove_mgr_dt'
                    , mgr_approved_by = '$supervisortwo'
                where
                    ot_id = '$ot_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "overtime has been Aproved"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to aprove overtime"
                                    )));
            }
        }else  if ($approved_by == $supervisorone){
            try {
                $sql = "
                UPDATE
                    tb_r_overtime
                set
                     spv_approved_dt = '$aprove_spv_dt'
                    , spv_approved_by = '$supervisorone'
                where
                    ot_id = '$ot_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "overtime has been Aproved"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to aprove overtime"
                                    )));
            }  
        }else{
            try {
                $sql = "
                UPDATE
                    tb_r_overtime
                set
                     spv_approved_dt = '$aprove_spv_dt'
                    , spv_approved_by = '$supervisorone'
                    , mgr_approved_dt = '$aprove_mgr_dt'
                    , mgr_approved_by = '$supervisortwo'
                    , ot_approve_dt = '$approved_dt'
                    , ot_approve_by = '$approved_by'
                where
                    ot_id = '$ot_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "overtime has been Aproved"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to aprove overtime"
                                    )));
            }
        }
     }
     public function setreject(){
        header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
        $ot_id = $this->input->get('ot_id');
        $employee_id = $this->input->get('employee_id');
        $sql ="select user_name, user_group from tb_m_employee where employee_id = '$employee_id'";
        $data = $this->db->query($sql);
        $approved_by = $data->row()->user_name;
        $approved_dt = date('Y-m-d H:i:s');
        $sql ="select 
            a.employee_id
            , a.user_name
            , a.user_group
            , ( case when b.position_name is null then 'HRD' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data = $this->db->query($sql);
        $user_name = $data->row()->user_name;
        $user_group = $data->row()->user_group;
        $position_name = strtolower($data->row()->position_name);
        if (($user_group == 'employee' ||  $user_group == 'employee_app') && $position_name == 'hrd') {
            $position_name = 'employee';
        }
        $position_name = strtoupper($position_name);
             try {
                $sql = "
                UPDATE
                    tb_r_overtime
                set
                    rejected_dt = '$approved_dt'
                    , rejected_by = '$approved_by'
                    , rejected_position = '$position_name'
                where
                    ot_id = '$ot_id'
                    ";      
            $this->db->query($sql);
                                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "info",
                                    'msgText' => "overtime has been Rejected"
                                    )));
            } catch (Exception $e) {
                return $this->output
                                ->set_content_type('application/json')
                                ->set_output(json_encode(array(
                                    'msgType' => "warning",
                                    'msgText' => "Failed to Rejected overtime"
                                    )));
            }   
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
					date_format(ot_time_from,'%H:%i') as ot_from,
					date_format(ot_time_to,'%H:%i') as ot_to,
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
		header("content-type: application/json");
        date_default_timezone_set('asia/jakarta');
		$inc_rest = $this->input->get('inc_rest');
		if ($inc_rest == '' || $inc_rest == null)
		{
			$inc_rest = 0;	
		}
		$ot_id	= $this->input->get('ot_id');
		$ot_dt	= $this->input->get('ot_dt');
		$ot_description	= $this->input->get('ot_description');
		$ot_time_from = $this->input->get('ot_time_from');
        $ot_time_to = $this->input->get('ot_time_to');
		$username	= $this->input->get('username');
		$employee_id	= $this->input->get('employee_id');
		$app_due_dt = $this->input->get('approval_due_dt');
		

		$sql ="select 
            a.employee_id
            , a.employee_name
            , a.user_name
            , a.user_group
            , a.work_email
            , ( case when b.position_name is null then 'ADMIN' else b.position_name end ) as position_name
                from tb_m_employee a left join tb_m_position b on a.position_id = b.position_id where a.employee_id
                = '$employee_id'";
        $data_user = $this->db->query($sql);
        $user_name = $data_user->row()->user_name;
        $user_group = $data_user->row()->user_group;
        $work_email = $data_user->row()->work_email;
        $employee_name = $data_user->row()->employee_name;
        $position_name = strtolower($data_user->row()->position_name);
        if ($user_group == 'employee' ||  $user_group == 'employee_app'){
            $position_name = 'employee';
        }
        // Calculate ot hour
        $_from = strtotime($ot_dt . ' ' . $ot_time_from);
        $_to = strtotime($ot_dt . ' ' . $ot_time_to);

        $time_from = date("Y-m-d H:i:s", $_from);
        $time_to = date("Y-m-d H:i:s", $_to);

        if ($_from > $_to) {
            $dt = date_create($time_to);
            date_add($dt, date_interval_create_from_date_string("1 days"));
            $time_to = date_format($dt, "Y-m-d H:i:s");
        }
        //
        $ot_hour = $this->calc_ot_hours($time_from, $time_to, $inc_rest, $employee_id, $ot_dt);
        $ot_calculate = 0;//$this->calculate_ot($ot_hour, $ot_dt);
        
        $data['employee_id'] = $employee_id;
        $data['ot_dt'] = $ot_dt;
        $data['ot_time_from'] = $time_from;
        $data['ot_time_to'] = $time_to;
        $data['ot_hour'] = $ot_hour;
        $data['ot_calculate'] = $ot_calculate;
        $data['ot_description'] = $ot_description;
        $data['breaktime_flag'] = $inc_rest;

        if ($ot_id == "") {
            $cnt_workday = $this->mdl->count_workday($employee_id, date('Y-m-d'));

            if ($app_due_dt != "") {
                $approval_due_dt = $app_due_dt;
            } else {
                $due_dt = date_create(date("Y-m-d H:i:s"));
                //date_add($due_dt, date_interval_create_from_date_string( $cnt_workday." days" ));
                date_add($due_dt, date_interval_create_from_date_string("7 days"));
                $approval_due_dt = date_format($due_dt, "Y-m-d");
            }

            $data['approval_due_dt'] = $approval_due_dt;
            //insert 
            $data['created_by'] = $username;
            $data['created_dt'] = date('Y-m-d H:i:s');

            $check_ot = $this->mdl->check_ot($employee_id, $data['ot_dt']);
            
            if ($check_ot < 1) {
                
                $result = $this->mdl->insert($data);
            	// $result = 1;
                
                if ($result == 1) {
                    try {
						$sent = $this->sent_notification($employee_id, $employee_name,$work_email);
                    }catch(Exception $e){

                    }
						return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Overtime telah disimpan.."
					)));
                } else {
                    /*$ret['msgType'] = "warning";
                    $ret['msgText'] = "Data failed to save";*/
                    return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "warning",
							'msgText' => "Data failed to save"
					)));
                }
            } else {
               /* $ret['msgType'] = "warning";
                $ret['msgText'] = "Data already exist";*/
                return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "warning",
							'msgText' => "Data already exist"
					)));
            }
        } else {
            //update            
            if ($app_due_dt != "" && $user_group == "administrator") {
                $data['approval_due_dt'] = $app_due_dt;
            }

            $data['changed_by'] = $username;
            $data['changed_dt'] = date('Y-m-d H:i:s');

            $res = $this->mdl->update($data, $ot_id);
            // $res = 1;
            if ($res == 1) {
                /*$ret['msgType'] = "info";
                $ret['msgText'] = "Data has been updated";*/
                return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "info",
							'msgText' => "Data Telah Di Update"
					)));
            } else {
                /*$ret['msgType'] = "warning";
                $ret['msgText'] = "Update failed";*/
                return $this->output
						->set_content_type('application/json')
						->set_output(json_encode(array(
							'msgType' => "warning",
							'msgText' => "Update failed"
					)));
            }
        }
       // echo json_encode($ret);
	}

	public function saveovertimeold()
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
	function calc_ot_hours($time_from, $time_to, $inc_rest,$employee_id, $ot_dt) {
        $diff2 = strtotime($time_to) - strtotime($time_from);
        $ot_hour2 = $diff2 / 3600;

        if ($inc_rest == 1 && $ot_hour2 > 3) {
            $_breaktime = $this->sm->get_data('config_others', 'breaktime')->value_time;
            list($hours, $minutes, $seconds) = sscanf($_breaktime, '%d:%d:%d');
            $interval = new DateInterval(sprintf('PT%dH%dM%dS', $hours, $minutes, $seconds));

            $breaktime = $interval->h . " hours " . $interval->i . " minutes";

            $dt = date_create($time_to);
            date_sub($dt, date_interval_create_from_date_string($breaktime));
            $time_to = date_format($dt, "Y-m-d H:i:s");
        }

        $diff = strtotime($time_to) - strtotime($time_from);
        $ot_hour = $diff / 3600;
        // echo $ot_hour;
        $check_schedule = $this->mdl->check_schedule($employee_id, $ot_dt);
        $check_holiday_date = $this->mdl->check_holiday_dt($ot_dt);
		// echo $check_schedule;
        if ($check_schedule != "H" && $check_schedule != "L"){
            if ($ot_hour > 3 && $check_holiday_date == false){
                $ot_hour = 3;
            }
        }
        
//        if ($ot_hour > 3){
//            $ot_hour = $ot_hour - 1;
//        }

        return $ot_hour;
    }
function sent_notification($employee_id = NULL, $employee_name = NULL, $work_email = NULL, $type = NULL) {
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


        $subject = 'overtime request';
        $emailFrom = $work_email;
        $sender = $employee_name;

        if ($type == 'notify_hrd') {
            $emailTo = $this->sm->get_data('config_others', 'hrd_email')->value_txt;
            $emailCc = $this->sm->get_data('config_others', 'hrd_email')->value_txt;

            //$message = $this->sm->get_data('notification_ot', 'content_message_hrd')->value_txt;
        } else {
            $emailTo = $this->mdl->get_email($employee_id, 'spv');
            $emailCc = $this->mdl->get_email($employee_id, 'mgr');


            $msg_content = $this->sm->get_data('notification_ot', 'content_message')->value_txt;
            //$message = str_replace("{employee_name}", $employee_name, $msg_content);
        }

        //print_r($emailTo);
        if (!empty($emailTo)) {
            $chk_emailTto = in_array("", $emailTo);
        } else {
            $chk_emailTto = 1;
        }

        if ($chk_emailTto == 0) {

            $data['type_request'] = "Overtime Request";

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
                return $this->email->print_debugger();
            }
        } else {
            return 0;
        }
    }

    /* End Overtime */
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */