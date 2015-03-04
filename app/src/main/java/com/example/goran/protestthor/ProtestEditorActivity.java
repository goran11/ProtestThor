package com.example.goran.protestthor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.goran.protestthor.R;
import com.example.goran.protestthor.data.DbUtil;
import com.example.goran.protestthor.data.ProtestDbHelper;
import com.example.goran.protestthor.data.ProtestorContract;


public class ProtestEditorActivity extends ActionBarActivity {

    public final static String PROTEST_ID_KEY = "ProtestID";

    private ImageButton btnSave;
    private ImageButton btnCancel;
    private long protestID;

    private EditText txt_title;
    private EditText txt_description;
    private Switch cbProtestActive;

    private Button btnEditParoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protest_editor);
        protestID = getIntent().getIntExtra(PROTEST_ID_KEY, 0);


        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        txt_title = (EditText)findViewById(R.id.txt_title);
        txt_description = (EditText)findViewById(R.id.txt_description);
        cbProtestActive = (Switch)findViewById(R.id.cbProtestActive);
        btnEditParoles = (Button)findViewById(R.id.btnEditParoles);



        //Dohvati id iz intenta
        if(protestID == 0) {
            btnEditParoles.setVisibility(View.INVISIBLE);
            txt_title.setText("");
            txt_description.setText("");
        }
        else {
            TextView txtActivityTitle = (TextView)findViewById(R.id.txtActivityTitle);
            txtActivityTitle.setText("Izmjeni zapis");

            //Dohvati zapis iz baze i napuni kontrole
            SQLiteDatabase db = DbUtil.getReadableDatabase(this);
            Cursor c = db.query(ProtestorContract.ProtestEntry.TABLE_NAME, null, ProtestorContract.ProtestEntry._ID + "=" + protestID, null, null, null, null);
            c.moveToFirst();
            txt_title.setText(c.getString(c.getColumnIndex(ProtestorContract.ProtestEntry.COLUMN_PROTEST_TITLE)));
            txt_description.setText(c.getString(c.getColumnIndex(ProtestorContract.ProtestEntry.COLUMN_PROTEST_DESCRIPTION)));
            int active = c.getInt(c.getColumnIndex(ProtestorContract.ProtestEntry.COLUMN_PROTEST_ACTIVE));
            if(active == 1) {
                cbProtestActive.setChecked(true);
            }
            else {
                cbProtestActive.setChecked(false);
            }

            c.close();
            db.close();
        }



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = DbUtil.getWritableDatabase(getApplicationContext());
                ContentValues cv = new ContentValues();
                cv.put(ProtestorContract.ProtestEntry.COLUMN_PROTEST_TITLE, txt_title.getText().toString());
                cv.put(ProtestorContract.ProtestEntry.COLUMN_PROTEST_DESCRIPTION, txt_description.getText().toString());

                int active = 0;
                if(cbProtestActive.isChecked()) active = 1;
                cv.put(ProtestorContract.ProtestEntry.COLUMN_PROTEST_ACTIVE, active);

                if(active == 1) {//Deaktiviraj sve proteste
                    DeactivateAllProtests();
                }

                if(protestID == 0) {
                    long id = db.insert(ProtestorContract.ProtestEntry.TABLE_NAME, null, cv);
                    protestID = id;
                }
                else {
                    db.update(ProtestorContract.ProtestEntry.TABLE_NAME, cv, ProtestorContract.ProtestEntry._ID + "=" + protestID, null);
                }
                db.close();

                Intent data = new Intent();
                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    getParent().setResult(Activity.RESULT_OK, data);
                }
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEditParoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ParoleEditorActivity.class);
                intent.putExtra(ParoleEditorActivity.PROTEST_ID_KEY, protestID);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_protest_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void DeactivateAllProtests() {
        SQLiteDatabase db = DbUtil.getWritableDatabase(getApplicationContext());
        ContentValues cv = new ContentValues();
        cv.put(ProtestorContract.ProtestEntry.COLUMN_PROTEST_ACTIVE, 0);
        db.update(ProtestorContract.ProtestEntry.TABLE_NAME, cv, null, null);
        db.close();
    }
}
