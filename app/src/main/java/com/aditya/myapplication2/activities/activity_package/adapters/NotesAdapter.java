package com.aditya.myapplication2.activities.activity_package.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.myapplication2.R;
import com.aditya.myapplication2.activities.activity_package.entities.Note;
import com.aditya.myapplication2.activities.activity_package.image_encoder_and_decoder.ImageBitmapString;
import com.aditya.myapplication2.activities.activity_package.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class  NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>
{
    //handeling search note operation in the recyclerView
    private Timer timer;
    private List<Note> notesSource;

    private List<Note> notes;

    //importing an interface class NoteListener
    //hadneling note read or update functionality
    private NotesListener notesListener;

    public NotesAdapter(List<Note> notes, NotesListener notesListener)
    {
        this.notes = notes;
        this.notesListener = notesListener;
        //hadeling search notes operations in RecyclerView
        notesSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        holder.setNote(notes.get(position));
        //setting up an onclick lister on the notes that are shown in the main activity recyclerView
        //hadneling note read or update functionality
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the position of the note using the noteListener interface class
                notesListener.onNoteClicked(notes.get(position),position);
            }
        });

        //setting up on long click listener for deleteing notes
        holder.layoutNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //handeling the note position on long clicked
                notesListener.onNoteLongClicked(notes.get(position),position);
                Log.i("note long pressed",notes.get(position).toString());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder
    {
        TextView textTitle, textSubTitle, textDateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubTitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }

        void setNote(Note note)
        {
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty())
            {
                //make textSubtitle textView invisible if subtitle of the note is not available
                textSubTitle.setVisibility(View.GONE);
            }
            else
            {
                textSubTitle.setText(note.getSubtitle());
                textSubTitle.setVisibility(View.VISIBLE);
            }
            textDateTime.setText(note.getDateTime());

            //setting color programatically to the layoutNote Linearlayout of item_container_note.xml
            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if(note.getColor() != null)
            {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }
            else
            {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getImagePath()!= null)
            {
                //setting the image View image programatically
                //decoding the file form its directory
                //imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));

                //here get the image from the database in form of byteArray
                //convert the image from byteArray to bitmap
                //then set the bitmap to the imageNote
                Bitmap bitmapImage = ImageBitmapString.ByteArrayToBitMap(note.getImage());
                imageNote.setImageBitmap(bitmapImage);
                //making the imageView visible
                imageNote.setVisibility(View.VISIBLE);
            }
            else
            {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    //handeling search note operation in the recyclerView
    public void searchNotes(final String searchKeyword)
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //here we will do search note operation
                if (searchKeyword.trim().isEmpty()) {
                    notes = notesSource;
                } else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : notesSource) {
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer()
    {
        if(timer!=null)
        {
            timer.cancel();
        }
    }

}
