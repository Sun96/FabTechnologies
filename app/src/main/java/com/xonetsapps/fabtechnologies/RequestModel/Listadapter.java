package com.xonetsapps.fabtechnologies.RequestModel;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import  com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.activity.Invoice;


public class Listadapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	private List<Countries> myList;

	public Listadapter(Context context, ArrayList<Countries> myList) {
		this.myList = myList;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}


	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Countries getItem(int position) {
		return (Countries) myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final Listadapter.MyViewHolder mViewHolder;
		ArrayList mySubList = new ArrayList();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_request, parent, false);
			mViewHolder = new Listadapter.MyViewHolder(convertView);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (MyViewHolder) convertView.getTag();
		}

		final Countries currentListData = getItem(position);

		mViewHolder.date.setText(currentListData.getDate());
		mViewHolder.ammount.setText("Amount: "+currentListData.getPrice());
		mViewHolder.order.setText("Order Id: "+currentListData.getOrder());

		if(currentListData.getStatus().equals("1")){
			mViewHolder.status.setText("   Status: Pending");
		}
		else if(currentListData.getStatus().equals("2")){
			mViewHolder.status.setText("   Status: Complete");
		}else
			mViewHolder.status.setText("   Status: Cancelled");

		return convertView;
	}
	private class MyViewHolder {
		TextView date, ammount,order,status;
		LinearLayout listclick;

		public MyViewHolder(View item) {
			date = (TextView) item.findViewById(R.id.date);
			ammount = (TextView) item.findViewById(R.id.ammount);
			status=(TextView)item.findViewById(R.id.status);
			order=(TextView)item.findViewById(R.id.orderNo);
			listclick=(LinearLayout)item.findViewById(R.id.listClick);


		}
	}
}
