<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Created by Soon
 * www.so-on.cn
 * Date: 2016/10
 */
class User_model extends CI_Model
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    /**
     * 用户注册
     * @param $role
     * @return array
     */
    public function register($role)
    {
        $role = ((!isset($role)) or empty($role)) ? $this->config->item('base_user') : $role;
        $acc = $this->input->post('acc', true);
        $pass = $this->input->post('psw', true);
        if (empty($acc) or empty($pass)) {
            return array('state' => 'error', 'info' => '非法访问');
        }
        $insertUserData = array(
            'id' => md5(time() . rand(1000, 9999)),
            'account' => $acc,
            'password' => $pass,
            'name' => $this->input->post('name', true),
            'sex' => $this->input->post('sex', true),
            'age' => $this->input->post('age', true),
            'role' => $role,
            'register_time' => time(),
        );

        $query = $this->db->where('account', $acc)
            ->get('tm_user');
        if (count($query->result_array())) {
            return array('state' => 'error', 'info' => '用户名已存在');
        } else {
            $insert = $this->db->insert('tm_user', $insertUserData);
            if ($insert) {
                return array('state' => 'success', 'info' => '注册成功');
            }
            return array('state' => 'error', 'info' => '注册失败');
        }
    }

    /**
     * 修改个人信息
     * @param string $acc
     * @param string $role
     * @return array
     */
    public function update_info($acc = '', $role = '')
    {
        $insertUserData = array();
        $role = ((!isset($role)) or empty($role)) ? $this->config->item('base_user') : $role;
        if (empty($acc)) {
            return array('state' => 'error', 'info' => '非法访问');
        }

        if (isset($_POST['psw'])) {
            if (empty($this->input->post('psw', true)) or empty($this->input->post('pswOld', true))) {
                return array('state' => 'error', 'info' => '密码不能为空');
            }
		$query = $this->db->where('account', $acc)
            ->where('password', $this->input->post('pswOld', true))
            ->get('tm_user');
        $query_result = $query->result_array();
        if (count($query_result) == 0) {
            return array('state' => 'error', 'info' => '密码错误');
        } 
            $insertUserData['password'] = $this->input->post('psw', true);
        }
        if (isset($_POST['name'])) {
            $insertUserData['name'] = $this->input->post('name', true);
        }
        if (isset($_POST['sex'])) {
            $insertUserData['sex'] = $this->input->post('sex', true);
        }
        if (isset($_POST['age'])) {
            $insertUserData['age'] = $this->input->post('age', true);
        }
        if (!empty($role)) {
            $insertUserData['role'] = $role;
        }

        $query = $this->db->where('account', $acc)
            ->get('tm_user');
        if (count($query->result_array())) {
            if (count($insertUserData) == 0) {//更新数据为空的时候
                return array('state' => 'error', 'info' => '非法访问');
            }
            $update = $this->db->where('account', $acc)
                ->update('tm_user', $insertUserData);
            return array('state' => $update ? 'success' : 'error', 'info' => $update ? '更新成功' : '更新失败');
        } else {
            return array('state' => 'error', 'info' => '用户不存在');
        }
    }
//
//    public function get_user_info($acc)
//    {
//        if (empty($acc)) {
//            return array('state' => false, 'info' => '非法访问');
//        }
//        $res = $this->Auth_model->auth($token);
//
//    }


  public function get_user_info($acc = '')
    {
        if (empty($acc)) {
            return array('state' => 'error', 'info' => '非法访问');
        }
        $query = $this->db->where('account', $acc)
            ->get('tm_user');
        $query_result = $query->result_array();
        if (count($query_result) == 0) {
            return array('state' => 'error', 'info' => '帐号或密码错误');
        } else {
            $token = md5( time());
            $user_data = array(
                'user_id' =>  $query_result[0]['account'],
                'user_name' =>  $query_result[0]['name'],
                'user_age' =>  $query_result[0]['age'],
                'user_sex' =>  $query_result[0]['sex'],
                'user_role' =>  $query_result[0]['role'],
            );
                return array('state' => 'success', 'info' => $token,'data' => $user_data);

        }

    }
}