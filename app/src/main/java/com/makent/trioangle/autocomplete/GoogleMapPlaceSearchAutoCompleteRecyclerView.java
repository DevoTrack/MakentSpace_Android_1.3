package com.makent.trioangle.autocomplete;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.makent.trioangle.R;

import java.util.List;


public class GoogleMapPlaceSearchAutoCompleteRecyclerView extends RecyclerView.Adapter<GoogleMapPlaceSearchAutoCompleteRecyclerView.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<AutocompletePrediction> autocompletePredictions;
    private Context mContext;
    private AutoCompleteAddressTouchListener autoCompleteAddressTouchListener;


    public GoogleMapPlaceSearchAutoCompleteRecyclerView(List<AutocompletePrediction> autocompletePredictions, Context mContext, AutoCompleteAddressTouchListener autoCompleteAddressTouchListener) {
        inflater = LayoutInflater.from(mContext);
        this.autocompletePredictions = autocompletePredictions;
        this.mContext = mContext;
        this.autoCompleteAddressTouchListener = autoCompleteAddressTouchListener;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.searchview_adapter_black, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        AutocompletePrediction currentObject = autocompletePredictions.get(position);
        holder.address.setText(currentObject.getPrimaryText(null));
        holder.address_secondry.setText(currentObject.getSecondaryText(null));
        holder.predictedRow.setOnClickListener(v->{
            autoCompleteAddressTouchListener.selectedAddress(currentObject);
        });

    }

    public void updateList(List<AutocompletePrediction> autocompletePredictions) {
        this.autocompletePredictions = autocompletePredictions;
        notifyDataSetChanged();
    }

    public void clearAddresses(){
        try {
            this.autocompletePredictions.clear();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return autocompletePredictions.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView address, address_secondry;
        RelativeLayout predictedRow;


        RecyclerViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            address_secondry = itemView.findViewById(R.id.address_secondry);
            predictedRow = itemView.findViewById(R.id.predictedRow);

        }
    }

    public interface AutoCompleteAddressTouchListener{
        public void selectedAddress(AutocompletePrediction autocompletePrediction);
    }
}
