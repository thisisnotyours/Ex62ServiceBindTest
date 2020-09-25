package com.suek.ex62servicebindtest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;





public class MusicService extends Service {

    MediaPlayer mp;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        MyBinder binder= new MyBinder();
        return binder;   //MainActivity 로 파견나갈 객체(Binder)를 리턴
    }


    //이 MusicService 객체의 메모리주소(객체의 참조값)을 리턴해주는
    //기능을 가진 Binder 클래스 설계         //직원 Binder
    class MyBinder extends Binder{
        //이 서비스객체의 주소를 리턴해주는 메소드
        public MusicService getService(){   //public 리턴타입(본인) getService(메소드명)
            return MusicService.this;   //본인의 주소를 리턴
        }
    }





    //음악 재생기능
     void playMusic(){
        if(mp == null){
            mp= MediaPlayer.create(this, R.raw.fireball);
            mp.setLooping(true);
            mp.setVolume(1.0f, 1.0f);
        }
        mp.start();     //처음실행 or 이어하기(resume)
    }

    //음악 일시정지기능
    void pauseMusic(){
        if(mp!=null && mp.isPlaying()) mp.pause();
    }

    //음악 정지기능
    void stopMusic(){
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;    //null 로 넣고 garbage collector 가 수거
        }
    }


}
