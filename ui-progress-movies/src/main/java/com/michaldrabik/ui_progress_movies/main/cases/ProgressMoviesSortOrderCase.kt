package com.michaldrabik.ui_progress_movies.main.cases

import com.michaldrabik.repository.SettingsRepository
import com.michaldrabik.ui_model.Settings
import com.michaldrabik.ui_model.SortOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressMoviesSortOrderCase @Inject constructor(
  private val settingsRepository: SettingsRepository,
) {

  suspend fun setSortOrder(sortOrder: SortOrder) {
    val settings = settingsRepository.load()
    settingsRepository.update(settings.copy(progressMoviesSortBy = sortOrder))
  }

  suspend fun loadSortOrder(): SortOrder {
    if (!settingsRepository.isInitialized()) {
      return Settings.createInitial().progressMoviesSortBy
    }
    return settingsRepository.load().progressMoviesSortBy
  }
}
