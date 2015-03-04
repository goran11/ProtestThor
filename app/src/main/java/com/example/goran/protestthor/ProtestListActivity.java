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
import android.widget.ListView;
import android.widget.TextView;

import com.example.goran.protestthor.data.DbUtil;
import com.example.goran.protestthor.data.ProtestorContract;


public class ProtestListActivity extends ActionBarActivity {

    static final int EDII_PROTEST_REQUEST = 1;  // The request code

    private static String LOG_TAG = MainActivity.class.getSimpleName();

    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protest_list);

        this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.list_item_protest,
                null,
                new String[] {
                        ProtestorContract.ProtestEntry.COLUMN_PROTEST_TITLE
                },
                new int[] {
                        R.id.list_item_protest_title,
                },
                0);


        this.cursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder()
        {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i)
            {
                if (view.getId()==R.id.list_item_protest_title)
                {
                    ((TextView)view).setText(cursor.getString(cursor.getColumnIndex(ProtestorContract.ProtestEntry.COLUMN_PROTEST_TITLE)));
                    return true;
                }
                return false;
            }

        });

        ListView listViewProtest = (ListView)this.findViewById(R.id.list_view_protest);
        listViewProtest.setAdapter(cursorAdapter);

        listViewProtest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ((SimpleCursorAdapter)((ListView)parent).getAdapter()).getCursor();
                if (c.moveToPosition(position))
                {
                    Intent intent = new Intent(getBaseContext(), ProtestEditorActivity.class);
                    intent.putExtra(ProtestEditorActivity.PROTEST_ID_KEY, c.getInt(c.getColumnIndex(ProtestorContract.ProtestEntry._ID)));
                    startActivityForResult(intent, EDII_PROTEST_REQUEST);
                }
            }
        });
        LoadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_protest_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            addProtest();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDII_PROTEST_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LoadData();//Desilo se spremanje osvježi listu
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null && !cursor.isClosed()) cursor.close();
        if(db != null && db.isOpen()) db.close();
    }

    private void addProtest() {

        Intent intent = new Intent(this, ProtestEditorActivity.class);
        startActivityForResult(intent, EDII_PROTEST_REQUEST);
    }

    //Učitaj podatke iz baze
    private void LoadData() {
        //Fill
        if(db == null) db = DbUtil.getReadableDatabase(this);
        cursor = db.query(ProtestorContract.ProtestEntry.TABLE_NAME, null, null, null, null, null, ProtestorContract.ProtestEntry._ID + " ASC");

        //this.cursorAdapter.swapCursor(cursor);
        this.cursorAdapter.changeCursor(cursor);//Mjenja + zatvara kursor
    }
}
