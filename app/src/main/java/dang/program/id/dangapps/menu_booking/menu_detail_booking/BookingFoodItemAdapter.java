package dang.program.id.dangapps.menu_booking.menu_detail_booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dang.program.id.dangapps.R;

public class BookingFoodItemAdapter extends BaseAdapter {

    Context context;
    String[] title_item, price_item, quantity;
    private static LayoutInflater inflater = null;

    public BookingFoodItemAdapter(Context context, String[] title_item, String[] price_item, String[] quantity) {
        this.context = context;
        this.title_item = title_item;
        this.price_item = price_item;
        this.quantity = quantity;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return title_item.length;
    }

    @Override
    public Object getItem(int i) {
        return title_item[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_item_booking, null);
        TextView tvTitleItem = vi.findViewById(R.id.title_item_booking);
        tvTitleItem.setText(title_item[i]);

        TextView tvPriceItem = vi.findViewById(R.id.price_item_booking);
        tvPriceItem.setText(price_item[i]);

        TextView tvQuantity = vi.findViewById(R.id.qty_item_booking);
        tvQuantity.setText(quantity[i]);
        return vi;
    }
}
