package euromillones.ateneasystems.es.euromillones;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cubel on 11/02/15.
 */
public class ZSorteosAdapter extends ArrayAdapter {

    Activity context;
    ArrayList<ZSorteosDatos> listaSorteos;

    // Le pasamos al constructor el contecto y la lista de contactos
    public ZSorteosAdapter(Activity context, ArrayList<ZSorteosDatos> listaSorteos) {
        super(context, R.layout.listview_item_sorteos, listaSorteos);
        this.context = context;
        this.listaSorteos = listaSorteos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Rescatamos cada item del listview y lo inflamos con nuestro layout
        View item = convertView;

        item = context.getLayoutInflater().inflate(R.layout.listview_item_sorteos, null);
        ZSorteosDatos c = listaSorteos.get(position);

        // Definimos los elementos que tiene nuestro layout
        TextView tv_numeroSorteo = (TextView) item.findViewById(R.id.tv_numeroSorteo);
        TextView tv_fechaSorteo = (TextView) item.findViewById(R.id.tv_fechaSorteo);

        tv_numeroSorteo.setText(c.getNumeroSorteo());
        tv_fechaSorteo.setText(c.getFechaSorteo());
        return (item);
    }
}
