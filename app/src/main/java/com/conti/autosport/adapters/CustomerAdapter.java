package com.conti.autosport.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.conti.autosport.R;
import com.conti.autosport.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private ArrayList<Customer> listaClientes;
    private ArrayList<Customer> filteredClients;
    private Context context;

    public ArrayList<Customer> getFilteredClients() {
        return filteredClients;
    }

    public void setFilteredClients(ArrayList<Customer> filteredClients) {
        this.filteredClients = filteredClients;
    }

    Filtro filter;

    public CustomerAdapter(Context context, ArrayList<Customer> clientes) {
        super(context, R.layout.cliente_row, clientes);
        // TODO Auto-generated constructor stub
        listaClientes = clientes;
        filteredClients = clientes;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filteredClients.size();
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
            vh.nameTxt.setText(filteredClients.get(position).getName());
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();

            vh.nameTxt = (TextView) convertView.findViewById(R.id.nametxt);
            vh.nameTxt.setText(filteredClients.get(position).getName());
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
                results.values = listaClientes;
                results.count = listaClientes.size();
            } else {
                List<Customer> nClients = new ArrayList<Customer>();

                for (Customer f : listaClientes) {

                    if (f.getName().toUpperCase()
                            .contains(constraint.toString().toUpperCase()))
                        nClients.add(f);
                }

                results.values = nClients;
                results.count = nClients.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.count == 0) {
                filteredClients = new ArrayList<Customer>();
                notifyDataSetChanged();
            } else {
                filteredClients = (ArrayList<Customer>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
