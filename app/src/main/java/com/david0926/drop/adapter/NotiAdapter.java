package com.david0926.drop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.david0926.drop.databinding.RowNotiBinding;
import com.david0926.drop.model.NotiModel;

import java.util.ArrayList;
import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiHolder> {

    private List<NotiModel> list;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, NotiModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, NotiModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public NotiAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public NotiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotiHolder(RowNotiBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotiHolder holder, int position) {
        NotiModel item = list.get(position);
        holder.bind(item, mOnItemClickListener, mOnItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setItem(List<NotiModel> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    static class NotiHolder extends RecyclerView.ViewHolder {

        RowNotiBinding binding;

        NotiHolder(RowNotiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NotiModel item, OnItemClickListener listener, OnItemLongClickListener longListener) {
            binding.setItem(item);
            itemView.setOnClickListener(view -> listener.onItemClick(view, item));
            itemView.setOnLongClickListener(view -> longListener.onItemLongClick(view, item));
        }
    }
}

