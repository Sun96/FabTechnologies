package com.xonetsapps.fabtechnologies;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xonetsapps.fabtechnologies.Http.InvoiceDatabase;

import java.util.ArrayList;

/**
 * Created by Mitch on 2016-05-13.
 */
public class ViewListContents extends Fragment {

    InvoiceDatabase myDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.viewlistcontents_layout, container, false);
       /* ListView listView = (ListView) rootView.findViewById(R.id.listView);
        myDB = new InvoiceDatabase(getActivity());

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getAllData();
        if(data.getCount() == 0){
            Toast.makeText(getActivity(), "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                listView.setAdapter(new ArrayAdapter<String>(rootView.getContext(),
                        android.R.layout.simple_list_item_1 , theList));
            }
        }*/
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("     Policy");
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
