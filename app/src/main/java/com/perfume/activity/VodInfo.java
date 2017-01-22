package com.perfume.activity;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.widget.*;
import com.perfume.*;
import java.io.*;
import java.net.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.bumptech.glide.Glide;
import com.perfume.Model.PlayUits;
import com.perfume.VideoPlay.Play;
import com.perfume.activity.VodInfo;
import com.yun.play.APP;
import com.perfume.Utis.UrlCode;
import com.perfume.Utis.ShowDialog;
/*
视频信息 播放处理
*/
public class VodInfo extends AppCompatActivity {
    public static final String EXTRA_NAME = "NAME";
    public static final String EXTRA_IMAGE = "IMAGE";
    public static final String EXTRA_URL = "URL";
    public static final String EXTRA_MSG = "MSG";
    public static final String EXTRA_PINFENG = "PINFENG";
    public static final String EXTRA_YANYUAN = "YANYUAN ";
    public static final String EXTRA_AV = "ISAV ";
    public static final String EXTRA_ISPLAY = "ISPLAY";
    public static final String EXTRA_ISVIP = "ISVIP";
    private TextView Yanyuan;
    private TextView Pinfen;
    private TextView VodInfo;
    private TextView Url;
    private FloatingActionButton fab;
    private StringBuffer buff = new StringBuffer();
    private URL u;
    private HttpURLConnection i;
    private OutputStreamWriter osw;
    private OutputStream outputStream;
    private InputStream in;
    private InputStreamReader inr;
    private ProgressDialog dialog;
    private boolean IsAV = false;
    private String NAME;
    private String MSG;
    private String URL;
    private boolean IsPlay;
    private boolean IsVip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        NAME = intent.getStringExtra(EXTRA_NAME);
        String IMAGE = intent.getStringExtra(EXTRA_IMAGE);
        URL = intent.getStringExtra(EXTRA_URL);
        MSG = intent.getStringExtra(EXTRA_MSG);
        IsPlay = intent.getBooleanExtra(EXTRA_ISPLAY, false);
        IsVip = intent.getBooleanExtra(EXTRA_ISVIP, false);
        String PINFENG = intent.getStringExtra(EXTRA_PINFENG);
        String YANYUAN = intent.getStringExtra(EXTRA_YANYUAN);
        IsAV = intent.getBooleanExtra(EXTRA_AV, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(NAME);
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(IMAGE).fitCenter().into(imageView);
        Yanyuan = (TextView) findViewById(R.id.Yanyuan);
        Pinfen = (TextView) findViewById(R.id.Pinfen);
        VodInfo = (TextView) findViewById(R.id.VodInfo);
        Url = (TextView) findViewById(R.id.Url);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Yanyuan.setText(YANYUAN);
        Pinfen.setText(PINFENG);
        VodInfo.setText(MSG);
        Url.setText(URL);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsAV) {
                    //AV
                    if (IsPlay) {
                        if (!IsVip) {
                            Context context = VodInfo.getContext();
                            Intent intent = new Intent(context, Play.class);
                            intent.putExtra(Play.PLAY_URL, URL);
                            intent.putExtra(Play.PLAY_COOKIC, MSG);
                            context.startActivity(intent);
                        } else {
                            if (!MainActivity.isLogin) {
                                  ShowDialog.showErrorBox(VodInfo.this, APP.b(5));
                                return;
                            } else if (!PlayUits.Is) {
                                  ShowDialog.showErrorBox(VodInfo.this, APP.b(3));
                                return;
                            } else {
                                Context context = VodInfo.getContext();
                                Intent intent = new Intent(context, Play.class);
                                intent.putExtra(Play.PLAY_URL, URL);
                                intent.putExtra(Play.PLAY_COOKIC, MSG);
                                context.startActivity(intent);
                            }
                        }
                    } else {
                        ShowDialog.showErrorBox(VodInfo.this, APP.b(4));
                        return;
                    }
                } else {
                    //电影或者电视剧
                    jiexi(URL);
                }

            }
        });
    }

    private void jiexi(final String uri) {
        showDialog(VodInfo.getContext(), "正在解析中");
        new Thread() {
            @Override
            public void run() {
                try {
                    u = new URL("http://vip.girl99.net/vodjx.php");
                    i = (HttpURLConnection) u.openConnection();
                    i.setRequestMethod("POST");
                    i.setDoOutput(true);
                    i.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    i.addRequestProperty("www.girl99.net", "loginmsg");
                    osw = new OutputStreamWriter(i.getOutputStream(), "UTF-8");
                    outputStream = i.getOutputStream();
                    outputStream.write(new String("url=" + uri + "&type=0" + "&key" + UrlCode.GetMD5(uri)).getBytes());
                    outputStream.flush();
                    in = i.getInputStream();
                    inr = new InputStreamReader(in);
                    BufferedReader stb = new BufferedReader(inr);
                    String line;
                    while ((line = stb.readLine()) != null) {
                        buff.append(line);
                    }
                    stb.close();
                    inr.close();
                    in.close();
                    h.sendEmptyMessage(1);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    hideDialog();
                    if (!UrlCode.Getmsg(buff.toString())) {
                        h.sendEmptyMessage(2);
                        buff.delete(0, buff.length());
                        return;
                    }
                    Context context = VodInfo.getContext();
                    Intent intent = new Intent(context, Play.class);
                    intent.putExtra(Play.PLAY_URL, UrlCode.GetUrl2(buff.toString()));
                    intent.putExtra(Play.PLAY_COOKIC, "");
                    context.startActivity(intent);
                    buff.delete(0, buff.length());
                    break;
                case 2:
                    hideDialog();
                    ShowDialog.showErrorBox(VodInfo.this, APP.b(6));
                    break;
            }
        }
    };

    public void showDialog(Context thzz, String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(thzz);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {

        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }
}

