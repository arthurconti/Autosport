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

    private ArrayList<City> cityList;
    private ArrayList<City> filteredCities;
    private Context context;

    public ArrayList<City> getFilteredCities() {
        return filteredCities;
    }

    public CityAdapter(Context context, ArrayList<City> cidades) {
        super(context, R.layout.cliente_row, cidades);
        cityList = cidades;
        filteredCities = cidades;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredCities.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.cliente_row, null, false);

            vh = new ViewHolder();
            vh.nameTxt = convertView.findViewById(R.id.nametxt);
            vh.nameTxt.setText(filteredCities.get(position).getNome());
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();

            vh.nameTxt = convertView.findViewById(R.id.nametxt);
            vh.nameTxt.setText(filteredCities.get(position).getNome());
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
                results.values = cityList;
                results.count = cityList.size();
            } else {
                List<City> nCidades = new ArrayList<>();

                for (City f : cityList) {

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
                filteredCities = new ArrayList<>();
                notifyDataSetChanged();
            } else {
                filteredCities = (ArrayList<City>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
