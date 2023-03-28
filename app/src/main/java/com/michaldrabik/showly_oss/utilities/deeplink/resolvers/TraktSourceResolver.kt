package com.michaldrabik.showly_oss.utilities.deeplink.resolvers

import com.michaldrabik.showly_oss.utilities.deeplink.DeepLinkResolver
import com.michaldrabik.showly_oss.utilities.deeplink.DeepLinkSource
import com.michaldrabik.ui_model.IdSlug

class TraktSourceResolver : SourceResolver {

  override fun resolve(linkPath: List<String>): DeepLinkSource? {
    if (linkPath.size < 2 || !(linkPath[0] == DeepLinkResolver.TRAKT_TYPE_TV || linkPath[0] == DeepLinkResolver.TRAKT_TYPE_MOVIE)) {
      return null
    }

    return DeepLinkSource.TraktSource(IdSlug(linkPath[1]), linkPath[0])
  }
}
