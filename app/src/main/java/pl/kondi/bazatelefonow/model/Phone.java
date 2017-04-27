package pl.kondi.bazatelefonow.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

import pl.kondi.bazatelefonow.BR;


public class Phone extends BaseObservable implements Serializable {
    public final ObservableInt id = new ObservableInt();
    public final ObservableField<String> manufacturer = new ObservableField<String>();
    public final ObservableField<String> model = new ObservableField<String>();
    public final ObservableField<String> android_version = new ObservableField<String>();
    public final ObservableField<String> www = new ObservableField<String>();
    public boolean wwwValidated = false;
    private boolean manufacturerValidated = false;
    private boolean modelValidated = false;
    private boolean androidVersionValidated = false;
    public boolean allValidated = false;

    public Phone(int id, String manufacturer, String model, String android_version, String www){
        this.id.set(id);
        this.manufacturer.set(manufacturer);
        this.model.set(model);
        this.android_version.set(android_version);
        this.www.set(www);
    }

    public Phone() {
    }


//listenery
    public void wwwOnFocusChange(View v, boolean hasFocus) {
          if (!hasFocus && !wwwValidated) {
                      Toast.makeText(v.getContext(), //kontekst-zazwyczaj referencja do Activity
                      "Niepoprawny adres. Zacznij od www.", //napis do wyświetlenia
                      Toast.LENGTH_SHORT).show();
                        }
    }
    public void manufacturerOnFocusChange(View v, boolean hasFocus) {
         if (!hasFocus && !manufacturerValidated) {
                      Toast.makeText(v.getContext(), //kontekst-zazwyczaj referencja do Activity
                      "Pole producent nie może być puste.", //napis do wyświetlenia
                      Toast.LENGTH_SHORT).show();
                        }
    }
    public void modelOnFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && !modelValidated) {
            Toast.makeText(v.getContext(), //kontekst-zazwyczaj referencja do Activity
                    "Pole model nie może być puste.", //napis do wyświetlenia
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void androidVersionOnFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && !androidVersionValidated) {
            Toast.makeText(v.getContext(), //kontekst-zazwyczaj referencja do Activity
                    "Pole wersja androida nie może być puste.", //napis do wyświetlenia
                    Toast.LENGTH_SHORT).show();
        }
    }
//listeners all edit text, whether are valid(whether are matching)
    public void modelAfterTextChanged(Editable s) {
        modelValidated = !s.toString().isEmpty();
        check();
    }
    public void manufacturerAfterTextChanged(Editable s) {
        manufacturerValidated = !s.toString().isEmpty();
        check();
    }
    public void androidVersionAfterTextChanged(Editable s) {
        androidVersionValidated = !s.toString().isEmpty();
        check();
    }
    public void wwwAfterTextChanged(Editable s) {
        wwwValidated = MyModyficatedPatterns.WEB_URL.matcher(s.toString()).matches() && (s.toString().startsWith("www.") || s.toString().startsWith("http://www.") || s.toString().startsWith("https://www."));
        //Log.d("1",String.valueOf(MyModyficatedPatterns.WEB_URL.matcher(s.toString()).matches()));
        check();
    }

    //metoda pomocnicza sprawdzająca czy buttony mogą już się pojawić
    //oraz zakomunikowanie ewentualnych zmian aby stan przycisku w xml się zmienił
    public void check() {
        boolean lastStatusValidating = allValidated;
        allValidated = (modelValidated && manufacturerValidated && androidVersionValidated && wwwValidated);
        if(lastStatusValidating!=allValidated)
            notifyPropertyChanged(BR._all);
    }

}
