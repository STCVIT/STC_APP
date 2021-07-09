package com.mstc.mstcapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.ProjectIdeaModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProjectIdeaFragment extends BottomSheetDialogFragment {
    private static final String TAG = "ProjectIdeaFragment";
    private Context context;
    private TextInputEditText name, email, phone, idea, description;
    private TextInputLayout name1, email1, phone1, idea1, description1;
    private ProgressDialog progressDialog;

    public ProjectIdeaFragment() {
    }

    public static ProjectIdeaFragment newInstance() {
        return new ProjectIdeaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_idea, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        idea = view.findViewById(R.id.idea);
        description = view.findViewById(R.id.description);
        name1 = view.findViewById(R.id.name1);
        email1 = view.findViewById(R.id.email1);
        phone1 = view.findViewById(R.id.phone1);
        idea1 = view.findViewById(R.id.idea1);
        description1 = view.findViewById(R.id.description1);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Posting Idea...");
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) name1.setError("Cannot be empty!");
                else name1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) phone1.setError("Invalid Phone Number!");
                else phone1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 10) phone1.setError("Invalid Phone Number!");
                else phone1.setError(null);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))
                    email1.setError("Invalid Email Address");
                else email1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        view.findViewById(R.id.post).setOnClickListener(v -> {
            ProjectIdeaModel projectIdeaModel = new ProjectIdeaModel(
                    Objects.requireNonNull(name.getText()).toString(),
                    Objects.requireNonNull(phone.getText()).toString(),
                    Objects.requireNonNull(email.getText()).toString(),
                    Objects.requireNonNull(idea.getText()).toString(),
                    Objects.requireNonNull(description.getText()).toString());
            if (projectIdeaModel.getName().length() == 0)
                name1.setError("Cannot be empty!");
            else if (projectIdeaModel.getPhone().length() == 0)
                phone1.setError("Cannot be empty!");
            else if (projectIdeaModel.getPhone().length() != 10)
                phone1.setError("Invalid Phone Number");
            else if (projectIdeaModel.getEmail().length() == 0)
                email1.setError("Cannot be empty!");
            else if (!projectIdeaModel.getEmail().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))
                email1.setError("Invalid Email Address");
            else if (projectIdeaModel.getIdea().length() == 0)
                idea1.setError("Cannot be empty!");
            else if (projectIdeaModel.getDescription().length() == 0)
                description1.setError("Cannot be empty!");
            else
                postData(projectIdeaModel);
        });
        return view;
    }

    private void postData(ProjectIdeaModel projectIdeaModel) {
        progressDialog.show();
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<Map<String, String>> call = retrofitInterface.postIdea(projectIdeaModel);
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call,
                                   @NonNull Response<Map<String, String>> response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse() returned: " + response.body());
                Map<String, String> map = response.body();
                if (map != null)
                    if (Objects.equals(map.get("statusCode"), "201")) {
                        dismiss();
                        new MaterialAlertDialogBuilder(context)
                                .setTitle("Idea Posted Successfully")
                                .setMessage("Your idea has been posted successfully! " +
                                        "Please feel free to check out the resources while we contact you.")
                                .setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss())
                                .show();
                    } else
                        Toast.makeText(context, "Could not post idea! Try Again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}