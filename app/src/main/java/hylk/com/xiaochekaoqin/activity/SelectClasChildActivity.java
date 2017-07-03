package hylk.com.xiaochekaoqin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.JiLuBean;
import hylk.com.xiaochekaoqin.bean.UserBean;
import hylk.com.xiaochekaoqin.dao.ClassDao;
import hylk.com.xiaochekaoqin.dao.UserDao;
import hylk.com.xiaochekaoqin.global.MyApplication;
import hylk.com.xiaochekaoqin.utils.LogUtil;
import hylk.com.xiaochekaoqin.utils.PrefUtils;


/**
 * Created by wenke on 2017/4/7.
 */

public class SelectClasChildActivity extends BaseActivity implements Selectclasschildadapter.SelectListener, View.OnClickListener {

    TextView title;
    RecyclerView recyclerView;
    ImageView back;

    private List<JiLuBean> JiLuList;
    private static final String Key_JiLu = "Key_JiLu";

    private PrefUtils sp;

    private Selectclasschildadapter adapter;
    private int stationid;
    private List<UserBean> list;
    private int positions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sch_selectclaschildactivity);
        title = (TextView) findViewById(R.id.title_tv);
        recyclerView = (RecyclerView) findViewById(R.id.rv_select_class_child);
        back = (ImageView) findViewById(R.id.title_back_iv);
        back.setOnClickListener(this);
        init();
    }

    protected void init() {
        sp = new PrefUtils();
        String classinfoName = getIntent().getStringExtra("selectclassname");
        int classinfoid = getIntent().getIntExtra("selectclassid",0);
        int flag = getIntent().getIntExtra("fangxiangFlag",0);
        JiLuList = (List<JiLuBean>) PrefUtils.queryForSharedToObject(MyApplication.getContext(), Key_JiLu);
//        stationid = getIntent().getIntExtra("stationid",0);
//        int kgid = getIntent().getIntExtra("KgId",0);
        UserDao userDao = new UserDao(this);
        list = userDao.queryUser(classinfoid,1,classinfoName);
        title.setText(classinfoName);
        if (list == null || list.size() == 0){
            recyclerView.setVisibility(View.VISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("提示：").setMessage("本班没有幼儿。");
            builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SelectClasChildActivity.this.finish();
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } else {
            if (JiLuList != null){
                List<Integer> useridList = new ArrayList<>();
                for (JiLuBean bean : JiLuList){
                    if (bean.getClassid() == classinfoid){
                        useridList.add(bean.getUserid());
                    }
                }
                if (useridList.size() != 0){
                    for (int i = 0; i < list.size(); i++){
                        for (int j = 0; j < useridList.size(); j++){
                            if (list.get(i).getUserId() == useridList.get(j)){
                                list.remove(i);
                                i--;
                                useridList.remove(j);
                                break;
                            }
                        }
                    }
                }
            }
            initadapter(list,flag);
        }
    }

    private void initadapter(final List<UserBean> list,int flag) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Selectclasschildadapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setSelectListener(SelectClasChildActivity.this);
    }

    @Override
    public void OnClickListener(int position) {
        ClassDao classDao = new ClassDao(SelectClasChildActivity.this);
        Child bean = classDao.queryChild(list.get(position).getUserId());
        Intent intent = new Intent();
        intent.putExtra("selectObject",bean);
        setResult(1,intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
