package com.michaldrabik.showly2.ui.discover.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.michaldrabik.showly2.model.ImageType.FANART
import com.michaldrabik.showly2.model.ImageType.FANART_WIDE
import com.michaldrabik.showly2.model.ImageType.POSTER
import com.michaldrabik.showly2.ui.common.base.BaseAdapter
import com.michaldrabik.showly2.ui.common.views.ShowFanartView
import com.michaldrabik.showly2.ui.common.views.ShowPosterView

class DiscoverAdapter : BaseAdapter<DiscoverListItem>() {

  override fun setItems(newItems: List<DiscoverListItem>) {
    val diffCallback = DiscoverItemDiffCallback(items, newItems)
    val diffResult = DiffUtil.calculateDiff(diffCallback)
    this.items.apply {
      clear()
      addAll(newItems)
    }
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
    POSTER.id -> ViewHolderShow(ShowPosterView(parent.context))
    FANART.id, FANART_WIDE.id -> ViewHolderShow(ShowFanartView(parent.context))
    else -> throw IllegalStateException("Unknown view type.")
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder.itemViewType) {
      POSTER.id ->
        (holder.itemView as ShowPosterView).bind(items[position], missingImageListener, itemClickListener)
      FANART.id, FANART_WIDE.id ->
        (holder.itemView as ShowFanartView).bind(items[position], missingImageListener, itemClickListener)
    }
  }

  override fun getItemViewType(position: Int) = items[position].image.type.id
}