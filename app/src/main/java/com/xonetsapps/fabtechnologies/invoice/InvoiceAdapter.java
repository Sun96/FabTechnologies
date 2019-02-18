package com.xonetsapps.fabtechnologies.invoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import  com.xonetsapps.fabtechnologies.R;


public class InvoiceAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    private ArrayList<InvoiceMode> myList;

    public InvoiceAdapter(Context context, ArrayList<InvoiceMode> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public InvoiceMode getItem(int position) {
        return (InvoiceMode) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final InvoiceAdapter.MyViewHolder mViewHolder;
        ArrayList mySubList = new ArrayList();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.invoice_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }




        final InvoiceMode currentListData = getItem(position);

        mViewHolder.num.setText(String.valueOf(position+1));
        mViewHolder.proNam.setText(" "+currentListData.getProName());
        mViewHolder.venName.setText(currentListData.getName());
        mViewHolder.order.setText(currentListData.getProPric());
        mViewHolder.pric.setText(currentListData.getAmmount());
        mViewHolder.tammount.setText(String.valueOf (Double.valueOf(currentListData.getProPric())*Double.valueOf(currentListData.getAmmount())));

        return convertView;
    }

 private class MyViewHolder {
     TextView num, proNam, pric, tammount, order,venName;

     public MyViewHolder(View item) {
         num = (TextView) item.findViewById(R.id.num);
         proNam = (TextView) item.findViewById(R.id.proName);
         order = (TextView) item.findViewById(R.id.count);
         pric = (TextView) item.findViewById(R.id.price);
         tammount = (TextView) item.findViewById(R.id.total);
         venName = (TextView) item.findViewById(R.id.venName);
     }
 }
}

