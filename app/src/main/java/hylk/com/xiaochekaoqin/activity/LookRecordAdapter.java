package hylk.com.xiaochekaoqin.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.bean.JiLuBean;
import hylk.com.xiaochekaoqin.global.MyApplication;
import hylk.com.xiaochekaoqin.utils.PrefUtils;

/**
 * Created by wenke on 2017/5/16.
 */

public class LookRecordAdapter extends BaseRecyclerAdapter {

    private List<JiLuBean> list;

    public LookRecordAdapter(List<JiLuBean> list) {
        this.list = list;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LookRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sch_look_record_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, final int position) {

        if (holder instanceof LookRecordViewHolder){
            LookRecordViewHolder holde = (LookRecordViewHolder) holder;
            holde.name.setText(list.get(position).getName());
            holde.time.setText(list.get(position).getTime());
            holde.classname.setText(list.get(position).getClassName());
            holde.zhuangtai.setText("下车");
            holde.zhuangtai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.setOnItemListener(position);
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class LookRecordViewHolder extends ClickableViewHolder{

        private TextView name,time,classname;
        private Button zhuangtai;

        public LookRecordViewHolder(View itemView) {
            super(itemView);
            name = $(R.id.tv_look_record_name);
            time = $(R.id.tv_look_record_time);
            classname = $(R.id.tv_look_record_class_name);
            zhuangtai = $(R.id.tv_look_record_zhuangtai);
        }
    }

    interface OnItemListener{
        void setOnItemListener(int position);
    }

    OnItemListener listener;

    public void setOnLookItemListener(OnItemListener listener){
        this.listener = listener;
    }
}
