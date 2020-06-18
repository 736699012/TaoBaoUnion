package com.example.taobaounion.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taobaounion.R;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_flow)
    public TextFlowLayout mTextFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        List<String> list = new ArrayList<>();
        list.add("路飞");
        list.add("罗罗诺亚 索隆");
        list.add("山治");
        list.add("妮可罗宾");
        list.add("小贼猫娜美");
        list.add("妮可罗宾");
        list.add("海侠甚平");
        list.add("布鲁克");
        list.add("妮可罗宾");
        list.add("海侠甚平");
        list.add("布鲁克");
        mTextFlowLayout.setTextList(list);
        mTextFlowLayout.setOnFlowItemClickListen(new TextFlowLayout.OnFlowItemClickListen() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtils.d(TestActivity.this,"  "+text);
            }
        });
    }
}
