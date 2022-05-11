package com.cdp.agenda.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdp.agenda.R;
import com.cdp.agenda.VerActivity;
import com.cdp.agenda.entidades.Contactos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaContactosAdapter extends RecyclerView.Adapter<ListaContactosAdapter.ContactoViewHolder> {

    ArrayList<Contactos> listaContactos;
    ArrayList<Contactos> listaOriginal;

    public ListaContactosAdapter(ArrayList<Contactos> listaContactos) {
        this.listaContactos = listaContactos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaContactos);
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_contacto, null, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        holder.viewTitulo.setText(listaContactos.get(position).getTitulo());
        //holder.viewDireccion.setText(listaContactos.get(position).getDireccion());
        holder.viewDescripcion.setText(listaContactos.get(position).getDescripcion());
    }

    public void filtrado(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            listaContactos.clear();
            listaContactos.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Contactos> collecion = listaContactos.stream()
                        .filter(i -> i.getTitulo().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listaContactos.clear();
                listaContactos.addAll(collecion);
            } else {
                for (Contactos c : listaOriginal) {
                    if (c.getTitulo().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listaContactos.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        TextView viewTitulo, viewDireccion, viewDescripcion;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            viewTitulo = itemView.findViewById(R.id.viewTitulo);
            //viewDireccion = itemView.findViewById(R.id.viewDireccion);
            viewDescripcion = itemView.findViewById(R.id.viewDescripcion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext(); //recuperan el id del evento seleccionado
                    Intent intent = new Intent(context, VerActivity.class);
                    intent.putExtra("ID", listaContactos.get(getAdapterPosition()).getId());
                    intent.putExtra("nombreAdulto",listaContactos.get(getAdapterPosition()).getAdulto_r());
                    context.startActivity(intent);
                }
            });
        }
    }
}
