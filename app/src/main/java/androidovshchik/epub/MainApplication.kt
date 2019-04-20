package androidovshchik.epub

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidovshchik.epub.extensions.openBook
import androidovshchik.epub.extensions.processName
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.AppUtil
import com.folioreader.util.FileUtil
import com.folioreader.util.ReadLocatorListener
import com.google.android.gms.ads.MobileAds
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import timber.log.Timber

@Suppress("unused")
class MainApplication : Application(), Application.ActivityLifecycleCallbacks, ReadLocatorListener {

    private lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager.processName.contains(":")) {
            return
        }
        preferences = getSharedPreferences("main", Context.MODE_PRIVATE)
        registerActivityLifecycleCallbacks(this)
        // Создание расширенной конфигурации библиотеки.
        val metricaConfig = YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_key)).build()
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(applicationContext, metricaConfig)
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this)
        if (!BuildConfig.DEBUG) {
            MobileAds.initialize(applicationContext, getString(R.string.admob_app))
        }
        FileUtil.FOLIO_READER_ROOT = getString(R.string.app_name)
        val epubConfig = (AppUtil.getSavedConfig(applicationContext) ?: Config()).apply {
            allowedDirection = Config.AllowedDirection.ONLY_HORIZONTAL
            setThemeColorRes(R.color.colorAccent)
            isShowTts = false
        }
        FolioReader.get().apply {
            setReadLocatorListener(this@MainApplication)
            setReadLocator(ReadLocator.fromJson(preferences.getString("lastPosition", null)))
            setConfig(epubConfig, true)
                .openBook(applicationContext, "file:///android_asset/book.epub")
        }
    }

    override fun saveReadLocator(readLocator: ReadLocator) {
        Timber.d("saveReadLocator")
        preferences.edit()
            .putString("lastPosition", readLocator.toJson())
            .apply()
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity) {
        if (activity is StartActivity) {
            activity.finish()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
}