<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Created by Soon
 * www.so-on.cn
 * Date: 2016/10
 */
class tm extends CI_Controller
{
    public function index()
    {
        echo 'error';
    }

    /**
     * 添加温度信息
     */
    public function add()
    {
		
        $token = $this->input->post('token', true);
        $auth_info = $this->Auth_model->auth($token);
        if (trim($auth_info['state']) == 'success') {
            $this->load->model('Tm_model');
			$re = $this->Tm_model->add_temperature($auth_info['user']);
            echo 'success';
        } else {
            echo 'error';
        }
    }

    /**
     * 获取温度记录，包括自定义历史记录
     * @param string $count
     */
    public function tlist($count = '')
    {
        $token = $this->input->post('token', true);
        $auth_info = $this->Auth_model->auth($token);
        if ($auth_info['state'] == 'success') {
            $this->load->model('Tm_model');
            echo base64_encode(json_encode($this->Tm_model->get_temperature($auth_info['user'], $count)));
        } else {
            echo 'error';
        }
    }
}
