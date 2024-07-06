package com.example.samachar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samachar.R
import com.example.samachar.adapters.NewsAdapter
import com.example.samachar.databinding.FragmentFavoritesBinding
import com.example.samachar.ui.NewsActivity
import com.example.samachar.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavoritesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)




        newsViewModel = (activity as NewsActivity).newsViewModel
        setupFavoritesRecyclerView()


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_favouritesFragment_to_articleFragment, bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Removed from article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        newsViewModel.addToFavorite(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        newsViewModel.getFavoritesNews().observe(viewLifecycleOwner) { articles ->
            newsAdapter.differ.submitList(articles)
        }
    }
    private fun setupFavoritesRecyclerView() {

        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }



}