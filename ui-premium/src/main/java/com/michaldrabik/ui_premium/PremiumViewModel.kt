package com.michaldrabik.ui_premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaldrabik.repository.settings.SettingsRepository
import com.michaldrabik.ui_base.utilities.events.Event
import com.michaldrabik.ui_base.utilities.events.MessageEvent
import com.michaldrabik.ui_base.utilities.extensions.SUBSCRIBE_STOP_TIMEOUT
import com.michaldrabik.ui_base.viewmodel.ChannelsDelegate
import com.michaldrabik.ui_base.viewmodel.DefaultChannelsDelegate
import com.michaldrabik.ui_premium.PremiumUiEvent.HighlightItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository,
) : ViewModel(), ChannelsDelegate by DefaultChannelsDelegate() {

  private val purchasePendingState = MutableStateFlow(false)
  private val loadingState = MutableStateFlow(false)
  private val finishEvent = MutableStateFlow<Event<Boolean>?>(null)

  fun sendErrorMessage() {
    viewModelScope.launch {
      messageChannel.send(MessageEvent.Info(R.string.textPurchaseNotAvailable))
    }
  }

  fun unlockAndFinish() {
    viewModelScope.launch {
      settingsRepository.isPremium = true
      messageChannel.send(MessageEvent.Info(R.string.textPurchaseThanks))
      finishEvent.value = Event(true)
    }
  }

  fun highlightItem(item: com.michaldrabik.ui_model.PremiumFeature) {
    viewModelScope.launch {
      delay(300)
      eventChannel.send(HighlightItem(item))
    }
  }

  val uiState = combine(
    purchasePendingState,
    loadingState,
    finishEvent
  ) { s2, s3, s4 ->
    PremiumUiState(
      isPurchasePending = s2,
      isLoading = s3,
      onFinish = s4
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(SUBSCRIBE_STOP_TIMEOUT),
    initialValue = PremiumUiState()
  )
}
