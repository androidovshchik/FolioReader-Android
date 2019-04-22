package androidovshchik.epub

import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.folioreader.ui.activity.FolioActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import timber.log.Timber

class MainActivity : FolioActivity() {

    private var pageCounter = 0

    private var isLoadingAds = false

    private lateinit var detector: GestureDetectorCompat

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detector = GestureDetectorCompat(applicationContext, object : GestureListener() {

            override fun onSwipeRight() {
                Timber.d("onSwipeRight")
                onSwipe()
            }

            override fun onSwipeLeft() {
                Timber.d("onSwipeLeft")
                onSwipe()
            }
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    private fun onSwipe() {
        pageCounter++
        if ((pageCounter - 2) % 30 == 0) {
            if (resources.getBoolean(R.bool.isAdmobWorks)) {
                if (pageCounter == 2) {
                    showAdmobInterstitial(R.string.admob_interstitial1)
                } else {
                    showAdmobInterstitial(R.string.admob_interstitial2)
                }
            } else {

            }
        }
    }

    private fun showAdmobInterstitial(id: Int) {
        if (isLoadingAds) {
            return
        }
        isLoadingAds = true
        val interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(id)
        interstitialAd.adListener = object : AdListener() {

            override fun onAdLoaded() {
                Timber.d("onAdLoaded")
                if (!isFinishing) {
                    interstitialAd.show()
                } else {
                    isLoadingAds = false
                }
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Timber.e("onAdFailedToLoad errorCode=$errorCode")
                isLoadingAds = false
            }

            override fun onAdOpened() {
                Timber.d("onAdOpened")
            }

            override fun onAdLeftApplication() {
                Timber.d("onAdLeftApplication")
            }

            override fun onAdClosed() {
                Timber.d("onAdClosed")
                isLoadingAds = false
            }
        }
        interstitialAd.loadAd(AdRequest.Builder().build())
    }
}