package doacao.doacao2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alexandre on 24/02/2015.
 */
public class ListArrayAdapter extends ArrayAdapter<ArrayList<String>> {

    private final Context context;
    private final ArrayList<ArrayList<String>> values;

    public ListArrayAdapter(Context context, ArrayList<ArrayList<String>> values) {
        super(context, R.layout.list_item_layout,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_layout, parent, false);
        TextView text = (TextView) rowView.findViewById(R.id.list_item_text);
        text.setText(values.get(position).get(0));
        rowView.setTag(values.get(position).get(1));
        return rowView;
    }
}
