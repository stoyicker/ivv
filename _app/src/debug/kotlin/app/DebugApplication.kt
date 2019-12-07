package app

import android.os.Build
import android.os.StrictMode

internal class DebugApplication : MyApplication() {
  override fun onCreate() {
    super.onCreate()
    enforceThreadStrictMode()
    enforceVMStrictMode()
  }

  /**
   * @see <a href="https://developer.android.com/reference/android/os/StrictMode.html">
   *     Strict Mode | Android Developers</a>
   */
  private fun enforceThreadStrictMode() =
      StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
          .detectCustomSlowCalls()
          .detectDiskReads()
          .detectDiskWrites()
          .detectNetwork()
          .penaltyLog()
          .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              detectResourceMismatches()
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                detectUnbufferedIo()
              }
            }
          }
          .build())

  /**
   * @see <a href="https://developer.android.com/reference/android/os/StrictMode.html">
   *     Strict Mode | Android Developers</a>
   */
  private fun enforceVMStrictMode() = StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
      .detectActivityLeaks()
      .detectLeakedClosableObjects()
      .detectLeakedSqlLiteObjects()
      .detectLeakedRegistrationObjects()
      .penaltyLog()
      .apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          detectCleartextNetwork()
          detectFileUriExposure()
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            detectContentUriWithoutPermission()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
              detectNonSdkApiUsage()
            }
          }

        }
      }
      .build())
}
