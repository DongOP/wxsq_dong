package com.magicing.opencv_java;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.magicing.opencv_java.compare.CompareUtils;
import com.magicing.opencv_java.compare.CutBitmapUtils;
import com.magicing.opencv_java.util.CodeFormatUtils;
import com.magicing.opencv_java.util.MockUtils;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int IMAGE_FLAG_1 = 1;
    private static final int IMAGE_FLAG_2 = 2;
    private Button mBtnResult;
    private Button mCutBtn;
    private Bitmap mFirstBitmap;
    private Bitmap mSecondBitmap;
    private Bitmap mCutBitmap;
    private TextView mTVResult;
    private ImageView mFirstIv;
    private ImageView mSecondIv;
    private ImageView mCurIv;
    private static final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnResult = (Button) findViewById(R.id.btnId);
        mCutBtn = (Button) findViewById(R.id.cut_btn);
        mTVResult = (TextView) findViewById(R.id.txtResultId);
        // 加载图像程序中并进行显示
        mFirstBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mSecondBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        mFirstIv = (ImageView) findViewById(R.id.img1Id);
        mSecondIv = (ImageView) findViewById(R.id.img2Id);
        mCurIv = (ImageView) findViewById(R.id.cut_img);
        mFirstIv.setImageBitmap(mFirstBitmap);
        mSecondIv.setImageBitmap(mSecondBitmap);
        mBtnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTV();
            }
        });
        mSecondIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用系统相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_FLAG_1);
            }
        });
        mFirstIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用系统相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_FLAG_2);
            }
        });
        mCutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cutAndShowBitmap();
//                testBase64();
                MockUtils.mockClickHome(getApplicationContext());
            }
        });
    }
    long startTime = System.currentTimeMillis();
    private void cutAndShowBitmap() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                // 获取屏幕
                View dView = MainActivity.this.getWindow().getDecorView();
                dView.setDrawingCacheEnabled(true);
                dView.buildDrawingCache();
                Bitmap bmp = dView.getDrawingCache();
//                mCutBitmap = CutBitmapUtils.cupBitmap(bmp, 700, 1200, 600, 600);
                // 抠图
                mCutBitmap = Bitmap.createBitmap(bmp, 700, 1200,600,600);
                mCurIv.setImageBitmap(mCutBitmap);
                Log.d(TAG, "do cutAndShowBitmap, 耗时 = " + (System.currentTimeMillis() - startTime));
            }
        });
    }

    private void testBase64() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //
                Bitmap  tempBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                String strBitmap = CodeFormatUtils.bitmapToString(tempBitmap);
                Log.e(TAG, "do testBase64 strBitmap =" + strBitmap);

                Bitmap bitmap = CodeFormatUtils.stringToBitmap(strBitmap);
                mCurIv.setImageBitmap(bitmap);
            }
        });
    }

    private void updateTV() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                double target = CompareUtils.comPareHist(mFirstBitmap, mSecondBitmap);
                // 当target = 1时，说明图片一样 mFirstBitmap mSecondBitmap
                mTVResult.setText(target == 1 ? "两张图片一致 ^_^" : "相似度 = " + target);
            }
        });
    }

    // OpenCV类库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.d(TAG, "MainActivity OpenCV loaded successfully");
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片路径
        if (requestCode == IMAGE_FLAG_1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            mSecondBitmap = BitmapFactory.decodeFile(imagePath);
            mSecondIv.setImageBitmap(mSecondBitmap);
            c.close();
        } else if (requestCode == IMAGE_FLAG_2 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            mFirstBitmap = BitmapFactory.decodeFile(imagePath);
            mFirstIv.setImageBitmap(mFirstBitmap);
            c.close();
        }
    }

    // 加载图片
    private void showImage(ImageView imageView, String imaePath) {
        mSecondBitmap = BitmapFactory.decodeFile(imaePath);
        imageView.setImageBitmap(mSecondBitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "MainActivity Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "MainActivity OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        // 回收图片避免内存溢出
        mFirstBitmap.recycle();
        mSecondBitmap.recycle();
        mCutBitmap.recycle();
        super.onDestroy();
    }

}
