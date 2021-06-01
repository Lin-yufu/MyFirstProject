package com.example.myfirstproject.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstproject.Activity.Bean.MaterialBean;
import com.example.myfirstproject.Activity.ArticleActivity;
import com.example.myfirstproject.Activity.MainActivity;
import com.example.myfirstproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {
    private static List<MaterialBean> materialList = new ArrayList<>();
    private OnImageClickListener mOnImageClickListener;
    private RecyclerView recyclerView;
    public void setmOnImageClickListener(OnImageClickListener clickListener){
        this.mOnImageClickListener=clickListener;
    }

    public void setData(List<MaterialBean> datas) {
        materialList.clear();
        if (datas != null && datas.size() > 0) {
            materialList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        private TextView materialTitle;
        private ImageView materialPeitu;

        public ViewHolder(View view) {
            super(view);
            contentView=view;
            materialTitle = (TextView) view.findViewById(R.id.material_title);
            materialPeitu = (ImageView) view.findViewById(R.id.material_peitu);
        }
        //这里是小柯的路径
        public void bindData(MaterialBean item) {
            materialTitle.setText(item.getTitle());
            Picasso.get().load("http://60.205.218.123/code_sql/home"+item.getPeitu().substring(1))
                    .fit()
                    .centerInside()
                    .into(materialPeitu);
        }
    }

    public MaterialAdapter(List<MaterialBean> materialList) {
        this.materialList = materialList;
    }
    //在OnCreateViewHolder方法中绑定点击事件
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                MaterialBean materialBean =materialList.get(position);
//                Intent intent=new Intent(v.getContext(), ArticleActivity.class);
//                intent.putExtra("materialId", materialBean.getId());
//                v.getContext().startActivity(intent);
//
//            }
//        });
        return holder;
    }
    //需要在这里实现setTag
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(materialList.get(position));
    }
    @Override
    public int getItemCount() {
        return materialList.size();
    }
    public void removeItem(int position){
        materialList.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }
}
