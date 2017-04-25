package pl.kondi.bazatelefonow.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PhonesContract {
    //constants for Uri
    public static final String CONTENT_AUTHORITY = "pl.kondi.bazatelefonow.phonesprovider";
    public static final String PATH_PHONES = "phones";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class PhonesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PHONES);
        // Table name
        public final static String TABLE_NAME = "phones";
        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public final static String COLUMN_MANUFACTURER = "manufacturer";
        public final static String COLUMN_MODEL = "model";
        public final static String COLUMN_ANDROIDVERSION = "android_version";
        public final static String COLUMN_WWW = "www";

    }
}
