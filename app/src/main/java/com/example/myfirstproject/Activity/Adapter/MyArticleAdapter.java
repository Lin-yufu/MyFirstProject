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
import com.example.myfirstproject.Activity.MyArticleActivity;
import com.example.myfirstproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
//设置点击事件接口

//我的文章recyclerview的适配器
public class MyArticleAdapter extends RecyclerView.Adapter<MyArticleAdapter.ViewHolder> {
    private OnDeleteClickListener mOnDeleteClickListener;
    private List<DiscoveryContentBean> MyArticleContentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private  static MyArticleActivity context;
    final static  int RELOAD=23;

    public void setmOnDeleteClickListener(OnDeleteClickListener clickListener){
        this.mOnDeleteClickListener=clickListener;
    }
    public void setData(List<DiscoveryContentBean> datas) {
        MyArticleContentList.clear();
        if (datas != null && datas.size() > 0) {
            MyArticleContentList.addAll(datas);
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
        private TextView deleteArticle;

        public ViewHolder(View view,final OnDeleteClickListener onClickListener) {
            super(view);
            contentView = view;
            authorIcon = (ImageView) view.findViewById(R.id.my_article_icon);
            authorNick = (TextView) view.findViewById(R.id.my_article_nick);
            publishTime = (TextView) view.findViewById(R.id.my_article_publish_time);
            articleTitle = (TextView) view.findViewById(R.id.my_article_title);
            articleImage = (ImageView) view.findViewById(R.id.my_article_image);
            deleteArticle=(TextView)view.findViewById(R.id.delete_article);
            deleteArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListener.onDeleteClicked(view, position);
                    }
                }
            });
        }

        public void bindData(DiscoveryContentBean item) {
            articleTitle.setText(item.getTitle());
            authorNick.setText(item.getAuthorNick());
            publishTime.setText(item.getPublishTime());
            Picasso.get().load("http://60.205.218.123/myfirstproject/icon/" + item.getAuthorIcon() + ".jpg")
                    .resize(authorIcon.getLayoutParams().width, authorIcon.getLayoutParams().height)
                    .centerInside()
                    .into(authorIcon);
            Picasso.get().load("http://60.205.218.123/myfirstproject/image/" + item.getArticleImage() + ".jpg")
                    .resize(articleImage.getLayoutParams().width, articleImage.getLayoutParams().height)
                    .centerInside()
                    .into(articleImage);

        }
    }

    public MyArticleAdapter(List<DiscoveryContentBean> MyArticleContentList) {
        this.MyArticleContentList = MyArticleContentList;
    }

    //在OnCreateViewHolder方法中绑定点击事件
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_article_item, parent, false);
        final ViewHolder holder = new ViewHolder(view,mOnDeleteClickListener);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DiscoveryContentBean discoveryContentBean = MyArticleContentList.get(position);
//                Toast.makeText(v.getContext(),"You click view"+discoveryContentBean.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ArticleActivity.class);
                intent.putExtra("articleId", discoveryContentBean.getArticleId());
                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    //需要在这里实现setTag
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(MyArticleContentList.get(position));

    }

    @Override
    public int getItemCount() {
        return MyArticleContentList.size();
    }
    public void removeItem(int position){
        MyArticleContentList.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }
}
