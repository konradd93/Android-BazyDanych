package pl.kondi.bazatelefonow.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;


public class PhonesQueryHandler extends AsyncQueryHandler {
    public PhonesQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
