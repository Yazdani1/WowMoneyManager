package yazdaniscodelab.wowmoneymanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import yazdaniscodelab.wowmoneymanager.Model.Data;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mExpenseDatabase;
    private FirebaseAuth mAuth;

    private int totalvalue;
    private String strValue;
    private TextView txtTotalvalue;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser muser=mAuth.getCurrentUser();
        String Uid=muser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(Uid);
        mExpenseDatabase.keepSynced(true);
        recyclerView=myview.findViewById(R.id.recycler_id);

        txtTotalvalue=myview.findViewById(R.id.txt_total);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalvalue=0;

                for (DataSnapshot mysnapshot:dataSnapshot.getChildren()){
                    Data data=mysnapshot.getValue(Data.class);
                    totalvalue+=data.getAmmount();
                    strValue=String.valueOf(totalvalue);
                    txtTotalvalue.setText(strValue+".00");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return myview;

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,IncomeFragment.MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, IncomeFragment.MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recycler_data,
                        IncomeFragment.MyViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(IncomeFragment.MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmmount(+model.getAmmount());

            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myview=itemView;
        }

        public void setType(String type){

            TextView mtype=myview.findViewById(R.id.type_data);
            mtype.setText(type+".00");

        }

        public void setAmmount(int ammount){

            TextView mAmmount=myview.findViewById(R.id.amount_data);

            String myammount=String.valueOf(ammount);

            mAmmount.setText(myammount+".00");

        }

        public void setNote(String note){

            TextView mNote=myview.findViewById(R.id.note_data);
            mNote.setText(note);

        }

        public void setDate(String date){

            TextView mDate=myview.findViewById(R.id.date_data);
            mDate.setText(date);

        }

    }





}
