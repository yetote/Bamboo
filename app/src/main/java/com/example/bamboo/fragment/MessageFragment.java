package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bamboo.ApplyContactActivity;
import com.example.bamboo.ChatActivity;
import com.example.bamboo.FriendActivity;
import com.example.bamboo.R;
import com.example.bamboo.adapter.MessageListAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.MessageListModel;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.util.MessageUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 消息页面
 * @time 2018/12/24 17:24
 * @change
 * @chang time
 * @class describe
 */
public class MessageFragment extends Fragment {
    private RecyclerView rv;
    private ArrayList<MessageListModel> list;
    private MessageListAdapter adapter;
    private Toolbar toolbar;
    private EMMessageListener msgListener;
    public static final int HANDLER_MESSAGE_CODE = 1;
    private static final String TAG = "MessageFragment";
    private ImageView apply;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, null);
        init(v);

        onClick();

        toolbar.inflateMenu(R.menu.message_toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.message_menu_people:
                    if (MyApplication.isLogin) {
                        Intent i = new Intent();
                        i.putExtra("u_name", MyApplication.uName);
                        i.setClass(getActivity(), FriendActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            return false;
        });

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.setAdapter(adapter);

        adapter.setOnClickListener((obj, position, tag) -> {
            MessageListModel model = (MessageListModel) obj;
            Intent i = new Intent();
            i.putExtra("u_id", model.getUserID());
            i.putExtra("u_name", model.getUser());
            i.putExtra("u_header", model.getHeads());
            i.setClass(getActivity(), ChatActivity.class);

            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(model.getUser());
            //指定会话消息未读数清零
            conversation.markAllMessagesAsRead();
            list.get(position).setMsgNum(0);
            adapter.notifyDataSetChanged();
            startActivity(i);
        });


        if (MyApplication.isLogin) {
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            Log.e(TAG, "onCreateView:1 " + conversations.size());
            if (conversations.size() != 0) {
                for (String username : conversations.keySet()) {
                    EMConversation conversation = conversations.get(username);
                    int num = EMClient.getInstance().chatManager().getConversation(username).getUnreadMsgCount();
                    EMMessage msg = EMClient.getInstance().chatManager().getConversation(username).getLastMessage();
                    String text = "";
                    if (msg.getType() == EMMessage.Type.TXT) {
                        text = ((EMTextMessageBody) msg.getBody()).getMessage();
                    }
                    list.add(new MessageListModel("", username, text, "", 1, msg.getMsgTime(), 1));
                }
            }
        }

        receiveMsg();

        return v;
    }

    private void onClick() {
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ApplyContactActivity.class));
            }
        });
    }

    private void receiveMsg() {
//        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//        Log.e(TAG, "onCreateView:2 " + conversations.size());
//        if (conversations.size() != 0) {
//            for (String username : conversations.keySet()) {
//                EMConversation conversation = conversations.get(username);
//                int num = EMClient.getInstance().chatManager().getConversation(username).getUnreadMsgCount();
//                EMMessage msg = EMClient.getInstance().chatManager().getConversation(username).getLastMessage();
//                String text = "";
//                if (msg.getType() == EMMessage.Type.TXT) {
//                    text = ((EMTextMessageBody) msg.getBody()).getMessage();
//                }
//                list.add(new MessageListModel("", username, text, "", 0, msg.getMsgTime(), num));
//            }
//            adapter.notifyDataSetChanged();
//        }


        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                Log.e(TAG, "onMessageReceived: " + messages.toString());
                Observable.create((ObservableOnSubscribe<List<EMMessage>>) emitter -> emitter.onNext(messages))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(emMessages -> {
                            for (int i = 0; i < emMessages.size(); i++) {
                                EMMessage emmMessage = emMessages.get(i);
                                String from = emmMessage.getFrom();
                                if (list.size() == 0) {
                                    list.add(new MessageListModel("", from, MessageUtil.getMessageText(emmMessage), "", 1, emmMessage.getMsgTime(), 1));
                                } else {
                                    for (int j = 0; j < list.size(); j++) {
                                        if (list.get(j).getUser().equals(from)) {
                                            list.get(j).setMsgNum(list.get(i).getMsgNum() + 1);
                                            Log.e(TAG, "handleMessage: " + (list.get(i).getMsgNum()));
                                            list.get(j).setContent(MessageUtil.getMessageText(emmMessage));
                                            list.get(j).setTime(emmMessage.getMsgTime());
                                            break;
                                        } else if (j == list.size() - 1) {
                                            list.add(new MessageListModel("", from, MessageUtil.getMessageText(emmMessage), "", 1, emmMessage.getMsgTime(), 1));
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        });
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void init(View v) {
        rv = v.findViewById(R.id.fragment_message_rv);
        toolbar = v.findViewById(R.id.fragment_message_toolbar);
        apply = v.findViewById(R.id.fragment_message_apply);
        list = new ArrayList<>();

        adapter = new MessageListAdapter(getActivity(), list);
    }

}
