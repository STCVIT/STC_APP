package com.mstc.mstcapp.ui

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.DialogProjectIdeaBinding
import com.mstc.mstcapp.model.ProjectIdea
import com.mstc.mstcapp.util.RetrofitService
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


private const val TAG = "ProjectIdeaFragment"

class ProjectIdeaFragment : BottomSheetDialogFragment() {
    lateinit var binding: DialogProjectIdeaBinding
    private val scheduler: ScheduledExecutorService by lazy { Executors.newSingleThreadScheduledExecutor() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogProjectIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindState()
    }

    private fun DialogProjectIdeaBinding.bindState() {
        post.setOnClickListener {
            ProjectIdea(
                etName.text.toString(),
                etPhone.text.toString(),
                etEmail.text.toString(),
                etIdea.text.toString(),
                etDescription.text.toString()
            ).apply {
                when {
                    name.isEmpty() -> etName.error = "Cannot be empty!"
                    phone.isEmpty() -> etPhone.error = "Cannot be empty!"
                    phone.length != 10 -> etPhone.error = "Invalid Phone Number"
                    email.isEmpty() -> etEmail.error = "Cannot be empty!"
                    !email.matches(
                        Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
                    ) -> etEmail.error = "Invalid Email Address"
                    idea.isEmpty() -> etIdea.error = "Cannot be empty!"
                    description.isEmpty() -> etDescription.error =
                        "Cannot be empty!"
                    else -> lifecycleScope.launch { postData(this@apply) }
                }
            }
        }
    }

    private suspend fun postData(projectIdeaModel: ProjectIdea) {
        animateUpload()
        try {
            val service: RetrofitService = RetrofitService.create()
            val response = service.postIdea(projectIdeaModel)
            if (response.isSuccessful) {
                Log.d(TAG, "postData() returned: ${response.body()}")
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
                throw Error(response.message())
            }
        } catch (e: Exception) {
            Log.e(TAG, "postData: ", e)
            Toast.makeText(context, "Could not post idea! Try Again", Toast.LENGTH_SHORT).show()
            binding.apply {
                post.text = getString(R.string.post)
                post.isEnabled = true
                post.strokeColor =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                scheduler.shutdown()
            }
        }
    }

    private fun animateUpload() {
        binding.apply {
            post.isEnabled = false
            post.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
            var count = 0
            try {
                scheduler.scheduleWithFixedDelay({
                    post.text = "POSTING${".".repeat((++count % 3) + 1)}"
                }, 0, 500, TimeUnit.MILLISECONDS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}