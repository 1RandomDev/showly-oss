package com.michaldrabik.showly2.ui.common.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.michaldrabik.showly2.Config.IMAGE_FADE_DURATION_MS
import com.michaldrabik.showly2.Config.TVDB_IMAGE_BASE_FANART_URL
import com.michaldrabik.showly2.Config.TVDB_IMAGE_BASE_POSTER_URL
import com.michaldrabik.showly2.Config.TVDB_IMAGE_BASE_URL
import com.michaldrabik.showly2.R
import com.michaldrabik.showly2.model.Image.Status.UNAVAILABLE
import com.michaldrabik.showly2.model.Image.Status.UNKNOWN
import com.michaldrabik.showly2.model.ImageType.POSTER
import com.michaldrabik.showly2.ui.discover.recycler.ListItem
import com.michaldrabik.showly2.utilities.extensions.dimenToPx
import com.michaldrabik.showly2.utilities.extensions.screenWidth
import com.michaldrabik.showly2.utilities.extensions.visible
import com.michaldrabik.showly2.utilities.extensions.withFailListener
import com.michaldrabik.showly2.utilities.extensions.withSuccessListener

abstract class ShowView<Item : ListItem> : FrameLayout {

  companion object {
    const val ASPECT_RATIO = 1.4705
  }

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  private val cornerRadius by lazy { context.dimenToPx(R.dimen.showTileCorner) }
  private val gridPadding by lazy { context.dimenToPx(R.dimen.gridPadding) }
  private val gridSpan by lazy { resources.getInteger(R.integer.discoverGridSpan).toFloat() }

  private val width by lazy { (screenWidth().toFloat() - (2.0 * gridPadding)) / gridSpan }
  private val height by lazy { width * ASPECT_RATIO }

  protected abstract val imageView: ImageView
  protected abstract val placeholderView: ImageView

  open fun bind(
    item: Item,
    missingImageListener: (Item, Boolean) -> Unit,
    itemClickListener: (Item) -> Unit
  ) {
    layoutParams = LayoutParams((width * item.image.type.spanSize.toFloat()).toInt(), height.toInt())
  }

  protected open fun loadImage(item: Item, missingImageListener: (Item, Boolean) -> Unit) {
    if (item.isLoading) return

    if (item.image.status == UNAVAILABLE) {
      placeholderView.visible()
      return
    }

    val base = when {
      item.image.type == POSTER -> TVDB_IMAGE_BASE_POSTER_URL
      else -> TVDB_IMAGE_BASE_FANART_URL
    }
    val url = when {
      item.image.status == UNKNOWN -> "${base}${item.show.ids.tvdb}-1.jpg"
      else -> "$TVDB_IMAGE_BASE_URL${item.image.fileUrl}"
    }

    Glide.with(this)
      .load(url)
      .transform(CenterCrop(), RoundedCorners(cornerRadius))
      .transition(withCrossFade(IMAGE_FADE_DURATION_MS))
      .withSuccessListener { onImageLoadSuccess() }
      .withFailListener { onImageLoadFail(item, missingImageListener) }
      .into(imageView)
  }

  protected open fun onImageLoadSuccess() = Unit

  protected open fun onImageLoadFail(item: Item, missingImageListener: (Item, Boolean) -> Unit) {
    val force = item.image.status != UNAVAILABLE
    missingImageListener(item, force)
  }
}