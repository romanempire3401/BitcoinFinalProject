package edu.temple.finalproject;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlockFragment extends Fragment {
    String blockNumber = "";
    String blockURL = "";
    TextView nbInfo, hashInfo, confInfo, sizeInfo;
    Button prevButton, nextButton, searchButton;
    EditText blockNumberText;

    public BlockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_block, container, false);
        nbInfo = (TextView) v.findViewById(R.id.nbInfo);
        hashInfo = (TextView) v.findViewById(R.id.hashInfo);
        confInfo = (TextView) v.findViewById(R.id.confInfo);
        sizeInfo = (TextView) v.findViewById(R.id.sizeInfo);
        prevButton = (Button) v.findViewById(R.id.prevButton);
        nextButton = (Button) v.findViewById(R.id.nextButton);
        blockNumberText = (EditText) v.findViewById(R.id.blockNumberText);
        searchButton = (Button) v.findViewById(R.id.searchButton);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blockNumber != null) {
                    try {
                        int prev = Integer.valueOf(blockNumber);
                        prev--;
                        blockNumber = Integer.toString(prev);
                        getBlockInfo(prev);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Please enter block number", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter block number", Toast.LENGTH_LONG).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blockNumber != null) {
                    try {
                        int next = Integer.valueOf(blockNumber);
                        next++;
                        blockNumber = Integer.toString(next);
                        getBlockInfo(next);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Please enter block number", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter block number", Toast.LENGTH_LONG).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockNumber = blockNumberText.getText().toString();
                try {
                    int block = Integer.parseInt(blockNumber);
                    getBlockInfo(block);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Please enter valid block number", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    private void getBlockInfo(int block) {
        blockURL = "http://btc.blockr.io/api/v1/block/info/" + block;

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(blockURL);
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
                    blockHandler.sendMessage(msg);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler blockHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            try {
                JSONObject blockObject = ((JSONObject) message.obj);
                nbInfo.setText(blockObject.getJSONObject("data").getString("nb"));
                hashInfo.setText(blockObject.getJSONObject("data").getString("hash"));
                confInfo.setText(blockObject.getJSONObject("data").getString("confirmations"));
                sizeInfo.setText(blockObject.getJSONObject("data").getString("size"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

}
