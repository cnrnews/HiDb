package com.imooc.hidb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.hidb.db.DaoSupportFactory;
import com.imooc.hidb.db.IDaoSupport;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IDaoSupport<Person> daoSupport;
    private boolean isGranted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        daoSupport = DaoSupportFactory.getFactory().getDao(Person.class);
                        isGranted = allGranted;
                    }
                });
        if (!isGranted)
        {
            Toast.makeText(this,"未申请存储权限",Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDbInsert();
            }
        });
        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDbQuery();
            }
        });
    }
    /**
     * 测试数据库插入功能
     */
    private void testDbInsert(){

        // 插入数据对象
        List<Person> datas = new ArrayList<>();
        long start = System.currentTimeMillis();
        // 批量插入数据
        Person person;
        for (int i=0;i<8000;i++){
            person =  new Person("jake"+i,i);
            datas.add(person);
        }
        daoSupport.insert(datas);
        long duration = System.currentTimeMillis() - start;
        Log.e("TAG","resule>>"+duration);
    }
    /**
     * 测试数据库查询
     */
    private void testDbQuery(){
        // 查询数据
        List<Person> query = daoSupport.querySupport()
                .selection("age=?")
                .selectionArgs("9")
                .query();
        StringBuffer sb = new StringBuffer();
        for (Person person : query) {
            sb.append("name:"+person.toString());
            sb.append("\n");
        }
        ((TextView)findViewById(R.id.tv_reqult)).setText(sb.toString());
    }
}