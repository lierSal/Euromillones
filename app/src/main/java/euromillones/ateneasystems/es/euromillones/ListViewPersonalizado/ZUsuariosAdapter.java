package euromillones.ateneasystems.es.euromillones.ListViewPersonalizado;

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

import euromillones.ateneasystems.es.euromillones.FichaUserActivity;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 11/02/15.
 */
public class ZUsuariosAdapter extends RecyclerView.Adapter<ZUsuariosAdapter.ViewHolder> {

    private ArrayList<ZUsuariosDatos> courses;
    private int itemLayout;


    public ZUsuariosAdapter(ArrayList<ZUsuariosDatos> data, int itemLayout) {
        courses = data;
        this.itemLayout = itemLayout;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

        public TextView tv_nombre;
        public TextView tv_mail;

        public ViewHolder(View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);
            tv_nombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            tv_mail = (TextView) itemView.findViewById(R.id.tv_mail);
        }


        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "Usuario con mail: " + this.tv_mail.getText(), Toast.LENGTH_SHORT).show();
            /*
            * Cargamos la informacion del user
            * */
            Context ctx = view.getContext();//Cogemos el context del View del boton para usarlo mas adelante
            Intent cargarUser = new Intent(ctx, FichaUserActivity.class);
            cargarUser.putExtra("Mail",this.tv_mail.getText());
            ctx.startActivity(cargarUser);
            //String asunto = "Resultado Euromillones día "+this.tv_fechaSorteo.getText();//Solo para cosas como los mails
            //String mensaje = "#Euromillones\nSorteo día "+this.tv_fechaSorteo.getText()+" -> "+this.tv_numeroSorteo.getText()+"\nBájate la APP para Android http://goo.gl/Nj0lHN\n#AteneaSystems";//Mensaje en si
            //Intent txtIntent = new Intent(Intent.ACTION_SEND);
            //txtIntent .setType("text/plain");
            //txtIntent .putExtra(Intent.EXTRA_SUBJECT, asunto);
            //txtIntent .putExtra(Intent.EXTRA_TEXT, mensaje);
            //view.getContext().startActivity(Intent.createChooser(txtIntent, "Share"));//ponemos view.getContext() antes del startactivity porque no hay activity aqui
            //ctx.startActivity(Intent.createChooser(txtIntent, ctx.getString(R.string.tv_activity_ultimos_res_compartir)));
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

        ZUsuariosDatos course = courses.get(position);
        viewHolder.tv_nombre.setText(course.getNombreUser());
        viewHolder.tv_mail.setText(course.getEmailUser());
        viewHolder.itemView.setTag(course);
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }

}