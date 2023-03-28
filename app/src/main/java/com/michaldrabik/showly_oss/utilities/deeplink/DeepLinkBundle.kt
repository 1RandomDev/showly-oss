package com.michaldrabik.showly_oss.utilities.deeplink

import com.michaldrabik.ui_model.Movie
import com.michaldrabik.ui_model.Show

data class DeepLinkBundle(
  val show: Show? = null,
  val movie: Movie? = null
) {

  companion object {
    val EMPTY = DeepLinkBundle()
  }
}
