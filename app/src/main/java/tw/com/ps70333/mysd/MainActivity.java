package tw.com.ps70333.mysd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private boolean isPermissionOK;
    private File sdroot, approot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Manifest.xml要先設定
        //ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) 檢查是否有寫入的權限
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //發出請求給使用者要求確認是否同意，會觸發onRequestPermissionsResult事件
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},123);
            //123 無意義，只是設定一個CODE，然後再來判斷
        }else {
            isPermissionOK = true;
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults回來的結果如果0, 代表使用者同意權限,如果回來的是-1, 代表被拒絕
        if (requestCode == 123){//代表是29行發過來的請求訊息
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //允許
                isPermissionOK = true;
            }else{
                //拒絕
            }
            init();
        }
    }
    public void init(){
        if (!isPermissionOK) {
            finish();
        }else{
            go();
        }
    }
    public void go(){
        sdroot = Environment.getExternalStorageDirectory();// 取得 SD Card 位置
        approot = new File(sdroot, "Android/data/" + getPackageName() + "/");//本機的目錄
        if (!approot.exists()){
            approot.mkdirs();
        }
    }

    public void doSaveFile(View v){
        File file1 = new File(sdroot,"SD_File");
        File file2 = new File(approot, "APP_File");
        try {
            FileOutputStream fout1 = new FileOutputStream(file1);
            FileOutputStream fout2 = new FileOutputStream(file2);
            fout1.write("I am SD File".getBytes());
            fout2.write("I am App File".getBytes());
            fout1.flush();fout2.flush();
            fout1.close();fout2.close();
            Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void doLoad(View v){
        File file1 = new File(sdroot,"SD_File");
        File file2 = new File(approot, "APP_File");
        try {
            FileInputStream fin = new FileInputStream(file1);
            FileInputStream fin2 = new FileInputStream(file2);
            StringBuffer sb=new StringBuffer();
            byte[] buf=new byte[8];
            int len;

            while((len=fin.read(buf))!=-1){
                sb.append(new String(buf,0,len));
            }
            sb.append("\n");
            while((len=fin2.read(buf))!=-1){
                sb.append(new String(buf,0,len));
            }
            fin.close();fin.close();
            Toast.makeText(this,sb, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
