package com.mstc.mstcapp.ui.resources.roadmap

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mstc.mstcapp.databinding.FragmentRoadmapBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.resource.Roadmap

private const val TAG = "RoadmapFragment"

class RoadmapFragment(val domain: String) : Fragment() {
    private lateinit var binding: FragmentRoadmapBinding
    private lateinit var viewModel: RoadmapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRoadmapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RoadmapViewModel::class.java)
        binding.apply {
            viewModel.getRoadmap(domain)
                .observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is Result.Loading -> {
                            retryButton.visibility = GONE
                            swipeRefreshLayout.isRefreshing = true
                            result.data?.let {
                                setImage(it.image)
                            }
                        }
                        is Result.Success<Roadmap> -> {
                            setImage(result.data.image)
                        }
                        else -> {
                            retryButton.visibility = VISIBLE
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                })
            retryButton.setOnClickListener { viewModel.refreshRoadmap(domain) }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshRoadmap(domain)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setImage(image: String) {
        binding.apply{
            Glide.with(requireContext())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        retryButton.visibility = VISIBLE
                        swipeRefreshLayout.isRefreshing = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        retryButton.visibility = GONE
                        swipeRefreshLayout.isRefreshing = false
                        return false
                    }
                })
                .into(roadmapImage)
        }
    }
}