package com.folioreader.android.sample

import android.app.Application
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.HighLight
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.AppUtil
import com.folioreader.util.OnHighlightListener
import com.folioreader.util.ReadLocatorListener
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class MainApplication : Application(), OnHighlightListener, ReadLocatorListener, FolioReader.OnClosedListener {

    override fun onCreate() {
        super.onCreate()
        // Создание расширенной конфигурации библиотеки.
        val metricaConfig = YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_key)).build()
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(applicationContext, metricaConfig)
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this)
        if (!BuildConfig.DEBUG) {
            //MobileAds.initialize(applicationContext, getString(R.string.ads_app))
        }
        val folioReader = FolioReader.get()
        val readLocator = getLastReadLocator()
        val config = AppUtil.getSavedConfig(applicationContext) ?: Config()
        config.allowedDirection = Config.AllowedDirection.ONLY_HORIZONTAL
        config.setThemeColorRes(R.color.colorAccent)
        folioReader.setReadLocator(readLocator)
        folioReader.setConfig(config, true)
            .openBook(applicationContext, "file:///android_asset/book.epub")
    }

    private fun getLastReadLocator(): ReadLocator? {
        val jsonString = assets.open("Locators/LastReadLocators/last_read_locator_1.json")
            .bufferedReader()
            .use { it.readText() }
        return ReadLocator.fromJson(jsonString)
    }

    override fun onHighlight(highlight: HighLight?, type: HighLight.HighLightAction?) {

    }

    override fun saveReadLocator(readLocator: ReadLocator?) {

    }

    override fun onFolioReaderClosed() {

    }
}