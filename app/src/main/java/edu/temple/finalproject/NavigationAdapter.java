package edu.temple.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by MacBookPro on 12/8/16.
 */

public class NavigationAdapter extends BaseAdapter {
    private Context context;
    private String[] items;

    public NavigationAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = new TextView(context);

        switch(i){
            case 0:
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coins, 0, 0, 0);
                break;
            case 1:
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.graph, 0, 0, 0);
                break;
            case 2:
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.block, 0, 0, 0);
                break;
            case 3:
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.money, 0, 0, 0);
                break;
            default:
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coins, 0, 0, 0);
                break;
        }

        tv.setText(items[i]);
        tv.setTextSize(24);
        tv.setTextColor(Color.BLACK);

        return tv;
    }
}
