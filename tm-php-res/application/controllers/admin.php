<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class admin extends CI_Controller
{

    public function index()
    {
        $this->load->view('index_view');
    }

    public function user_admin()
    {
        $this->load->view('user_admin_view');
    }

    public function pass()
    {
        $acc = $this->input->post('acc', true);
        if (empty($acc)) {
            echo '非法访问';
        }
        $insertUserData['password'] = '123456';
        $query = $this->db->where('account', $acc)
            ->get('tm_user');
        if (count($query->result_array())) {
            if (count($insertUserData) == 0) {
                echo '非法访问';
            }
            $update = $this->db->where('account', $acc)
                ->update('tm_user', $insertUserData);
            echo $update ? '更新成功' : '更新失败';
        } else {
            echo '用户不存在';
        }
    }

    public function add()
    {
        $this->load->model('User_model');
        $res = $this->User_model->register();
//        $data = array(
//            'state' => $res['state'],
//            'info' => $res['info']
//        );
        echo $res['state'] == 'success' ? '更新成功' : '更新失败';
    }
}
