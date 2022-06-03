package com.michaldrabik.ui_show.sections.people

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.michaldrabik.common.Mode
import com.michaldrabik.ui_base.BaseFragment
import com.michaldrabik.ui_base.utilities.events.Event
import com.michaldrabik.ui_base.utilities.extensions.addDivider
import com.michaldrabik.ui_base.utilities.extensions.launchAndRepeatStarted
import com.michaldrabik.ui_base.utilities.extensions.navigateToSafe
import com.michaldrabik.ui_base.utilities.extensions.onClick
import com.michaldrabik.ui_base.utilities.extensions.trimWithSuffix
import com.michaldrabik.ui_base.utilities.extensions.visibleIf
import com.michaldrabik.ui_model.Person
import com.michaldrabik.ui_model.Person.Department
import com.michaldrabik.ui_model.Show
import com.michaldrabik.ui_navigation.java.NavigationArgs
import com.michaldrabik.ui_people.details.PersonDetailsBottomSheet
import com.michaldrabik.ui_people.list.PeopleListBottomSheet
import com.michaldrabik.ui_show.R
import com.michaldrabik.ui_show.ShowDetailsEvent
import com.michaldrabik.ui_show.ShowDetailsViewModel
import com.michaldrabik.ui_show.sections.people.recycler.ActorsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_show_details.*
import kotlinx.android.synthetic.main.fragment_show_details_people.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ShowDetailsPeopleFragment : BaseFragment<ShowDetailsPeopleViewModel>(R.layout.fragment_show_details_people) {

  override val navigationId = R.id.showDetailsFragment

  private val parentViewModel by viewModels<ShowDetailsViewModel>({ requireParentFragment() })
  override val viewModel by viewModels<ShowDetailsPeopleViewModel>()

  private var actorsAdapter: ActorsAdapter? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView()
    launchAndRepeatStarted(
      { parentViewModel.parentEvents.collect { viewModel.handleEvent(it) } },
      { viewModel.uiState.collect { render(it) } },
      { viewModel.eventFlow.collect { handleEvent(it) } },
      doAfterLaunch = { viewModel.loadLastPerson() }
    )
  }

  private fun setupView() {
    actorsAdapter = ActorsAdapter().apply {
      itemClickListener = { viewModel.loadPersonDetails(it) }
    }
    showDetailsActorsRecycler.apply {
      setHasFixedSize(true)
      adapter = actorsAdapter
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      addDivider(R.drawable.divider_horizontal_list, LinearLayoutManager.HORIZONTAL)
    }
  }

  private fun openPersonSheet(show: Show, person: Person) {
    val bundle = PersonDetailsBottomSheet.createBundle(person, show.ids.trakt)
    navigateToSafe(R.id.actionShowDetailsFragmentToPerson, bundle)
  }

  private fun openPeopleSheet(event: ShowDetailsEvent.OpenPeopleSheet) {
    val (show, people, department) = event

    if (people.isEmpty()) return
    if (people.size == 1) {
      viewModel.loadPersonDetails(people.first())
      return
    }

    clearFragmentResultListener(NavigationArgs.REQUEST_PERSON_DETAILS)
    val title = requireParentFragment().showDetailsTitle.text.toString()
    val bundle = PeopleListBottomSheet.createBundle(show.ids.trakt, title, Mode.SHOWS, department)
    navigateToSafe(R.id.actionShowDetailsFragmentToPeopleList, bundle)
  }

  private fun render(uiState: ShowDetailsPeopleUiState) {
    with(uiState) {
      actors?.let {
        if (actorsAdapter?.itemCount != 0) return@let
        actorsAdapter?.setItems(it)
        showDetailsActorsRecycler.visibleIf(actors.isNotEmpty(), gone = false)
        showDetailsActorsEmptyView.visibleIf(actors.isEmpty())
      }
      crew?.let { renderCrew(it) }
      isLoading.let {
        showDetailsActorsProgress.visibleIf(it)
      }
    }
  }

  private fun renderCrew(crew: Map<Department, List<Person>>) {

    fun renderPeople(
      labelView: View,
      valueView: TextView,
      people: List<Person>,
      department: Department
    ) {
      labelView.visibleIf(people.isNotEmpty())
      valueView.visibleIf(people.isNotEmpty())
      valueView.text = people
        .take(2)
        .joinToString("\n") { it.name.trimWithSuffix(20, "…") }
        .plus(if (people.size > 2) "\n…" else "")
      valueView.onClick { viewModel.loadPeopleList(people, department) }
    }

    if (!crew.containsKey(Department.DIRECTING)) {
      return
    }

    val directors = crew[Department.DIRECTING] ?: emptyList()
    val writers = crew[Department.WRITING] ?: emptyList()
    val sound = crew[Department.SOUND] ?: emptyList()

    renderPeople(showDetailsDirectingLabel, showDetailsDirectingValue, directors, Department.DIRECTING)
    renderPeople(showDetailsWritingLabel, showDetailsWritingValue, writers, Department.WRITING)
    renderPeople(showDetailsMusicLabel, showDetailsMusicValue, sound, Department.SOUND)
  }

  private fun handleEvent(event: Event<*>) {
    when (event) {
      is ShowDetailsEvent.OpenPersonSheet -> openPersonSheet(event.show, event.person)
      is ShowDetailsEvent.OpenPeopleSheet -> openPeopleSheet(event)
    }
  }

  override fun setupBackPressed() = Unit

  override fun onDestroyView() {
    actorsAdapter = null
    super.onDestroyView()
  }
}
