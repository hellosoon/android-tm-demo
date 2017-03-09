<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Created by Soon
 * www.so-on.cn
 * Date: 2016/10
 */
class Tm_model extends CI_Model
{
    /**
     * 添加温度信息
     * @param $user
     * @return bool
     */
    public function add_temperature($user)
    {
        if (empty($user)) {
            return 'error';
        }
        $insertTemperatureData = array(
            'id' => md5(time() . rand(1000, 9999)),
            'user_id' => $user,
            'temperature' => $this->input->post('temperature', true),
            'time' => time(),
            'ip' => $this->input->ip_address()
        );
		$this->db->insert('tm_data', $insertTemperatureData);
        return 'success';
    }

    public function get_temperature($user, $count = '')
    {
        if (empty($user)) {
            return array(
                'state' => 'error',
            );
        }
        $this->db->where('user_id', $user);

        $start_time = $this->input->post('start_time', true);
        $end_time = $this->input->post('end_time', true);

        if (!empty($start_time) && !empty($end_time)) {
            $this->db->where('time >', $start_time);
            $this->db->where('time <', $end_time);
        }
        if (!empty($count)) {
            $this->db->limit($count);
        }
        $query = $this->db->select('user_id, temperature, time')
            ->order_by('time', 'DESC')
            ->get('tm_data');
        $res = $query->result_array();
       $data = array(
            'state' => 'success',
            'count' => count($res),
            'data' => $res
        );
        return $data;
    }

    public function dget_temperature($user, $count = 10)
    {
        if (empty($user)) {
            return array();
        }
        $query = $this->db->select('user_id, temperature, time')
            ->where('user_id', $user)
            ->order_by('time', 'DESC')
            ->limit($count)
            ->get('tm_data');
        $res = $query->result_array();
        $data = array(
            'count' => count($res),
            'data' => $res
        );
        return $data;
    }
}