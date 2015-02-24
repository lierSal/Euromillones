package euromillones.ateneasystems.es.euromillones.Personalizaciones;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by cubel on 24/02/15.
 * Esta clase lo unico que hace es para decir que numero Maximo y Minimo queremos que se
 * escriba en un editTex. El editText tiene que ser del tipo number, si no no podremos escribir.
 */
public class NumberMinMax implements InputFilter {

    private int min, max;

    public NumberMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public NumberMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}