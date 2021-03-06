package com.example.luhongcheng.SWZL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.luhongcheng.ImageFullDisplay;
import com.example.luhongcheng.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class FirstFragment extends Fragment {


    ListView listView;
    private static Bitmap bitmap;
    List<String> url = new ArrayList<>();
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton add,add2;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.swzl_first, container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout = getActivity().findViewById(R.id.swzl_refresh);
        listView = (ListView) getActivity().findViewById(R.id.listView);
        final FloatingActionMenu fab = (FloatingActionMenu) getActivity().findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);
        add = (FloatingActionButton) getActivity().findViewById(R.id.fab_share);
        add2 = (FloatingActionButton)getActivity(). findViewById(R.id.diudiu);

        get();
        onClick();


    }

    private void onClick() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                        get();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        });

                    }
                }).run();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getActivity(),send.class);
                startActivity(intent1);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getActivity(),send2.class);
                startActivity(intent1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }

    public void get(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<com.example.luhongcheng.Bmob_bean.SWZL> query = new BmobQuery<com.example.luhongcheng.Bmob_bean.SWZL>();
                query.findObjects(new FindListener<com.example.luhongcheng.Bmob_bean.SWZL>(){
                    @Override
                    public void done(List<com.example.luhongcheng.Bmob_bean.SWZL> list, BmobException e) {
                        List<com.example.luhongcheng.Bmob_bean.SWZL> lists = new ArrayList<>();
                        if (list != null) {
                            //System.out.println("查询成功"+list.get(0).getTitle()+list.get(0).getContent()+list.get(0).getTime()+list.get(0).getAdress()+list.get(0).getIconUrl());
                            final String[] title  =  new String[list.size()];
                            final String[] content  =  new String[list.size()];
                            final String[] time  =  new String[list.size()];
                            final String[] adress  =  new String[list.size()];
                            final String[] createtime = new String[list.size()];
                            final String[] image = new String[list.size()];
                            for(int i = 0;i<list.size();i++){
                                title[i] = list.get(i).getTitle();
                                content[i] = list.get(i).getContent();
                                time[i] = list.get(i).getTime();
                                adress[i] = list.get(i).getAdress();
                                createtime[i] = list.get(i).getUpdatedAt();
                                image[i] = list.get(i).getimageUrl();
                               // Log.d("imageURL",list.get(i).getimageUrl());


                                url.add(image[i]);
                            }


                            class MyAdaper extends BaseAdapter {

                                private FindListener<com.example.luhongcheng.Bmob_bean.SWZL> context ;
                                public MyAdaper(FindListener<com.example.luhongcheng.Bmob_bean.SWZL> context){
                                    this.context = context;
                                }

                                @Override
                                public int getCount() {
                                    return title.length;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return title[position];
                                }

                                @Override
                                public long getItemId(int position) {
                                    return position;
                                }

                                @Override
                                public View getView(final int position, View convertView, ViewGroup parent) {
                                    ViewHolder viewHolder;
                                    if (convertView == null){
                                        LayoutInflater inflater = LayoutInflater.from(getContext());
                                        convertView = inflater.inflate(R.layout.swzl_first_item, null);//实例化一个布局文件
                                        viewHolder = new ViewHolder();
                                        viewHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
                                        viewHolder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
                                        viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
                                        viewHolder.tv_adress = (TextView)convertView.findViewById(R.id.tv_adress);
                                        viewHolder.create_time = (TextView) convertView.findViewById(R.id.create_time);
                                        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv);

                                        convertView.setTag(viewHolder);
                                    }else {
                                        viewHolder = (ViewHolder) convertView.getTag();
                                    }
                                    viewHolder.tv_title.setText(title[position]);
                                    viewHolder.tv_content.setText("内容："+content[position]);
                                    viewHolder.tv_time.setText("时间地点："+time[position]);
                                    viewHolder.tv_adress.setText("联系方式："+adress[position]);
                                    viewHolder.create_time.setText("发布时间："+createtime[position]);

                                    viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(),ImageFullDisplay.class);
                                            intent.putExtra("url2",image[position]);
                                            startActivity(intent);
                                        }
                                    });
                                    Glide.with(getContext())
                                            .load(image[position])
                                            .apply(new RequestOptions().placeholder(R.drawable.loading))
                                            .apply(new RequestOptions() .error(R.drawable.error))
                                            .apply(new RequestOptions().fitCenter())
                                            .into(viewHolder.imageView);

                                    return convertView;

                                }



                                class ViewHolder {
                                    TextView tv_title;
                                    TextView tv_content;
                                    TextView tv_time;
                                    TextView tv_adress;
                                    TextView create_time;
                                    ImageView imageView;
                                }


                            }




                            listView.setAdapter(new MyAdaper(this));
                        }


                    }
                });
            }
        }); //声明一个子线程
        thread.start();


    }




}
