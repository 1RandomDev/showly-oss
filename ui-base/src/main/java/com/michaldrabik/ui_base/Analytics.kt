package com.michaldrabik.ui_base

import com.michaldrabik.ui_model.DiscoverFilters
import com.michaldrabik.ui_model.Episode
import com.michaldrabik.ui_model.Movie
import com.michaldrabik.ui_model.Show

// Analytics removed, do nothing with log events
object Analytics {
  fun logShowDetailsDisplay(show: Show) {}

  fun logMovieDetailsDisplay(movie: Movie) {}

  fun logShowAddToMyShows(show: Show) {}

  fun logMovieAddToMyMovies(movie: Movie) {}

  fun logShowAddToWatchlistShows(show: Show) {}

  fun logMovieAddToWatchlistMovies(movie: Movie) {}

  fun logShowAddToArchive(show: Show) {}

  fun logMovieAddToArchive(movie: Movie) {}

  fun logShowTrailerClick(show: Show) {}

  fun logMovieTrailerClick(movie: Movie) {}

  fun logShowCommentsClick(show: Show) {}

  fun logMovieCommentsClick(movie: Movie) {}

  fun logShowShareClick(show: Show) {}

  fun logMovieShareClick(movie: Movie) {}

  fun logShowGalleryClick(idTrakt: Long) {}

  fun logMovieGalleryClick(idTrakt: Long) {}

  fun logShowQuickProgress(show: Show) {}

  fun logMovieRated(movie: Movie, rating: Int) {}

  fun logEpisodeRated(idTrakt: Long, episode: Episode, rating: Int) {}

  fun logDiscoverFiltersApply(filters: DiscoverFilters) {}

  fun logDiscoverMoviesFiltersApply(filters: DiscoverFilters) {}

  // In App Rate

  fun logInAppRateDisplayed() {}

  fun logInAppRateDecision(isYes: Boolean) {}

  // Trakt

  fun logTraktLogin() {}

  fun logTraktLogout() {}

  fun logTraktFullSyncSuccess(import: Boolean, export: Boolean) {}

  fun logTraktQuickSyncSuccess(count: Int) {}

  // Settings

  fun logSettingsTraktQuickSync(enabled: Boolean) {}

  fun logSettingsTraktQuickRemove(enabled: Boolean) {}

  fun logSettingsTraktQuickRate(enabled: Boolean) {}

  fun logSettingsRecentlyAddedAmount(amount: Long) {}

  fun logSettingsAnnouncements(enabled: Boolean) {}

  fun logSettingsSpecialSeasons(enabled: Boolean) {}

  fun logSettingsProgressUpcoming(enabled: Boolean) {}

  fun logSettingsMoviesEnabled(enabled: Boolean) {}

  fun logSettingsNewsEnabled(enabled: Boolean) {}

  fun logSettingsStreamingsEnabled(enabled: Boolean) {}

  fun logSettingsWidgetsTitlesEnabled(enabled: Boolean) {}

  fun logSettingsWhenToNotify(value: String) {}

  fun logSettingsLanguage(value: String) {}

  fun logSettingsTheme(value: Int) {}

  fun logSettingsPremium(value: Boolean) {}

  fun logSettingsWidgetsTheme(value: Int) {}

  fun logSettingsCountry(value: String) {}

  fun logSettingsProgressType(value: String) {}

  fun logInAppUpdate(versionName: String, versionCode: Long) {}

  fun logUnsupportedSubscriptions() {}

  fun logExportHistory(episodesCount: Int, moviesCount: Int, retryCount: Int) {}

  fun logQuickExportHistory(episodesCount: Int, moviesCount: Int, retryCount: Int) {}
}
