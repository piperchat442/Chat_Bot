package com.example.vaibhav.chat_bot;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 16/12/17.
 */

public class MessageAdapter extends RecyclerView.Adapter {
    public List<Information> messsageList = new ArrayList<>();
    public Context context;

    private static final int VIEW_TYPE_MESSAGE_SENT = 2;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;
    private static final int VIEW_IMAGE_SENT=21;
    private static final int VIEW_IMAGE_RECEIVED=11;
    private static final int VIEW_CONFLICT_RECEIVED = 22;



    public MessageAdapter(Context context, List<Information> messsageList){
        this.context=context;
        this.messsageList=messsageList;


//        print();

    }


    private void print() {

        for(int i=0;i<messsageList.size();i++){
            String inf = messsageList.get(i).getMessage()+"time ="+messsageList.get(i).getTime_message()+"type = "+messsageList.get(i).getType();
            Log.d("message",inf);

        }
    }

    @Override
    public int getItemViewType(int position) {
        Information message = (Information)messsageList.get(position);
        Log.d("message view type ",message.getType()+"");
        if(message.getType() == 2){
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else if(message.getType() == 1){
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
        else if(message.getType() == 21){
            return  VIEW_IMAGE_SENT;
        }
        else if(message.getType() == 22){
            return VIEW_CONFLICT_RECEIVED;
        }
        else
            return VIEW_IMAGE_RECEIVED;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
       Log.d("inside view holder ",viewType+"");

        if(viewType == 2){
            view = LayoutInflater.from(context).inflate(R.layout.item_messages_sent,parent,false);
            return new SentMessageViewHolder(view);
        }

        else if(viewType == 1){
            view = LayoutInflater.from(context).inflate(R.layout.item_message_received,parent,false);
            return new ReceivedMessageViewHolder(view);
        }

        else if(viewType == 21){
            view = LayoutInflater.from(context).inflate(R.layout.picture_send,parent,false);
            return new PictureSent(view);
        }

        else if(viewType == 11){
            view = LayoutInflater.from(context).inflate(R.layout.picture_received,parent,false);
            return new PictureReceived(view);
        }

//        else if(viewType == 22){
//            view = LayoutInflater.from(context).inflate(R.layout.conflict_resolver,parent,false);
//            return new conflictReceived(view);
//        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Information information = (Information)messsageList.get(position);

        switch(holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewHolder)holder).bind(information);

                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:

                ((ReceivedMessageViewHolder)holder).bind(information);
                break;
            case VIEW_IMAGE_SENT:
                ((PictureSent)holder).bind(information);
                break;

            case VIEW_IMAGE_RECEIVED:
                ((PictureReceived)holder).bind(information);
                break;



        }

    }

    @Override
    public int getItemCount() {
        return messsageList.size();
    }


    public class ReceivedMessageViewHolder extends  RecyclerView.ViewHolder{

        TextView messagebody , time_received;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messagebody = (TextView)itemView.findViewById(R.id.text_message_body);
            time_received=(TextView)itemView.findViewById(R.id.text_message_time);
            Log.d("inside received","ds");

        }


        public void bind(Information msg){
            messagebody.setText(msg.getMessage());
            time_received.setText(msg.getTime_message());
        }
    }


    public class SentMessageViewHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText;

        public SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            Log.d("inside sent","ds");
        }

        void bind(Information message) {
            messageText.setText(message.getMessage());

            timeText.setText(message.getTime_message());
        }


    }


    public class PictureSent extends RecyclerView.ViewHolder{
        ImageView img;
        TextView time;
        public PictureSent(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.image_sent);
            time =(TextView)itemView.findViewById(R.id.image_time);

        }
        void bind(Information message){
            Log.d("message is",message.getMessage());
            img.setImageURI(Uri.parse(message.getMessage()));

            time.setText(message.getTime_message());

        }


    }


    public class PictureReceived extends RecyclerView.ViewHolder{
        ImageView img;
        TextView time;
        public PictureReceived(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.image_received);
            time =(TextView)itemView.findViewById(R.id.image_time);

        }
        void bind(Information message){
            Log.d("message is",message.getMessage());
           img.setImageURI(Uri.parse(message.getMessage()));
            time.setText(message.getTime_message());

        }


    }


//    public class conflictReceived extends RecyclerView.ViewHolder{
//        TextView name,number;
//
//
//        public conflictReceived(View itemView) {
//            super(itemView);
//            name = (TextView)itemView.findViewById(R.id.name_contact);
//            number =(TextView)itemView.findViewById(R.id.number);
//
//        }
//
//        void bind(Information message){
//
//
//
//
//        }
//    }






}
