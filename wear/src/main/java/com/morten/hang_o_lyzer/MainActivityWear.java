package com.morten.hang_o_lyzer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.util.EventListener;
import java.util.List;

import static android.graphics.Color.RED;

public class MainActivityWear extends Activity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        WearableListView.ClickListener{

    private static final String DEBUG_TAG = "Hang_O_Lyzer gestue";
    private TextView mTextView;
    private String TAG = "MainWear: ";

    Node mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    boolean mResolvingError = false;

    //For progress bar
    int pStatus = 0;
    ProgressBar pBar;

    //For list view
//    WearableListView listView;
//    String[] elements = { "List Item 1", "List Item 2", "List Item 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);

        pBar = (ProgressBar) findViewById(R.id.progressBar1);
        pBar.setSecondaryProgress(pBar.getMax()-pBar.getProgress());

        //Attempt to implement GridViewPager
        final Resources res = getResources();
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Adjust page margins:
                //   A little extra horizontal spacing between pages looks a bit
                //   less crowded on a round display.
                final boolean round = insets.isRound();
                int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = res.getDimensionPixelOffset(round ?
                        R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                pager.setPageMargins(rowMargin, colMargin);

                // GridViewPager relies on insets to properly handle
                // layout for round displays. They must be explicitly
                // applied since this listener has taken them over.
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });
        pager.setAdapter(new SimpleGridPagerAdapter(this, getFragmentManager()));
        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);

        //For sending messages to phone
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Listening to messages from the phone
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Resolve the node = the connected device to send the message to
     */
    private void resolveNode() {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                        for (Node node : nodes.getNodes()) {
                            mNode = node;
                        }
                    }
                });
    }

//    public void onButtonClicked(View target) {
//        if (mGoogleApiClient == null)
//            return;
//        if (!mGoogleApiClient.isConnected())
//            mGoogleApiClient.connect();
//
//        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
//        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
//            @Override
//            public void onResult(NodeApi.GetConnectedNodesResult result) {
//                final List<Node> nodes = result.getNodes();
//                final String msg = "Example Message";
//                if (nodes != null) {
//                    for (int i = 0; i < nodes.size(); i++) {
//                        final Node node = nodes.get(i);
//
//                        // You can just send a message
//                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/ALKOTJEK", msg.getBytes());
//
//                        // or you may want to also check check for a result:
//                        final PendingResult<MessageApi.SendMessageResult> pendingSendMessageResult = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/MESSAGE", msg.getBytes());
//                        pendingSendMessageResult.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
//                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
//                                if (sendMessageResult.getStatus().getStatusCode() == WearableStatusCodes.SUCCESS) {
//                                    // do something is successed
//                                    Toast tjektoast = Toast.makeText(getApplicationContext(), "TOAST", Toast.LENGTH_LONG);
//                                    tjektoast.show();
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
//    }

    public void onButtonClicked(View target){
        String Key = "Test message remove";
        sendMessage(Key);
        pRemove(1);
    }



    public void onButton2Clicked(View target) {
        String Key = "Test message string";
        sendMessage(Key);
        pAdd(1);
//        Toast t = Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG);
//        t.show();

    }

    //Progress bar methods
    public void pAdd(int units){
        if(pBar == null){
            pBar = (ProgressBar) findViewById(R.id.progressBar1);
        }
        int progress = pBar.getProgress();
        progress = progress + units;
        if (progress > 7){
            pBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
        pBar.setProgress(progress);
    }

    private void pRemove(int units) {
        if(pBar == null){
            pBar = (ProgressBar) findViewById(R.id.progressBar1);
        }
        int progress = pBar.getProgress();
        progress = progress - units;
        if (progress <= 7){
            pBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
        pBar.setProgress(progress);
    }

    //MessageApi implementation

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void sendMessage(String Key) {

        if (mNode != null && mGoogleApiClient!= null && mGoogleApiClient.isConnected()) {
            Log.d(TAG, "-- " + mGoogleApiClient.isConnected());
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), "/ALKOTJEK" , Key.getBytes()).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        }

    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    //Message recieving code:
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.v("myTag", "Main activity received message: " + message);
            // Display message in UI
            mTextView.setText(message);
        }
    }
}