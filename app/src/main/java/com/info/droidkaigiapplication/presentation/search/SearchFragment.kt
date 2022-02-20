package com.info.droidkaigiapplication.presentation.search

import android.app.SearchManager
import android.content.Context
import android.database.CrossProcessCursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.FragmentSearchBinding
import com.info.droidkaigiapplication.presentation.DataBindingFragment
import com.info.droidkaigiapplication.presentation.search.submit.OnQueryTextSubmitListener
import com.info.droidkaigiapplication.presentation.search.suggestion.OnSuggestionClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment
    : DataBindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModel()

    override fun getLayoutResId() = R.layout.fragment_search

    override fun setDataBinding(dataBinding: FragmentSearchBinding) {
        with(dataBinding) {
            context = requireContext()
            vm = viewModel
            lifecycleOwner = this@SearchFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchMenu = menu.findItem(R.id.search) as MenuItem
        val searchView = (searchMenu.actionView as SearchView).apply {
            maxWidth = Int.MAX_VALUE
        }

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        if (viewModel.keyword.isNotEmpty()) {
            searchView.onActionViewExpanded()
            searchView.setQuery(viewModel.keyword, false)
        }

        searchView.setOnQueryTextListener(OnQueryTextSubmitListener{ query ->
            val searchRecentSuggestions = SearchRecentSuggestions(context,
                    SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
            searchRecentSuggestions.saveRecentQuery(viewModel.keyword, null)
            searchView.clearFocus()
            query?.let {
                viewModel.search(query)
            }
        })

        searchView.setOnSuggestionListener(OnSuggestionClickListener{ position ->
            val item = searchView.suggestionsAdapter.getItem(position) as CrossProcessCursor
            val selectedSuggestionKeyword = item.getString(2)
            searchView.setQuery(selectedSuggestionKeyword, false)
            viewModel.search(selectedSuggestionKeyword)
        })
    }

    private fun setActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(dataBinding.toolbar)
        dataBinding.toolbar.title = getString(R.string.app_name)
    }
}