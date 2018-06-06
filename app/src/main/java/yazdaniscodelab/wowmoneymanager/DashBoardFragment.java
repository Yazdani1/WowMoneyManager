package yazdaniscodelab.wowmoneymanager;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import yazdaniscodelab.wowmoneymanager.Model.Data;

import static android.support.constraint.ConstraintSet.INVISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    private EditText editTextTYPE;
    private EditText editTextNote;
    private EditText editTextAmmount;
    private Button buttonSave;
    private Button buttonCancel;



//    credit data


    private EditText editTextCredit_TYPE;
    private EditText editTextCredit_Note;
    private EditText editTextCredit_Ammount;
    private Button Credit_buttonSave;
    private Button Credit_buttonCancel;


    //Firebase database..

    private DatabaseReference incomeDatabase;
    private DatabaseReference expenseDatabase;
    private DatabaseReference maindatabase;

    private FirebaseAuth mAuth;


    //Credit data variable

    private String ammount_Credit;
    private String type_Credit;
    private String note_Credit;

    //Debit data variable

    private String ammount_Debit;
    private String type_Debit;
    private String note_Debit;


    //Query for last 10 data..

    private Query mRecentQuery;



    FloatingActionButton fab_plus,expense_ammount,income_ammount;

    TextView two,three;


    private Animation FadeOpen,FadeClose;



     boolean isOpen=false;


     //For Dashbor income and expense data;

    private TextView income_text;
    private TextView expense_Text;

    //Recycler view..

    private RecyclerView recyclerView;



    //For Calculation Data..

    private String income_txt;
    private String debit_sum_value;

    private String totalt_String;

    ///int variamble for all ammount..

    //For income data calculation..

    private int income_initialize;
    private String convert_to_String;


    //For expense data calculation..

    private int expense_initialize;
    private String expnxconvert_to_String;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_dash_board, container, false);


        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();

        String uid=mUser.getUid();


        incomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        expenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        maindatabase=FirebaseDatabase.getInstance().getReference().child("MainData").child(uid);

       //mRecentQuery=maindatabase.child("MainData").child(uid).limitToFirst(5);
//        mRecentQuery.keepSynced(true);

        incomeDatabase.keepSynced(true);
        expenseDatabase.keepSynced(true);

        fab_plus=myview.findViewById(R.id.fab_plus_xml);
        income_ammount=myview.findViewById(R.id.income);
        expense_ammount=myview.findViewById(R.id.expense);

        two=myview.findViewById(R.id.incomee_txt);
        three=myview.findViewById(R.id.expense_txt);


        //For calculation adn set to text..

        income_text=myview.findViewById(R.id.income_dashboard);
        expense_Text=myview.findViewById(R.id.expense_dashboard);


        FadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out);

        //Recycler view..

        recyclerView=myview.findViewById(R.id.recyclerdata);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add();
                if (isOpen){

                    expense_ammount.startAnimation(FadeClose);
                    income_ammount.startAnimation(FadeClose);
                    expense_ammount.setClickable(false);
                    income_ammount.setClickable(false);
                    two.startAnimation(FadeClose);
                    three.startAnimation(FadeClose);
                    two.setClickable(false);
                    three.setClickable(false);
                    isOpen=false;
                }

                else {

                    expense_ammount.startAnimation(FadeOpen);
                    income_ammount.startAnimation(FadeOpen);
                    two.startAnimation(FadeOpen);
                    three.startAnimation(FadeOpen);
                    two.setClickable(true);
                    three.setClickable(true);
                    expense_ammount.setClickable(true);
                    income_ammount.setClickable(true);
                    isOpen=true;
                }


            }
        });


        incomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                income_initialize=0;

                for (DataSnapshot mdatasnapshot:dataSnapshot.getChildren()){

                    Data data=mdatasnapshot.getValue(Data.class);
                    income_initialize+=data.getAmmount();
                    //Convert integer value to string
                    convert_to_String=String.valueOf(income_initialize);
                }

                income_text.setText(convert_to_String+".00");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        expenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                expense_initialize=0;

                for (DataSnapshot exdatasnapshot:dataSnapshot.getChildren()){

                    Data data=exdatasnapshot.getValue(Data.class);
                    expense_initialize+=data.getAmmount();
                    //Convert integer value to string
                    expnxconvert_to_String=String.valueOf(expense_initialize);
                }

                expense_Text.setText(expnxconvert_to_String+".00");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return myview;
    }

    public void add(){

        //For income button

        income_ammount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incomeDataAdd();

            }
        });


        //For expense button...

        expense_ammount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                expenseData();
            }
        });

    }

    public void expenseData(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.add_debit,null);
        mydialog.setView(myview);

        editTextAmmount=myview.findViewById(R.id.edittext_Amount);
        editTextTYPE=myview.findViewById(R.id.edittext_Type);
        editTextNote=myview.findViewById(R.id.edittext_Note);

        final AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);

        buttonSave=myview.findViewById(R.id.btnsave);
        buttonCancel=myview.findViewById(R.id.btncancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ammount_Debit=editTextAmmount.getText().toString().trim();
                type_Debit=editTextTYPE.getText().toString().trim();
                note_Debit=editTextNote.getText().toString().trim();

                if (TextUtils.isEmpty(ammount_Debit)){
                    editTextAmmount.setError("Ammount Required");
                    return;
                }

                int amnt_debit=Integer.parseInt(ammount_Debit);

                if (TextUtils.isEmpty(type_Debit)){
                    editTextTYPE.setError("Type Required");
                    return;
                }

                if (TextUtils.isEmpty(note_Debit)){
                    editTextNote.setError("Note Required");
                    return;
                }

                String id=expenseDatabase.push().getKey();

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(amnt_debit,type_Debit,note_Debit,id,mDate);
                expenseDatabase.child(id).setValue(data);

                String mainDataid=maindatabase.push().getKey();
                Data mdata=new Data(amnt_debit,type_Debit,note_Debit,id,mDate);
                maindatabase.child(mainDataid).setValue(mdata);


                //maindatabase.child("DebidDatabase").child(id).setValue(data);

                Toast.makeText(getActivity(),"Data Added..",Toast.LENGTH_LONG).show();



                if (isOpen){
                    expense_ammount.startAnimation(FadeClose);
                    income_ammount.startAnimation(FadeClose);
                    expense_ammount.setClickable(false);
                    income_ammount.setClickable(false);
                    two.startAnimation(FadeClose);
                    three.startAnimation(FadeClose);
                    two.setClickable(false);
                    three.setClickable(false);
                    isOpen=false;
                }

                else {
                    expense_ammount.startAnimation(FadeOpen);
                    income_ammount.startAnimation(FadeOpen);
                    two.startAnimation(FadeOpen);
                    three.startAnimation(FadeOpen);
                    two.setClickable(true);
                    three.setClickable(true);
                    expense_ammount.setClickable(true);
                    income_ammount.setClickable(true);
                    isOpen=true;
                }


                dialog.dismiss();


            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void incomeDataAdd(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.add_credit,null);
        mydialog.setView(myview);

        editTextCredit_Ammount=myview.findViewById(R.id.edittext_Amount);
        editTextCredit_TYPE=myview.findViewById(R.id.edittext_Type);
        editTextCredit_Note=myview.findViewById(R.id.edittext_Note);

        final AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);
        Credit_buttonSave=myview.findViewById(R.id.btnsave);
        Credit_buttonCancel=myview.findViewById(R.id.btncancel);

        Credit_buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ammount_Credit=editTextCredit_Ammount.getText().toString().trim();
                type_Credit=editTextCredit_TYPE.getText().toString().trim();
                note_Credit=editTextCredit_Note.getText().toString().trim();


                if (TextUtils.isEmpty(ammount_Credit)){
                    editTextCredit_Ammount.setError("Ammount Required");
                    return;
                }

                int amm_credit=Integer.parseInt(ammount_Credit);

                if (TextUtils.isEmpty(type_Credit)){
                    editTextCredit_TYPE.setError("Type Required");
                    return;
                }

                if (TextUtils.isEmpty((note_Credit))){
                    editTextCredit_Note.setError("Note Required");
                    return;
                }

                String id=incomeDatabase.push().getKey();

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(amm_credit,type_Credit,note_Credit,id,mDate);
                incomeDatabase.child(id).setValue(data);


                //Common database
                String mainDataid=maindatabase.push().getKey();
                Data indata=new Data(amm_credit,type_Credit,note_Credit,id,mDate);
                maindatabase.child(mainDataid).setValue(indata);

                //maindatabase.child("CreditDatabase").child(id).setValue(data);

                Toast.makeText(getActivity(),"Data Added..",Toast.LENGTH_LONG).show();


                if (isOpen){
                    expense_ammount.startAnimation(FadeClose);
                    income_ammount.startAnimation(FadeClose);
                    expense_ammount.setClickable(false);
                    income_ammount.setClickable(false);
                    two.startAnimation(FadeClose);
                    three.startAnimation(FadeClose);
                    two.setClickable(false);
                    three.setClickable(false);
                    isOpen=false;
                }

                else {
                    expense_ammount.startAnimation(FadeOpen);
                    income_ammount.startAnimation(FadeOpen);
                    two.startAnimation(FadeOpen);
                    three.startAnimation(FadeOpen);
                    two.setClickable(true);
                    three.setClickable(true);
                    expense_ammount.setClickable(true);
                    income_ammount.setClickable(true);
                    isOpen=true;
                }





                dialog.dismiss();

            }
        });

        Credit_buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recycler_data,
                        DashBoardFragment.MyViewHolder.class,
                        incomeDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmmount(model.getAmmount());

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







}
