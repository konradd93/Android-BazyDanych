package pl.kondi.bazatelefonow;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import pl.kondi.bazatelefonow.data.PhonesContract;

public class PhonesCursorAdapter extends CursorAdapter{
    public PhonesCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Producent
        TextView manufacturerTextView = (TextView) view.findViewById(R.id.tvItemManufacturer);
        int manufacturerColumn = cursor.getColumnIndex(PhonesContract.PhonesEntry.COLUMN_MANUFACTURER);
        String manufacturerText = cursor.getString(manufacturerColumn);
        manufacturerTextView.setText(manufacturerText);
        //Model
        TextView modelTextView = (TextView) view.findViewById(R.id.tvItemModel);
        int modelColumn = cursor.getColumnIndex(PhonesContract.PhonesEntry.COLUMN_MODEL);
        String modelText = cursor.getString(modelColumn);
        modelTextView.setText(modelText);
    }
}
