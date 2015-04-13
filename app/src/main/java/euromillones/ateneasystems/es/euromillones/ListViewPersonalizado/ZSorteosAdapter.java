package euromillones.ateneasystems.es.euromillones.ListViewPersonalizado;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 11/02/15.
 */
public class ZSorteosAdapter extends RecyclerView.Adapter<ZSorteosAdapter.ViewHolder> {

    private ArrayList<ZSorteosDatos> courses;
    private int itemLayout;


    public ZSorteosAdapter(ArrayList<ZSorteosDatos> data, int itemLayout) {
        courses = data;
        this.itemLayout = itemLayout;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

        public TextView description;
        public TextView tv_fechaSorteo;
        public TextView tv_numeroSorteo;

        public ViewHolder(View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);
            tv_fechaSorteo = (TextView) itemView.findViewById(R.id.tv_fechaSorteo);
            tv_numeroSorteo = (TextView) itemView.findViewById(R.id.tv_numeroSorteo);
        }


        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "Sorteo Día: " + this.tv_fechaSorteo.getText(), Toast.LENGTH_SHORT).show();
            /*
            * Compartimos el mensaje
            * */
            Context ctx = view.getContext();//Cogemos el context del View del boton para usarlo mas adelante
            String asunto = "Resultado Euromillones día "+this.tv_fechaSorteo.getText();//Solo para cosas como los mails
            String mensaje = "#Euromillones\nSorteo día "+this.tv_fechaSorteo.getText()+" -> "+this.tv_numeroSorteo.getText()+"\nBájate la APP para Android http://goo.gl/Nj0lHN\n#AteneaSystems";//Mensaje en si
            Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
            txtIntent .setType("text/plain");
            txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
            txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
            //view.getContext().startActivity(Intent.createChooser(txtIntent, "Share"));//ponemos view.getContext() antes del startactivity porque no hay activity aqui
            ctx.startActivity(Intent.createChooser(txtIntent, ctx.getString(R.string.tv_activity_ultimos_res_compartir)));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ZSorteosDatos course = courses.get(position);
        viewHolder.tv_fechaSorteo.setText(course.getFechaSorteo());
        viewHolder.tv_numeroSorteo.setText(course.getNumeroSorteo());
        viewHolder.itemView.setTag(course);
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }

}