package com.cyanbirds.tanlove.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.ChatActivity;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.db.ConversationSqlManager;
import com.cyanbirds.tanlove.db.FConversationSqlManager;
import com.cyanbirds.tanlove.db.IMessageDaoManager;
import com.cyanbirds.tanlove.entity.ClientUser;
import com.cyanbirds.tanlove.entity.Conversation;
import com.cyanbirds.tanlove.entity.FConversation;
import com.cyanbirds.tanlove.listener.MessageChangedListener;
import com.cyanbirds.tanlove.listener.MessageUnReadListener;
import com.cyanbirds.tanlove.manager.NotificationManager;
import com.cyanbirds.tanlove.utils.DateUtil;
import com.cyanbirds.tanlove.utils.EmoticonUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-12-26 15:24 GMT+8
 * @email 395044952@qq.com
 */
public class FMessageAdapter extends
        RecyclerView.Adapter<FMessageAdapter.ViewHolder> {

    private List<FConversation> mConversations;
    private Context mContext;
    private Conversation mConversation;

    public FMessageAdapter(Context context, Conversation conversation, List<FConversation> mConversations) {
        this.mConversations = mConversations;
        this.mConversation = conversation;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mConversations == null ? 0 : mConversations.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FConversation conversation = mConversations.get(position);
        if(!TextUtils.isEmpty(conversation.localPortrait)){
            if (conversation.localPortrait.startsWith("res://")) {//官方头像
                holder.mPortrait.setImageURI(Uri.parse(conversation.localPortrait));
            } else {
                holder.mPortrait.setImageURI(Uri.parse("file://" + conversation.localPortrait));
            }
        }
        holder.mTitle.setText(conversation.talkerName);
        holder.mContent.setText(Html.fromHtml(
                EmoticonUtil.convertExpression(conversation.content==null ? "" : conversation.content),
                EmoticonUtil.conversation_imageGetter_resource, null));

        holder.mUpdateTime.setText(DateUtil.longToString(conversation.createTime));
        holder.mUnreadCount.setVisibility(View.GONE);
        if (conversation.unreadCount != 0) {
            holder.mUnreadCount.setVisibility(View.VISIBLE);
            if (conversation.unreadCount >= 100) {
                holder.mUnreadCount.setText(String.valueOf("99+"));
            } else {
                holder.mUnreadCount.setText(String
                        .valueOf(conversation.unreadCount));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_f_message, parent, false));
    }


    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnLongClickListener{
        SimpleDraweeView mPortrait;
        TextView mUnreadCount;
        ImageView mRedPoint;
        TextView mTitle;
        TextView mUpdateTime;
        TextView mContent;
        LinearLayout mItemMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            mPortrait = (SimpleDraweeView) itemView.findViewById(R.id.portrait);
            mUnreadCount = (TextView) itemView.findViewById(R.id.un_read_number);
            mRedPoint = (ImageView) itemView.findViewById(R.id.red_point);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mUpdateTime = (TextView) itemView.findViewById(R.id.update_time);
            mContent = (TextView) itemView.findViewById(R.id.content);
            mItemMsg = (LinearLayout) itemView.findViewById(R.id.item_msg);
            mItemMsg.setOnClickListener(this);
            mItemMsg.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mConversations.size() > position && position > -1) {
                FConversation conversation = mConversations.get(position);
                mConversation.unreadCount = mConversation.unreadCount - conversation.unreadCount;
                if (mConversation.unreadCount < 0) {
                    mConversation.unreadCount = 0;
                }
                ConversationSqlManager.getInstance(mContext).updateConversation(mConversation);
                MessageChangedListener.getInstance().notifyMessageChanged(null);//通知真实用户会话未读数减少

                conversation.unreadCount = 0;
                mConversations.set(position, conversation);
                FConversationSqlManager.getInstance(mContext).updateConversation(conversation);
                MessageUnReadListener.getInstance().notifyDataSetChanged(0);

                Intent intent = new Intent(mContext, ChatActivity.class);
                ClientUser clientUser = new ClientUser();
                clientUser.face_local = conversation.localPortrait;
                clientUser.user_name = conversation.talkerName;
                clientUser.userId = conversation.talker;
                clientUser.face_url = conversation.faceUrl;
                intent.putExtra(ValueKey.USER, clientUser);
                intent.putExtra(ValueKey.CONVERSATION, conversation);
                intent.putExtra(ValueKey.USER_ID, mConversation.talker);
                mContext.startActivity(intent);
                mUnreadCount.setVisibility(View.GONE);
                mRedPoint.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            final int position = getAdapterPosition();
            new AlertDialog.Builder(mContext)
                    .setItems(
                            new String[] {
                                    mContext.getResources().getString(
                                            R.string.delete_conversation),
                                    mContext.getResources().getString(
                                            R.string.delete_all_conversation)},
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            FConversationSqlManager.getInstance(mContext).deleteConversationById(mConversations.get(position));
                                            IMessageDaoManager.getInstance(mContext).deleteIMessageByConversationId(mConversations.get(position).id);
                                            mConversations.remove(position);
                                            notifyDataSetChanged();
                                            MessageUnReadListener.getInstance().notifyDataSetChanged(0);
                                            break;
                                        case 1:
                                            FConversationSqlManager.getInstance(mContext).deleteAllConversation();
                                            IMessageDaoManager.getInstance(mContext).deleteAllIMessage();
                                            mConversations.clear();
                                            notifyDataSetChanged();
                                            MessageUnReadListener.getInstance().notifyDataSetChanged(0);
                                            NotificationManager.getInstance().cancelNotification();
                                            break;
                                    }
                                    dialog.dismiss();

                                }
                            }).setTitle("操作").show();
            return true;
        }
    }

    public void setConversations(List<FConversation> conversations){
        this.mConversations = conversations;
        this.notifyDataSetChanged();
    }
}
