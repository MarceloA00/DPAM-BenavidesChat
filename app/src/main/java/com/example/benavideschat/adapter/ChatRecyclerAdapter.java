package com.example.benavideschat.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.benavideschat.Message;
import com.example.benavideschat.R;

import java.util.ArrayList;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatModelViewHolder> {

    private ArrayList<Message> messages;
    public ChatRecyclerAdapter(ArrayList<Message> messages) {
        Log.d("TEST", messages.toString());
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_recycler_bubbles, parent, false);
        return new ChatModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position) {
        Log.d("TEST", messages.get(position).getSender());
        if(messages.get(position).getSender().equals("user")) {
            holder.userChatBubble.setVisibility(View.VISIBLE);
            holder.userChatImage.setVisibility(View.VISIBLE);
            holder.userChatCard.setVisibility(View.VISIBLE);
            holder.botChatBubble.setVisibility(View.GONE);
            holder.botChatCard.setVisibility(View.GONE);
            holder.userChatTextview.setText(messages.get(position).getMessage());
        }
        else {
            holder.botChatBubble.setVisibility(View.VISIBLE);
            holder.botChatImage.setVisibility(View.VISIBLE);
            holder.botChatCard.setVisibility(View.VISIBLE);
            holder.userChatBubble.setVisibility(View.GONE);
            holder.userChatCard.setVisibility(View.GONE);
            holder.botChatTextview.setText(messages.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout botChatBubble, userChatBubble;
        TextView botChatTextview, userChatTextview;
        CardView botChatCard, userChatCard;
        ImageView botChatImage, userChatImage;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            botChatBubble = itemView.findViewById(R.id.bot_chat_bubble);
            userChatBubble = itemView.findViewById(R.id.user_chat_bubble);
            botChatTextview = itemView.findViewById(R.id.bot_chat_textview);
            userChatTextview = itemView.findViewById(R.id.user_chat_textview);
            botChatImage = itemView.findViewById(R.id.bot_chat_image);
            userChatImage = itemView.findViewById(R.id.user_chat_image);
            botChatCard = itemView.findViewById(R.id.bot_chat_card);
            userChatCard = itemView.findViewById(R.id.user_chat_card);

        }
    }
}
