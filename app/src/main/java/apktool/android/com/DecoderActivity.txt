package apktool.android.com;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.d2j.Method;
import com.googlecode.d2j.dex.Dex2jar;
import com.googlecode.d2j.dex.DexExceptionHandler;
import com.googlecode.d2j.node.DexMethodNode;
import com.googlecode.d2j.reader.BaseDexFileReader;
import com.googlecode.d2j.reader.MultiDexFileReader;

import java.io.File;
import java.io.IOException;

import com.folderv.apktool.andadapter.Logger;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;


import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apktool.android.com.fragment.AboutDialogFragment;
import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.androlib.ApplicationHolder;
import brut.androlib.err.CantFindFrameworkResException;
import brut.androlib.err.InFileNotFoundException;
import brut.androlib.err.OutDirExistsException;
import brut.directory.DirectoryException;

public class DecoderActivity extends AppCompatActivity implements FolderChooserDialog.FolderCallback, FileChooserDialog.FileCallback{

    private static final String TAG = "DecoderActivity";
    boolean DEBUG = false;

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String[] from = {"time", "title", "content"};
                    // 列表项组件Id 数组
                    int[] to = {R.id.tv2, R.id.tv1, R.id.tv3};

                    list = new ArrayList<>();
                    List<String> logs = Logger.getList();
                    synchronized (logs){
                        for (String log : logs) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", log);
                            list.add(map);
                        }
                    }


                    adapter = new SimpleAdapter(DecoderActivity.this, list,
                            R.layout.item_log, from, to);
                    lvLog.setAdapter(adapter);
                    sendEmptyMessageDelayed(0, 1000);
                    break;
            }
        }
    };

    private TextView tvApkFile;
    private TextView tvOutputDir;
    private TextView tv;

    private ProgressBar progressBar;
    private ListView lvLog;

    private List<Map<String, Object>> list;
    private SimpleAdapter adapter;

    private String apkPath = null;
    private String outputPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ApplicationHolder.setApplication(getApplication());

        tv = findViewById(R.id.tv);

        TextView apktoolVersion = findViewById(R.id.apktool_version);
        apktoolVersion.setText("Apktool v" + App.getInstance().getApkToolVersion());

        tvApkFile = findViewById(R.id.tvApkFile);

        tvOutputDir = findViewById(R.id.tvOutputDir);

        progressBar = findViewById(R.id.progressBar);
        lvLog = findViewById(R.id.lvLog);

        String[] from = {"time", "title", "content"};
        // 列表项组件Id 数组
        int[] to = {  R.id.tv1, R.id.tv2, R.id.tv3};
        list = new ArrayList<>();
        adapter = new SimpleAdapter(this, list,
                R.layout.item_log, from, to);
        lvLog.setAdapter(adapter);

        tvApkFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                new com.nbsp.materialfilepicker.MaterialFilePicker()
                        .withActivity(DecoderActivity.this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.apk$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
                        */

                new FileChooserDialog.Builder(DecoderActivity.this)
                        //.initialPath("/sdcard/Download")  // changes initial path, defaults to external storage directory
                        //.mimeType("image/*") // Optional MIME type filter
                        .extensionsFilter(".apk", ".dex", ".jar", ".xml") // Optional extension filter, will override mimeType()
                        .tag("apkFile")
                        //.goUpLabel("Up") // custom go up label, default label is "..."
                        .show(DecoderActivity.this); // an AppCompatActivity which implements FileCallback
            }
        });

        /*
        tvOutputDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new com.nbsp.materialfilepicker.MaterialFilePicker()
//                        .withActivity(DecoderActivity.this)
//                        .withRequestCode(2)
//                        .withFilter(Pattern.compile(".*\\.apk$")) // Filtering files and directories by file name using regexp
//                        .withFilterDirectories(false) // Set directories filterable (false by default)
//                        .withHiddenFiles(true) // Show hidden files and folders
//                        .start();


                new FolderChooserDialog.Builder(DecoderActivity.this)
                        .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                        //.initialPath("/sdcard/Download")  // changes initial path, defaults to external storage directory
                        .tag("outputDir")
                        .allowNewFolder(true, R.string.new_folder)
                        //.goUpLabel("Up") // custom go up label, default label is "..."
                        .show(DecoderActivity.this);
            }
        });
        */


        findViewById(R.id.btDecode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(apkPath)) {
                    Toast.makeText(DecoderActivity.this, R.string.please_select_apk_file, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(outputPath)) {

                }

                doDecode(apkPath, outputPath);


            }
        });


        findViewById(R.id.btDextojar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(apkPath)) {
                    Toast.makeText(DecoderActivity.this, R.string.please_select_apk_file, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(outputPath)) {

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dex2jar(apkPath, outputPath);
                    }
                }).start();
            }
        });

    }

    private void showAlipay(){
        boolean hasInstalledAlipayClient = android.didikee.donate.AlipayDonate.hasInstalledAlipayClient(DecoderActivity.this);
        if (hasInstalledAlipayClient) {
            findViewById(R.id.donate).setVisibility(View.VISIBLE);
            findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.didikee.donate.AlipayDonate.startAlipayClient(DecoderActivity.this, "tsx04892tojcizvz0ij5535");
                }
            });
        }
        else {
            findViewById(R.id.donate).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAlipay();
    }

    private void dex2jar(final String apkPath, final String outDir){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    pxb.java.nio.file.Path toFile = j6.Files.toPath(new File(outDir, "class.jar"));
                    BaseDexFileReader reader = MultiDexFileReader.open(pxb.java.nio.file.Files.readAllBytes(j6.Files.toPath(new File(apkPath))));

                    if (DEBUG) {
                        android.os.Debug.startMethodTracing("dex2jar");
                    }

                    Dex2jar.from(reader).withExceptionHandler(new DexExceptionHandler() {
                        @Override
                        public void handleFileException(Exception e) {
                            Log.e(TAG, "handleFileException: ");
                            e.printStackTrace();
                        }

                        @Override
                        public void handleMethodTranslateException(Method method, DexMethodNode methodNode, MethodVisitor mv, Exception e) {
                            Log.e(TAG, "handleMethodTranslateException: ");
                        }
                    })
                            .topoLogicalSort()
                            .to(toFile);

                    if (DEBUG) {
                        android.os.Debug.stopMethodTracing();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void doDecode(final String filePath,final String outDir){
        Logger.clearLog();
        handler.sendEmptyMessage(0);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String apkName = "";
                ApkDecoder decoder = new ApkDecoder();
                try {
                    decoder.setOutDir(new File(outDir));
                    File apkFile = new File(filePath);
                    decoder.setApkFile(apkFile);
                } catch (AndrolibException e) {
                    e.printStackTrace();
                }
                try {
                    if (DEBUG) {
                        android.os.Debug.startMethodTracing("apktool");
                    }
                    decoder.decode();
                    if (DEBUG) {
                        android.os.Debug.stopMethodTracing();
                    }

                } catch (OutDirExistsException ex) {
                    Log.e(TAG, "Destination directory ("
                            + outDir
                            + ") "
                            + "already exists. Use -f switch if you want to overwrite it.");
                    //System.exit(1);
                } catch (InFileNotFoundException ex) {
                    Log.e(TAG, "Input file (" + apkName + ") " + "was not found or was not readable.");
                    //System.exit(1);
                } catch (CantFindFrameworkResException ex) {
                    Log.e(TAG, "Can't find framework resources for package of id: "
                            + String.valueOf(ex.getPkgId())
                            + ". You must install proper "
                            + "framework files, see project website for more info.");
                    //System.exit(1);
                } catch (IOException ex) {
                    Log.e(TAG, "Could not modify file. Please ensure you have permission.");
                    //System.exit(1);
                } catch (DirectoryException ex) {
                    Log.e(TAG, "Could not modify internal dex files. Please ensure you have permission.");
                    //System.exit(1);
                } catch (AndrolibException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        decoder.close();
                    } catch (IOException ignored) {
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("Done");progressBar.setVisibility(View.GONE);
                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.removeMessages(0);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == 1 && resultCode == RESULT_OK) {
            apkPath = data.getStringExtra(com.nbsp.materialfilepicker.ui.FilePickerActivity.RESULT_FILE_PATH);
            if (!TextUtils.isEmpty(apkPath)) {
                tvApkFile.setText(apkPath);
                File apkFile = new File(apkPath);
                File outputFile = new File(apkFile.getParentFile(), apkFile.getName() + "_decode");

                int i =1;
                String tmp = outputFile.getAbsolutePath();
                while (outputFile.exists()) {
                    outputFile = new File(tmp + i++);
                }
                outputPath = outputFile.getAbsolutePath();
                tvOutputDir.setText(outputPath);
            }

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(com.nbsp.materialfilepicker.ui.FilePickerActivity.RESULT_FILE_PATH);
            tvOutputDir.setText(filePath);
        }
        */
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        final String tag = dialog.getTag(); // gets tag set from Builder, if you use multiple dialogs
        if ("outputDir".equals(tag)) {
            outputPath = folder.getAbsolutePath();
            tvOutputDir.setText(outputPath);
        }
    }

    @Override
    public void onFolderChooserDismissed(@NonNull FolderChooserDialog dialog) {

    }

    @Override
    public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
        final String tag = dialog.getTag();
        if ("apkFile".equals(tag)) {
            apkPath = file.getAbsolutePath();
            if (!TextUtils.isEmpty(apkPath)) {
                tvApkFile.setText(apkPath);
                File apkFile = new File(apkPath);
                File outputFile = new File(apkFile.getParentFile(), apkFile.getName() + "_decode");

                int i =1;
                String tmp = outputFile.getAbsolutePath();
                while (outputFile.exists()) {
                    outputFile = new File(tmp + i++);
                }
                outputPath = outputFile.getAbsolutePath();
                tvOutputDir.setText(outputPath);
            }
        }
    }

    @Override
    public void onFileChooserDismissed(@NonNull FileChooserDialog dialog) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option_menu_decode, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                new AboutDialogFragment().show(getSupportFragmentManager(), "about");
                //startActivity(new Intent(this, DecoderActivity.class));
                break;

        }
        return true;
    }
}
