package savion.tns.com.savion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class History extends Fragment implements myListener {
    public List<tickets> bookings = new ArrayList<>();
    public RecyclerView recyclerView;
    public BookingAdapter mAdapter;
    DatabaseHandler db;
    public static myListener myListener;
    Bundle bundle ;
    public History() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        // get our folding cell
        db=new DatabaseHandler(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter=new BookingAdapter(bookings);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        /*tickets booking=new tickets("Nandhu","8129280754","Kottayam","Washing","2018-10-15","10Am-10:30Am","KL-05-AA-1361","Car","");
        tickets booking1=new tickets("Amal","8129280754","Kottayam","Washing","2018-09-03","10Am-10:30Am","KL-05-AC-142","Car","");
        bookings.add(booking);
        bookings.add(booking1);*/
        bookings.addAll(db.getAllEducation());
        mAdapter.notifyDataSetChanged();

        myListener=new myListener() {
            @Override
            public void updateView(boolean success, String message) {
                bookings.clear();
                mAdapter=new BookingAdapter(bookings);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                bookings.addAll(db.getAllEducation());
                mAdapter.notifyDataSetChanged();
            }
        };
        return view;
    }


    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest("",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                String name = person.getString("name");



                            }



                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }


    @Override
    public void updateView(boolean success, String message) {

    }
}
