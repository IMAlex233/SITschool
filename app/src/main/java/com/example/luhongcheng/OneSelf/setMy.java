package com.example.luhongcheng.OneSelf;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.luhongcheng.Bmob_bean.UserInfo;
import com.example.luhongcheng.R;


import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class setMy extends AppCompatActivity {
    ImageButton setIcon;
    Button send1,send2,send3 ,send4;
    public static final int CHOOSE_PHOTO = 1;
    EditText qianming,nickname,QQ;
    String ID;
    String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmy);
        setIcon = (ImageButton) findViewById(R.id.setIcon);
        setIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
        qianming =(EditText)findViewById(R.id.qm);
        nickname =(EditText)findViewById(R.id.nk);
        QQ = (EditText)findViewById(R.id.QQ);

        SharedPreferences sp=getSharedPreferences("userid",0);
        username = sp.getString("username","");

        SharedPreferences sp1=getSharedPreferences("personID",0);
        ID = sp1.getString("ID","");

        //System.out.println("ID:"+ID);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.teal_300));//设置状态栏背景色
        }


        send1 =(Button)findViewById(R.id.send1);
        send2 =(Button)findViewById(R.id.send2);
        send3 =(Button)findViewById(R.id.send3);
        send4 =(Button)findViewById(R.id.send4);

        send1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  icon_path;
                icon_path = imagePath;
                if (icon_path == null){
                    Toast.makeText(setMy.this,"请选择图片",Toast.LENGTH_SHORT).show();
                }else {
                    final BmobFile bmobfile = new BmobFile(new File(icon_path));
                    bmobfile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                UserInfo object = new  UserInfo();
                                object.setIcon(bmobfile);
                                object.update(ID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e1) {
                                        if(e1==null){
                                            // Log.i("bmob","更新成功");
                                            Toast.makeText(setMy.this, "更新成功", Toast.LENGTH_SHORT).show();
                                        }else{
                                            //Toast.makeText(setMy.this, "你已经设置过了", Toast.LENGTH_SHORT).show();
                                            Log.i("bmob","更新失败："+e1.getMessage()+","+e1.getErrorCode());
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(setMy.this, "文件上传失败", Toast.LENGTH_SHORT).show();
                                System.out.println("文件上传失败");
                            }
                        }
                    });

                }
            }
        });


        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nk;
                nk = nickname.getText().toString();

                if (nk.length() ==0){
                    Toast.makeText(setMy.this,"请输入",Toast.LENGTH_SHORT).show();
                }else {
                    UserInfo object = new  UserInfo();
                    object.setNickname(nk);
                    object.update(ID, new UpdateListener() {
                        @Override
                        public void done(BmobException e1) {
                            if(e1==null){
                                // Log.i("bmob","更新成功");
                                Toast.makeText(setMy.this, "更新成功", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(setMy.this, "你已经设置过了", Toast.LENGTH_SHORT).show();
                                Toast.makeText(setMy.this, "更新失败", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败："+e1.getMessage()+","+e1.getErrorCode());
                            }
                        }
                    });

                }

            }
        });


        send3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String qm;
                qm = qianming.getText().toString();
                if (qm.length() ==0){
                    Toast.makeText(setMy.this,"请输入",Toast.LENGTH_SHORT).show();
                }else {
                    UserInfo object = new  UserInfo();
                    object.setQM(qm);
                    object.update(ID, new UpdateListener() {
                        @Override
                        public void done(BmobException e1) {
                            if(e1==null){
                                // Log.i("bmob","更新成功");
                                Toast.makeText(setMy.this, "更新成功", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(setMy.this, "你已经设置过了", Toast.LENGTH_SHORT).show();
                                Toast.makeText(setMy.this, "更新失败", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败："+e1.getMessage()+","+e1.getErrorCode());
                            }
                        }
                    });
                }

            }
        });

        send4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String qq;
                qq = QQ.getText().toString();
                if (qq.length() ==0){
                    Toast.makeText(setMy.this,"请输入",Toast.LENGTH_SHORT).show();
                }else {
                    UserInfo object = new  UserInfo();
                    object.setQQ(qq);
                    object.update(ID, new UpdateListener() {
                        @Override
                        public void done(BmobException e1) {
                            if(e1==null){
                                // Log.i("bmob","更新成功");
                                Toast.makeText(setMy.this, "更新成功", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(setMy.this, "你已经设置过了", Toast.LENGTH_SHORT).show();
                                Toast.makeText(setMy.this, "更新失败", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败："+e1.getMessage()+","+e1.getErrorCode());
                            }
                        }
                    });
                }

            }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT>=19){
                        //4.4以上使用这个方法处理图片
                        handleIMageOnKitKat(data);
                    }else{
                        handleIMageBeforKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    String imagePath = null;
    @SuppressLint("NewApi")
    private void handleIMageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this , uri)){
            //如果是document类型的URI，则使用document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID +"=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri ,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果不是document类型的URI，则使用普通方式处理
            imagePath = getImagePath(uri , null);
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            setIcon.setImageBitmap(bitmap);
        }else {
            Toast.makeText(setMy.this, "未得到图片", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri , null , selection , null , null);
        if (cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleIMageBeforKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri , null);
        displayImage(imagePath);
    }




}
