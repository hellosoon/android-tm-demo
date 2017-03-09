<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Created by Soon
 * www.so-on.cn
 * Date: 2016/10
 */
class user extends CI_Controller
{

    public function auth()
    {
        $token = $this->input->post('token', true);
        echo json_encode($this->Auth_model->auth($token));
    }

    /**
     * 登录
     */
    public function login()
    {
        $acc = $this->input->post('acc', true);
        $psw = $this->input->post('psw', true);
        $res = $this->Auth_model->login($acc, $psw);
        $data = array(
            'state' => $res['state'],
            'info' => $res['info'],
            'data' => $res['data']
        );
        echo base64_encode(json_encode($data));
    }

    /**
     * 登出
     */
    public function logout()
    {
        $token = $this->input->post('token', true);
        $res = $this->Auth_model->logout($token);
        $data = array(
            'state' => $res['state'],
            'info' => $res['info']
        );
        echo base64_encode(json_encode($data));
    }

    public function register()
    {
        $this->load->model('User_model');
        $res = $this->User_model->register();
//        $data = array(
//            'state' => $res['state'],
//            'info' => $res['info']
//        );
        echo $res['state'];
    }
	
	    public function update()
    {
		$token = $this->input->post('token', true);
        $auth_info = $this->Auth_model->auth($token);
        if (trim($auth_info['state']) == 'success') {
         $this->load->model('User_model');
        $res = $this->User_model->update_info($auth_info['user']);
        echo $res['state'];
        } else {
            echo 'error';
        }

    }
		    public function getUserInfo()
    {
		$token = $this->input->post('token', true);
        $auth_info = $this->Auth_model->auth($token);
        if (trim($auth_info['state']) == 'success') {
         $this->load->model('User_model');
        $res = $this->User_model->get_user_info($auth_info['user']);
        $data = array(
            'state' => $res['state'],
            'info' => $res['info'],
            'data' => $res['data']
        );
        echo base64_encode(json_encode($data));
        } else {
            echo 'error';
        }

    }
}
