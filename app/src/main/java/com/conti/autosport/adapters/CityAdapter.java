package com.conti.autosport.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.conti.autosport.R;
import com.conti.autosport.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {

    private ArrayList<City> listaCidades;
    private ArrayList<City> filteredCidades;
    private Context context;

    public ArrayList<City> getFilteredClients() {
        return filteredCidades;
    }

    public void setFilteredClients(ArrayList<City> filteredCidades) {
        this.filteredCidades = filteredCidades;
    }

    Filtro filter;

    public CityAdapter(Context context, ArrayList<City> cidades) {
        super(context, R.layout.cliente_row, cidades);
        // TODO Auto-generated constructor stub
        listaCidades = cidades;
        filteredCidades = cidades;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filteredCidades.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.cliente_row, null, false);

            vh = new ViewHolder();
            vh.nameTxt = (TextView) convertView.findViewById(R.id.nametxt);
            vh.nameTxt.setText(filteredCidades.get(position).getNome());
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();

            vh.nameTxt = (TextView) convertView.findViewById(R.id.nametxt);
            vh.nameTxt.setText(filteredCidades.get(position).getNome());
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView nameTxt;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filtro();
        return filter;
    }

    private class Filtro extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = listaCidades;
                results.count = listaCidades.size();
            } else {
                List<City> nCidades = new ArrayList<City>();

                for (City f : listaCidades) {

                    if (f.getNome().toUpperCase()
                            .contains(constraint.toString().toUpperCase()))
                        nCidades.add(f);
                }

                results.values = nCidades;
                results.count = nCidades.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.count == 0) {
                filteredCidades = new ArrayList<City>();
                notifyDataSetChanged();
            } else {
                filteredCidades = (ArrayList<City>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
