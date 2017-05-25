package hylk.com.xiaochekaoqin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import hylk.com.xiaochekaoqin.R;


/**
 * Created by wenke on 2017/5/16.
 */

public class LookRecordActivity extends BaseActivity implements View.OnClickListener {

    TextView title;
    RecyclerView recyclerView;
    ImageView back;

    private LookRecordAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sch_lookrecord_activity);
        title = (TextView) findViewById(R.id.title_tv);
        recyclerView = (RecyclerView) findViewById(R.id.rv_record);
        back = (ImageView) findViewById(R.id.title_back_iv);
        back.setOnClickListener(this);
        init();
    }

    protected void init() {
        title.setText("查看记录");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LookRecordAdapter();
        recyclerView.setAdapter(adapter);
        LogUtils.d("adapter.getItemCount();" + adapter.getItemCount());
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
