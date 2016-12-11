package edu.temple.finalproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class PriceFragment extends Fragment {
    TextView coinName;
    TextView currentPrice;

    public PriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_price, container, false);
        coinName = (TextView) v.findViewById(R.id.coinName);
        currentPrice = (TextView) v.findViewById(R.id.currentPrice);

        getCurrentPrice();

        return v;
    }

    private void getCurrentPrice() {
        Thread t = new Thread() {
            @Override
            public void run() {
                URL bitcoinURL;

                try {
                    bitcoinURL = new URL("http://btc.blockr.io/api/v1/coin/info");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    bitcoinURL.openStream()));
                    String response = "", tmpResponse;
                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject bitcoinObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = bitcoinObject;
                    priceHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler priceHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject coinObject = ((JSONObject) msg.obj);

                String currentCoinName;
                Double currentCoinPrice;
                String usdPrice;

                JSONObject data = coinObject.getJSONObject("data");

                currentCoinName = data.getJSONObject("coin").getString("name");
                currentCoinPrice = data.getJSONObject("markets").getJSONObject("coinbase").getDouble("value");
                usdPrice = "$" + currentCoinPrice + " USD";

                coinName.setText(currentCoinName);
                currentPrice.setText(usdPrice);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    });

}
