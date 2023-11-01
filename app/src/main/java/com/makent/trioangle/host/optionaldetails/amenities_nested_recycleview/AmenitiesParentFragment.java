package com.makent.trioangle.host.optionaldetails.amenities_nested_recycleview;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails/amenties_nested_recycleview
 * @category    AmenitiesParentFragment
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makent.trioangle.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class AmenitiesParentFragment extends Fragment {

    private static final String TAG = AmenitiesParentFragment.class.getSimpleName();

    @BindView(R.id.rv_parent)
    RecyclerView recyclerViewParent;

    ArrayList<AmenitiesParentChild> parentChildObj;


    public AmenitiesParentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.amenities_fragment_parent, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewParent.setLayoutManager(manager);
        recyclerViewParent.setHasFixedSize(true);
        AmenitiesParentAdapter parentAdapter = new AmenitiesParentAdapter(getActivity(), getParentChildData());
        recyclerViewParent.setAdapter(parentAdapter);
    }

    private ArrayList<AmenitiesParentChild> getParentChildData() {
        parentChildObj = new ArrayList<>();
        ArrayList<AmenitiesChild> c1List = new ArrayList<>();
        ArrayList<AmenitiesChild> c2List = new ArrayList<>();
        ArrayList<AmenitiesChild> c3List = new ArrayList<>();
        ArrayList<AmenitiesChild> c4List = new ArrayList<>();

        String amenitiestypes[]= getResources().getStringArray(R.array.amenities_type);
        String amenities_mostcommon[]= getResources().getStringArray(R.array.amenities_mostcommon);
        String amenities_extra[]= getResources().getStringArray(R.array.amenities_extra);
        String amenities_special_Features[]= getResources().getStringArray(R.array.amenities_special_Features);
        String amenities_home_safety[]= getResources().getStringArray(R.array.amenities_home_safety);


        for (int i = 0; i < amenities_mostcommon.length; i++) {
            AmenitiesChild c1 = new AmenitiesChild();
            c1.setChild_name(amenities_mostcommon[i]);
            c1List.add(c1);
        }

        for (int i = 0; i < amenities_extra.length; i++) {
            AmenitiesChild c2 = new AmenitiesChild();
            c2.setChild_name(amenities_extra[i]);
            c2List.add(c2);
    }

        for (int i = 0; i < amenities_special_Features.length; i++) {
            AmenitiesChild c3 = new AmenitiesChild();
            c3.setChild_name(amenities_special_Features[i]);
            c3List.add(c3);
        }

        for (int i = 0; i < amenities_home_safety.length; i++) {
            AmenitiesChild c4 = new AmenitiesChild();
            c4.setChild_name(amenities_home_safety[i]);
            c4List.add(c4);
        }

        AmenitiesParentChild pc1 = new AmenitiesParentChild();
        pc1.setHeader(amenitiestypes[0]);
        pc1.setChild(c1List);
        parentChildObj.add(pc1);

        AmenitiesParentChild pc2 = new AmenitiesParentChild();
        pc2.setHeader(amenitiestypes[1]);
        pc2.setChild(c2List);
        parentChildObj.add(pc2);

        AmenitiesParentChild pc3 = new AmenitiesParentChild();
        pc3.setHeader(amenitiestypes[2]);
        pc3.setChild(c3List);
        parentChildObj.add(pc3);

        AmenitiesParentChild pc4 = new AmenitiesParentChild();
        pc4.setHeader(amenitiestypes[3]);
        pc4.setChild(c4List);
        parentChildObj.add(pc4);


        return parentChildObj;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }
}
