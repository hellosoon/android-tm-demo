<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Created by Soon
 * www.so-on.cn
 * Date: 2016/10
 */
class Auth_model extends CI_Model
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    /**
     * 检验是否登录
     * 返回登录状态和登录人信息
     * @param $token
     * @return array
     */
    public function auth($token = '')
    {
        if (empty($token)) {
            return array('state' => 'error', 'info' => '非法访问');
        }
        $query = $this->db->where('auth_token', $token)
            ->get('tm_user');
        $result = $query->result_array();
        if (count($result) == 0) {
            return array('state' => 'error', 'info' => '未登录');
        } else {
            $user = $result[0]['account'];
            return array('state' => 'success', 'user' => $user, 'info' => $result[0]);
        }
    }

    /**
     * 登录
     * @param $acc
     * @param $psw
     * @return array
     */
    public function login($acc, $psw)
    {
        if (empty($acc) or empty($psw)) {
            return array('state' => 'error', 'info' => '非法访问');
        }
        $query = $this->db->where('account', $acc)
            ->where('password', $psw)
            ->get('tm_user');
        $query_result = $query->result_array();
        if (count($query_result) == 0) {
            return array('state' => 'error', 'info' => '帐号或密码错误');
        } else {
            $token = md5($acc . $psw . time());//生成token
            $data = array(
                'last_login_time' => time(),
                'last_login_ip' => $this->input->ip_address(),
                'auth_token' => $token
            );
            $user_data = array(
                'user_id' =>  $query_result[0]['account'],
                'user_name' =>  $query_result[0]['name'],
                'user_age' =>  $query_result[0]['age'],
                'user_sex' =>  $query_result[0]['sex'],
                'user_role' =>  $query_result[0]['role'],
            );
            $insert = $this->db->where('account', $acc)
                ->update('tm_user', $data);//保存token
            if ($insert == 0) {
                return array('state' => 'error', 'info' => '系统错误');
            } else {
                return array('state' => 'success', 'info' => $token,'data' => $user_data);
            }

        }

    }

    /**
     * 登出
     * @param $token
     * @return array
     */
    public function logout($token = '')
    {
        if (empty($token)) {
            return array('state' => 'error', 'info' => '非法访问');
        }
        $data = array(
            'auth_token' => ''
        );
        $insert = $this->db->where('auth_token', $token)
            ->update('tm_user', $data);//清空token
        if ($insert == 0) {
            return array('state' => 'error', 'info' => '登出失败');
        } else {
            return array('state' => 'success', 'info' => '登出成功');
        }
    }
}