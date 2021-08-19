package com.mstc.mstcapp.ui

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mstc.mstcapp.databinding.FragmentProjectIdeaBinding
import com.mstc.mstcapp.model.ProjectIdea
import com.mstc.mstcapp.util.RetrofitService
import kotlinx.coroutines.launch


private const val TAG = "ProjectIdeaFragment"

class ProjectIdeaFragment : BottomSheetDialogFragment() {
    lateinit var progressDialog: ProgressDialog
    lateinit var binding: FragmentProjectIdeaBinding

    companion object {
        fun newInstance() = ProjectIdeaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProjectIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        binding.apply {
            post.setOnClickListener {
                val projectIdeaModel = ProjectIdea(
                    name.text.toString(),
                    phone.text.toString(),
                    email.text.toString(),
                    idea.text.toString(),
                    description.text.toString()
                )
                if (projectIdeaModel.name.isEmpty())
                    name1.error = "Cannot be empty!"
                else if (projectIdeaModel.phone.isEmpty())
                    phone1.error = "Cannot be empty!"
                else if (projectIdeaModel.phone.length != 10)
                    phone1.error = "Invalid Phone Number"
                else if (projectIdeaModel.email.isEmpty())
                    email1.error = "Cannot be empty!"
                else if (!projectIdeaModel.email.matches(Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")))
                    email1.error = "Invalid Email Address"
                else if (projectIdeaModel.idea.isEmpty())
                    idea1.error = "Cannot be empty!"
                else if (projectIdeaModel.description.isEmpty())
                    description1.error = "Cannot be empty!"
                else
                    lifecycleScope.launch { postData(projectIdeaModel) }
            }
        }
    }

    private suspend fun postData(projectIdeaModel: ProjectIdea) {
        progressDialog.show()
        val service: RetrofitService = RetrofitService.create()
        val response = service.postIdea(projectIdeaModel)
        if (response.isSuccessful) {
            Log.d(TAG, "postData() returned: ${response.body()}")
            progressDialog.dismiss()
            dismiss()
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Idea Posted Successfully")
                .setMessage(
                    "Your idea has been posted successfully! " +
                            "Please feel free to check out the resources while we contact you."
                )
                .setPositiveButton("Dismiss") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .show()
        } else {
            Toast.makeText(context, "Could not post idea! Try Again", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "postData: Failed! ${response.code()}")
        }

    }
}