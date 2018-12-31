package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.bamboo.util.StatusBarUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.List;

import javax.security.auth.login.LoginException;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 聊天界面
 * @time 2018/11/28 15:45
 * @change
 * @chang time
 * @class describe
 */
public class ChatActivity extends AppCompatActivity {
    private Toolbar charToolbar;
    private EaseChatMessageList charMsgList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EaseChatInputMenu chatInputMenu;
    private static final String TAG = "ChatActivity";
    private EMMessageListener msgListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.changedStatusBar(this);
        setContentView(R.layout.activity_chat);

        initView();

        Intent i = getIntent();
        String uName = i.getStringExtra("u_name");
        String uHeader = i.getStringExtra("u_header");
        int uid = i.getIntExtra("u_id", -1);

        if (uName != null) {
            charToolbar.setTitle(uName);
            charMsgList.init(uName, EaseConstant.CHATTYPE_SINGLE, null);
        }

        charMsgList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            @Override
            public boolean onBubbleClick(EMMessage message) {
                //气泡框点击事件，EaseUI有默认实现这个事件，如果需要覆盖，return值要返回true

                return false;
            }

            @Override
            public boolean onResendClick(EMMessage message) {
                //重发消息按钮点击事件
                return false;
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                //气泡框长按事件
            }

            @Override
            public void onUserAvatarClick(String username) {
                //头像点击事件
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                //头像长按事件
            }

            @Override
            public void onMessageInProgress(EMMessage message) {
                //消息加载中事件
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> charMsgList.refresh());

        chatInputMenu.init();
        chatInputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onTyping(CharSequence s, int start, int before, int count) {
                //???
            }

            @Override
            public void onSendMessage(String content) {
                //发送文本消息
                EMMessage msg = EMMessage.createTxtSendMessage(content, uName);
                EMClient.getInstance().chatManager().sendMessage(msg);
                charMsgList.refresh();
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {

            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                //语音输入
                return false;
            }
        });


        receiveMsg();
    }

    private void receiveMsg() {
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                charMsgList.refresh();
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

    private void initView() {
        charToolbar = findViewById(R.id.char_toolbar);
        charMsgList = findViewById(R.id.char_msgRow);
        swipeRefreshLayout = charMsgList.getSwipeRefreshLayout();
        chatInputMenu = findViewById(R.id.char_input_menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
