package com.makent.trioangle.travelling.Nested_search;

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
public class ParentFragment extends Fragment {

    private static final String TAG = ParentFragment.class.getSimpleName();

    @BindView(R.id.rv_parent)
    RecyclerView recyclerViewParent;

    ArrayList<ParentChild> parentChildObj;


    public ParentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nested_search_fragment_parent, container, false);
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
        ParentAdapter parentAdapter = new ParentAdapter(getActivity(), getParentChildData());
        recyclerViewParent.setAdapter(parentAdapter);
    }

    private ArrayList<ParentChild> getParentChildData() {
        parentChildObj = new ArrayList<>();
        ArrayList<Child> c1List = new ArrayList<>();
        ArrayList<Child> c2List = new ArrayList<>();
        ArrayList<Child> c3List = new ArrayList<>();

        String Popular[] = {"Amsterdam","Venice","Rome","Munich","Istanbul","Vienna","New Delhi","Paris","Bangkok","Prague","Dubai","Zurich"};


        //for (int i = 0; i < 25; i++) {
            Child c1 = new Child();
            c1.setChild_name("Nearby");
            c1List.add(c1);
        //}

        //for (int i = 0; i < Popular.length; i++) {
            Child c2 = new Child();
            c2.setChild_name("Anywhere");
            c2List.add(c2);
       // }

        for (int i = 0; i < Popular.length; i++) {
            Child c3 = new Child();
            c3.setChild_name(Popular[i]);
            c3List.add(c3);
        }

        ParentChild pc1 = new ParentChild();
        pc1.setHeader("");
        pc1.setChild(c1List);
        parentChildObj.add(pc1);

        ParentChild pc2 = new ParentChild();
        pc2.setHeader("");
        pc2.setChild(c2List);
        parentChildObj.add(pc2);

        ParentChild pc3 = new ParentChild();
        pc3.setHeader("Popular near you");
        pc3.setChild(c3List);
        parentChildObj.add(pc3);


        return parentChildObj;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // ButterKnife.unbind(this);
    }
}
