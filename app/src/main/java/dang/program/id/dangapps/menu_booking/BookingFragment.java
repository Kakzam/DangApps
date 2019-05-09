package dang.program.id.dangapps.menu_booking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dang.program.id.dangapps.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {

    private RecyclerView rc;
    private dang.program.id.dangapps.AdapterFoodPlace adapter;
    private List<dang.program.id.dangapps.ModelFoodPlace> data;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        myToolbar.setTitle("Select Restaurant");
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));

        rc = view.findViewById(R.id.rc_view);

        data = new ArrayList<>();
        adapter = new dang.program.id.dangapps.AdapterFoodPlace(getContext(), data);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rc.setLayoutManager(mLayoutManager);
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.setAdapter(adapter);

        dataLoad();

        return view;
    }

    private void dataLoad() {
        String[] img = new String[] {
                "https://bugatan.com/wp-content/uploads/2017/01/boegatane-tugu-jogja-768x489.jpg",
                "https://handsscript.files.wordpress.com/2016/05/1461406281414.jpg?w=748",
                "https://media-cdn.tripadvisor.com/media/photo-w/10/92/97/16/borobudur-statue.jpg",
                "https://fakta.news/wp-content/uploads/2018/04/Jembatan-Bengawan-Solo.jpg",
                "https://c1.staticflickr.com/8/7072/7185846065_bd2c639e50_b.jpg",
                "https://cdn-asset.hipwee.com/wp-content/uploads/2015/11/73959901-750x563.jpg"
        };

        dang.program.id.dangapps.ModelFoodPlace a = new dang.program.id.dangapps.ModelFoodPlace("Yogyakarta", img[0]);
        data.add(a);

        a = new dang.program.id.dangapps.ModelFoodPlace("Semarang", img[1]);
        data.add(a);

        a = new dang.program.id.dangapps.ModelFoodPlace("Magelang", img[2]);
        data.add(a);

        a = new dang.program.id.dangapps.ModelFoodPlace("Solo", img[3]);
        data.add(a);

        a = new dang.program.id.dangapps.ModelFoodPlace("Klaten", img[4]);
        data.add(a);

        a = new dang.program.id.dangapps.ModelFoodPlace("Purworejo", img[5]);
        data.add(a);

        adapter.notifyDataSetChanged();
    }
}
