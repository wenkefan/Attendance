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

import java.util.List;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.UserBean;
import hylk.com.xiaochekaoqin.dao.ClassDao;
import hylk.com.xiaochekaoqin.dao.UserDao;
import hylk.com.xiaochekaoqin.utils.LogUtil;
import hylk.com.xiaochekaoqin.utils.PrefUtils;


/**
 * Created by wenke on 2017/4/7.
 */

public class SelectClasChildActivity extends BaseActivity implements Selectclasschildadapter.SelectListener, View.OnClickListener {

    TextView title;
    RecyclerView recyclerView;
    ImageView back;

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
            initadapter(list,flag);
        }
    }

    private void initadapter(final List<UserBean> list,int flag) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Selectclasschildadapter(list,flag);
        recyclerView.setAdapter(adapter);
        adapter.setSelectListener(SelectClasChildActivity.this);
    }

//    @Override
//    public void NetWorkSuccess(int Flag) {
//        if (Flag == Keyword.FLAGDOWNCAR) {//上车
//            closeDialog();
//            UpAndDownRecordData data = new UpAndDownRecordData(this);
//            UpAndDownRecordBean udBean = new UpAndDownRecordBean();
//            udBean.setKgId(list.get(positions).getKgId());
//            udBean.setClassId(list.get(positions).getClassInfoID());//班级ID
//            udBean.setChildId(list.get(positions).getUserId());//幼儿ID
//            udBean.setChildName(list.get(positions).getUserName());//幼儿姓名
//            udBean.setSACardNo(list.get(positions).getSACardNo());//卡号
//            udBean.setBusOrderId(sp.getInt(Keyword.BusOrderId));//发车单号
//            udBean.setShang(stationid);//上车站点
//            udBean.setXia(0);//下车站点
//            udBean.setIsworkShang(1);//是否上传
//            udBean.setIsworkXia(0);//是否上传
//            udBean.setIsShang(1);
//            udBean.setIsXia(0);
//            data.add(udBean);
//            ToastUtil.show(list.get(positions).getUserName() + "上车");
//            finish();
//        }
//    }
//
//    @Override
//    public void NetWorkError(int Flag) {
//        if (Flag == Keyword.ShangURL){
//            closeDialog();
//            UpAndDownRecordData data = new UpAndDownRecordData(this);
//            UpAndDownRecordBean udBean = new UpAndDownRecordBean();
//            udBean.setKgId(list.get(positions).getKgId());
//            udBean.setClassId(list.get(positions).getClassInfoID());//班级ID
//            udBean.setChildId(list.get(positions).getUserId());//幼儿ID
//            udBean.setChildName(list.get(positions).getUserName());//幼儿姓名
//            udBean.setSACardNo(list.get(positions).getSACardNo());//卡号
//            udBean.setBusOrderId(sp.getInt(Keyword.BusOrderId));//发车单号
//            udBean.setShang(stationid);//上车站点
//            udBean.setXia(0);//下车站点
//            udBean.setIsworkShang(0);//是否上传
//            udBean.setIsworkXia(0);//是否上传
//            udBean.setIsShang(1);
//            udBean.setIsXia(0);
//            data.add(udBean);
//            ToastUtil.show(list.get(positions).getUserName() + "上车");
//            finish();
//        }
//    }

    @Override
    public void OnClickListener(int position) {
        ClassDao classDao = new ClassDao(SelectClasChildActivity.this);
        Child bean = classDao.queryChild(list.get(position).getUserId());
        Intent intent = new Intent();
        intent.putExtra("selectObject",bean);
        setResult(1,intent);
        finish();

//        showDialog();
//        this.positions = position;
//        UpAndDownRecordData data = new UpAndDownRecordData(SelectClasChildActivity.this);
//        if (!data.queryShangche(UserInfoUtils.getInstance().getUserKgId(),sp.getInt(Keyword.BusOrderId),list.get(position).getUserId())){
//            String url = String.format(
//                    HTTPURL.API_STUDENT_OPEN_DOWN,
//                    sp.getInt(Keyword.BusOrderId),
//                    list.get(position).getUserId(),
//                    stationid,
//                    GetDateTime.getdatetime(),
//                    1,
//                    UserInfoUtils.getInstance().getUserKgId(),
//                    1);
//            LogUtils.d("上车接口-----：" + url);
//            DownCarNetWork downCarNetWork = DownCarNetWork.newInstance(SelectClasChildActivity.this);
//            downCarNetWork.setNetWorkListener(SelectClasChildActivity.this);
//            downCarNetWork.setUrl(Keyword.FLAGDOWNCAR, url, UpDownCar.class);
//        } else {
//            closeDialog();
//            ToastUtil.show(list.get(position).getUserName() + "已经上车");
//            finish();
//        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
