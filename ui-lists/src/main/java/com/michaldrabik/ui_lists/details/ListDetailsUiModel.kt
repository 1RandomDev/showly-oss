package com.michaldrabik.ui_lists.details

import com.michaldrabik.ui_base.UiModel
import com.michaldrabik.ui_base.utilities.ActionEvent
import com.michaldrabik.ui_lists.details.recycler.ListDetailsItem
import com.michaldrabik.ui_model.CustomList

data class ListDetailsUiModel(
  val details: CustomList? = null,
  val items: List<ListDetailsItem>? = null,
  val resetScroll: ActionEvent<Boolean>? = null,
  val deleteEvent: ActionEvent<Boolean>? = null
) : UiModel() {

  override fun update(newModel: UiModel) =
    (newModel as ListDetailsUiModel)
      .copy(
        details = newModel.details ?: details,
        items = newModel.items?.toList() ?: items,
        resetScroll = newModel.resetScroll ?: resetScroll,
        deleteEvent = newModel.deleteEvent ?: deleteEvent
      )
}
