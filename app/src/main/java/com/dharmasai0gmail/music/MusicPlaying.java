package com.dharmasai0gmail.music;

import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import android.support.annotation.RequiresApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dharmasai0gmail.music.Main2Activity.delSet;
import static com.dharmasai0gmail.music.Main2Activity.musicFiles;
import static java.lang.Thread.sleep;

//class Miscellaneous
//{
//    public MediaPlayer getMusicPlayerObject(Context context, int songid, ArrayList<MusicFiles> mFiles)
//    {
//        MediaPlayer mp=new MediaPlayer();
//        try
//        {
//            Uri uri = Uri.parse(mFiles.get(songid).getPath());
//            mp = MediaPlayer.create(context, uri);//
//        }
//        catch(Exception e)
//        {
//            Toast.makeText(context,"no such path",Toast.LENGTH_SHORT).show();
//        }
//        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        return mp;
//    }
//
//}

public class MusicPlaying extends AppCompatActivity
{
    //Miscellaneous obj = new Miscellaneous();
    ImageButton playBut,pauseBut,ffBut,rwBut,nextBut,prevBut;
    MediaPlayer mp;
    SeekBar sb;
    TextView currTime,totalTime,Title;

    int songid;
    boolean isPlaySelected = false;
    boolean isPlayAll=false;
    boolean isRepeat = false;
    static ArrayList<MusicFiles> listFiles;
    static TreeSet<Integer> selected;

    int currSong=0;

    private void updatePositionTo(int i)
    {
        mp.seekTo(i);
        currTime.setText(mp.getCurrentPosition()/60000+":"+(mp.getCurrentPosition()/1000)%60);
    }
    private void playSong(int pos)
    {
        Title.setText(listFiles.get(currSong).getTitle());
        playBut.setVisibility(GONE);
        pauseBut.setVisibility(VISIBLE);
        //mp = obj.getMusicPlayerObject(this,songid,listFiles);
       //MediaPlayer mp;//=new MediaPlayer();
        try
        {
            Uri uri = Uri.parse(listFiles.get(currSong).getPath());
            mp = MediaPlayer.create(this, uri);//
        }
        catch(Exception e)
        {
            Toast.makeText(this,"no such path",Toast.LENGTH_SHORT).show();
        }
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mediaplayer)
            {
                sb.setMax(mp.getDuration());
                mp.start();
            }
        });
        totalTime.setText(mp.getDuration()/1000/60+":"+(mp.getDuration()/1000)%60);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(mp!=null && fromUser)
                {
                    mp.seekTo(progress);
                    currTime.setText(mp.getCurrentPosition()/60000+":"+(mp.getCurrentPosition()/1000)%60);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        //boolean flag = true;
        class NewThread implements java.lang.Runnable {
            Thread t;

            public NewThread() {
                t = new Thread(this, "seekBarThread");
            }

            public void run()
            {
                try
                {
                    while(true) {
                        if (mp.getCurrentPosition() < mp.getDuration()) {
                            currTime.setText(mp.getCurrentPosition() / 60000 + ":" + (mp.getCurrentPosition() / 1000) % 60);
                            sb.setProgress(mp.getCurrentPosition());
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                Toast.makeText(MusicPlaying.this, e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        //else if (mp.getCurrentPosition() >= mp.getDuration()-1000) {
                        //    mp.seekTo(0);
                        //    sb.setProgress(0);
                        //    //pauseBut.callOnClick();
                        //}
                    }
                }
                catch(Exception ignored){}
            }
        }

        NewThread nt= new NewThread();
        try {
            nt.t.start();
        }
        catch(Exception e) {
            Toast.makeText(MusicPlaying.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        pauseBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pauseBut.setVisibility(GONE);
                playBut.setVisibility(VISIBLE);
                mp.pause();
            }
        });

        playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                playBut.setVisibility(GONE);
                pauseBut.setVisibility(VISIBLE);
                mp.start();
            }
        });

        ffBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int x = mp.getCurrentPosition();
                if(x+5000 < mp.getDuration()) {
                    updatePositionTo(mp.getCurrentPosition() + 5000);

                }
                else
                    return;
            }
        });
        rwBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int x = mp.getCurrentPosition();
                updatePositionTo(Math.max(x - 5000, 0));
            }
        });
        nextBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(currSong < listFiles.size()-1)
                {
                    currSong++;
                    mp.release();
                    mp = null;
                    playSong(currSong);
                }
                else return;
            }
        });
        prevBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(currSong > 0)
                {
                    currSong--;
                    mp.release();
                    mp = null;
                    playSong(currSong);
                }

            }
        });
//        try
//        {
//            nt.t.join();
//            Toast.makeText(MusicPlaying.this,"applying finish",Toast.LENGTH_SHORT).show();
//            //finish();
//        }
//        catch(Exception e)
//        {
//            Toast.makeText(MusicPlaying.this,"joining exception",Toast.LENGTH_SHORT).show();
//        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isPlayAll)
                {
                    if(currSong < listFiles.size()-1)
                    {
                        currSong++;
                        sb.setProgress(0);

                        assert mp != null;
                        mp.release();
                        mp = null;
                        playSong(currSong);
                    }
                    else
                    {
                        assert mp != null;
                        mp.seekTo(0);
                        sb.setProgress(0);
                        pauseBut.callOnClick();
                    }

                }
                else if(isRepeat)
                {
                    mp.seekTo(0);
                    sb.setProgress(0);
                }
                else if(isPlaySelected)
                {
                    try
                    {
                        currSong = selected.pollLast();
                        sb.setProgress(0);
                        assert mp != null;
                        mp.release();
                        mp = null;
                        playSong(currSong);
                    }
                    catch(NullPointerException e)
                    {
                        assert mp != null;
                        mp.seekTo(0);
                        sb.setProgress(0);
                        pauseBut.callOnClick();
                    }

                }
                else
                {
                    mp.seekTo(0);
                    sb.setProgress(0);
                    pauseBut.callOnClick();
                }
            }
        });

    }
    private void initialiseSeekbarAndAllButtons()
    {
        sb = findViewById(R.id.seekBar);

        Title = findViewById(R.id.texttitle);
        currTime = findViewById(R.id.currtime);
        totalTime = findViewById(R.id.totaltime);

        playBut = findViewById(R.id.play);
        pauseBut = findViewById(R.id.pause);

        ffBut = findViewById(R.id.forward5secs);
        rwBut = findViewById(R.id.rewind5secs);

        prevBut = findViewById(R.id.prevsong);
        nextBut = findViewById(R.id.nextsong);

        playBut.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplaying);

        Bundle data=getIntent().getExtras();
        if(data == null) return;
        songid = data.getInt("songName");
        isPlayAll=data.getBoolean("isPlayAll");
        isPlaySelected = data.getBoolean("isPlaySelected");
        listFiles = musicFiles;

        initialiseSeekbarAndAllButtons();

        if(isPlaySelected)
        {
            selected = delSet;
            currSong = selected.pollLast();
            playSong(currSong);
        }
        else
        {
            currSong = songid;
            playSong(currSong);
        }

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        selected = null;
        mp.release();
        mp = null;
        finish();
    }
}
