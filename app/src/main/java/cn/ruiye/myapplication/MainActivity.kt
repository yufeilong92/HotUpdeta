package cn.ruiye.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devilwwj.jni.TestJNI
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.interfaces.BetaPatchListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEvent()
        initListener()
        initViewModel()
    }

    private fun initEvent() {
        tvCurrentVersion.setText("当前版本：" + getCurrentVersion(this))
        tv_data.text="这是补丁包"
        Beta.applyDownloadedPatch()
    }


    private fun initListener() {
        // 测试热更新功能
        btnShowToast.setOnClickListener {
            testToast()
        }
        // 杀死进程
        btnKillSelf.setOnClickListener {
            Process.killProcess(Process.myPid())
        }
        // 本地加载补丁测试
        btnLoadPatch.setOnClickListener {

            Beta.applyTinkerPatch(applicationContext, Environment.getExternalStorageDirectory().absolutePath + "/patch_signed_7zip.apk")
        }
        // 本地加载so库测试
        btnLoadLibrary.setOnClickListener {
            val testJNI = TestJNI()
            testJNI.createANativeCrash()
        }
        btnDownloadPatch.setOnClickListener {
            Beta.downloadPatch()
        }
        btnPatchDownloaded.setOnClickListener {
            Beta.applyDownloadedPatch()
        }
        btnCheckUpgrade.setOnClickListener {
            Beta.checkUpgrade()
        }
    }

    private fun initViewModel() {

    }

    /**
     * 获取当前版本.
     *
     * @param context 上下文对象
     * @return 返回当前版本
     */
    fun getCurrentVersion(context: Context): String? {
        try {
            val packageInfo = context.packageManager.getPackageInfo(this.packageName,
                    PackageManager.GET_CONFIGURATIONS)
            val versionCode = packageInfo.versionCode
            val versionName = packageInfo.versionName
            return "$versionName.$versionCode"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 根据应用patch包前后来测试是否应用patch包成功.
     *
     * 应用patch包前，提示"This is a bug class"
     * 应用patch包之后，提示"The bug has fixed"
     */
    fun testToast() {
        Toast.makeText(this, LoadBugClass.getBugString(), Toast.LENGTH_SHORT).show()
    }
}
