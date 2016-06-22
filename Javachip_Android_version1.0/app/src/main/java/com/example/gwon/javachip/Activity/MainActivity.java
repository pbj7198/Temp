package com.example.gwon.javachip.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gwon.javachip.Fragment.Fragment_compile;
import com.example.gwon.javachip.Fragment.Fragment_memo;
import com.example.gwon.javachip.Fragment.Fragment_question;
import com.example.gwon.javachip.Fragment.Fragment_study;
import com.example.gwon.javachip.R;

/**
 * 로그인한뒤 메인메뉴를 띄워즈는 엑티비티클래스
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager manager;
    Button bt_study, bt_memo, bt_compile, bt_question;
    Fragment_study fragment_study;
    Fragment_question fragment_question;
    Fragment_compile fragment_compile;
    Fragment_memo fragment_memo;
    SharedPreferences.Editor edit_security;
    SharedPreferences spf_security;

    /**
     * fragememt manager 이용하여 해당 fragement 교환이 이루어지는 메소드
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spf_security = getSharedPreferences("login", MODE_PRIVATE);
        edit_security = spf_security.edit();
        setResult(RESULT_OK);

        fragment_study = new Fragment_study();
        fragment_question = new Fragment_question();
        fragment_compile = new Fragment_compile();
        fragment_memo = new Fragment_memo();
        bt_study = (Button) findViewById(R.id.bt_study);
        bt_memo = (Button) findViewById(R.id.bt_memo);
        bt_compile = (Button) findViewById(R.id.bt_compile_main);
        bt_question = (Button) findViewById(R.id.bt_question);

        /**
         * 학습하기 화면으로 fragment를 교환하는 메소드
         */
        bt_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.fragment_main, fragment_study, fragment_study.getTag()).commit();
            }
        });

        /**
         * 메모하기 화면으로 fragment를 교환하는 메소드
         */
        bt_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.fragment_main, fragment_memo, fragment_memo.getTag()).commit();
            }
        });

        /**
         * 컴파일 화면으로 fragment를 교환하는 메소드
         */
        bt_compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.fragment_main, fragment_compile, fragment_compile.getTag()).commit();
            }
        });

        /**
         * 질문하기 화면으로 fragment를 교환하는 메소드
         */
        bt_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.fragment_main, fragment_question, fragment_question.getTag()).commit();
            }
        });


        manager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 뒤로가기 버튼으로 사이드 바가 사라지는는 메소드
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 사이드 바에 대한 액션 효과를 주는 메소드
     *
     * @param item 선택한 메뉴의 아이템정보
     * @return 선택한 메뉴의 화면 뷰를 리턴
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_study) {
            manager.beginTransaction().replace(R.id.fragment_main, fragment_study, fragment_study.getTag()).commit();
        } else if (id == R.id.nav_question) {
            manager.beginTransaction().replace(R.id.fragment_main, fragment_question, fragment_question.getTag()).commit();
        } else if (id == R.id.nav_memo) {
            manager.beginTransaction().replace(R.id.fragment_main, fragment_memo, fragment_memo.getTag()).commit();
        } else if (id == R.id.nav_compile) {
            manager.beginTransaction().replace(R.id.fragment_main, fragment_compile, fragment_compile.getTag()).commit();
        } else if (id == R.id.nav_logout) {
            edit_security.putString("loginId", "");
            edit_security.putBoolean("check", false);
            edit_security.commit();
            Intent it_login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it_login);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 뒤로 가기 버튼으로 어플이 종료되는 것을 방지
     * @param keyCode 일력된 키 값
     * @param event 키값의 종류를 확인
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();

                return false;
            default:
                return false;
        }
    }

}
