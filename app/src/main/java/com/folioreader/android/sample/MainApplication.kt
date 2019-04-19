package com.folioreader.android.sample

import android.app.Application
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.HighLight
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.AppUtil
import com.folioreader.util.OnHighlightListener
import com.folioreader.util.ReadLocatorListener

class MainApplication : Application(), OnHighlightListener, ReadLocatorListener, FolioReader.OnClosedListener {

    override fun onCreate() {
        super.onCreate()
        val folioReader = FolioReader.get()
        val readLocator = getLastReadLocator()
        var config = AppUtil.getSavedConfig(applicationContext)
        if (config == null)
            config = Config()
        config.allowedDirection = Config.AllowedDirection.VERTICAL_AND_HORIZONTAL
        folioReader.setReadLocator(readLocator)
        folioReader.setConfig(config, true)
            .openBook("file:///android_asset/TheSilverChair.epub")
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