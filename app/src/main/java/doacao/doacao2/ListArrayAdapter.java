package doacao.doacao2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alexandre on 24/02/2015.
 */
public class ListArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public ListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.list_item_layout,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_item_text);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.list_item_image);
        textView.setText(values[position]);
        // change the icon for Windows and iPhone
//        String s = values[position];
//        if (s.startsWith("iPhone")) {
//            imageView.setImageResource(R.drawable.no);
//        } else {
//            imageView.setImageResource(R.drawable.ok);
//        }
        return rowView;
    }
}