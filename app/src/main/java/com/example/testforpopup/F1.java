package com.example.testforpopup;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class F1 extends Fragment {
    public Button button1,button2,button3;
    public TextView count;
    private MainActivity mainActivity;
private MyHandler handler;
    private View view;
    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button1:
                    mainActivity.tv2.setText("點按了碰");
                    break;
                case R.id.button2:
                    mainActivity.tv2.setText("點按了吃");
                    break;
                case R.id.button3:
                    mainActivity.tv2.setText("點按了槓");
                    break;
            }
            mainActivity.closefragment();
        }
    };
    private Timer timer;

    public F1() {
        // Required empty public constructor
    }
    //MainThread
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            view = inflater.inflate(R.layout.fragment_f1, container, false);

            button1 = view.findViewById(R.id.button1);
            button2 = view.findViewById(R.id.button2);
            button3 = view.findViewById(R.id.button3);
            count = view.findViewById(R.id.count);

            button1.setOnClickListener(clickListener);
            button2.setOnClickListener(clickListener);
            button3.setOnClickListener(clickListener);

        handler=new MyHandler();

        if(getArguments()!=null){
            //寫判斷什麼時候要讓按鈕不見
            //取出arguments裡的bundle,並取出key為name的布林陣列
            boolean[] temp=getArguments().getBooleanArray("name");

            setButton(temp[0],temp[1],temp[2]);
        }
            //timer任務,7秒後調用mainActivity的關閉本fragment方法
            //這裡一個執行緒
            timer = new Timer();
            timer.schedule(new MytimerTask(), 6000);

            //這裡也一個執行緒,它會調用實做runnable物件的run方法
        new Thread(new countdown()).start();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity=(MainActivity)context;
    }


    //fragment被remove掉時,把timer物件消滅,目的在於連續點擊時不會出現多個timertask
    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
        super.onDestroy();
    }

    private class MytimerTask extends TimerTask{
        @Override
        public void run() {
            mainActivity.closefragment();
            mainActivity.tv2.setText("什麼也不幹");
        }
    }
    public class countdown implements Runnable{

        @Override
        public void run() {
            Bundle bundle=new Bundle();
            for(int i=5;i>=0;i--){
                Message msg=new Message();//注意在sendmessage後,Handler 會將 Message 加入 MessageQueue 中，造成下次要處理的message，已經不是原來的對象,
                //因此每使用後,都要new一個出來,不然會出現 This message is already in use.的錯誤
                try {
                    bundle.putInt("time",i);
                    msg.setData(bundle);
                    handler.sendMessage(msg);//每秒丟出一個bundle給handler,注意每次都要丟給沒用過的message物件
                    //要提的是,似乎message物件有使用的上限?
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }}
    }

    //用來處理每次收到數據要怎麼更新自己頁面
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
//            每次收到數據
            Bundle b = msg.getData();
            int time = b.getInt("time");
            count.setText("剩餘:"+time+"秒");
        }
    }

    public static F1 createF1(boolean...params){
        F1 f1=new F1();
        Bundle bundle=new Bundle();
        boolean[] temp=new boolean[3];
        List<Boolean> temp2=new ArrayList<>();
        for (boolean param:params
             ) {
            temp2.add(param);
            Log.v("wei","傳進來:"+param);
        }
        for (int i=0;i<temp2.size();i++){
            temp[i]=temp2.get(i);
        }

        bundle.putBooleanArray("name",temp);
        f1.setArguments(bundle);
        return f1;
    }

    public void setButton(boolean a,boolean b,boolean c){
        if (a) button1.setVisibility(View.INVISIBLE);
        if (b) button2.setVisibility(View.INVISIBLE);
        if (c) button3.setVisibility(View.INVISIBLE);
    }
}
