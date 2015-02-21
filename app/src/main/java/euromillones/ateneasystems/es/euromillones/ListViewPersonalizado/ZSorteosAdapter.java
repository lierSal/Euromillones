package euromillones.ateneasystems.es.euromillones.ListViewPersonalizado;

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
            Toast.makeText(view.getContext(), "OnItemClick :D" + this.tv_fechaSorteo.getText(), Toast.LENGTH_SHORT).show();
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