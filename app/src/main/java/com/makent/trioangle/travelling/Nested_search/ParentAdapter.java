package com.makent.trioangle.travelling.Nested_search;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makent.trioangle.R;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cliqers on 23/1/2016.
 */
public class ParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    ArrayList<Parent> parentData;
//    ArrayList<AmenitiesChild> childData;
    ArrayList<ParentChild> parentChildData;

    Context ctx;

    public ParentAdapter(Context ctx, ArrayList<ParentChild> parentChildData) {
        this.ctx = ctx;
//        this.parentData = parentData;
//        this.childData= childData;
        this.parentChildData = parentChildData;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_header)
        TextView tv_header;
        @BindView(R.id.rv_child)
        RecyclerView rv_child;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nessted_search_item_parent, parent, false);

        ParentAdapter.ViewHolder pavh = new ParentAdapter.ViewHolder(itemLayoutView);

        return pavh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;


        ParentChild p = parentChildData.get(position);
        if(p.getHeader().toString().equals(""))
        {
            vh.tv_header.setVisibility(View.GONE);
        }
        else {
            vh.tv_header.setText(p.getHeader());
        }

        initChildLayoutManager(vh.rv_child, p.getChild());
    }

    private void initChildLayoutManager(RecyclerView rv_child, ArrayList<Child> childData) {
        LinearLayoutManager manager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_child.setLayoutManager(manager);
        rv_child.setHasFixedSize(true);
        //rv_child.addItemDecoration(new SimpleDividerItemDecoration(ctx));

        rv_child.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        ChildAdapter childAdapter = new ChildAdapter(childData);
        rv_child.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return parentChildData.size();
    }
}
