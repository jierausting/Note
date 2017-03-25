package experiment.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditActivity extends AppCompatActivity {
    //ADD BY JIAJUN
    private boolean isEdited = false;
    //ADD BY JIAJUN
    private EditText title;
    private EditText inputText;
    private List<String> listMonth = new ArrayList<>();
    private Spinner spinnerMonth;
    private ArrayAdapter<String> adapterMonth;
    private List<String> listDay = new ArrayList<>();
    private Spinner spinnerDay;
    private ArrayAdapter<String> adapterDay;
    private TextView weather;
    private ImageButton image;
    private Button editBtn;
    private Button saveBtn;
    private Button cancelBtn;
    private ImageButton backBtn;
    private LinearLayout groupBtn;
    private Diary diary;
    private DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
    private byte[] temPic;

    private final int REQUEST_CODE_PICK_IMAGE = 1;

    private final String IMG_FILE_NAME = "tem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        title = (EditText) findViewById(R.id.diary_title);
        inputText = (EditText) findViewById(R.id.diary_text);
        spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
        spinnerDay = (Spinner) findViewById(R.id.spinner_day);
        image = (ImageButton) findViewById(R.id.imageView);
        editBtn = (Button) findViewById(R.id.edit_btn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        backBtn = (ImageButton) findViewById(R.id.back_to_lists);
        groupBtn = (LinearLayout) findViewById(R.id.groupBtn);

        setButtonsEditable(false);
        groupBtn.setVisibility(View.GONE);
        image.setAdjustViewBounds(true);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int monthForInit = bundle.getInt("month");
            int dayForInit = bundle.getInt("day");
            initSpinner(monthForInit, dayForInit);
            diary = dbAdapter.queryDiaryByMonthAndByDay(monthForInit, dayForInit);
            refresh();
        } else {
            Calendar c = Calendar.getInstance();
            int temMonth = c.get(Calendar.MONTH)+1;
            int temDay = c.get(Calendar.DAY_OF_MONTH);
            initSpinner(temMonth, temDay);
            diary = dbAdapter.queryDiaryByMonthAndByDay(temMonth, temDay);
            if (diary == null) {
                diary = new Diary(temMonth,temDay,"","",temPic);
                backUp();
            } else {
                refresh();
            }
        }
        setListeners();
    }

    private void initSpinner(int month, int day) {
        listMonth.add("Jan");
        listMonth.add("Feb");
        listMonth.add("Mar");
        listMonth.add("Apr");
        listMonth.add("May");
        listMonth.add("Jun");
        listMonth.add("Jul");
        listMonth.add("Aug");
        listMonth.add("Sep");
        listMonth.add("Oct");
        listMonth.add("Nov");
        listMonth.add("Dec");
        for (int i = 1; i < 32; i++)
            listDay.add(String.valueOf(i));

        adapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMonth);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerMonth.setSelection(month-1, true); // initialize selection

        adapterDay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDay);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);
        spinnerDay.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerDay.setSelection(day-1, true);
    }

    private void backUp() {
        diary.setMonth(spinnerMonth.getSelectedItemPosition()+1);
        diary.setDay(spinnerDay.getSelectedItemPosition()+1);
        diary.setTitle(title.getText().toString());
        diary.setContent(inputText.getText().toString());
        diary.setPicture(temPic);
    }

    private void refresh() {
        spinnerMonth.setSelection(diary.getMonth()-1);
        spinnerDay.setSelection(diary.getDay()-1);
        title.setText(diary.getTitle());
        inputText.setText(diary.getContent());
        temPic = diary.getPicture();
        if (diary.getPicture() == null) {
            image.setImageBitmap(null);
        } else {
            image.setImageBitmap(Diary.bytesToBimap(diary.getPicture()));
        }
    }

    private void setListeners() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add picture
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //
                intent.putExtra("crop", true);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", false);
                intent.putExtra("noFaceDetection", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                //intent.putExtra("outputX", 4096);
                //intent.putExtra("outputY", 4096);
                //
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为可编辑状态
                groupBtn.setVisibility(View.VISIBLE);
//                Log.e("key","click editBtn");
                setButtonsEditable(true);

                editBtn.setVisibility(View.GONE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backUp();
                if (dbAdapter.queryDiaryByMonthAndByDay(diary.getMonth(), diary.getDay()) == null) {
                    dbAdapter.insertDiary(diary.getMonth(), diary.getDay(), diary.getTitle(), diary.getContent(), diary.getPicture());
                } else {
                    dbAdapter.updateDiary(diary.getMonth(), diary.getDay(), diary.getTitle(), diary.getContent(), diary.getPicture());
                }

                setButtonsEditable(false);
                editBtn.setVisibility(View.VISIBLE);
                groupBtn.setVisibility(View.GONE);
                //ADD BY JIAJUN
                isEdited = true;
                //ADD BY JIAJUN
                //finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                setButtonsEditable(false);
                editBtn.setVisibility(View.VISIBLE);
                groupBtn.setVisibility(View.GONE);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ADD BY JIAJUN
                if (isEdited) {
                    EditActivity.this.setResult(RESULT_OK);
                }
                //ADD BY JIAJUN
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE: {
                Uri uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bm = (Bitmap) bundle.get("data");
                    if (bm != null) {
                        //Bitmap bm = decodeUriAsBitmap(uri);
                        bm = compressImage(bm);

                        image.setImageBitmap(bm);
                        temPic = Diary.bitmapToBytes(bm);
                        diary.setPicture(temPic);
                    }
                } else {
                    Bitmap bm = null;
                    try {
                        bm = decodeUriAsBitmap(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    image.setImageBitmap(bm);
                    temPic = Diary.bitmapToBytes(bm);
                    diary.setPicture(temPic);

                }
                break;
            }
            /*case REQUEST_CODE_CROP_IMAGE: {
                Uri uri = data.getData();
                image.setImageURI(uri);
                break;
            }*/
            default:
                break;
        }
    }

    private void setButtonsEditable(boolean bool) {
        inputText.setEnabled(bool);
        title.setEnabled(bool);
        spinnerDay.setEnabled(bool);
        spinnerMonth.setEnabled(bool);
        image.setEnabled(bool);
    }

    private Bitmap compress(Bitmap squareBM) {
        if (squareBM == null) return null;
        final int outputHeight = 1024;
        if (squareBM.getHeight() < 1024) return squareBM;
        float compressSize = squareBM.getHeight()/outputHeight;
        Matrix matrix = new Matrix();
        matrix.setScale(compressSize, compressSize);
        return Bitmap.createBitmap(squareBM, 0, 0, outputHeight, outputHeight, matrix, true);
    }

    private Bitmap decodeUriAsBitmap(Uri uri) throws IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        int height = 1024;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > height) {//如果宽度大的话根据宽度固定大小缩放
            be = (originalWidth / height);
        } else if (originalWidth < originalHeight && originalHeight > height) {//如果高度高的话根据宽度固定大小缩放
            be = (originalHeight / height);
        }
        if (be <= 0)
            be = 1;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /*
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        File file = new File(Environment.getExternalStorageDirectory(), IMG_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
            default:
                break;
        }
    }

    private void cropImage(Uri uri) {
        if (uri == null) return;
        Intent intent = new Intent("com.android.camera.action.CROP");
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }
    */
}


