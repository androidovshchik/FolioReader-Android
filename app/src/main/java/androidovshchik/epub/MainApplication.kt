package androidovshchik.epub

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.HighLight
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.AppUtil
import com.folioreader.util.FileUtil
import com.folioreader.util.OnHighlightListener
import com.folioreader.util.ReadLocatorListener
import timber.log.Timber

class MainApplication : Application(), OnHighlightListener, ReadLocatorListener, FolioReader.OnClosedListener {

    private lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        preferences = getSharedPreferences("main", Context.MODE_PRIVATE)
        // Создание расширенной конфигурации библиотеки.
        /*val metricaConfig = YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_key)).build()
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(applicationContext, metricaConfig)
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this)
        if (!BuildConfig.DEBUG) {
           //MobileAds.initialize(applicationContext, getString(R.string.admob_app))
        }*/
        FileUtil.FOLIO_READER_ROOT = getString(R.string.app_name)
        val epubConfig = (AppUtil.getSavedConfig(applicationContext) ?: Config()).apply {
            allowedDirection = Config.AllowedDirection.ONLY_HORIZONTAL
            setThemeColorRes(R.color.colorAccent)
            isShowTts = false
        }
        FolioReader.get().apply {
            setOnHighlightListener(this@MainApplication)
            setReadLocatorListener(this@MainApplication)
            setOnClosedListener(this@MainApplication)
            setReadLocator(ReadLocator.fromJson(preferences.getString("lastPosition", null)))
            setConfig(epubConfig, true)
                .openBook(applicationContext, "file:///android_asset/book.epub")
        }
    }

    override fun onHighlight(highlight: HighLight, type: HighLight.HighLightAction) {
        Timber.d("onHighlight")
    }

    override fun saveReadLocator(readLocator: ReadLocator) {
        Timber.d("saveReadLocator")
        preferences.edit()
            .putString("lastPosition", readLocator.toJson())
            .apply()
    }

    override fun onFolioReaderClosed() {
        Timber.d("onFolioReaderClosed")
    }
}