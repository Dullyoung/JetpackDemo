package com.dullyoung.jetpackdemo.controller.activities;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dullyoung.jetpackdemo.Config;
import com.dullyoung.jetpackdemo.controller.listener.Custom3Observer;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import kotlin.Unit;

/**
 * Description : recyclerView adapter base
 *
 * @author Dullyoung
 * Date : 2021/12/1
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    private boolean clickable = true;


    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    protected int getColor(int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    protected String getString(int s) {
        return getContext().getString(s);
    }


    protected void clickTrigger(View view, Runnable runnable) {
        RxView.clicks(view).throttleFirst(Config.CLICK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Custom3Observer<Unit>() {
                    @Override
                    public void onResult(Unit unit) {
                        runnable.run();
                    }
                });
    }

    @Override
    protected void bindViewClickListener(@NotNull BaseViewHolder viewHolder, int viewType) {

        RxView.clicks(viewHolder.itemView).throttleFirst(Config.CLICK_INTERVAL, TimeUnit.MILLISECONDS).subscribe(
                new Custom3Observer<Unit>() {
                    @Override
                    public void onResult(@NonNull Unit unit) {
                        if (!isClickable()) {
                            return;
                        }

                        int position = viewHolder.getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }
                        position -= getHeaderLayoutCount();
                        setOnItemClick(viewHolder.itemView, position);
                    }
                }
        );

        RxView.longClicks(viewHolder.itemView).throttleFirst(Config.CLICK_INTERVAL, TimeUnit.MILLISECONDS).subscribe(new Custom3Observer<Unit>() {
            @Override
            public void onResult(@NonNull Unit unit) {
                if (!isClickable()) {
                    return;
                }

                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                position -= getHeaderLayoutCount();
                setOnItemLongClick(viewHolder.itemView, position);
            }
        });

        if (getOnItemChildClickListener() != null) {
            for (int id : getChildClickViewIds()) {
                RxView.clicks(viewHolder.getView(id)).throttleFirst(Config.CLICK_INTERVAL, TimeUnit.MILLISECONDS)
                        .subscribe(new Custom3Observer<Unit>() {
                            @Override
                            public void onResult(@NonNull Unit unit) {
                                if (!isClickable()) {
                                    return;
                                }

                                int position = viewHolder.getAdapterPosition();
                                if (position == RecyclerView.NO_POSITION) {
                                    return;
                                }
                                position -= getHeaderLayoutCount();
                                setOnItemChildClick(viewHolder.getView(id), position);
                            }
                        });
            }
        }
    }
}
