package com.xonetsapps.fabtechnologies.home;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import  com.xonetsapps.fabtechnologies.R;
import  com.xonetsapps.fabtechnologies.activity.HomeFragment;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {



    LayoutInflater inflater;
    Context context;
    private List<HomeMode> myList;
    private HomeFragment.OnEditText_Changed onEditText_Changed;
    private SubListAdapter subListAdapter;

    public HomeAdapter(Context context, List<HomeMode> myList, HomeFragment.OnEditText_Changed onEditText_Changed) {
        this.myList = myList;
        this.context = context;
        this.onEditText_Changed = onEditText_Changed;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.main_list, parent, false);
        HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(view);

        return holder;
    }
    @Override
    public int getItemCount() {
        return myList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder mViewHolder, final int position) {
        ArrayList<SubMode> mySubList=new ArrayList<SubMode>();
        // sublist= new ArrayList<String>()(currentListData.setSublist());
        mViewHolder.tvTitle.setText(myList.get(position).getTitle());
        mViewHolder.tvTitle.setHeight(50*myList.get(position).getSublist().length);
        final Double[] enteredNumber = new Double[myList.get(position).getSublist().length];
        final String[] enteredName = new String[myList.get(position).getSublist().length];

        //Toast.makeText(context,String.valueOf(myList.get(position).getSublist().length),Toast.LENGTH_SHORT).show();
        for (int i = 0; i < myList.get(position).getSublist().length; i++){

            // Create a new object for each list item
            SubMode ld = new SubMode(myList.get(position).getSublist()[i],myList.get(position).getPrice()[i],myList.get(position).getVendorId()[i],myList.get(position).getPId());
            // Add this object into the ArrayList myList
            mySubList.add(ld);
        }
        for (int k = 0; k < myList.get(position).getSublist().length; k++) {
            enteredNumber[k] = 0.0;
        }
        mViewHolder.tvDesc.setVisibility(View.GONE);
        mViewHolder.listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mViewHolder.listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
         subListAdapter= new SubListAdapter(context, mySubList, new OnEditTextChanged() {
            @Override
            public void onTextChanged(int po, Double charSeq, String name) {
                Log.d("TAG", "position " + position + " " + charSeq);
               // Toast.makeText(context,String.valueOf(position),Toast.LENGTH_SHORT).show();
                enteredNumber[po] = charSeq;
                enteredName[po]=name;
                Double sum = 0.0;
                for (int i = 0; i <myList.get(position).getSublist().length; i++) {
                    sum += enteredNumber[i];
                }
                mViewHolder.tvDesc.setText(String.valueOf(sum));
               // Toast.makeText(context,name, Toast.LENGTH_SHORT).show();
            }
        });


        mViewHolder.tvDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final double a= !s.toString().equals("")?Double.parseDouble(s.toString()) :0;
                onEditText_Changed.onText_Changed(position, a);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mViewHolder.listView.setAdapter(subListAdapter);
    }





    public interface OnEditTextChanged {
        void onTextChanged(int position, Double charSeq, String name);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        RecyclerView listView;

        public MyViewHolder(View item) {
            super(item);
            tvTitle = (TextView) item.findViewById(R.id.textView);
            tvDesc = (TextView) item.findViewById(R.id.amount);
            listView=(RecyclerView)item.findViewById(R.id.ivIcon);
        }
    }
}