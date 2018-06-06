package yazdaniscodelab.wowmoneymanager;


import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import javax.microedition.khronos.egl.EGLDisplay;

import yazdaniscodelab.wowmoneymanager.Model.Data;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference maindatabase;
    private FirebaseAuth mAuth;


    private int totalvalue;
    private String strValue;
    private TextView txtTotalvalue;





     String gtType;
     String gtNote;
     String gtAmmount;

     //Edt

    private EditText edt_Tyepe;
    private EditText edt_note;
    private EditText edt_ammount;

    private Button btn_update;
    private Button btn_delete;

    //Update  value..

    String mdType;
    String mdNote;
    int ndAmount;
    String post_key;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_income, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser muser=mAuth.getCurrentUser();
        String Uid=muser.getUid();
        txtTotalvalue=myview.findViewById(R.id.txt_total);

        maindatabase=FirebaseDatabase.getInstance().getReference().child("MainData").child(Uid);


        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(Uid);
        mIncomeDatabase.keepSynced(true);
        recyclerView=myview.findViewById(R.id.recycler_id);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
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

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recycler_data,
                        MyViewHolder.class,
                        mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model,final int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmmount(model.getAmmount());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       post_key=getRef(position).getKey();

                        mdType=model.getType();
                        mdNote=model.getNote();
                        ndAmount=model.getAmmount();

                        upDate();

                       //upDate(mdType,mdNote,ndAmount,post_key);


//                        Intent intent = new Intent(getActivity().getBaseContext(),
//                                UpdateActivity.class);
//                        intent.putExtra("key", post_key);
//                        intent.putExtra("type",model.getType());
//                        intent.putExtra("ammopunt",String.valueOf(model.getAmmount()));
//                        intent.putExtra("note",model.getNote());
//                        getActivity().startActivity(intent);

                    }
                });

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
            mtype.setText(type);

        }

        public void setAmmount(int ammount){

            TextView mAmmount=myview.findViewById(R.id.amount_data);

            String myammount=String.valueOf(ammount);

            mAmmount.setText(myammount);

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


    public void upDate(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_layout,null);
        mydialog.setView(myview);


        edt_Tyepe=myview.findViewById(R.id.edittext_Type_upd);
        edt_note=myview.findViewById(R.id.edittext_Note_upd);
        edt_ammount=myview.findViewById(R.id.edittext_Amount_upd);

        btn_update=myview.findViewById(R.id.btnUpadete_upd);
        btn_delete=myview.findViewById(R.id.btndelete_upd);



        //SEtt data to edittext field...

        edt_Tyepe.setText(mdType);
        edt_Tyepe.setSelection(mdType.length());

        edt_note.setText(mdNote);
        edt_note.setSelection(mdNote.length());

        edt_ammount.setText(String.valueOf(ndAmount));
        edt_ammount.setSelection(String.valueOf(ndAmount).length());

        final AlertDialog dialog=mydialog.create();


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mdType=edt_Tyepe.getText().toString().trim();
                mdNote=edt_note.getText().toString().trim();

                String conStrvalue=String.valueOf(ndAmount);
                conStrvalue=edt_ammount.getText().toString().trim();

                //update ammount  need to convert intewger..

                int upAmmount=Integer.parseInt(conStrvalue);

                String mNoteDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(upAmmount,mdType,mdNote,post_key,mNoteDate);
                mIncomeDatabase.child(post_key).setValue(data);


                Toast.makeText(getActivity(),"Data Updated",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncomeDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }



}
