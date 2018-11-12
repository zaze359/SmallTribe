package com.zaze.tribe.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class BaseRecyclerAdapter<V, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    private Context context;
    private final List<V> dataList = new ArrayList<>();


    public BaseRecyclerAdapter(Context context, Collection<V> data) {
        this.context = context;
        setDataList(data, false);
    }

    public void setDataList(Collection<V> data) {
        setDataList(data, true);
    }

    private void setDataList(Collection<V> data, boolean isNotify) {
        dataList.clear();
        if (data != null && data.size() > 0) {
            dataList.addAll(data);
        }
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createViewHolder(LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        V v = getItem(position);
        if (v != null) {
            onBindView(holder, v, position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private V getItem(int position) {
        if (position < 0 || position >= dataList.size()) {
            return null;
        } else {
            return dataList.get(position);
        }
    }

    public Context getContext() {
        return context;
    }

    // --------------------------------------------------

    /**
     * get view layout id
     *
     * @return int
     */
    public abstract int getViewLayoutId();

    /**
     * 构建viewHolder
     *
     * @param convertView convertView
     * @return H
     */
    public abstract H createViewHolder(@NonNull View convertView);

    /**
     * view赋值
     *
     * @param holder   holder
     * @param value    value
     * @param position position
     */
    public abstract void onBindView(@NonNull H holder, @NonNull V value, int position);
}
