package com.liberty.orda;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainThread implements OnMapReadyCallback {
    public static final MainThread Instance = new MainThread();
    private GoogleMap mMap;
    private MainActivity parentActivity;

    public MainThread() {

    }

    public void initMap(MainActivity activity) {
        parentActivity = activity;

        SupportMapFragment supportMapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlng = new LatLng(43.237376,76.857344 );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12f));

        Network network = new Network();
        List<LatLng> units = network.getUnitsPosition();

        this.setUpClusterer(parentActivity);
        this.addItems();

        messagesView = (ListView) parentActivity.findViewById(R.id.messages_view);
        //arrayList = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(parentActivity.getApplicationContext(), android.R.layout.simple_expandable_list_item_1, arrayList);
        messagesAdapter = new MessageAdapter(parentActivity.getApplicationContext());
        messagesView.setAdapter(messagesAdapter);

        parentActivity.findViewById(R.id.sendMessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText)parentActivity.findViewById(R.id.sendMessageEdit);
                String text = edit.getText().toString();
                edit.setText("");

                if(text.length() == 0) return;

                Message msg = new Message(text, new MemberData("bar", "#000"), true);
                messagesAdapter.add(msg);

                messagesView.setSelection(messagesView.getCount() - 1);
            }
        });


        String msg = "some text message!";


        messagesAdapter.add(new Message("some text",
                new MemberData("bar", "#000"), true));
        //arrayList.add(msg);
        //adapter.notifyDataSetChanged();

    }

    private ListView messagesView;
    private MessageAdapter messagesAdapter;

    class MemberData {
        private String name;
        private String color;

        public MemberData(String name, String color) {
            this.name = name;
            this.color = color;
        }

        // Add an empty constructor so we can later parse JSON into MemberData using Jackson
        public MemberData() {
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }
    }

    public class Message {
        private String text; // message body
        private MemberData memberData; // data of the user that sent this message
        private boolean belongsToCurrentUser; // is this message sent by us?

        public Message(String text, MemberData memberData, boolean belongsToCurrentUser) {
            this.text = text;
            this.memberData = memberData;
            this.belongsToCurrentUser = belongsToCurrentUser;
        }

        public String getText() {
            return text;
        }

        public MemberData getMemberData() {
            return memberData;
        }

        public boolean isBelongsToCurrentUser() {
            return belongsToCurrentUser;
        }
    }

    public class MessageAdapter extends BaseAdapter {
        List<Message> messages = new ArrayList<Message>();
        Context context;

        public MessageAdapter(Context context) {
            this.context = context;
        }

        public void add(Message message) {
            this.messages.add(message);
            notifyDataSetChanged(); // to render the list we need to notify
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int i) {
            return messages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            MessageViewHolder holder = new MessageViewHolder();
            LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            Message message = messages.get(i);

            convertView = messageInflater.inflate(R.layout.message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());

            return convertView;
        }

    }

    class MessageViewHolder {
        public View avatar;
        public TextView name;
        public TextView messageBody;
    }



    public void onMessage(String msg) {
        /*
        ListView list = (ListView) parentActivity.findViewById(R.id.messages_view);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(parentActivity.getApplicationContext(), android.R.layout.activity_list_item, arrayList);
        list.setAdapter(adapter);

        arrayList.add(msg);
        adapter.notifyDataSetChanged();
        */
    }

    public void onUnitUpdatePosition() {

    }


    public class MyItem implements ClusterItem {
        private final LatLng position;
        private final String title;
        private final String snippet;

        public MyItem(double lat, double lng, String title, String snippet) {
            position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        public String getTitle() {
            return title;
        }

        public String getSnippet() {
            return snippet;
        }
    }

    public ClusterManager<MyItem> clusterManager;

    private void setUpClusterer(MainActivity activity) {
        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        //mClusterManager = new ClusterManager<MyItem>(activity, mMap);
        clusterManager = new ClusterManager<MyItem>(activity.getApplicationContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        //mMap.setOnCameraChangeListener(this.clusterManager);
        //mMap.setOnMarkerClickListener(clusterManager);
    }

    private void addItems() {
        // Set some lat/lng coordinates to start with.
        double lat = 43.237376;
        double lng = 76.857344;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 20; i++) {
            double offset = i / 6000d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng, "ee", "bb");
            clusterManager.addItem(offsetItem);
            //mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));
        }

        clusterManager.cluster();
    }

    public void onUserInteraction() {
        clusterManager.cluster();
    }
}
