package apktool.android.com

import android.content.Intent
import android.didikee.donate.AlipayDonate
import android.os.Bundle
import android.os.Debug
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import apktool.android.com.fragment.AboutDialogFragment
import brut.androlib.AndrolibException
import brut.androlib.ApkDecoder
import brut.androlib.ApplicationHolder
import brut.androlib.err.CantFindFrameworkResException
import brut.androlib.err.InFileNotFoundException
import brut.androlib.err.OutDirExistsException
import brut.directory.DirectoryException
import com.afollestad.materialdialogs.folderselector.FileChooserDialog
import com.afollestad.materialdialogs.folderselector.FileChooserDialog.FileCallback
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog.FolderCallback
import com.folderv.apktool.andadapter.Logger
//import com.googlecode.d2j.Method
//import com.googlecode.d2j.dex.Dex2jar
//import com.googlecode.d2j.dex.DexExceptionHandler
//import com.googlecode.d2j.node.DexMethodNode
//import com.googlecode.d2j.reader.MultiDexFileReader
//import j6.Files
import kotlinx.android.synthetic.main.activity_main.*
//import org.objectweb.asm.MethodVisitor
import java.io.File
import java.io.IOException

class DecoderActivity : AppCompatActivity(), FolderCallback, FileCallback {
    var DEBUG = false
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    val from = arrayOf("time", "title", "content")
                    // 列表项组件Id 数组
                    val to = intArrayOf(R.id.tv2, R.id.tv1, R.id.tv3)
                    list = ArrayList()
                    val logs = Logger.getList()
                    synchronized(logs) {
                        for (log in logs) {
                            list.add(mutableMapOf("title" to log))
                        }
                    }
                    adapter = SimpleAdapter(this@DecoderActivity, list,
                            R.layout.item_log, from, to)
                    lvLog!!.adapter = adapter
                    sendEmptyMessageDelayed(0, 1000)
                }
            }
        }
    }
    private lateinit var list: MutableList<Map<String, Any?>>
    private var adapter: SimpleAdapter? = null
    private var apkPath: String? = null
    private var outputPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApplicationHolder.setApplication(application)
        val apktoolVersion = findViewById<TextView>(R.id.apktool_version)
        apktoolVersion.text = "Apktool v" + App.getInstance().apkToolVersion
        val from = arrayOf("time", "title", "content")
        // 列表项组件Id 数组
        val to = intArrayOf(R.id.tv1, R.id.tv2, R.id.tv3)
        list = ArrayList()
        adapter = SimpleAdapter(this, list,
                R.layout.item_log, from, to)
        lvLog.setAdapter(adapter)
        tvApkFile.setOnClickListener(View.OnClickListener {
            /*
                new com.nbsp.materialfilepicker.MaterialFilePicker()
                        .withActivity(DecoderActivity.this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.apk$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
                        */
            FileChooserDialog.Builder(this@DecoderActivity) //.initialPath("/sdcard/Download")  // changes initial path, defaults to external storage directory
                    //.mimeType("image/*") // Optional MIME type filter
                    .extensionsFilter(".apk", ".dex", ".jar", ".xml") // Optional extension filter, will override mimeType()
                    .tag("apkFile") //.goUpLabel("Up") // custom go up label, default label is "..."
                    .show(this@DecoderActivity) // an AppCompatActivity which implements FileCallback
        })

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
        */findViewById<View>(R.id.btDecode).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(apkPath)) {
                Toast.makeText(this@DecoderActivity, R.string.please_select_apk_file, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(outputPath)) {
            }
            doDecode(apkPath, outputPath)
        })
        findViewById<View>(R.id.btDextojar).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(apkPath)) {
                Toast.makeText(this@DecoderActivity, R.string.please_select_apk_file, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(outputPath)) {
            }
            Thread { dex2jar(apkPath, outputPath) }.start()
        })
    }

    private fun showAlipay() {
        val hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this@DecoderActivity)
        if (hasInstalledAlipayClient) {
            findViewById<View>(R.id.donate).visibility = View.VISIBLE
            findViewById<View>(R.id.donate).setOnClickListener { AlipayDonate.startAlipayClient(this@DecoderActivity, "tsx04892tojcizvz0ij5535") }
        } else {
            findViewById<View>(R.id.donate).visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        showAlipay()
    }

    private fun dex2jar(apkPath: String?, outDir: String?) {
        /*
        Thread {
            try {
                val toFile = Files.toPath(File(outDir, "class.jar"))
                val reader = MultiDexFileReader.open(pxb.java.nio.file.Files.readAllBytes(Files.toPath(File(apkPath))))
                if (DEBUG) {
                    Debug.startMethodTracing("dex2jar")
                }
                Dex2jar.from(reader).withExceptionHandler(object : DexExceptionHandler {
                    override fun handleFileException(e: Exception) {
                        Log.e(TAG, "handleFileException: ")
                        e.printStackTrace()
                    }

                    override fun handleMethodTranslateException(method: Method, methodNode: DexMethodNode, mv: MethodVisitor, e: Exception) {
                        Log.e(TAG, "handleMethodTranslateException: ")
                    }
                })
                        .topoLogicalSort()
                        .to(toFile)
                if (DEBUG) {
                    Debug.stopMethodTracing()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()*/
    }

    private fun doDecode(filePath: String?, outDir: String?) {
        Logger.clearLog()
        handler.sendEmptyMessage(0)
        progressBar!!.visibility = View.VISIBLE
        Thread {
            val apkName = ""
            val decoder = ApkDecoder()
            try {
                decoder.setOutDir(File(outDir))
                val apkFile = File(filePath)
                decoder.setApkFile(apkFile)
            } catch (e: AndrolibException) {
                e.printStackTrace()
            }
            try {
                if (DEBUG) {
                    Debug.startMethodTracing("apktool")
                }
                decoder.decode()
                if (DEBUG) {
                    Debug.stopMethodTracing()
                }
            } catch (ex: OutDirExistsException) {
                Log.e(TAG, "Destination directory ("
                        + outDir
                        + ") "
                        + "already exists. Use -f switch if you want to overwrite it.")
                //System.exit(1);
            } catch (ex: InFileNotFoundException) {
                Log.e(TAG, "Input file ($apkName) was not found or was not readable.")
                //System.exit(1);
            } catch (ex: CantFindFrameworkResException) {
                Log.e(TAG, "Can't find framework resources for package of id: "
                        + ex.pkgId.toString() + ". You must install proper "
                        + "framework files, see project website for more info.")
                //System.exit(1);
            } catch (ex: IOException) {
                Log.e(TAG, "Could not modify file. Please ensure you have permission.")
                //System.exit(1);
            } catch (ex: DirectoryException) {
                Log.e(TAG, "Could not modify internal dex files. Please ensure you have permission.")
                //System.exit(1);
            } catch (e: AndrolibException) {
                e.printStackTrace()
            } finally {
                try {
                    decoder.close()
                } catch (ignored: IOException) {
                }
            }
            handler.post {
                tv!!.text = "Done"
                progressBar!!.visibility = View.GONE
            }
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.removeMessages(0)
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

    override fun onFolderSelection(dialog: FolderChooserDialog, folder: File) {
        val tag = dialog.tag // gets tag set from Builder, if you use multiple dialogs
        if ("outputDir" == tag) {
            outputPath = folder.absolutePath
            tvOutputDir!!.text = outputPath
        }
    }

    override fun onFolderChooserDismissed(dialog: FolderChooserDialog) {}
    override fun onFileSelection(dialog: FileChooserDialog, file: File) {
        val tag = dialog.tag
        if ("apkFile" == tag) {
            apkPath = file.absolutePath
            if (!TextUtils.isEmpty(apkPath)) {
                tvApkFile!!.text = apkPath
                val apkFile = File(apkPath)
                var outputFile = File(apkFile.parentFile, apkFile.name + "_decode")
                var i = 1
                val tmp = outputFile.absolutePath
                while (outputFile.exists()) {
                    outputFile = File(tmp + i++)
                }
                outputPath = outputFile.absolutePath
                tvOutputDir!!.text = outputPath
            }
        }
    }

    override fun onFileChooserDismissed(dialog: FileChooserDialog) {}
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.option_menu_decode, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> AboutDialogFragment().show(supportFragmentManager, "about")
        }
        return true
    }

    companion object {
        private const val TAG = "DecoderActivity"
    }
}