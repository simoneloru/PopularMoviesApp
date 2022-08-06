package com.simoneloru.popularmoviesapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simoneloru.popularmoviesapp.data.model.Movie
import com.simoneloru.popularmoviesapp.databinding.MovieDetailCardBinding


class PopuplarMoviesAdapter(val context: Context, val recyclerViewHome: RecyclerViewHomeClickListener) : RecyclerView.Adapter<ViewHolder>(){
    private lateinit var recyclerView: RecyclerView

    var items: List<Movie> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MovieDetailCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!!.get(position)
        item?.let {
            holder.apply {
                bind(item, isLinearLayoutManager())
                itemView.tag = item
            }
        }

        holder.itemView.setOnClickListener {
            recyclerViewHome.clickOnItem(
                item!!,
                holder.itemView
            )
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        if (items == null) {
            return 0
        } else {
            return items!!.size
        }
    }

    fun submitList(itemList: List<Movie>){
        items = itemList!!
        notifyDataSetChanged()
    }

    private fun isLinearLayoutManager() = recyclerView.layoutManager is LinearLayoutManager
}

class ViewHolder(private val binding: MovieDetailCardBinding) : RecyclerView.ViewHolder(binding.root) {



    fun bind(item: Movie, isLinearLayoutManager: Boolean) {
        binding.apply {
            doc = item
            executePendingBindings()
        }
    }


}
interface RecyclerViewHomeClickListener {
    fun clickOnItem(data: Movie, card: View)
}