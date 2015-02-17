package euromillones.ateneasystems.es.euromillones;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by cubel on 14/02/15.
 */
public class OpcionesActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.activity_opciones);
    }
}
