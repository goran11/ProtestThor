package com.example.goran.protestthor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.goran.protestthor.data.DbUtil;
import com.example.goran.protestthor.data.ProtestorContract;


public class ParoleEditorActivity extends ActionBarActivity {

    public final static String PROTEST_ID_KEY = "ProtestID";
    private long protestID;
    private long paroleID;


    private TextView txtActivityTitle;
    private EditText txtParoleText;
    private Button btnNewParole;
    private Button btnSaveParole;
    private Button btnDeleteParole;


    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parole_editor);
        protestID = getIntent().getLongExtra(PROTEST_ID_KEY, 0);

        txtActivityTitle = (TextView) findViewById(R.id.txtActivityTitle);
        txtParoleText = (EditText) findViewById(R.id.txtParoleText);
        btnNewParole = (Button) findViewById(R.id.btnNewParole);
        btnSaveParole = (Button) findViewById(R.id.btnSaveParole);
        btnDeleteParole = (Button) findViewById(R.id.btnDeleteParole);

        this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.list_item_parole,
                null,
                new String[] {
                        ProtestorContract.ParoleEntry.COLUMN_PAROLE_TEXT
                },
                new int[] {
                        R.id.list_item_parole_text,
                },
                0);


        this.cursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder()
        {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i)
            {
                if (view.getId() == R.id.list_item_parole_text)
                {
                    ((TextView)view).setText(cursor.getString(cursor.getColumnIndex(ProtestorContract.ParoleEntry.COLUMN_PAROLE_TEXT)));
                    return true;
                }
                return false;
            }

        });

        ListView listViewParole = (ListView)this.findViewById(R.id.list_view_protest);
        listViewParole.setAdapter(cursorAdapter);
        listViewParole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ((SimpleCursorAdapter)((ListView)parent).getAdapter()).getCursor();
                if (c.moveToPosition(position))
                {
                    btnDeleteParole.setVisibility(View.VISIBLE);
                    btnNewParole.setVisibility(View.VISIBLE);
                    txtActivityTitle.setText("Uredi parolu");
                    paroleID = c.getInt(c.getColumnIndex(ProtestorContract.ParoleEntry._ID));
                    txtParoleText.setText(c.getString(c.getColumnIndex(ProtestorContract.ParoleEntry.COLUMN_PAROLE_TEXT)));
                }
            }
        });


        LoadData();

        btnNewParole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewRecord();
            }
        });

        btnSaveParole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = DbUtil.getWritableDatabase(getApplicationContext());
                ContentValues cv = new ContentValues();
                cv.put(ProtestorContract.ParoleEntry.COLUMN_PAROLE_TEXT, txtParoleText.getText().toString());
                cv.put(ProtestorContract.ParoleEntry.COLUMN_PROTEST_ID, (int)protestID);
                if(paroleID == 0) {
                    long id = db.insert(ProtestorContract.ParoleEntry.TABLE_NAME, null, cv);
                    paroleID = id;
                }
                else {
                    db.update(ProtestorContract.ParoleEntry.TABLE_NAME, cv, ProtestorContract.ParoleEntry._ID + "=" + paroleID, null);
                }
                db.close();
                NewRecord();
                LoadData();
            }
        });

        btnDeleteParole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(paroleID > 0) {

                SQLiteDatabase db = DbUtil.getWritableDatabase(getApplicationContext());
                db.delete(ProtestorContract.ParoleEntry.TABLE_NAME, ProtestorContract.ParoleEntry._ID + "=" + paroleID, null);
                db.close();
                NewRecord();
                LoadData();
            }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parole_editor, menu);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null && !cursor.isClosed()) cursor.close();
        if(db != null && db.isOpen()) db.close();
    }

    private void LoadData() {
        if(db == null) db = DbUtil.getReadableDatabase(this);
        cursor = db.query(ProtestorContract.ParoleEntry.TABLE_NAME, null, ProtestorContract.ParoleEntry.COLUMN_PROTEST_ID + "=" + protestID, null, null, null, ProtestorContract.ParoleEntry._ID + " DESC");

        //this.cursorAdapter.swapCursor(cursor);
        this.cursorAdapter.changeCursor(cursor);//Mjenja + zatvara kursor

    }

    private void NewRecord() {
        btnDeleteParole.setVisibility(View.GONE);
        btnNewParole.setVisibility(View.GONE);
        txtActivityTitle.setText("Nova parola");
        txtParoleText.setText("");
        paroleID = 0;
    }


}
