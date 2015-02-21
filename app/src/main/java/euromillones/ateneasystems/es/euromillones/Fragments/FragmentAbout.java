package euromillones.ateneasystems.es.euromillones.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentAbout extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * Declaración de Variables
         */
        String texto = new String();
        /**
         * Declaración de componentes en Fragment
         */
        TextView tv_about = (TextView) getActivity().findViewById(R.id.tv_about);
        /**
         * Contenido
         */
        texto = "Euromillones es una aplicación creada por <a href=\"http://www.ateneasystems.es\">AteneaSystems</a>. Euromillones está en una fase de pruebas (Versión Beta) y por lo tanto se esta trabajando para corregir los errores. Durante la versión beta, el acceso será limitado y cada nuevo registro tendrá que ser activado por un administrador.";
        tv_about.setText(Html.fromHtml(texto));
        tv_about.setMovementMethod(LinkMovementMethod.getInstance());


    }

}