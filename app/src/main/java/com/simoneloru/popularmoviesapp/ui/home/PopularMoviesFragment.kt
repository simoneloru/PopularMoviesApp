package com.simoneloru.popularmoviesapp.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.simoneloru.popularmoviesapp.BuildConfig
import com.simoneloru.popularmoviesapp.R
import com.simoneloru.popularmoviesapp.data.model.Movie
import com.simoneloru.popularmoviesapp.databinding.MovieDetailBinding
import com.simoneloru.popularmoviesapp.databinding.PopularmoviesLayoutBinding
import com.simoneloru.popularmoviesapp.ui.MainActivity
import com.simoneloru.popularmoviesapp.ui.home.adapter.PopuplarMoviesAdapter
import com.simoneloru.popularmoviesapp.ui.home.adapter.RecyclerViewHomeClickListener
import com.simoneloru.popularmoviesapp.ui.home.viewmodel.PopularMoviesViewModel
import com.simoneloru.popularmoviesapp.util.ResourceUtil
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class PopularMoviesFragment: Fragment(), RecyclerViewHomeClickListener {
    private lateinit var binding: PopularmoviesLayoutBinding
    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels()
    private val popuplarMoviesAdapter: PopuplarMoviesAdapter by lazy { PopuplarMoviesAdapter(requireContext(), this@PopularMoviesFragment) }

    var totalPages = 0
    var counter = 1
    var present_state = POPULAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopularmoviesLayoutBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            adapter = popuplarMoviesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) { //1 for down
                        if(counter <= totalPages){
                            if (present_state.equals(POPULAR)) {
                                popularMoviesViewModel.fetchPopular(BuildConfig.TMDB_API_TOKEN)
                            }
                            ++counter
                        }
                    }
                }
            })
        }

        popularMoviesViewModel.fetchPopular(BuildConfig.TMDB_API_TOKEN)
        (activity as MainActivity?)!!.changeTitle("Popular Movies")
        observeUI()


        return binding.root
    }

    private fun observeUI() {
        popularMoviesViewModel.moviePopular.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceUtil.Success -> {
                    binding.progress.visibility = View.GONE
                    val value = it.data!!
                    totalPages = value.totalPages
                    val data = value.movies
                    popuplarMoviesAdapter.submitList(data!!)
                }
                is ResourceUtil.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is ResourceUtil.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }

        }

    }

    override fun clickOnItem(data: Movie, card: View) {
        Log.e("personal", ""+data.title)
        //AlertDialog.Builder(card.context).setMessage("prova").show()
        var dialog = Dialog(card.context)
        var mdb = MovieDetailBinding.inflate(LayoutInflater.from(card.context))
        mdb.apply {
            doc = data
            executePendingBindings()
        }
        dialog.setContentView(mdb.root)
        dialog.show();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        const val POPULAR = "popular"
    }
}