package com.example.luhongcheng;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.annotation.LongDef;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.luhongcheng.Bmob.Tips;
import com.example.luhongcheng.Bmob.lan;
import com.example.luhongcheng.MBox.MBoxItem;
import com.example.luhongcheng.OAitem.item0;
import com.example.luhongcheng.OAitem.item1;
import com.example.luhongcheng.OAitem.item2;
import com.example.luhongcheng.OAitem.item3;
import com.example.luhongcheng.OAitem.item4;
import com.example.luhongcheng.OAitem.item5;
import com.example.luhongcheng.OAitem.item7;
import com.example.luhongcheng.OAitem.item8;
import com.example.luhongcheng.OAitem.item9;
import com.example.luhongcheng.SQ.ZoomOutPageTransformer;
import com.example.luhongcheng.SWZL.swzlmain;
import com.example.luhongcheng.WeiXin.Weixin_more;
import com.example.luhongcheng.about.about0;
import com.example.luhongcheng.userCard.userCardinfo;
import com.example.luhongcheng.zixun.news;
import com.example.luhongcheng.zixun.zhuyeDisplayActvivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/7.
 */

@SuppressLint("ValidFragment")
public class OneFragment extends Fragment{
    private String context;
    public OneFragment(String context){
        this.context = context;
    }
    /*以下是GridView定义的*/
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    String souhu_url;
    SwipeRefreshLayout refresh;
    //天气
    ImageButton weather_icon;
    TextView weather_t1,weather_t2,weather_t3,weather_t4;

    private List<Box> fruitList = new ArrayList<Box>();
    /*轮换图片定义的*/
    //统计下载了几张图片
    int n=0;
    //统计当前viewpager轮播到第几页
    int p=0;
    private ViewPager vp;
    //准备好三张网络图片的地址
    private String imageUrl[]=new String[]
            {"http://www.sit.edu.cn/_upload/article/images/fd/24/913fa2ae4dc88bf724a71a530894/bb653676-1b6c-4e50-bd30-c0213af59eb9.jpg",
                    "http://www.sit.edu.cn/_upload/article/images/a9/d3/76e681a242adbbebb4535280a80b/c2b923c4-d810-414f-a4be-9488f7cf399e.jpg",
                    "http://www.sit.edu.cn/_upload/article/images/fa/92/ba956995444ab5d9949e0c4a9e9e/32ecaf27-6854-4fa3-9a22-fb57e9b207fc.jpg",
                    "http://www.sit.edu.cn/_upload/article/images/5a/ce/852e2e394bee8d00cca59870d5de/0267dfee-8ff9-4ea5-be0f-d6bbd89d68d4.jpg"};
    //装载下载图片的集合
    private List<ImageView> data;
    //控制图片是否开始轮播的开关,默认关的
    private boolean isStart=false;
    //开始图片轮播的线程
    private MyThread t;
    //存放代表viewpager播到第几张的小圆点
    private LinearLayout ll_tag;
    //存储小圆点的一维数组
    private ImageView tag[];
    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 0:
                    n++;
                    Bitmap bitmap=(Bitmap) msg.obj;
                    ImageView iv=new ImageView(getActivity());
                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    iv.setImageBitmap(bitmap);
                    //把图片添加到集合里
                    data.add(iv);
                    //当接收到第三张图片的时候，设置适配器,
                    if(n==imageUrl.length){
                        vp.setAdapter(new ImageLunhuanAdapter(data,getActivity()));
                        //创建小圆点
                        creatTag();
                        //把开关打开
                        isStart=true;
                        t= new MyThread();
                        //启动轮播图片线程
                        t.start();
                    }
                    break;
                case 1:
                    //接受到的线程发过来的p数字
                    int page=(Integer) msg.obj;
                    vp.setCurrentItem(page);
                    break;
            }
        };
    };

    private Toolbar mToolbar;
    Button more,more2;
    TextView tips,QQ;
    Button box;
    ImageButton one;

    ImageButton souhuiv;
    TextView souhutitle,souhusubtitle;

    ImageView swzl_iv;
    TextView swzl_title,swzl_subtitle,swzl_time;

    private OkHttpClient okHttpClient;
    private OkHttpClient.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment, container, false);
        //((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        return  view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView) getView().findViewById(R.id.gridview);
        initData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.red_300));//设置状态栏背景色
        }

        refresh = (SwipeRefreshLayout)getActivity().findViewById(R.id.refresh_one);
        weather_icon = (ImageButton) getActivity().findViewById(R.id.weather_icon);
        weather_t1 = (TextView)getActivity().findViewById(R.id.weather_t1);
        weather_t2 = (TextView)getActivity().findViewById(R.id.weather_t2);
        weather_t3 = (TextView)getActivity().findViewById(R.id.weather_t3);
        weather_t4 = (TextView)getActivity().findViewById(R.id.weather_t4);
        more = (Button) getActivity().findViewById(R.id.more);
        more2 = (Button) getActivity().findViewById(R.id.more2);

        swzl_iv = (ImageView) getActivity().findViewById(R.id.swzl_iv);
        swzl_title = (TextView)getActivity().findViewById(R.id.swzl_title);
        swzl_subtitle = (TextView)getActivity().findViewById(R.id.swzl_subtitle);
        swzl_time = (TextView)getActivity().findViewById(R.id.swzl_time);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setTitle("SITschool");
        mToolbar.setSubtitle("明德、明学、明事");
        box =(Button) getActivity().findViewById(R.id.moreBox);

        Bmob.initialize(getActivity(), "69d2a14bfc1139c1e9af3a9678b0f1ed");
        tips = (TextView) getActivity().findViewById(R.id.tips);
        one = (ImageButton) getActivity().findViewById(R.id.OneSelf);

        String[] from={"ItemImage","ItemText"};
        int[] to={R.id.ItemImage,R.id.ItemText};
        adapter=new SimpleAdapter(getActivity(), dataList, R.layout.gridview_item, from, to);
        gridView.setAdapter(adapter);

        souhuiv = (ImageButton) getActivity().findViewById(R.id.souhu_iv);
        souhutitle = (TextView)getActivity().findViewById(R.id.souhu_title);
        souhusubtitle =(TextView)getActivity().findViewById(R.id.souhu_subtitle);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        BoxAdapter adapter = new BoxAdapter(fruitList);
        recyclerView.setAdapter(adapter);


        initOnClick();
        initSet();
    }

    @SuppressLint("ResourceAsColor")
    private void initOnClick() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //加载数据
                        initSet();
                        //关闭刷新
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();

            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(),OneSelf.class);
                startActivity(intent);
            }
        });
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MoreTips.class);
                startActivity(intent);
            }
        });
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MoreBox.class);
                startActivity(intent);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),zhuyeDisplayActvivity.class);
                intent.putExtra("news_url","https://m.sm.cn/s?q=%E4%B8%8A%E6%B5%B7%E5%A5%89%E8%B4%A4%E5%A4%A9%E6%B0%94&by=submit&snum=6");
                startActivity(intent);
            }
        });
        more2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Weixin_more.class);
                startActivity(intent);
            }
        });

        souhuiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SouHuNews.class);
                startActivity(intent);
            }
        });

        souhutitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SouHuNews.class);
                intent.putExtra("url",souhu_url);
                startActivity(intent);
            }
        });

        souhusubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SouHuNews.class);
                intent.putExtra("url",souhu_url);
                startActivity(intent);
            }
        });

        swzl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),swzlmain.class);
                startActivity(intent);
            }
        });
        swzl_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),swzlmain.class);
                startActivity(intent);
            }
        });
        swzl_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),swzlmain.class);
                startActivity(intent);
            }
        });

        /* 给item设置点击事件*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent=new Intent(getActivity(),item2.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(getActivity(),item1.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(getActivity(),item3.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3=new Intent(getActivity(),item4.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4=new Intent(getActivity(),item5.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent6=new Intent(getActivity(),item7.class);
                        startActivity(intent6);
                        break;
                    case 6:
                        Intent intent7 = new Intent(getActivity(),userCardinfo.class);
                        startActivity(intent7);
                        break;
                    case 7:
                        Intent intent5 = new Intent(getActivity(),swzlmain.class);
                        startActivity(intent5);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getActivity(),item8.class);
                        startActivity(intent8);
                        break;
                    case 9:
                        Intent intent10 = new Intent(getActivity(),news.class);
                        startActivity(intent10);
                        break;
                    case 10:
                        Intent intent11 = new Intent(getActivity(),item9.class);
                        startActivity(intent11);
                        break;
                    case 11:
                        startActivity(new Intent(getActivity(),item0.class));
                        getActivity().overridePendingTransition(R.anim.bottom_in,R.anim.bottom_silent);
                        break;
                    default:
                        break;
                }
            }
        });
        /*点击事件设置完毕*/

    }

    private void initSet() {
        getsouhu();
        init();
        getlan();
        gettip();
        initFruits();
        getWeather();
        getSwzl();
    }


    void initData() {
        //图标
        int icno[] = { R.mipmap.g16,R.mipmap.g1,R.mipmap.g7,R.mipmap.g4,
                R.mipmap.g5,R.mipmap.g3,R.drawable.card,R.mipmap.swzl,
                R.drawable.library,R.drawable.zixun,R.drawable.weixin,
                R.mipmap.g15};
        //图标下的文字
        String name[]={"学院","第二课堂","OA主页","成绩",
                "电费","考试","学生卡","失物招领","读书馆","公告","微信","更多"};

        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("ItemImage", icno[i]);
            map.put("ItemText",name[i]);
            dataList.add(map);
        }
    }

    Bitmap bitmap;
    int num =0;
    private void getsouhu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final OkHttpClient client = new OkHttpClient().newBuilder()
                            .followRedirects(false)//禁止重定向
                            .followSslRedirects(false)//哈哈哈哈哈哈哈好开心啊
                            .build();

                    Request request = new Request.Builder()
                            .url("http://m.sohu.com/media/694346?spm=smwp.content.author-info.1.1537437344995hk1YAuY")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Document doc = Jsoup.parse(responseData);
                    Elements url = doc.select("ul.feed-list-area");
                    Element link =  url.select("li").get(num);


                    souhu_url = link.select("a.onePic").attr("href");
                    souhu_url = " http://m.sohu.com"+souhu_url+"&spm=smwp.media.fd-s.1.1537437360311dAYraYh";
                    //System.out.println("文章链接:"+souhu_url.toString());

                    String A2 = link.select("section.onePic__img-area").select("img").attr("original");
                   // System.out.println("图片链接:"+A2.toString());

                    /*
                    if (A2.length() != 0){
                        Glide.with(getContext())
                                .load(A2)
                                .placeholder(R.drawable.loading)
                                .error(R.drawable.error)
                                .fitCenter()
                                .into(souhuiv);
                    }*/

                    URL myFileURL;
                    if (A2.length() !=0){
                        try {
                            myFileURL = new URL(A2);
                            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
                            conn.setConnectTimeout(3000);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = handler.obtainMessage();
                        msg.obj = bitmap;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }else {
                        num = num +1;
                        getsouhu();
                    }

                    final String A3 = link.select("article.onePic__content").select("h4.feed__title").text();
                   // System.out.println("标题:"+A3.toString());

                    final String A4 = link.select("article.onePic__content").select("footer.feed__detail").select("span.time").text();
                    //System.out.println("时间:"+A4.toString());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            souhutitle.setText(A3);
                            souhusubtitle.setText(A4);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    souhuiv.setBackgroundResource(0);
                    souhuiv.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    public void gettip(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<Tips> query = new BmobQuery<Tips>();
                query.findObjects(new FindListener<Tips>(){
                    @Override
                    public void done(List<Tips> list, BmobException e) {
                        List<Tips> lists = new ArrayList<>();
                        if (list != null) {
                            final String[] tip  =  new String[list.size()];
                            for(int i = 0;i<list.size();i++){
                                tip[i] = list.get(i).getTips();
                            }
                            tips.setText(tip[list.size() - 1]);
                        }
                    }
                });
            }
        }); //子线程
        thread.start();
    }

    public void getlan(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<lan> query = new BmobQuery<lan>();
                query.findObjects(new FindListener<lan>(){
                    @Override
                    public void done(List<lan> list, BmobException e) {
                        List<lan> lists = new ArrayList<>();
                        if (list != null) {
                            String[] sub = new  String[list.size()];
                            int i;
                            for(i = 0;i<list.size();i++){
                                sub[i] = list.get(i).getSubtitle();
                            }
                            mToolbar.setSubtitle(sub[0]);
                        }
                    }
                });
            }
        }); //子线程
        thread.start();
    }

    //toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_action1:
                Intent intent = new Intent(getActivity(),about0.class);
                startActivity(intent);
                return true;
            case R.id.connect_vpn:
                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent2=new Intent();
                intent2 = packageManager.getLaunchIntentForPackage("com.topsec.topsap");
                if(intent2==null){
                    Toast.makeText(getActivity(), "未安装！！", Toast.LENGTH_LONG).show();
                    Intent intent3= new Intent(getActivity(),connect_vpn.class);
                    startActivity(intent3);
                }else{
                    startActivity(intent2);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*轮换图片*/
    private void init() {
        // TODO Auto-generated method stub
        vp=(ViewPager) getView().findViewById(R.id.vp);
        vp.setPageTransformer(true, new ZoomOutPageTransformer());
        ll_tag=(LinearLayout) getView().findViewById(R.id.ll_tag);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //把当前的页数赋值给P
                p=position;
                //得到当前图片的索引,如果图片只有三张，那么只有0，1，2这三种情况
                int currentIndex=(position%imageUrl.length);
                for(int i=0;i<tag.length;i++){
                    if(i==currentIndex){
                        tag[i].setBackgroundResource(R.drawable.dot_focused);
                    }else{
                        tag[i].setBackgroundResource(R.drawable.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        //构造一个存储照片的集合
        data=new ArrayList<ImageView>();
        //从网络上把图片下载下来
        for(int i=0;i<imageUrl.length;i++){
            getImageFromNet(imageUrl[i]);
        }
    }

    private void getImageFromNet(final String imagePath) {
        // TODO Auto-generated method stub
        new Thread(){
            public void run() {
                try {
                    URL url=new URL(imagePath);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(10*1000);
                    InputStream is=con.getInputStream();
                    //把流转换为bitmap
                    Bitmap bitmap= BitmapFactory.decodeStream(is);
                    Message message=new Message();
                    message.what=0;
                    message.obj=bitmap;
                    //把这个bitmap发送到hanlder那里去处理
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            };
        }.start();

    }


    //控制图片轮播
    class MyThread extends Thread{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            while(isStart){
                Message message=new Message();
                message.what=1;
                message.obj=p;
                mHandler.sendMessage(message);
                try {
                    //睡眠5秒,在isStart为真的情况下，一直每隔三秒循环
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                p++;
            }
        }
    }

    protected void creatTag() {
        tag=new ImageView[imageUrl.length];
        for(int i=0;i<imageUrl.length;i++){

            tag[i]=new ImageView(getActivity());
            //第一张图片画的小圆点是白点
            if(i==0){
                tag[i].setBackgroundResource(R.drawable.dot_focused);
            }else{
                //其它的画灰点
                tag[i].setBackgroundResource(R.drawable.dot_normal);
            }
            //设置上下左右的间隔
            tag[i].setPadding(0,0,40,40);
            tag[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //添加到viewpager底部的线性布局里面
            ll_tag.addView(tag[i]);
        }
    }


    private void initFruits() {
        Box orange = new Box(R.string.A1, R.drawable.best_min);
        fruitList.add(orange);
        Box apple = new Box(R.string.A2, R.drawable.talk_min);
        fruitList.add(apple);
        Box banana = new Box(R.string.A3, R.drawable.love_min);
        fruitList.add(banana);
        Box watermelon = new Box(R.string.A4, R.drawable.learn_min);
        fruitList.add(watermelon);
        Box pear = new Box(R.string.A5, R.drawable.anli_min);
        fruitList.add(pear);
        Box grape = new Box(R.string.A6, R.drawable.food_min);
        fruitList.add(grape);
        Box pineapple = new Box(R.string.A7, R.drawable.xuqiu_min);
        fruitList.add(pineapple);
    }

    class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder>{
        private List<Box> mFruitList;
        class ViewHolder extends RecyclerView.ViewHolder {
            View fruitView;
            ImageView fruitImage;
            TextView fruitName;
            public ViewHolder(View view) {
                super(view);
                fruitView = view;
                fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
                fruitName = (TextView) view.findViewById(R.id.fruit_name);
            }
        }

        public BoxAdapter(List<Box> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.fruitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Box fruit = mFruitList.get(position);
                    switch(position){
                        case 0:
                            //今日最佳
                            Intent intent = new Intent(getActivity(),MBoxItem.class);
                            intent.putExtra("flag","A1");
                            startActivity(intent);
                            break;
                        case 1:
                            //众话说
                            Intent intent1 = new Intent(getActivity(), MBoxItem.class);
                            intent1.putExtra("flag","A4");
                            startActivity(intent1);
                            break;
                        case 2:
                            //表白墙
                            Intent intent2 = new Intent(getActivity(),MBoxItem.class);
                            intent2.putExtra("flag","A3");
                            startActivity(intent2);
                            break;
                        case 3:
                            //学习交流
                            Intent intent3 = new Intent(getActivity(), MBoxItem.class);
                            intent3.putExtra("flag","A6");
                            startActivity(intent3);
                            break;
                        case 4:
                            //安利
                            Intent intent4 = new Intent(getActivity(),MBoxItem.class);
                            intent4.putExtra("flag","A7");
                            startActivity(intent4);
                            break;
                        case 5:
                            //一日三餐
                            Intent intent5 = new Intent(getActivity(), MBoxItem.class);
                            intent5.putExtra("flag","A2");
                            startActivity(intent5);
                            break;
                        case 6:
                            //需求池
                            Intent intent6 = new Intent(getActivity(), MBoxItem.class);
                            intent6.putExtra("flag","A8");
                            startActivity(intent6);
                            break;
                        default:
                            break;
                    }

                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Box fruit = mFruitList.get(position);
            holder.fruitImage.setImageResource(fruit.getImageId());
            holder.fruitName.setText(fruit.getName());
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }

    private void getWeather() {
        Thread threadx = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();

                    Request request = new Request.Builder()
                            .url("https://www.tianqi.com/fengxian/")
                            .build();
                    Response response = client.newCall(request).execute();
                    Document doc = Jsoup.parse(response.body().string());

                    Elements url2 = doc.getElementsByClass("weather");
                    final String t1= url2.select("p").text();
                    final String t2 = url2.select("span").text();

                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            weather_t1.setText(t1);
                            weather_t2.setText(t2);
                            weather_icon.setBackground(getResources().getDrawable(R.drawable.b1));
                        }
                    });


                    Message message=new Message();
                    String tt = url2.select("span").select("b").text();
                    if (tt.contains("晴")){
                        message.what=0;
                        handler_weather.sendMessage(message);
                    }
                    if (tt.contains("多云")){
                        message.what=1;
                        handler_weather.sendMessage(message);
                    }
                    if (tt.contains("阴")){
                        message.what=2;
                        handler_weather.sendMessage(message);
                    }
                    if (tt.contains("小雨")){
                        message.what=7;
                        handler_weather.sendMessage(message);
                    }
                    if (tt.contains("中雨") || tt.contains("暴雨")){
                        message.what=8;
                        handler_weather.sendMessage(message);
                    }

                    Elements url3 = doc.getElementsByClass("shidu");
                    String t3 = url3.get(0).text();
                    weather_t3.setText(t3);

                    Elements url4 = doc.getElementsByClass("kongqi").select("h5");
                    String t4= url4.text();
                    weather_t4.setText(t4);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        threadx.start();
    }


    @SuppressLint("HandlerLeak")
    Handler handler_weather = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    weather_icon.setBackgroundResource(0);
                    weather_icon.setBackground(getResources().getDrawable(R.drawable.b0));
                    break;
                case 1:
                    weather_icon.setBackgroundResource(0);
                    weather_icon.setBackground(getResources().getDrawable(R.drawable.b1));
                    break;
                case 2:
                    weather_icon.setBackgroundResource(0);
                    weather_icon.setBackground(getResources().getDrawable(R.drawable.b2));
                    break;
                case 7:
                    weather_icon.setBackgroundResource(0);
                    weather_icon.setBackground(getResources().getDrawable(R.drawable.b7));
                    break;
                case 8:
                    weather_icon.setBackgroundResource(0);
                    weather_icon.setBackground(getResources().getDrawable(R.drawable.b8));
                    break;
            }
        }
    };


    private void getSwzl() {
        Thread swzl = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<com.example.luhongcheng.Bmob.SWZL> query = new BmobQuery<com.example.luhongcheng.Bmob.SWZL>();
                query.setLimit(1);
                query.order("-createdAt");//时间降序查询
                query.findObjects(new FindListener<com.example.luhongcheng.Bmob.SWZL>(){
                    @Override
                    public void done(List<com.example.luhongcheng.Bmob.SWZL> list, BmobException e) {
                        if(e==null){
                            String title = list.get(0).getTitle();
                            String subtitle = list.get(0).getContent();
                            String time = list.get(0).getCreatedAt();
                            String image = list.get(0).getimageUrl();

                            if (title.length() != 0){
                                swzl_title.setText(title);
                                swzl_subtitle.setText(subtitle);
                                swzl_time.setText(time);
                                Glide.with(getContext())
                                        .load(image)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.error)
                                        .fitCenter()
                                        .into(swzl_iv);
                            }


                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }

                    }
                });


            }
        });
        swzl.start();

    }


}

