package com.dharmasai0gmail.music;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;
import static android.view.View.VISIBLE;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener
{
    int mod = 3;
    static class MyComp implements Comparator<Integer>
    {
        @Override
        public int compare(Integer i, Integer j) {
            return j-i;
        }
    }
    Uri uri;
    public static ArrayList<MusicFiles> musicFiles;
    ArrayList<RelativeLayout> layoutarr = new ArrayList<>();
    ArrayList<Button> playButArr = new ArrayList<>();
    ArrayList<ImageButton> delButArrNS = new ArrayList<>();
    ArrayList<ImageButton> delButArrS = new ArrayList<>();
    public static TreeSet<Integer> delSet = new TreeSet<>(new MyComp());

    Button editsongsbtn,cancelbtn,playAllbtn,playSelectedBtn;
    ImageButton delsongsbtn;
    @Override
    public void onClick(View v)
    {
        long id = v.getId();

        int tempId = (int)(id-300);

        int pos = tempId/mod;

        if(tempId%mod == 0)
        {
            playSong(pos,false,false);
        }
        else if(tempId%mod == 1)
        {
            delSet.add(pos);
            delButArrNS.get(pos).setVisibility(GONE);
            delButArrS.get(pos).setVisibility(VISIBLE);
        }
        else if(tempId%mod == 2)
        {
            delSet.remove(pos);
            delButArrNS.get(pos).setVisibility(VISIBLE);
            delButArrS.get(pos).setVisibility(GONE);
        }
    }
    private void clearInAllArrays(int i)
    {
        musicFiles.remove(i);
        layoutarr.remove(i);
        delButArrNS.remove(i);
        delButArrS.remove(i);
        playButArr.remove(i);
    }

    public void deletelayout(int i)
    {
        LinearLayout outer = findViewById(R.id.layoutOuter);
        RelativeLayout inner = layoutarr.get(i);
        outer.removeView(inner);
    }

    private boolean deleteFile(int pos)
    {
        File file = new File(musicFiles.get(pos).getPath());
        boolean isDeleted = file.delete();
        if(isDeleted)
        {
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Long.parseLong(musicFiles.get(pos).getId()));
            this.getContentResolver().delete(contentUri,null,null);
        }
        return isDeleted;
    }

    public void deleteSong(int i)
    {
        boolean isDeleted = deleteFile(i);
        if(isDeleted)
        {
            deletelayout(i);
            clearInAllArrays(i);
            Toast.makeText(getApplicationContext(),"Deletion successful.",Toast.LENGTH_SHORT).show();
        }
        else
        {
             Toast.makeText(getApplicationContext(),"Deletion failed.",Toast.LENGTH_SHORT).show();
        }
    }

    private void playSong(int song_id,boolean isPlayAll,boolean isPlaySelected)
    {
        Intent intent=new Intent(getApplicationContext(), MusicPlaying.class);
        intent.putExtra("songName",song_id);
        intent.putExtra("isPlayAll",isPlayAll);
        intent.putExtra("isPlaySelected",isPlaySelected);
        //Toast.makeText(getApplicationContext(),""+song_id + " " + musicFiles.size(),Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void setListenersToAllButtons(Context context)
    {
        for(ImageButton delBut:delButArrNS) delBut.setOnClickListener(this);
        for(ImageButton delBut:delButArrS) delBut.setOnClickListener(this);
        for(Button playBut:playButArr) playBut.setOnClickListener(this);

        editsongsbtn = findViewById(R.id.EditSongsbutton);
        editsongsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!musicFiles.isEmpty())
                {
                    for(Button playBut : playButArr)
                        playBut.setWidth(context.getResources().getDisplayMetrics().widthPixels -(int) ((53) * context.getResources().getDisplayMetrics().density + 0.5f));
                }
                //Toast.makeText(context,delButArrNS.get(0).getVisibility()+" gone="+ GONE+" "+VISIBLE,Toast.LENGTH_SHORT).show();
                for(ImageButton delButNS : delButArrNS)
                {
                    delButNS.setVisibility(View.VISIBLE);
                }
//                try
//                {
//                    Thread.sleep(500);
//                }
//                catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
                for(ImageButton btn:delButArrS)
                {
                    btn.setVisibility(View.GONE);
                }
                cancelbtn.setVisibility(VISIBLE);
                delsongsbtn.setVisibility(VISIBLE);
                playSelectedBtn.setVisibility(VISIBLE);
                editsongsbtn.setVisibility(GONE);
                playAllbtn.setVisibility(GONE);
            }
        });

        cancelbtn = findViewById(R.id.cancelbutton);
        cancelbtn.setVisibility(GONE);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                for(Button playBut:playButArr) playBut.setWidth(context.getResources().getDisplayMetrics().widthPixels);
                for(ImageButton btn:delButArrNS) btn.setVisibility(GONE);
                for(ImageButton btn:delButArrS) btn.setVisibility(GONE);

                cancelbtn.setVisibility(GONE);
                delsongsbtn.setVisibility(GONE);
                playSelectedBtn.setVisibility(GONE);
                editsongsbtn.setVisibility(VISIBLE);
                playAllbtn.setVisibility(VISIBLE);

            }
        });

        playAllbtn = findViewById(R.id.playAllButton);
        playAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                playSong(0,true,false);
            }
        });

        delsongsbtn = findViewById(R.id.DeleteSongsbutton);
        delsongsbtn.setVisibility(GONE);
        delsongsbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int x:delSet)
                {
                    deleteSong(x);
                }
                delSet = null;
                for(Button playBut:playButArr) playBut.setWidth(context.getResources().getDisplayMetrics().widthPixels);//(int)((400)* context.getResources().getDisplayMetrics().density + 0.5f)
                for(ImageButton btn:delButArrNS) btn.setVisibility(GONE);
                for(ImageButton btn:delButArrS) btn.setVisibility(GONE);


                delsongsbtn.setVisibility(GONE);
                cancelbtn.setVisibility(GONE);
                playAllbtn.setVisibility(VISIBLE);
                editsongsbtn.setVisibility(VISIBLE);
            }
        });
        playSelectedBtn = findViewById(R.id.playSelectedButton);
        playSelectedBtn.setVisibility(GONE);
        playSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(0,false,true);
            }
        });
    }
    public RelativeLayout getTheLayout(Context context, int i)
    {
        RelativeLayout layout = new RelativeLayout(context);
        int ht = (int) (53 * this.getResources().getDisplayMetrics().density + 0.5f);
        try
        {
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ht));
            layout.setBackgroundColor(Color.BLACK);
            //layout.setId(Integer.MAX_VALUE);

            Button playBut = new Button(this);
            playBut.setId(mod*i+300);

            playBut.setText("             "+musicFiles.get(i).getTitle());
            playBut.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            playBut.setTextColor(Color.BLACK);

            playBut.setBackgroundColor(Color.WHITE);

            playBut.setHeight(ht-1);
            playBut.setWidth(context.getResources().getDisplayMetrics().widthPixels);

            RelativeLayout.LayoutParams ImagebuttonParam = new RelativeLayout.LayoutParams(ht-1, ht-1);
            ImagebuttonParam.addRule(RelativeLayout.RIGHT_OF, 3*i+300);

            ImageButton delButNS = new ImageButton(this);
            delButNS.setId(mod*i+1+300);
            delButNS.setLayoutParams(ImagebuttonParam);
            delButNS.setImageResource(R.drawable.ic_check_not_selected);
            delButNS.setBackgroundColor(Color.WHITE);

            ImageButton delButS = new ImageButton(this);
            delButS.setId(mod*i+2+300);
            delButS.setLayoutParams(ImagebuttonParam);
            delButS.setImageResource(R.drawable.ic_check_selected);
            delButS.setBackgroundColor(Color.WHITE);


            layoutarr.add(layout);
            playButArr.add(playBut);
            delButArrNS.add(delButNS);
            delButArrS.add(delButS);

            layout.addView(delButNS);
            layout.addView(delButS);
            layout.addView(playBut);
            //setContentView(layout);
            delButNS.setVisibility(GONE);
            delButS.setVisibility(GONE);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        return layout;
    }
    public void addLayout(int i)
    {
        LinearLayout parent = findViewById(R.id.layoutOuter);
        RelativeLayout inner = getTheLayout(this,i);
        parent.addView(inner);
    }
    public void setTheFrontend()
    {
        for(int i=0; i<musicFiles.size(); i++) addLayout(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        musicFiles = getAllAudio(this);

        setTheFrontend();
        setListenersToAllButtons(this);
    }

    public ArrayList<MusicFiles> getAllAudio(Context context)
    {
        ArrayList<MusicFiles> songs = new ArrayList<>();
        uri =  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[]  projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,
        };

        Cursor c = context.getContentResolver().query(uri,projection,null,null,null);
        if(c != null)
        {
            while (c.moveToNext())
            {
                String album = c.getString(0);
                String title = c.getString(1);
                String duration = c.getString(2);
                String pathe = c.getString(3);
                String artist = c.getString(4);
                String id = c.getString(5);

                MusicFiles musicFile = new MusicFiles(pathe,title,artist,album,duration,id);
                songs.add(musicFile);
            }
            c.close();
        }
        Collections.sort(songs,Order::ascending);
        return songs;
    }
}
class Order
{
    public static int ascending(MusicFiles a, MusicFiles b)
    {
        return a.getTitle().compareToIgnoreCase(b.getTitle());
    }
}