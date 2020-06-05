package com.krraju.vise.homescreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.announcement.DataClass;
import com.krraju.vise.homescreen.announcement.SingleTon;
import com.krraju.vise.notes.PDFViewer;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class MainAnnouncementAdaptor extends RecyclerView.Adapter<MainAnnouncementAdaptor.ViewHolderClass> {

    private LinkedList<DataSnapshot> dataClassList;
    private Context context;

    MainAnnouncementAdaptor(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Announcements");
        databaseReference.keepSynced(true);
        dataClassList = SingleTon.getInstance().getDataSnapshots();
        this.context = context;
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design_announcement_card_view, parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        DataSnapshot snapshot = dataClassList.get(position);
        DataClass data = snapshot.getValue(DataClass.class);

        holder.imageView.setImageURI(null);
        holder.imageView.setVisibility(View.VISIBLE);
        holder.fileAttachedText.setVisibility(View.VISIBLE);

        if(data != null){
            holder.message.setText(data.getMessage());
            holder.dateAndTime.setText(data.getDateAndTime());
            if(data.getFileType().equals("Image")){
                holder.fileAttachedText.setVisibility(View.GONE);
                Picasso.get()
                        .load(Uri.parse(data.getFileUri()))
                        .placeholder(R.drawable.uvce_vector_bw)
                        .into(holder.imageView);
            }else if(data.getFileType().equals("File")){
                holder.imageView.setVisibility(View.GONE);
                holder.fileAttachedText.setVisibility(View.VISIBLE);
                holder.fileAttachedText.setOnClickListener(v-> viewPdf(data.getFileUri()));
            }else {
                holder.fileAttachedText.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.GONE);
            }
        }
    }

    private void viewPdf(String fileUri) {
        Intent intent = new Intent(context, PDFViewer.class);
        intent.putExtra("title","Announcement");
        intent.putExtra("uri",fileUri);
        intent.putExtra("subjectName","Announcement");
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }

    static class ViewHolderClass extends RecyclerView.ViewHolder{

        PhotoView imageView;
        TextView message;
        TextView dateAndTime;
        TextView fileAttachedText;

        ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.announcement_image);
            message = itemView.findViewById(R.id.announcement_text);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            fileAttachedText = itemView.findViewById(R.id.fileAttachFile);
        }
    }
}
