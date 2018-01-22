package com.cyanbirds.tanlove.entity;

import static com.cyanbirds.tanlove.entity.AppointmentModel.AppointStatus.ACCEPT;
import static com.cyanbirds.tanlove.entity.AppointmentModel.AppointStatus.DECLINE;
import static com.cyanbirds.tanlove.entity.AppointmentModel.AppointStatus.OUT_TIME;
import static com.cyanbirds.tanlove.entity.AppointmentModel.AppointStatus.WAIT_CALL_BACK;

/**
 * Created by wangyb on 2018/1/22.
 * 约会model
 */

public class AppointmentModel {

    public int id;
    public String userId;     //约会者id
    public String userById;   //被约会者id
    public String userName;   //约会者名字
    public String userByName;   //被约会者名字
    public String faceUrl;    //被约会者头像
    public int status;     //约会状态
    public String theme;      //约会主题
    public String time;       //约会时间
    public double latitude;   //经度
    public double longitude;  //纬度
    public String remark;     //留言


    public static class AppointStatus {

        public static final int WAIT_CALL_BACK = 0;//等待回应
        public static final int ACCEPT = 1;//已接受
        public static final int DECLINE = 2;//已拒绝
        public static final int OUT_TIME = 3;//超时已取消
    }

    public static String getStatus(int status) {
        String AppointStatus = "";
        switch (status) {
            case WAIT_CALL_BACK:
                AppointStatus = "等待回应";
                break;
            case ACCEPT:
                AppointStatus = "已接受";
                break;
            case DECLINE:
                AppointStatus = "已拒绝";
                break;
            case OUT_TIME:
                AppointStatus = "超时已取消";
                break;
        }
        return AppointStatus;
    }

}
