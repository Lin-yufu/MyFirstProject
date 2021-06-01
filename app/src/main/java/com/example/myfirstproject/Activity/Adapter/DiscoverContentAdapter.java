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

import com.example.myfirstproject.Activity.Bean.DiscoveryContentBean;
import com.example.myfirstproject.Activity.ArticleActivity;
import com.example.myfirstproject.Activity.MainActivity;
import com.example.myfirstproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DiscoverContentAdapter extends RecyclerView.Adapter<DiscoverContentAdapter.ViewHolder> {
    private static List<DiscoveryContentBean> discoverContentList = new ArrayList<>();
    private OnImageClickListener mOnImageClickListener;
    private RecyclerView recyclerView;
    public void setmOnImageClickListener(OnImageClickListener clickListener){
        this.mOnImageClickListener=clickListener;
    }

    public void setData(List<DiscoveryContentBean> datas) {
        discoverContentList.clear();
        if (datas != null && datas.size() > 0) {
            discoverContentList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        private ImageView authorIcon;
        private TextView authorNick;
        private TextView publishTime;
        private TextView articleTitle;
        private ImageView articleImage;

        public ViewHolder(View view,final OnImageClickListener onClickListener) {
            super(view);
            contentView=view;
            authorIcon = (ImageView) view.findViewById(R.id.author_icon);
            authorNick = (TextView) view.findViewById(R.id.author_nick);
            publishTime = (TextView) view.findViewById(R.id.publish_time);
            articleTitle = (TextView) view.findViewById(R.id.article_title);
            articleImage = (ImageView) view.findViewById(R.id.article_image);
            articleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListener.onImageClicked(view, position,
                                "http://60.205.218.123/myfirstproject/image/"+discoverContentList.get(position).getArticleImage()+".jpg");
                    }
                }
            });

        }

        public void bindData(DiscoveryContentBean item) {
            articleTitle.setText(item.getTitle());
            authorNick.setText(item.getAuthorNick());
            publishTime.setText(item.getPublishTime());
            Picasso.get().load("http://60.205.218.123/myfirstproject/icon/"+item.getAuthorIcon()+".jpg")
                    .resize(authorIcon.getLayoutParams().width, authorIcon.getLayoutParams().height)
                    .centerInside()
                    .into(authorIcon);
            if(item.getArticleImage().equals("no_picture")){
                articleImage.setVisibility(View.GONE);
            }
            else{
                articleImage.setVisibility(View.VISIBLE);
                Picasso.get().load("http://60.205.218.123/myfirstproject/image/"+item.getArticleImage()+".jpg")
//                        .resize(articleImage.getLayoutParams().width, articleImage.getLayoutParams().height)
                        .fit()
                        .centerInside()
                        .into(articleImage);

            }

        }
    }

    public DiscoverContentAdapter(List<DiscoveryContentBean> discoverContentList) {
        this.discoverContentList = discoverContentList;
    }
    //在OnCreateViewHolder方法中绑定点击事件
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_item, parent, false);
        final ViewHolder holder = new ViewHolder(view,mOnImageClickListener);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                DiscoveryContentBean discoveryContentBean =discoverContentList.get(position);
//                Toast.makeText(v.getContext(),"You click view"+discoveryContentBean.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(v.getContext(), ArticleActivity.class);
                intent.putExtra("articleId", discoveryContentBean.getArticleId());
                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }
    //需要在这里实现setTag
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(discoverContentList.get(position));


    }

    @Override
    public int getItemCount() {
        return discoverContentList.size();
    }
    public void removeItem(int position){
        discoverContentList.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }
}
