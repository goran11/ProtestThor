package com.example.goran.protestthor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.goran.protestthor.data.DbUtil;
import com.example.goran.protestthor.data.ProtestorContract;

import java.util.Random;
import java.util.Vector;


public class MainActivity extends ActionBarActivity  {


    private long activeProtestID;
    private TextView txtActiveProtestTitle;
    private TextView txtMesage;
    private Button btnNextParole;
    private Vector<String> paroles;
    private Vector<String> tempParoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtActiveProtestTitle = (TextView) findViewById(R.id.txtActiveProtestTitle);
        txtMesage = (TextView) findViewById(R.id.txtMesage);
        btnNextParole  = (Button) findViewById(R.id.btnNextParole);
        btnNextParole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayNextParole();
            }
        });

        LoadActiveProtest();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            editProtests();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadActiveProtest();
    }

    private void editProtests() {
        Intent intent = new Intent(this, ProtestListActivity.class);
        startActivityForResult(intent, 1);
    }



    private void LoadActiveProtest() {

        SQLiteDatabase db = DbUtil.getReadableDatabase(this);
        Cursor c = db.query(ProtestorContract.ProtestEntry.TABLE_NAME, null, ProtestorContract.ProtestEntry.COLUMN_PROTEST_ACTIVE + "=1", null, null, null, null);
        if(c.moveToFirst()) {
            long protestID = c.getLong(c.getColumnIndex(ProtestorContract.ProtestEntry._ID));

            activeProtestID = protestID;
            txtActiveProtestTitle.setText(c.getString(c.getColumnIndex(ProtestorContract.ProtestEntry.COLUMN_PROTEST_TITLE)));
            paroles = new Vector<String>();
            c.close();
            c = db.query(ProtestorContract.ParoleEntry.TABLE_NAME, null, ProtestorContract.ParoleEntry.COLUMN_PROTEST_ID + "=" + activeProtestID, null, null, null, null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                paroles.add(c.getString(c.getColumnIndex(ProtestorContract.ParoleEntry.COLUMN_PAROLE_TEXT)));
            }
            tempParoles = new Vector<String>();

            btnNextParole.setVisibility(View.VISIBLE);
            DisplayNextParole();

        }

        db.close();
    }

    private void DisplayNextParole() {
        if(paroles.size() == 0) {
            txtMesage.setText("U aktivnom protestu nema parola");
            return;
        }

        Random r = new Random();

        if(tempParoles.size() == 0) {
            tempParoles.addAll(paroles);
        }

        int index = r.nextInt(tempParoles.size());
        txtMesage.setText(tempParoles.get(index));
        tempParoles.remove(index);
    }

}
