package com.michaldrabik.ui_premium

import com.michaldrabik.ui_base.utilities.events.Event

data class PremiumUiState(
  val isLoading: Boolean = false,
  val isPurchasePending: Boolean = false,
  val onFinish: Event<Boolean>? = null,
)
