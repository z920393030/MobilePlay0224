package com.atguigu.app2.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.atguigu.app2.IMusicPlayService;
import com.atguigu.app2.R;
import com.atguigu.app2.activity.AudioPlayerActivity;
import com.atguigu.app2.domain.MediaItem;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayService extends Service {

    private IMusicPlayService.Stub stub = new IMusicPlayService.Stub() {
        MusicPlayService service = MusicPlayService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getPlaymode() throws RemoteException {
            return service.getPlaymode();
        }

        @Override
        public void setPlaymode(int playmode) throws RemoteException {
            service.setPlaymode(playmode);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }
    };


    private ArrayList<MediaItem> mediaItems;
    private MediaPlayer mediaPlayer;
    private int position;
    private MediaItem mediaItem;
    public static final String OPEN_COMPLETE = "com.atguigu.mobileplayer.OPEN_COMPLETE";
    private NotificationManager nm;
    public static final int REPEAT_NORMAL = 1;
    public static final int REPEAT_SINGLE = 2;
    public static final int REPEAT_ALL = 3;

    private int playmode = REPEAT_NORMAL;
    private boolean isCompletion = false;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("atguigu", MODE_PRIVATE);
        playmode = sp.getInt("playmode", getPlaymode());
        getData();
    }


    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频在sdcard上的名称
                        MediaStore.Audio.Media.DURATION,//视频时长
                        MediaStore.Audio.Media.SIZE,//视频文件的大小
                        MediaStore.Audio.Media.DATA,//视频播放地址
                        MediaStore.Audio.Media.ARTIST//艺术家
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                        if (duration > 10 * 1000) {
                            mediaItems.add(new MediaItem(name, duration, size, data, artist));
                        }

                    }

                    cursor.close();
                }

            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private void openAudio(int position) {
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0) {

            if (position < mediaItems.size()) {
                mediaItem = mediaItems.get(position);

                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                }
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(mediaItem.getData());
                    mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                    mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                    mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                    mediaPlayer.prepareAsync();
                    if (playmode == MusicPlayService.REPEAT_SINGLE) {
                        isCompletion = false;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(MusicPlayService.this, "音频还没有加载完成", Toast.LENGTH_SHORT).show();
        }


    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
            EventBus.getDefault().post(mediaItem);

        }
    }

    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            isCompletion = true;
            next();
        }
    }

    private void start() {
        mediaPlayer.start();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("notification", true);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notifation = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321音乐")
                .setContentText("正在播放：" + getAudioName())
                .setContentIntent(pi)
                .build();
        nm.notify(1, notifation);

    }

    private void pause() {
        mediaPlayer.pause();
        nm.cancel(1);
    }

    private String getArtistName() {
        return mediaItem.getArtist();
    }

    private String getAudioName() {
        return mediaItem.getName();
    }


    private String getAudioPath() {
        return mediaItem.getData();
    }

    private int getDuration() {
        return mediaPlayer.getDuration();
    }


    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    private void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    private void next() {
        setNextPosition();
        openNextPosition();
    }

    private void setNextPosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            position++;
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (!isCompletion) {
                position++;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            position++;
            if (position > mediaItems.size() - 1) {
                position = 0;
            }
        }
    }
    public static long startTime = 0;

    private void openNextPosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            if (position < mediaItems.size()) {
                openAudio(position);

            } else {
                position = mediaItems.size() - 1;
            }
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (position < mediaItems.size()) {
                openAudio(position);
            } else {
                position = mediaItems.size() - 1;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            openAudio(position);
        }
        startTime = SystemClock.uptimeMillis();
    }

    private void pre() {
        setPrePosition();
        openPrePosition();
    }

    private void setPrePosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            position--;
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (!isCompletion) {
                position--;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }
        }
    }

    private void openPrePosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            if (position >= 0) {
                openAudio(position);

            } else {
                position = 0;
            }
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (position >= 0) {
                openAudio(position);
            } else {
                position = 0;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            openAudio(position);
        }
    }

    public int getPlaymode() {
        return playmode;
    }


    public void setPlaymode(int playmode) {
        this.playmode = playmode;
        sp.edit().putInt("playmode",playmode).commit();
    }

}
