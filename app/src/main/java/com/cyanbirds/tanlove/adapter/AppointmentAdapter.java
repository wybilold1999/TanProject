package com.cyanbirds.tanlove.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.entity.AppointmentModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-12-26 18:34 GMT+8
 * @email 395044952@qq.com
 */
public class AppointmentAdapter extends
        RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<AppointmentModel> mAppointmentModels;
    private Context mContext;
    private boolean mShowFooter = false;

    private OnItemClickListener mOnItemClickListener;

    public AppointmentAdapter(List<AppointmentModel> appointmentModels, Context mContext) {
        this.mAppointmentModels = appointmentModels;
        this.mContext = mContext;
    }



    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        AppointmentModel model = mAppointmentModels.get(position);
        if(model == null){
            return;
        }
        holder.appointmentTime.setText(model.time);
        holder.portrait.setImageURI(Uri.parse(model.faceUrl));
        holder.userName.setText(model.userByName);
        holder.theme.setText(model.theme);
        holder.status.setText(AppointmentModel.getStatus(model.status));
    }

    @Override
    public int getItemCount() {
        return mAppointmentModels == null ? 0 : mAppointmentModels.size();
    }

    public AppointmentModel getItem(int position){
        if (mAppointmentModels == null || mAppointmentModels.size() < 1) {
            return null;
        }
        return mAppointmentModels == null ? null : mAppointmentModels.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView portrait;
        TextView userName;
        TextView theme;
        TextView appointmentTime;
        TextView status;
        public ViewHolder(View itemView) {
            super(itemView);
            portrait = (SimpleDraweeView) itemView.findViewById(R.id.portrait);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            theme = (TextView) itemView.findViewById(R.id.appointment_theme);
            appointmentTime = (TextView) itemView.findViewById(R.id.appointment_time);
            status = (TextView) itemView.findViewById(R.id.applay_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAppointmentModels(List<AppointmentModel> list) {
        mAppointmentModels = list;
        notifyDataSetChanged();
    }
}
