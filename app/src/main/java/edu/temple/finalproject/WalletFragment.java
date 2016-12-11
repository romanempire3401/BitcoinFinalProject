package edu.temple.finalproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.R.id.edit;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {
    TextView addressInfo, balanceInfo;
    EditText walletAddressText;
    ListView savedAddresses;
    Button checkWallet;
    String addressURL = "";
    URL url;
    ArrayList<String> addresses = new ArrayList<>();
    int i = addresses.size() - 1;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(addresses.isEmpty()) {
            deserializeWalletList("walletListInfo", addresses, this.getContext());
//            Log.d("in onResume", addresses.toString());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this.getContext(), android.R.layout.simple_list_item_1, addresses);

            savedAddresses.setAdapter(arrayAdapter);
            savedAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String selectedAddress = addresses.get(position).toString();
                    getWalletBalance(selectedAddress);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        Log.d("in onPause", addresses.toString());
        serializeWalletList("walletListInfo", addresses, this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);

        addressInfo = (TextView) v.findViewById(R.id.addressInfo);
        balanceInfo = (TextView) v.findViewById(R.id.walletInfo);
        walletAddressText = (EditText) v.findViewById(R.id.walletAddressText);
        checkWallet = (Button) v.findViewById(R.id.checkWallet);
        savedAddresses = (ListView) v.findViewById(R.id.savedAddressList);

        checkWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String query = walletAddressText.getText().toString();
                    addresses.add(query);
                    i = addresses.size() - 1;
                    getWalletBalance(addresses.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_list_item_1, addresses);

        savedAddresses.setAdapter(arrayAdapter);
        savedAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedAddress = addresses.get(position).toString();
                getWalletBalance(selectedAddress);
            }
        });

        return v;
    }

    private void getWalletBalance(String address) {
        addressURL = "http://btc.blockr.io/api/v1/address/info/" + address;

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    url = new URL(addressURL);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));
                    String response = "", tmpResponse;
                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject bitcoinObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = bitcoinObject;
                    walletHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler walletHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            try {
                JSONObject walletObject = ((JSONObject) message.obj);
                double balance = walletObject.getJSONObject("data")
                        .getDouble("balance");
                String balanceString = balance + " BTC";

                balanceInfo.setText(balanceString);
                addressInfo.setText(walletObject.getJSONObject("data").getString("address"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    public void serializeWalletList(String filename, ArrayList<String> arrayList, Context context) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayList);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializeWalletList(String filename, ArrayList<String> arrayList, Context context) {
        try{
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Log.d("array in deserialize", arrayList.toString());
            addresses = (ArrayList<String>)ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
