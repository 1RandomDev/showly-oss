package com.michaldrabik.ui_base

import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.Buffer
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

object Logger {

  fun record(error: Throwable, source: String) {
    if (error is CancellationException) {
      return
    }
    if (error is IOException && error.message == "Canceled") {
      return
    }
    if (error is HttpException) {
      return
    }
  }

  private fun RequestBody.asString(): String? {
    val buffer = Buffer()
    return try {
      this.writeTo(buffer)
      buffer.readUtf8()
    } catch (error: Throwable) {
      null
    } finally {
      buffer.closeQuietly()
    }
  }
}
