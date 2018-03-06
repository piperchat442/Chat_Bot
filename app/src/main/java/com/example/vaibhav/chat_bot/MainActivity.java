package com.example.vaibhav.chat_bot;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vaibhav.chat_bot.DbHandler.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public List<Information> messageLists ;
    //public Button send;
    public RecyclerView recyclerView,recyclerView1;
    public MessageAdapter messageAdapter;
    public Conflict_resolver_adapter conflict_resolver_adapter;
    public  int requestcode =0;
    public String rememberName;
    public String rememberTime;
    public EditText messages;
    public SharedPreferences sharedPref;
    public SharedPreferences sharedprefs;
    public String msg;
    public int f,ff;
    public AlarmManager[] alarmManager = new AlarmManager[24];
    public AlarmManager[] alarmManager1 = new AlarmManager[24];
    public ArrayList<Call_Information> results = new ArrayList<>();
    public ArrayList<PendingIntent> intentArray = new ArrayList<>();
    public ArrayList<PendingIntent> intentArray1 = new ArrayList<>();

    public LinearLayout gall,conn,docc,locc;
    public static final int SELECT_PICTURE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPref = getSharedPreferences("chatbot_alarm", Context.MODE_PRIVATE);
        sharedprefs=getSharedPreferences("chatbot_reminder",0);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

            recyclerView = (RecyclerView)findViewById(R.id.recyclerViews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageLists = new ArrayList<>();
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        messageLists = databaseHandler.getAllContacts();
        Log.d("size of message list",messageLists.size()+"");
        if(messageLists.size()==0){
            Log.d("inside if size is ",messageLists.size()+"");
        }
        else{
            recyclerView.scrollToPosition(messageLists.size()-1);

            Log.d("inside else size is ",messageLists.size()+"");
        }


        messages =(EditText)findViewById(R.id.type_message);
        messages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("clicledontouchlistener","clickeddd");
                recyclerView.scrollToPosition(messageLists.size()-1);
                return false;
            }
        });


        ImageView imageView = (ImageView)findViewById(R.id.send);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                 msg = messages.getText().toString();
                    if(msg.contains("cancel alarm")){
                        show_alarms(sharedPref);
                        int id = Integer.parseInt(msg.charAt(13)+"");
                        System.out.println("################### id alarm"+id);
                        cancel_alarm(id);

                    }

                    if(msg.contains("cancel reminder")){
                        int id = Integer.parseInt(msg.charAt(16)+"");
                        System.out.println("################### id reminder"+id);
                        cancel_reminder(id);
                    }

                if (requestcode == 1) {

                    processMessage(msg, results);
                    messages.setText("");

                } else {


                messages.setText("");
                final String fmsg = msg.replaceAll(" ", "%20");
                Date date = new Date();
//
                String strDateFormat = "hh:mm a";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String time = sdf.format(date);
                System.out.println(time);

                messageLists.add(new Information(2, msg, time));
                recyclerView.scrollToPosition(messageLists.size() - 1);

                messageAdapter.notifyDataSetChanged();

                insert_db(msg, time, 2); // sent message type =2


                RequestQueue request = Volley.newRequestQueue(getApplicationContext());
                url url = new url();
                String server_url = url.Send_Message_Url + "?message=" + fmsg;

                JsonObjectRequest jsonarray = new JsonObjectRequest(Request.Method.GET, server_url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String clf_class = response.getString("class");
                                    String status = response.getString("status");
                                    System.out.println("status is " + status);
                                    if (status.equals("failure")) {
                                        String mesage1 = "ok";
                                        String tim = response.getString("time");

                                        messageLists.add(new Information(1, mesage1, tim));
                                        recyclerView.scrollToPosition(messageLists.size() - 1);
                                        messageAdapter.notifyDataSetChanged();


                                        insert_db(mesage1, tim, 1);


                                    } else {


                                        if (clf_class.equals("reminder")) {    // Reminder

                                            String date = response.getString("extracted_date");
                                            String extract_time = response.getString("extracted_time");
                                            String reminder = response.getString("reminder_message");
                                            String message = response.getString("message");
                                            String time = response.getString("time");


                                            String received = "date:" + date + "extracted time:" + extract_time + "reminder msg:" + reminder;
                                            Log.d("received message", received);

                                            messageLists.add(new Information(1, message, time));
                                            recyclerView.scrollToPosition(messageLists.size() - 1);
                                            messageAdapter.notifyDataSetChanged();


                                            insert_db(message, time, 1);

                                            try {
                                                sendToReminder(date, extract_time, reminder);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                        } else if (clf_class.equals("alarm")) {
                                            // alarm

                                            String date = response.getString("extracted_date");
                                            String extract_time = response.getString("extracted_time");
                                            String message = response.getString("message");


                                            String time = response.getString("time");
                                            String received = "date:" + date + "extracted time:" + extract_time + "message " + message;
                                            Log.d("received message", received);

                                            messageLists.add(new Information(1, message, time));
                                            recyclerView.scrollToPosition(messageLists.size() - 1);
                                            messageAdapter.notifyDataSetChanged();

                                            insert_db(message, time, 1);

                                            try {
                                                sendToAlarm(date, extract_time);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                        } else if (clf_class.equals("call")) {  // calling feature
                                            String name = response.getString("name");
                                            String message = response.getString("message");
                                            String time = response.getString("time");
                                            String number = response.getString("number");
                                            System.out.println("number is########" + number + "name is " + name);
                                            String msg = null;

                                            if (number.equals("null")) {


                                                String received = "name is " + name + "message is " + message;
                                                Log.d("received", received);

                                                messageLists.add(new Information(1, message, time));
                                                recyclerView.scrollToPosition(messageLists.size() - 1);
                                                messageAdapter.notifyDataSetChanged();


                                                ArrayList<Call_Information> arr = new ArrayList<>();
                                                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                                                while (phones.moveToNext()) {
                                                    String names = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                                    arr.add(new Call_Information(names, phoneNumber));

                                                }


                                                for (int i = 0; i < arr.size(); i++) {
                                                    System.out.println(arr.get(i).getName() + "  " + arr.get(i).getNumber());
                                                }

                                                results = conflict_resolve(arr, name);

                                                System.out.println("printing results retrieved");
                                                for (int i = 0; i < results.size(); i++) {
                                                    System.out.println(results.get(i).getName() + " " + results.get(i).getNumber());
                                                }

                                                if (results.size() == 1) {
                                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                    callIntent.setData(Uri.parse("tel:" + results.get(0).getNumber()));
                                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                                        return;
                                                    }
                                                    startActivity(callIntent);

                                                } else if (results.size() == 0) {


                                                    System.out.println("ooops no contact found ");
                                                    messageLists.add(new Information(1, "Sorry No contact matched", time));
                                                    recyclerView.scrollToPosition(messageLists.size() - 1);
                                                    messageAdapter.notifyDataSetChanged();

                                                } else {
                                                    requestcode = 1;


                                                    System.out.println("multiple contacts found");
                                                    StringBuilder sb = new StringBuilder("");

                                                    for (int i = 0; i < results.size(); i++) {
                                                        String msgs = results.get(i).getName() + ":-" + results.get(i).getNumber() + "\n";
                                                        sb.append(msgs);

                                                    }
                                                    System.out.println(sb.toString());
                                                    msg = sb.toString();
                                                    System.out.println("msg is" + msg);
                                                    messageLists.add(new Information(1, "Which " + name + " ?", time));
                                                    messageAdapter.notifyDataSetChanged();
                                                    rememberName = name;
                                                    rememberTime = time;

                                                    insert_db(msg, time, 1);

                                                    messageLists.add(new Information(1, sb.toString(), time));
                                                    recyclerView.scrollToPosition(messageLists.size() - 1);
                                                    messageAdapter.notifyDataSetChanged();


                                                }


//

                                                phones.close();


                                                insert_db(message, time, 1);
                                                insert_db(msg, time, 1);


                                            } else {

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + number));
                                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                                    return;
                                                }
                                                startActivity(callIntent);
                                                messageLists.add(new Information(1, message, time));
                                                recyclerView.scrollToPosition(messageLists.size() - 1);
                                                messageAdapter.notifyDataSetChanged();
                                                insert_db(message, time, 1);

                                            }


                                        } else if (clf_class.equals("-1")) {

                                            System.out.println("Inside else if where class = 2");
                                            String msg = "I didnt get you!";
                                            String time = response.getString("time");

                                            messageLists.add(new Information(1, msg, time));
                                            recyclerView.scrollToPosition(messageLists.size() - 1);
                                            messageAdapter.notifyDataSetChanged();
                                            insert_db(msg, time, 1);


                                        }

                                        else if(clf_class.equals("search")){
                                            String _query = response.getString("query");
                                            String time = response.getString("time");
                                            String message = response.getString("message");
                                            messageLists.add(new Information(1,message,time));
                                            recyclerView.scrollToPosition(messageLists.size()-1);
                                            messageAdapter.notifyDataSetChanged();
                                            insert_db(message,time,1);
                                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.com#q="+_query));
                                            startActivity(intent);

                                        }
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), " Ooops seems no Internet connection " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                request.add(jsonarray);


            }
            }
        });


        int size = messageLists.size();
        Log.d("size of array list is ",size+"");
        messageAdapter = new MessageAdapter(this,messageLists);
//        msgAdapter=new MsgAdapter(messageLists,this);


        recyclerView.setAdapter(messageAdapter);
    }

    private String  show_alarms(SharedPreferences sharedPref) {
        StringBuilder sb = new StringBuilder("");

        Map<String,?> keys = sharedPref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
            String s = entry.getKey()+" "+entry.getValue().toString()+"\n";
            sb.append(s);


        }

        System.out.println("listed alarms are "+sb.toString());
        return  sb.toString();

    }

    private void cancel_reminder(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(),
                RemindReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), id, myIntent,     PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

    private void cancel_alarm(int id) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(),
                AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), id, myIntent,     PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

    private void insert_shared(String msg, int f) {
        System.out.println("msg is"+msg+"id is "+f);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("msg", msg);
        editor.putString("f",f+"");

        editor.commit();

    }

    private void processMessage(String msg, ArrayList<Call_Information> results) {
        //assume naming conflict as parth patang and parth patangia
        //received msg is patang
       messageLists.add(new Information(2,msg,rememberTime));
       recyclerView.scrollToPosition(messageLists.size()-1);
       messageAdapter.notifyDataSetChanged();
       insert_db(msg,rememberTime,2);

        String num = null;
       for(int i=0;i<results.size();i++){
           String name = results.get(i).getName();
           if (name.contains(msg)) {
               num = results.get(i).getNumber();
               System.out.println("number retrieved is"+num);
               break;

           }
       }
       String name= "Trying to call-:"+rememberName+" "+msg;
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
       messageLists.add(new Information(1,name,rememberTime));
       recyclerView.scrollToPosition(messageLists.size()-1);
       messageAdapter.notifyDataSetChanged();
       requestcode=0;
       insert_db(name,rememberTime,1);
       rememberName=null;
       rememberTime=null;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + num));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(callIntent);





    }

    private ArrayList<Call_Information> conflict_resolve(ArrayList<Call_Information> arr, String name) {
        ArrayList<Call_Information> results = new ArrayList<>();

        for(int i=0;i<arr.size();i++){
            String names_contact = arr.get(i).getName();
            String no = arr.get(i).getNumber();
            if(names_contact.contains(name)){
                results.add(new Call_Information(names_contact,no));
            }

        }
        return results;
    }

    private void sendToReminder(String date, String extract_time, String reminder) throws ParseException {
        ff++;
        insert_shared_reminder(msg,f);
        int min=0;
        int hour=0;
        String array[] = extract_time.split(":");
        if(extract_time.contains("PM") && Integer.parseInt(array[0])!=12){

            hour = 12+Integer.parseInt(array[0]);
            System.out.println("hour in greater than 12 is"+hour);
            array[1]= array[1].replace("PM","");
            array[1] =array[1].replace(" ","");
            min=Integer.parseInt(array[1]);
            System.out.println("Hour is "+hour+"Min is "+min);

        }
        else if(extract_time.contains("PM") && Integer.parseInt(array[0])==12){
            hour = Integer.parseInt(array[0]);
            System.out.println("hour in equal than 12 is"+hour);
            array[1]= array[1].replace("PM","");
            array[1] =array[1].replace(" ","");
            min=Integer.parseInt(array[1]);
            System.out.println("Hour is "+hour+"Min is "+min);
        }
        else{
            hour =Integer.parseInt(array[0]);
            array[1]=array[1].replace("AM","");
            array[1]= array[1].replace(" ","");
            min=Integer.parseInt(array[1]);
            System.out.println("Hour is "+hour+"Min is "+min);

        }
        PendingIntent pendingIntent;
//        AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = simpleDateFormat.parse(date);

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        Date dat = new Date();
        cal_now.setTime(dat);
        cal_alarm.setTime(date1);
        cal_alarm.set(Calendar.HOUR,hour);
        cal_alarm.set(Calendar.MINUTE,min);
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }
        Intent myIntent = new Intent(getApplicationContext(), RemindReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("reminder_value",reminder);
        myIntent.putExtras(bundle);

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ff, myIntent, 0);
        alarmManager1[ff] = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager1[ff].set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis() ,pendingIntent);
        intentArray1.add(pendingIntent);




//        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);

    }

    private void insert_shared_reminder(String msg, int f) {
        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putString("msg",msg);
        editor.putString("f",f+"");
        editor.commit();

    }

    private void sendToAlarm(String date, String extract_time) throws ParseException {
        f++;
        insert_shared(msg,f);
        System.out.println("Time is "+extract_time);
        PendingIntent pendingIntent;
        int hour=0;
        AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        Log.d("extracted time ",extract_time);
        String array[] =extract_time.split(":");
        int min=0;
        if(extract_time.contains("PM") && Integer.parseInt(array[0])!=12){

            hour = 12+Integer.parseInt(array[0]);
            System.out.println("hour in greater than 12 is"+hour);
            array[1]= array[1].replace("PM","");
            array[1] =array[1].replace(" ","");
            min=Integer.parseInt(array[1]);
            System.out.println("Hour is "+hour+"Min is "+min);

        }
        else if(extract_time.contains("PM") && Integer.parseInt(array[0])==12){
            hour = Integer.parseInt(array[0]);
            System.out.println("hour in equal than 12 is"+hour);
            array[1]= array[1].replace("PM","");
            array[1] =array[1].replace(" ","");
            min=Integer.parseInt(array[1]);
            System.out.println("Hour is "+hour+"Min is "+min);
        }
        else{
            hour =Integer.parseInt(array[0]);
            array[1]=array[1].replace("AM","");
            array[1]= array[1].replace(" ","");
            min=Integer.parseInt(array[1]);
            System.out.println("Hour is "+hour+"Min is "+min);

        }

//        hour = Integer.parseInt(array[0]);
//        array[1]= array[1].replace("PM","");
//            array[1] =array[1].replace(" ","");
//            min=Integer.parseInt(array[1]);
        Date dat = new Date();



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date dates = dateFormat.parse(date);
        Log.d("date formatted",dates.toString());
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dates);
        cal_alarm.set(Calendar.HOUR,hour);
        cal_alarm.set(Calendar.MINUTE,min);
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("alarm_value","Alarm ringing on"+extract_time);
        myIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), f, myIntent, 0);
        alarmManager[f] = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager[f].set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis() ,pendingIntent);
        intentArray.add(pendingIntent);

//        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

    private void insert_db(String fmsg, String time, int i) {
        DatabaseHandler db = new DatabaseHandler(this);
        Log.d("inserting record","insertig");
        db.addContact(new Information(i,fmsg,time));
    }


//    @OnClick(R.id.attach)
//    public void showBottomSheetDialog() {
//
//
//        View view = getLayoutInflater().inflate(R.layout.item_bottom_sheet, null);
//        gall=(LinearLayout)view.findViewById(R.id.gal);
//        conn=(LinearLayout)view.findViewById(R.id.con);
//        locc=(LinearLayout)view.findViewById(R.id.loc);
//        docc=(LinearLayout)view.findViewById(R.id.doc);
//        gall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                print();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
//            }
//        });
//
//
//
//
//
//
//
//        BottomSheetDialog dialog = new BottomSheetDialog(this);
//        dialog.setContentView(view);
//        dialog.show();
//    }


    private void print() {
        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath;
                selectedImagePath = getPath(selectedImageUri);
                messageLists.add(new Information(21,selectedImagePath,"08:31pm"));
                messageLists.add(new Information(11,selectedImagePath,"08:45pm"));
                messageAdapter.notifyDataSetChanged();

                System.out.println("Image Path : " + selectedImagePath);
//                img.setImageURI(selectedImageUri);
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            messageLists.add(new Information(21,"R.mipmap.attach.png","06:09pm"));
            messageLists.add(new Information(11,"R.mipmap.attachh.png","06:13pm"));
            messageAdapter.notifyDataSetChanged();
            return true;
        }
        else if(id == R.id.image_gallery){
            Intent intent = new Intent(this,Settings.class);


//
            startActivity(intent);
        }

        else if(id == R.id.contact){
          Intent browserX = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com#q=fish"));
            startActivity(browserX);
        }

        else if(id == R.id.recycler){
            Intent intent = new Intent(this,RecyclerAct.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
