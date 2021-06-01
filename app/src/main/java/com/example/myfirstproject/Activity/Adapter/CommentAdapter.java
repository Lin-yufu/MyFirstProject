package com.example.myfirstproject.Activity.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstproject.Activity.ArticleActivity;
import com.example.myfirstproject.Activity.Bean.DiscoveryContentBean;
import com.example.myfirstproject.Activity.Bean.DownloadReviewBean;
import com.example.myfirstproject.Activity.MyArticleActivity;
import com.example.myfirstproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
//设置点击事件接口

//我的文章recyclerview的适配器
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<DownloadReviewBean> commentContentList = new ArrayList<>();
    private RecyclerView recyclerView;

    public void setData(List<DownloadReviewBean> datas) {
        commentContentList.clear();
        if (datas != null && datas.size() > 0) {
            commentContentList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        private ImageView reviewerIcon;
        private TextView reviewerNick;
        private TextView reviewTime;
        private TextView reviewContent;

        public ViewHolder(View view) {
            super(view);
            contentView = view;
            reviewerIcon = (ImageView) view.findViewById(R.id.reviewer_icon);
            reviewerNick = (TextView) view.findViewById(R.id.reviewer_nick);
            reviewTime = (TextView) view.findViewById(R.id.review_time);
            reviewContent= (TextView) view.findViewById(R.id.review_content);
        }

        public void bindData(DownloadReviewBean item) {
            reviewContent.setText(item.getReviewerContent());
            reviewerNick.setText(item.getReviewerNick());
            reviewTime.setText(item.getRevieweTime());
            Picasso.get().load("http://60.205.218.123/myfirstproject/icon/" + item.getReviewerIcon() + ".jpg")
                    .resize(reviewerIcon.getLayoutParams().width, reviewerIcon.getLayoutParams().height)
                    .centerInside()
                    .into(reviewerIcon);
        }
    }

    public CommentAdapter(List<DownloadReviewBean> commentContentList) {
        this.commentContentList = commentContentList;
    }

    //在OnCreateViewHolder方法中绑定点击事件
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //需要在这里实现setTag
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(commentContentList.get(position));

    }

    @Override
    public int getItemCount() {
        return commentContentList.size();
    }
    public void removeItem(int position){
        commentContentList.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }
}
