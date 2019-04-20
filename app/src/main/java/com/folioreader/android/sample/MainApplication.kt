package com.folioreader.android.sample

import android.app.Application
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.HighLight
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.AppUtil
import com.folioreader.util.OnHighlightListener
import com.folioreader.util.ReadLocatorListener
import timber.log.Timber

class MainApplication : Application(), OnHighlightListener, ReadLocatorListener, FolioReader.OnClosedListener {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // Создание расширенной конфигурации библиотеки.
        /*val metricaConfig = YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_key)).build()
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(applicationContext, metricaConfig)
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this)
        if (!BuildConfig.DEBUG) {
           //MobileAds.initialize(applicationContext, getString(R.string.admob_app))
        }*/
        val folioReader = FolioReader.get()
            .setOnHighlightListener(this)
            .setReadLocatorListener(this)
            .setOnClosedListener(this)
        val readLocator = getLastReadLocator()
        val config = AppUtil.getSavedConfig(applicationContext) ?: Config()
        config.allowedDirection = Config.AllowedDirection.ONLY_HORIZONTAL
        config.setThemeColorRes(R.color.colorAccent)
        folioReader.setReadLocator(readLocator)
        folioReader.setConfig(config, true)
            .openBook(applicationContext, "file:///android_asset/book.epub")
    }

    private fun getLastReadLocator(): ReadLocator? {
        return ReadLocator.fromJson(
            "{\n" +
                    "  \"bookId\": \"_simple_book\",\n" +
                    "  \"href\": \"/OEBPS/ch03.xhtml\",\n" +
                    "  \"created\": 1539934158390,\n" +
                    "  \"locations\": {\n" +
                    "    \"cfi\": \"epubcfi(/0!/4/4)\"\n" +
                    "  }\n" +
                    "}"
        )
    }

    override fun onHighlight(highlight: HighLight?, type: HighLight.HighLightAction?) {
        Timber.d("onHighlight")
    }

    override fun saveReadLocator(readLocator: ReadLocator?) {
        Timber.d("saveReadLocator")
    }

    override fun onFolioReaderClosed() {
        Timber.d("onFolioReaderClosed")
    }
}