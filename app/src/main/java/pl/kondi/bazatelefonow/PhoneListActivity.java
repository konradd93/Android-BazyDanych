package pl.kondi.bazatelefonow;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import pl.kondi.bazatelefonow.data.PhonesContract;
import pl.kondi.bazatelefonow.data.PhonesQueryHandler;
import pl.kondi.bazatelefonow.model.Phone;

public class PhoneListActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    static final int ALL_RECORDS = -1;
    private static final int URL_LOADER = 0;
    private Cursor cursor;
    PhonesCursorAdapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // createListView();
       /* DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(writableDatabase, 2,3);*/

        getLoaderManager().initLoader(URL_LOADER, null, this);
        //ListView - set adapter
        lv = (ListView) findViewById(R.id.lvPhones);
        adapter = new PhonesCursorAdapter(this, cursor, false);
        lv.setAdapter(adapter);


        //When we hold on longer list item
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode,Menu menu) {
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                getMenuInflater().inflate(R.menu.menu_main, mode.getMenu());
            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_phone, menu);
                return true;
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.deletePhones:
                        long[] checkedItemIds = lv.getCheckedItemIds();
                        deletePhones(checkedItemIds);
                        return true;
                }
                return false;
            }
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,int position, long id,
                                                  boolean checked) { }

        });
        //adds the click event to the listView, reading the content, preparing Extra
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                cursor = (Cursor) adapterView.getItemAtPosition(position);
                int phoneId = cursor.getInt(cursor.getColumnIndex(PhonesContract.PhonesEntry._ID));
               // Log.d("Id", String.valueOf(phoneId));
                String phoneManufacturer = cursor.getString(cursor.getColumnIndex(
                        PhonesContract.PhonesEntry.COLUMN_MANUFACTURER));
              //  Log.d("Manufacturer", phoneManufacturer);
                String phoneModel = cursor.getString(cursor.getColumnIndex(
                        PhonesContract.PhonesEntry.COLUMN_MODEL));
              //  Log.d("Model", phoneModel);
                String phoneAndroidVersion = cursor.getString(cursor.getColumnIndex(
                        PhonesContract.PhonesEntry.COLUMN_ANDROIDVERSION));
              //  Log.d("AndrVer", phoneAndroidVersion);
                String phoneWww = cursor.getString(cursor.getColumnIndex(
                        PhonesContract.PhonesEntry.COLUMN_WWW));
              //  Log.d("Www", phoneWww);

                Phone phone = new Phone(phoneId, phoneManufacturer, phoneModel, phoneAndroidVersion, phoneWww);
               /* Log.d("Phone: ",String.valueOf(phone.id.get())+"-"+phone.manufacturer.get()
                        +"-"+phone.model.get()+"-"+phone.android_version.get()+"-"+phone.www.get());*/
                Intent intent = new Intent(PhoneListActivity.this, EditActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });


    }

    private void createListView(){
        final ListView lv = (ListView) findViewById(R.id.lvPhones);
        PhonesCursorAdapter adapter = new PhonesCursorAdapter(this, cursor, false);
        lv.setAdapter(adapter);

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

        switch (id) {
            case R.id.action_delete_all_phones:
                deletePhonesById(ALL_RECORDS);
                //createListView();
                break;
            case R.id.action_create_test:
                createPhonesDatabase();
               // createListView();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPhonesDatabase() {

            ContentValues values = new ContentValues();
        for (int i = 0; i < 20; i++) {
            values = new ContentValues();
            values.put(PhonesContract.PhonesEntry.COLUMN_MANUFACTURER, "Samsung");
            values.put(PhonesContract.PhonesEntry.COLUMN_MODEL, "G5");
            values.put(PhonesContract.PhonesEntry.COLUMN_ANDROIDVERSION, 2);
            values.put(PhonesContract.PhonesEntry.COLUMN_WWW, "www.samsung.com");
            PhonesQueryHandler handler = new PhonesQueryHandler(
                    this.getContentResolver());
            handler.startInsert(1, null, PhonesContract.PhonesEntry.CONTENT_URI, values);
        }

    }

    private void updatePhonesById(){
        int id = 2;
        String[] args = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put(PhonesContract.PhonesEntry.COLUMN_MANUFACTURER, "Nokia");
        int numRows = getContentResolver().update(PhonesContract.PhonesEntry.CONTENT_URI,
                values, PhonesContract.PhonesEntry._ID + " =?", args);
        Log.d("Updated Rows", String.valueOf(numRows));
    }

    private void deletePhones(long[] checkedItemIds){
       // long[] checkedItemIds = lv.getCheckedItemIds();
        for(long id:checkedItemIds) {
            deletePhonesById((int) id);
        }

    }

    private void deletePhonesById(int id){
        PhonesQueryHandler handler = new PhonesQueryHandler(
                getContentResolver());
        String[] args = {String.valueOf(id)};
        if(id == ALL_RECORDS){
            args = null;


            handler.startDelete(1, null,
                    PhonesContract.PhonesEntry.CONTENT_URI, null, args);
        }
        else {

            handler.startDelete(1, null,
                    PhonesContract.PhonesEntry.CONTENT_URI, PhonesContract.PhonesEntry._ID + "=?", args);
        }
    }

    public void addPhone(View view) {
        /*long[] checkedItemIds = lv.getCheckedItemIds();
        for(long id:checkedItemIds){
            lv.setItemChecked((int)id,false);
        }
        lv.clearChoices();*/

        Intent intent = new Intent(PhoneListActivity.this, EditActivity.class);
        startActivity(intent);
    }

    //Aktywność za chwilę stanie się widoczna
    @Override
    protected void onStart() {
        super.onStart();
        //Tworzenie elementów niezbędnych do uaktualniania interfejsu użytkownika
    }        //Aktywność jest na pierwszym planie

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Aktywność traci "focus". Zostanie zapauzowana.
    @Override
    protected void onPause() {
        super.onPause();
    }

    //Aktywność nie jest widoczna. Została wstrzymana.
    @Override
    protected void onStop() {
        super.onStop();
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system

    }

    //Za chwilę aktywność zostanie zniszczona
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {PhonesContract.PhonesEntry.COLUMN_MANUFACTURER,
                PhonesContract.PhonesEntry.TABLE_NAME + "." + PhonesContract.PhonesEntry._ID,
                PhonesContract.PhonesEntry.COLUMN_MODEL,
                PhonesContract.PhonesEntry.COLUMN_ANDROIDVERSION,
                PhonesContract.PhonesEntry.COLUMN_WWW};

        return new CursorLoader(
                this,
                PhonesContract.PhonesEntry.CONTENT_URI,
                projection,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
