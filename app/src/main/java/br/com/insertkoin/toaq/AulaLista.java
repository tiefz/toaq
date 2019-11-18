package br.com.insertkoin.toaq;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AulaLista extends ArrayAdapter<String> {

    private Activity context;
    private List<String> listaAula;

    public AulaLista(Activity context, List<String> listaAula) {
        super(context, R.layout.aula_lista, listaAula);
        this.context = context;
        this.listaAula = listaAula;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.aula_lista, null, true);
        TextView nomeAulaText = listViewItem.findViewById(R.id.nomeAulaText);
        TextView dataAulaText = listViewItem.findViewById(R.id.dataAulaText);

        String aula = listaAula.get(position);

        nomeAulaText.setText("Aula teste");
        dataAulaText.setText(aula);

        return listViewItem;
    }
}
