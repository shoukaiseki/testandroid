package com.example.shoukaiseki.ttttt;

import android.app.Activity;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shoukaiseki.ttttt.databinding.ActivityMainBinding;
import com.example.shoukaiseki.ttttt.model.Person;
import com.example.shoukaiseki.ttttt.model.User;

public class MainActivity extends AppCompatActivity {
    ViewDataBinding binding;
    //名称为 xml 名称
//    ActivityMainBinding binding;
    User user ;
    int cishu=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_main);
        Person person=new Person();
        person.setName("name");
        person.setNickName("nickname");
        person.setEmail("linux@asus.com");

        user = new User();
        user.setName("用户名");
        user.setNickName("昵称");
        user.setEmail("example@qq.com");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main );
        binding.setVariable(BR.user,user);
        binding.setVariable(BR.person,person);
//        binding.setUser(user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //爱上的说法
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cishu++;
                                user.setName("username"+cishu);
                                //刷新显示的数据
                                binding.invalidateAll();
                                Log.d("test","点击");
                            }
                        }).show();
            }
        });
        /**
        **/


//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
