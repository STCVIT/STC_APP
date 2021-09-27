package com.mstc.mstcapp.ui.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.FragmentResourcesBinding
import com.mstc.mstcapp.model.Domain
import com.mstc.mstcapp.util.ClickListener
import com.mstc.mstcapp.util.RecyclerTouchListener
import java.util.*

private const val TAG = "DomainsFragment"

class DomainsFragment : Fragment() {

    private lateinit var binding: FragmentResourcesBinding
    private lateinit var list: ArrayList<Domain>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.apply {
            bindRecyclerView()
        }
    }

    private fun FragmentResourcesBinding.bindRecyclerView() {
        recyclerView.apply {
            val domainAdapter = DomainAdapter()
            domainAdapter.submitList(list)
            adapter = domainAdapter
            addOnItemTouchListener(
                RecyclerTouchListener(context,
                    this,
                    object : ClickListener {
                        override fun onClick(view: View?, position: Int) {
                            viewResource(list[position])
                        }

                        override fun onLongClick(view: View?, position: Int) {}
                    })
            )
        }
    }

    private fun viewResource(domain: Domain) {
        findNavController().navigate(
            DomainsFragmentDirections.actionNavigationResourcesToNavigationViewResourceActivity(
                domain
            )
        )
    }

    private fun init() {
        val style1: Int = R.style.resources_red
        val style2: Int = R.style.resources_blue
        val style3: Int = R.style.resources_yellow
        list = arrayListOf(
            Domain("Android", R.drawable.ic_app_dev, style1),
            Domain("Frontend", R.drawable.ic_frontend, style2),
            Domain("Backend", R.drawable.ic_backend, style3),
            Domain("Design", R.drawable.ic_design, style1),
            Domain("Machine Learning", R.drawable.ic_ml, style2),
            Domain("Competitive Coding", R.drawable.ic_cc, style3),
            /**
             *  ADD A NEW RESOURCE HERE
             *
             * To add a new domain, create a new object of [Domain] with the following parameters
             * domain - Same name as in backend
             * drawable - The drawable resource file to display in the list and in collapsing toolbar
             * style - Since this app uses 3 tertiary colors
             * @see Domain
             * @sample Domain("domain_name", drawable_resource, style[1/2/3])
             *
             * <i>This is the only place you have to add a domain</i>
             **/
        )
    }
}