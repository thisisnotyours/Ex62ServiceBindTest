package com.suek.ex62servicebindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //액티비티가 화면에 보일대
    @Override
    protected void onResume() {
        super.onResume();

        //서비스객체 실행 및 연결(bind)
        if( musicService == null ){    //연경되어있는 뮤직서비스가 없다면
            Intent intent= new Intent(this, MusicService.class);
            startService(intent);      //일단 Service 객체 생성!!  //start 목적의 intent1 보냄
                                       // [만약 서비스객체가 없다면 만들고 onStartCommand()가 발동, 있다면 생성은 안하고 onStartCommand() 만 발동]

            //만들어진 Service 객체와 연결
            //bindService() 호출하면 Service 클래스안의 onBind()메소드가 발동하고
            //이 onBind()가 서비스객체의 참조값을 가진 객체를 리턴해줌.
            bindService(intent, conn, 0);       //bind 목적으로 intent2 를 보냄   //flags 값이 0이면 bind_auto_create 를 안하겠다는 의미
        }
    }




    //binderService()를 실행했을때 Service 객체와 연결된 통로 객체
    ServiceConnection conn= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {  //IBinder 부모
            //두번째 파라미터 IBinder service(직원 binder): 서비스객체의 참조주소를 주는 기능메소드가 있는 객체
            MusicService.MyBinder binder= (MusicService.MyBinder)service;
            musicService= binder.getService();  //리턴해준 '서비스객체 주소참조'

            Toast.makeText(musicService, "서비스와 연결이 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };




    public void clickPlay(View view) {
        if(musicService!=null) musicService.playMusic();  //start 와 resume 이 같이됨
    }

    public void clickPause(View view) {
        if(musicService!=null) musicService.pauseMusic();
    }

    public void clickStop(View view) {      //뒤로가기가 아니라 강제로 종료했을대
        if(musicService!=null){
            musicService.stopMusic();
            unbindService(conn);
            musicService=null;
        }


        //서비스객체도 아예 종료
        Intent intent= new Intent(this, MainActivity.class);
        stopService(intent);

        finish();  //이 앱의 모든 기능을 꺼버림 //액티비티 종료
    }
}
