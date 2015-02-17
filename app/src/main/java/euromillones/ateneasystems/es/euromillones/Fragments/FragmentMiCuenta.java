package euromillones.ateneasystems.es.euromillones.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentMiCuenta extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mi_cuenta, container, false);
    }
}