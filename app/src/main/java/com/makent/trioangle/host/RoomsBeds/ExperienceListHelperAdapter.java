package com.makent.trioangle.host.RoomsBeds;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.createspace.BookingType;
import com.makent.trioangle.createspace.CancelltionPolicy;
import com.makent.trioangle.createspace.model.hostlisting.readytohost.SpaceTimings;
import com.makent.trioangle.helper.Constants;

import java.util.List;


public class ExperienceListHelperAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final ExperienceListHelperAdapter.OnItemClickListener onItemClickListener;
    public String selectedValue;
    public Context context;
    private List<? extends BaseModel> mList;
    private LayoutInflater mInflator;

    public ExperienceListHelperAdapter(List<? extends BaseModel> list, Context context, ExperienceListHelperAdapter.OnItemClickListener onItemClickListener, String selectedValue) {
        this.mList = list;
        this.context = context;
        this.mInflator = LayoutInflater.from(context);
        this.selectedValue = selectedValue;
        this.onItemClickListener = onItemClickListener;
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.ViewType.BedType:
                return new CategoryHolder(mInflator.inflate(R.layout.list_items, parent, false));
            case Constants.ViewType.MultipleTime:
                return new MultipleTimeHolder(mInflator.inflate(R.layout.list_items, parent, false));
            case Constants.ViewType.CancellationPolicy:
                return new CancellationPolicyHolder(mInflator.inflate(R.layout.list_items, parent, false));
            case Constants.ViewType.Bookingtype:
                return new BookingTypePolicyHolder(mInflator.inflate(R.layout.list_items, parent, false));


        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    public int getItemCount() {
        return mList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position, int ViewType);
    }



    public class MultipleTimeHolder extends BaseViewHolder<SpaceTimings> {
        private TextView tvName;

        public MultipleTimeHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        @Override
        public void bind(SpaceTimings ageModelList) {

            if (selectedValue.equals(ageModelList.getTime())) {
                tvName.setTextColor(context.getResources().getColor(R.color.title_text_color));
            } else {
                tvName.setTextColor(context.getResources().getColor(R.color.material_grey_850));
            }

            tvName.setText(ageModelList.getTime()+" "+ageModelList.getTimeZone());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), Constants.ViewType.MultipleTime);
                }
            });
        }
    }

    public class BookingTypePolicyHolder extends BaseViewHolder<BookingType> {
        private TextView tvName;

        public BookingTypePolicyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        @Override
        public void bind(BookingType ageModelList) {

            if (selectedValue.equals(ageModelList.getName())) {
                tvName.setTextColor(context.getResources().getColor(R.color.title_text_color));
            } else {
                tvName.setTextColor(context.getResources().getColor(R.color.material_grey_850));
            }

            tvName.setText(ageModelList.getName());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), Constants.ViewType.Bookingtype);
                }
            });
        }
    }


    public class CancellationPolicyHolder extends BaseViewHolder<CancelltionPolicy> {
        private TextView tvName;

        public CancellationPolicyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        @Override
        public void bind(CancelltionPolicy ageModelList) {

            if (selectedValue.equals(ageModelList.getName())) {
                tvName.setTextColor(context.getResources().getColor(R.color.title_text_color));
            } else {
                tvName.setTextColor(context.getResources().getColor(R.color.material_grey_850));
            }

            tvName.setText(ageModelList.getName());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), Constants.ViewType.CancellationPolicy);
                }
            });
        }
    }



    public class CategoryHolder extends BaseViewHolder<BedTypesList> {
        private TextView tvName;

        public CategoryHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        @Override
        public void bind(BedTypesList BedTypesList) {

            if (selectedValue.equals(BedTypesList.getName())) {
                tvName.setTextColor(context.getResources().getColor(R.color.title_text_color));
            } else {
                tvName.setTextColor(context.getResources().getColor(R.color.material_grey_850));
            }

            tvName.setText(BedTypesList.getName());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), Constants.ViewType.BedType);
                }
            });
        }
    }


}