package com.xonetsapps.fabtechnologies.home;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.xonetsapps.fabtechnologies.PersonDatabase;
import  com.xonetsapps.fabtechnologies.R;

public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.MyViewHolder> {

    LayoutInflater inflater;
    Context context;
    private List<SubMode> myList;
    private HomeAdapter.OnEditTextChanged onEditTextChanged;
    PersonDatabase mydb;
    ArrayList<String> proList,vendorList,idList,priceList;
    boolean isInserted;
    int k,j;
    public SubListAdapter(Context context, List myList, HomeAdapter.OnEditTextChanged onEditTextChanged) {
        this.myList = myList;
        this.context = context;
        this.onEditTextChanged = onEditTextChanged;
        inflater = LayoutInflater.from(this.context);
    }
    @Override
    public SubListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.sub_list, parent, false);
        SubListAdapter.MyViewHolder holder = new SubListAdapter.MyViewHolder(view);

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
    public void onBindViewHolder(final SubListAdapter.MyViewHolder mViewHolder, final int position) {
        SubMode album = myList.get(position);
        // sublist= new ArrayList<String>()(currentListData.setSublist());
        mViewHolder.subTitle.setText(album.getSubtitle());
        mViewHolder.pric.setText(album.getSubprice());
        final Double b= !myList.get(position).getSubprice().equals("")?Double.parseDouble(myList.get(position).getSubprice()) :0;
        mydb = new PersonDatabase(context);


        mViewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int a= !s.toString().equals("")?Integer.parseInt(s.toString()) :0;
                final Double p= b*a;
                mViewHolder.tprice.setText(String.valueOf(p));
                mViewHolder.editText.setHint(s);

                 proList = new ArrayList<>();
                idList = new ArrayList<>();
                vendorList = new ArrayList<>();
                priceList = new ArrayList<>();
                Cursor data = mydb.getAllData();
                while(data.moveToNext()){
                    idList.add(data.getString(0));
                    proList.add(data.getString(1));
                    vendorList.add(data.getString(2));
                }
                if(data.getCount()==0){
                    mydb.insertdata(myList.get(position).getPid(),myList.get(position).getVid(),String.valueOf(a),myList.get(position).getSubprice());
                }else {
                    for (int i = 0; i <vendorList.size(); i++) {

                        if (proList.get(i).equals(myList.get(position).getPid()) && vendorList.get(i).equals(myList.get(position).getVid())) {
                            //Toast.makeText(context, "It's Your Fevriout", Toast.LENGTH_SHORT).show();
                            k = 1;
                            j=i;
                        }
                    }
                    if (k==1){
                        if (a > 0) {
                            mydb.updateuserData(idList.get(j),myList.get(position).getPid(),myList.get(position).getVid(),String.valueOf(a));
                        } else {
                             mydb.deleteData(idList.get(j));
                        }
                        k=0;

                    }
                    else {
                            mydb.insertdata(myList.get(position).getPid(), myList.get(position).getVid(),String.valueOf(a),myList.get(position).getSubprice());

                        }

                }
                onEditTextChanged.onTextChanged(position, p,myList.get(position).getPid()+"and"+myList.get(position).getVid());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subTitle, pric,tprice;
        ListView listView;
        EditText editText;

        public MyViewHolder(View item) {
            super(item);
            subTitle = (TextView) item.findViewById(R.id.label);
            pric = (TextView) item.findViewById(R.id.price);
            tprice = (TextView) item.findViewById(R.id.total);
            editText = (EditText) item.findViewById(R.id.num);


        }
    }
}
