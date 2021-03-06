package androidovshchik.epub.extensions

import android.content.Context
import android.content.Intent
import androidovshchik.epub.MainActivity
import com.folioreader.FolioReader

fun FolioReader.openBook(context: Context, assetOrSdcardPath: String): FolioReader {
    val intent = getIntentFromUrl(MainActivity::class.java, assetOrSdcardPath, 0)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    return FolioReader.singleton
}