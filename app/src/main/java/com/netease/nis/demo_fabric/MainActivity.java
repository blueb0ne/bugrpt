package com.netease.nis.demo_fabric;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.netease.nis.bugrpt.CrashHandler;

public class MainActivity extends AppCompatActivity {

    private Button java_crash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        java_crash = (Button)findViewById(R.id.button);

        java_crash.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "你点击了按钮,即将崩溃", Toast.LENGTH_LONG).show();
                String a = null;
                a.charAt(0);
            }
        });

        CrashHandler cls = CrashHandler.getInstance();
        cls.init(this.getApplicationContext(),"jcenter demo");
        String App_UID = getAppID(this.getApplicationContext());
        Log.d("bugrpt", "AppID:" + App_UID);
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

    //获取App_UID
    private String getAppID(Context ctx) {
        String	App_UID = null;
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if(appInfo!=null){
                if(appInfo.metaData!=null){
				    /*备注：BUGRPT_APPID的值应当取字符串或者取小于2^32次方（10位）
                     *原因：函数 appInfo.metaData.get获取的值会被系统自动定义类型。
                     *如纯数字，定义为整型，若有一个小数点，定义为浮点型，存在字符或者多个小数点定义为String……
                     *而当定义为整型时，存在整型最大值，即2^32次方，若大于最大值，会自动进行哈希变换
                     *即在Xml中配置value=“6002348169” ，此时大于2^32次方，会进行hash，变为1707380873，不再是原先的数字
                     *如果Xml配置value="21474836423333.33" ，大于浮点型的最大值，会自动变为科学计数“2.1474836E13”
					*/
                    Object bundle = appInfo.metaData.get("BUGRPT_APPID");
                    if (bundle!=null){
                        App_UID = bundle.toString();
                        if (App_UID.length()>100){
                            App_UID = App_UID.substring(0,100);
                            //Log.d(TAG,App_UID);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return App_UID;
        }
        return App_UID;
    }
}
