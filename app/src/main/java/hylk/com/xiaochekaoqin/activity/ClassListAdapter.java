package hylk.com.xiaochekaoqin.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.bean.ClassMessage;
import hylk.com.xiaochekaoqin.utils.PrefUtils;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    private  ArrayList<ClassMessage> list;
    private  Context context;

    public ClassListAdapter(ArrayList<ClassMessage> list, Context context){
        this.list = list;
        this.context = context;

    }

    @Override
    public ClassListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_classinfo,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ClassListAdapter.ViewHolder viewHolder, final int position) {

        ClassMessage bean = list.get(position);
        viewHolder.infoName.setText(bean.ClassName);
        viewHolder.infoId.setText(bean.ClassInfoID + "");
        viewHolder.infoBox.setSelection( PrefUtils.getInt(context,bean.ClassInfoID + "",-1) + 1 );

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView infoName;
        public TextView infoId;
        public Spinner infoBox;

        public ViewHolder(View itemView) {
            super(itemView);

            infoName = (TextView) itemView.findViewById(R.id.tv_info_name);
            infoId = (TextView) itemView.findViewById(R.id.tv_info_id);
            infoBox = (Spinner) itemView.findViewById(R.id.sp_info_box);

            infoBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PrefUtils.putInt(context, list.get(getAdapterPosition()).ClassInfoID + "", i - 1 );
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }



    //  更新数据
    public void update(ArrayList<ClassMessage> parents) {

        this.list = parents ; // 更新
        notifyDataSetChanged();

    }

}
