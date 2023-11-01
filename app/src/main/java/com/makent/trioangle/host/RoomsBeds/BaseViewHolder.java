package com.makent.trioangle.host.RoomsBeds;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by sreekumar on 7/24/17.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T object);
}
