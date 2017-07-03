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

import java.util.List;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.bean.JiLuBean;
import hylk.com.xiaochekaoqin.global.MyApplication;
import hylk.com.xiaochekaoqin.utils.PrefUtils;


/**
 * Created by wenke on 2017/5/16.
 */

public class LookRecordActivity extends BaseActivity implements View.OnClickListener, LookRecordAdapter.OnItemListener {

    TextView title;
    RecyclerView recyclerView;
    ImageView back;

    private List<JiLuBean> list;
    private static final String Key_JiLu = "Key_JiLu";

    private LookRecordAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sch_lookrecord_activity);

        title = (TextView) findViewById(R.id.title_tv);
        recyclerView = (RecyclerView) findViewById(R.id.rv_record);
        back = (ImageView) findViewById(R.id.title_back_iv);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

        list = (List<JiLuBean>) PrefUtils.queryForSharedToObject(MyApplication.getContext(), Key_JiLu);

        init();
    }

    protected void init() {
        title.setText("查看记录");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LookRecordAdapter(list);
        adapter.setOnLookItemListener(this);
        recyclerView.setAdapter(adapter);
        LogUtils.d("adapter.getItemCount();" + adapter.getItemCount());
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void setOnItemListener(int position) {
        JiLuBean bean = list.get(position);
        int userid = bean.getUserid();
        Intent intent = new Intent();
        intent.putExtra("userid", userid);
        setResult(2, intent);
        finish();
    }
}
