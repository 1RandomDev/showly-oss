package com.michaldrabik.showly_oss.utilities.deeplink.resolvers

import com.michaldrabik.showly_oss.utilities.deeplink.DeepLinkSource

interface SourceResolver {
  fun resolve(linkPath: List<String>): DeepLinkSource?
}
