package pl.kondi.bazatelefonow;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.kondi.bazatelefonow.data.PhonesContract;
import pl.kondi.bazatelefonow.data.PhonesQueryHandler;
import pl.kondi.bazatelefonow.databinding.ActivityEditBinding;
import pl.kondi.bazatelefonow.model.Phone;

public class EditActivity extends AppCompatActivity {
    Phone phone;
    PhonesQueryHandler handler;
    ActivityEditBinding binding;
    Intent intentPhonesList;
    Boolean isCreatingNew=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    Intent intent = getIntent();
    phone = (Phone) intent.getSerializableExtra("phone");
    if (phone == null) {
        phone = new Phone();
        isCreatingNew = true;
    }
        //for two way data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        binding.setPhone(phone);
        handler = new PhonesQueryHandler(getContentResolver());
       // Log.d("Id", String.valueOf(phone.id.get()));
        if(savedInstanceState!=null){
            String[] phoneForm = savedInstanceState.getStringArray("phoneForm");
            phone.manufacturer.set(phoneForm[0]);
            phone.model.set(phoneForm[1]);
            phone.android_version.set(phoneForm[2]);
            phone.www.set(phoneForm[3]);
        }

    }

    //when Zapisz onClick
    public void savePhone(View view) {
        if(phone != null && phone.id.get() != 0){
            //update existing phone
            String[] args = new String[1];
            args[0] = String.valueOf(phone.id.get());
            ContentValues values = new ContentValues();
            values.put(PhonesContract.PhonesEntry.COLUMN_MANUFACTURER, phone.manufacturer.get());
            values.put(PhonesContract.PhonesEntry.COLUMN_MODEL, phone.model.get());
            values.put(PhonesContract.PhonesEntry.COLUMN_ANDROIDVERSION, phone.android_version.get());
            values.put(PhonesContract.PhonesEntry.COLUMN_WWW, phone.www.get());
            handler.startUpdate(1,null, PhonesContract.PhonesEntry.CONTENT_URI,values,
                    PhonesContract.PhonesEntry._ID + "=?", args);
            intentPhonesList = new Intent(EditActivity.this, PhoneListActivity.class);
            startActivity(intentPhonesList);
        }
        //insert new phone when doesn't exists
        else if(phone != null && phone.id.get() == 0){
           // Log.d("Phone: ",String.valueOf(phone.id.get())+"-"+phone.manufacturer.get()+"-"+phone.model.get()+"-"+phone.android_version.get()+"-"+phone.www.get());
            //add new phone
            ContentValues values = new ContentValues();
            values.put(PhonesContract.PhonesEntry.COLUMN_MANUFACTURER, phone.manufacturer.get());
            values.put(PhonesContract.PhonesEntry.COLUMN_MODEL, phone.model.get());
            values.put(PhonesContract.PhonesEntry.COLUMN_ANDROIDVERSION, phone.android_version.get());
            values.put(PhonesContract.PhonesEntry.COLUMN_WWW, phone.www.get());
            handler.startInsert(1,null, PhonesContract.PhonesEntry.CONTENT_URI,values);

        }
        intentPhonesList = new Intent(EditActivity.this, PhoneListActivity.class);
        startActivity(intentPhonesList);

    }
    //when Anuluj onClick
    public void cancelEdit(View view) {
        String message;
        if(isCreatingNew) message = "Dane nie zostaną zapisane.";
        else message = "Ewentualne zmiany nie zostaną zapisane.";
        new AlertDialog.Builder(EditActivity.this)
                .setTitle("Czy na pewno chcesz wyjść?")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intentPhonesList = new Intent(EditActivity.this, PhoneListActivity.class);
                                startActivity(intentPhonesList);
                            }
                        })
                .setNegativeButton(android.R.string.no,null).show();
    }

    //when www onClick
    public void goToWww(View view) {
        String adres =phone.www.get();
        StringBuilder stringBuilder = new StringBuilder(adres);

        if(!adres.startsWith("http://"))
        stringBuilder.insert(0,"http://");
        Intent zamiarPrzegladarki = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuilder.toString()));
        startActivity(zamiarPrzegladarki);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String[] phoneForm = {phone.manufacturer.get(), phone.model.get(),
                phone.android_version.get(), phone.www.get()};
        outState.putStringArray("phoneForm", phoneForm);

        super.onSaveInstanceState(outState);
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system
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



}
