package com.goestech.goesplayer.view.home.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goestech.goesplayer.databinding.CategoryListFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryListFragment : Fragment() {

    private val categoryListType: CategoryListType by lazy {
        arguments?.get("type") as? CategoryListType ?: throw IllegalArgumentException("CategoryType not found")
    }
    private val viewModel: CategoryListViewModel by viewModel(parameters = { parametersOf(categoryListType) })
    private val adapter: CategoryListAdapter = CategoryListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        CategoryListFragmentBinding.inflate(inflater, container, false).apply {
            categoryListFragmentTitle.text = getText(categoryListType.title)
            categoryListFragmentList.adapter = adapter
            observeViewModel()
            viewModel.loadList()
        }.root

    private fun observeViewModel() {
        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    companion object {
        fun newInstance(type: CategoryListType): CategoryListFragment =
            CategoryListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("type", type)
                }
            }
    }
}