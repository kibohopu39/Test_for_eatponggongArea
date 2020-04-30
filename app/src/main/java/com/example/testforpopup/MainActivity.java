package com.example.testforpopup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private Button show;
    private FragmentManager frgm=getSupportFragmentManager();
    public FragmentTransaction frgT;
    private F1 f1;
    public TextView tv1,tv2;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1=findViewById(R.id.word);
        tv2=findViewById(R.id.word2);
        show=findViewById(R.id.random);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

    }

    public void show() {
        tv1.setText("");
        tv2.setText("");
        num = (int) (Math.random() * 4 + 1);
        tv1.setText("現在數字:"+ num);
        if(num==1){
            if(frgm.getFragments().size()<1) {//getFragment是獲得目前存放在frgm裡Fragment的陣列
                // 長度大小要小於1才能執行,這樣就保證已經fragment叫進來的情況不會被再叫進來一次
                f1 = F1.createF1(true,false,true);//
                frgm.beginTransaction().add(R.id.container, f1).commit();
            }else{
                Log.v("wei","已經有跑出來了");
        }}
    }

    //做一個監聽器讓它監聽show出來的文字,假如文字符合,就把F1呼叫出來
    //監聽器做法
    //先做一個要被監聽的類別
    //在其內部做個實做被監聽的方法---->就是把做出來的監聽器賦予這個類別的監聽器,預設給它null
    //
    //做個介面定義監聽要做的事情------>因為監聽概念就是偵測到你要監聽的變化,但實做會看情況而改變
    //如果有監聽器類別的話,可以實做這個介面
    //準備工作到這裡....

    //實做的話,先到要執行的activity,做出監聽器物件
    //在被監聽類別中,只要有做的方法,都在下面呼叫監聽器物件的方法
    //那麼,只要被監聽的類別有註冊(事實上就是自己設計賦予
    //該類別只要呼叫其方法,就會觸發監聽的方法,從而達到監聽的效果




    //寫一個關閉fragment的方法,然後由fragment裡的按鈕呼叫
    //不寫在fragment的原因在
    //1.fragment沒有finish方法
    //2.目前可以調用Transaction裡的方法移除,但若把Transaction寫在fragment
    //會沒辦法把自己所在的fragment remove
    //3.注意commit()前,要再做一次beginTransaction(),不然會跳出異常
    public void closefragment() {
        frgT=frgm.beginTransaction();
        frgT.remove(f1).commit();

    }




}
