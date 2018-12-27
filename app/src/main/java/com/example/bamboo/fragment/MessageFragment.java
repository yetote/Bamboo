package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bamboo.ChatActivity;
import com.example.bamboo.FriendActivity;
import com.example.bamboo.R;
import com.example.bamboo.adapter.MessageListAdapter;
import com.example.bamboo.model.MessageListModel;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private static final String TAG = "MessageFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, null);
        init(v);

        toolbar.inflateMenu(R.menu.message_toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.message_menu_people:
                    Intent i = new Intent();
                    i.setClass(getActivity(), FriendActivity.class);
                    startActivity(i);
                    break;
                default:
                    break;
            }
            return false;
        });

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        adapter.setOnClickListener(obj -> {
            MessageListModel model = (MessageListModel) obj;
            Intent i = new Intent();
            i.putExtra("u_id", model.getUserID());
            i.putExtra("u_name", model.getUser());
            i.putExtra("u_header", model.getHeads());
            i.setClass(getActivity(), ChatActivity.class);
            startActivity(i);
        });

        receiveMsg();

        return v;
    }

    private void receiveMsg() {
        //获取会话
        EMClient.getInstance().chatManager().loadAllConversations();
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                Log.e(TAG, "onMessageReceived: " + messages.toString());
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


    }

    private void init(View v) {
        rv = v.findViewById(R.id.fragment_message_rv);
        toolbar = v.findViewById(R.id.fragment_message_toolbar);

        list = new ArrayList<>();
        list.add(new MessageListModel("data:image/jpeg;base64,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",
                "Harrison Jones", "这东西应该藏在博物馆里", "star", 0, 1545709350000L, 0));
        list.add(new MessageListModel("data:image/jpeg;base64,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",
                "Alsace", "凛冬已至，冰冷刺骨", "star", 1, 1545709350000L, 72));
        list.add(new MessageListModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5iBSDmjcLAgrdOAioJzYozzP1AqKKnAcBUiewi-DbF1CNo4Zf",
                "Hagasha", "来自黑暗的灵魂啊，扭曲树木，玷污湖水，诅咒这片土地", "star", 2, 1545709350000L, 55));
        list.add(new MessageListModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa4xTUGhwi2OKN_T_GG9LlQhQYgHRe5S2C4sflCz8V6KrIc4Ol6Q   ",
                "Galush Hellscream", "我要焚烧这个世界，然后吞噬他的灰烬", "star", 3, 1545709350000L, 100));
        list.add(new MessageListModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpQ-Tf2gXPVxP4szljPtGGlki7AUefcAUFuawbp0q00u5uGsi7_A",
                "Sardwalker", "我，牙尖爪利", "star", 4, 1545709350000L, 4));
        list.add(new MessageListModel("http://img4.imgtn.bdimg.com/it/u=1834635025,2278545549&fm=26&gp=0.jpg",
                "Dr.Boom", "呼呼嘿哈哈", "star", 5, 1545709350000L, 9));
        list.add(new MessageListModel("http://img3.imgtn.bdimg.com/it/u=4103396069,1193167664&fm=26&gp=0.jpg",
                "Rodger", "我就是达拉然之力", "star", 6, 1545709350000L, 5));
        list.add(new MessageListModel("http://img5.imgtn.bdimg.com/it/u=400084580,2020607301&fm=26&gp=0.jpg",
                "Antonidas", "你需要我的帮助么", "star", 7, 1545709350000L, 1));
        list.add(new MessageListModel("http://img2.imgtn.bdimg.com/it/u=1706227310,1982342322&fm=26&gp=0.jpg",
                "Ysera", "当我入梦，这个世界就将颤抖", "star", 8, 1545709350000L, 4));
        list.add(new MessageListModel("http://img2.imgtn.bdimg.com/it/u=3859421672,511986628&fm=200&gp=0.jpg",
                "Harrison Jones", "今天天气不错", "star", 9, 1545709350000L, 2));

        adapter = new MessageListAdapter(getActivity(), list);
    }

}
