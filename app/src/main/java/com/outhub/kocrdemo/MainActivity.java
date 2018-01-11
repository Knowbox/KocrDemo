package com.outhub.kocrdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.outhub.kocrdemo.Utils.CameraUtil;
import com.outhub.kocrdemo.Utils.Cv4jUti;
import com.outhub.kocrdemo.Utils.PermissionUtil;
import com.outhub.kocrdemo.camera.EasyCamera;
import com.outhub.kocrdemo.data.DataSource;
import com.outhub.kocrdemo.data.OcrAnalyzeData;
import com.outhub.kocrdemo.loader.DataLoader;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks {
    private static final int TASK_GET_OCR_TEXT_BY_PIC = 1;

    private ImageView mIvPic;
    private TextView mTvWord;
    private ProgressBar mProgressBar;

    private LoaderManager mLoaderManager;
    private DataLoader mDataLoader;
    private DataSource mDataSource;
    private String mCachePicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.checkWritePermission(this);
        PermissionUtil.checkCameraPermission(this);
        PermissionUtil.checkStrictModeForCamera();

        initView();

        mCachePicPath = getExternalCacheDir() + "/TesseractSample/" + "cache_pic.png";
        mLoaderManager = getSupportLoaderManager();
        mDataSource = new OcrAnalyzeData(this, ((BitmapDrawable) mIvPic.getDrawable()).getBitmap());
        mDataLoader = new DataLoader(this, mDataSource);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoaderManager.initLoader(TASK_GET_OCR_TEXT_BY_PIC, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Loader loader = null;
        switch (id) {
            case TASK_GET_OCR_TEXT_BY_PIC:
                loader = mDataLoader;
                if (loader.takeContentChanged()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mProgressBar.setVisibility(View.GONE);
        switch (loader.getId()) {
            case TASK_GET_OCR_TEXT_BY_PIC:
                mTvWord.setText(String.valueOf(data));
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CameraUtil.REQUEST_CAMERA:
                    CameraUtil.cropPicture(this, mCachePicPath);
                    break;
                case CameraUtil.REQUEST_CROP_PIC:
                case EasyCamera.REQUEST_CAPTURE:
                    mIvPic.setImageDrawable(null);
                    Bitmap bitmap = BitmapFactory.decodeFile(mCachePicPath);
//                    mIvPic.setImageBitmap(Cv4jUti.getCv4jBitmap(bitmap));
                    mIvPic.setImageBitmap(bitmap);
                    chineseOCR();
                    break;
            }
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProgressBar = findViewById(R.id.progressbar);
        mIvPic = findViewById(R.id.iv_pic);
        mTvWord = findViewById(R.id.tv_word);
        mIvPic.setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_click).setOnClickListener(new MyOnClickListener());
    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_click:
                    chineseOCR();
                    break;
                case R.id.iv_pic:
                    //CameraUtil.openCamera(MainActivity.this, mCachePicPath);
                    openCamera();
                    break;
            }
        }
    }

    private void openCamera() {
        Uri destination = Uri.fromFile(new File(mCachePicPath));
        EasyCamera.create(destination)
                .withViewRatio(0.5f)        //取景框高宽比
                .withMarginCameraEdge(50,50)
                .start(this);
    }

    public void chineseOCR() {
        mTvWord.setText(null);
        mProgressBar.setVisibility(View.VISIBLE);
        Bitmap bmp = ((BitmapDrawable) mIvPic.getDrawable()).getBitmap();
        ((OcrAnalyzeData)mDataSource).setBitmap(bmp);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
