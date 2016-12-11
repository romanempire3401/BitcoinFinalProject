package edu.temple.finalproject;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    ImageView graphDisplay;
    Button oneDayButton, fiveDayButton, oneMonthButton;
    String day = "";

    public GraphFragment() {
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
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        graphDisplay = (ImageView) v.findViewById(R.id.graphImageView);
        oneDayButton = (Button) v.findViewById(R.id.oneDay);
        fiveDayButton = (Button) v.findViewById(R.id.fiveDay);
        oneMonthButton = (Button) v.findViewById(R.id.oneMonth);

        oneDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = "1d";
                getGraph();
            }
        });

        fiveDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = "5d";
                getGraph();
            }
        });

        oneMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = "30d";
                getGraph();
            }
        });

        return v;
    }

    private void getGraph(){
        Thread t = new Thread(){
            @Override
            public void run() {
                URL url;

                try {
                    while (!isInterrupted()) {
                        url = new URL("https://chart.yahoo.com/z?s=BTCUSD=X&t=" + day);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        Message msg = Message.obtain();
                        msg.obj = bmp;
                        graphHandler.sendMessage(msg);
                        Thread.sleep(15000);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler graphHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bitmap bmp = (Bitmap) message.obj;
            graphDisplay.setImageBitmap(bmp);
            return false;
        }
    });

}
