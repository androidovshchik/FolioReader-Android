package androidovshchik.epub

import android.content.Context
import android.content.Intent
import com.folioreader.FolioReader

fun FolioReader.openBook(context: Context, assetOrSdcardPath: String): FolioReader {
    val intent = getIntentFromUrl(MainActivity::class.java, assetOrSdcardPath, 0)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    return FolioReader.singleton
}