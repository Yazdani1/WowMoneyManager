package yazdaniscodelab.wowmoneymanager;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import yazdaniscodelab.wowmoneymanager.Model.Data;

public class UpdateActivity extends AppCompatActivity {

    String typer;
    String note;
    String ammount;
    String post_key;

    private EditText mytype;
    private EditText myAmmount;
    private EditText myNote;

    private Button btnUpdate;
    private Button btnDelete;


    //Firebase...

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mytype=findViewById(R.id.edittext_Type_upd);
        myNote=findViewById(R.id.edittext_Note_upd);
        myAmmount=findViewById(R.id.edittext_Amount_upd);


        btnUpdate=findViewById(R.id.btnUpadete_upd);
        btnDelete=findViewById(R.id.btndelete_upd);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser muser=mAuth.getCurrentUser();
        String Uid=muser.getUid();


        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(Uid);

        Intent intent = getIntent();

        typer=intent.getStringExtra("type");
        note=intent.getStringExtra("note");
        ammount=intent.getStringExtra("ammopunt");
        post_key=intent.getStringExtra("key");


        myAmmount.setText(ammount);
        myAmmount.setSelection(ammount.length());

        mytype.setText(typer);
        mytype.setSelection(typer.length());

        myNote.setText(note);
        myNote.setSelection(note.length());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                typer=mytype.getText().toString().trim();
                note=myNote.getText().toString().trim();
                ammount=myAmmount.getText().toString().trim();

                String mDate= DateFormat.getDateInstance().format(new Date());

                int tmammount=Integer.parseInt(ammount);

                Data data=new Data(tmammount,typer,note,post_key,mDate);
                mIncomeDatabase.child(post_key).setValue(data);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIncomeDatabase.child(post_key).removeValue();
            }
        });

    }



}
