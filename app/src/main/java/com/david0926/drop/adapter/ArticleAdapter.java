package com.david0926.drop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.david0926.drop.databinding.RowArticleBinding;
import com.david0926.drop.model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private List<ArticleModel> list;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ArticleModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, ArticleModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public ArticleAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleHolder(RowArticleBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        ArticleModel item = list.get(position);
        holder.bind(item, mOnItemClickListener, mOnItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setItem(List<ArticleModel> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    static class ArticleHolder extends RecyclerView.ViewHolder {

        RowArticleBinding binding;

        ArticleHolder(RowArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ArticleModel item, OnItemClickListener listener, OnItemLongClickListener longListener) {
            binding.setItem(item);
            itemView.setOnClickListener(view -> listener.onItemClick(view, item));
            itemView.setOnLongClickListener(view -> longListener.onItemLongClick(view, item));
        }
    }
}

