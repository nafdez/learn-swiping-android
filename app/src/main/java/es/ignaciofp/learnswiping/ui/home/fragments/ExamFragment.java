package es.ignaciofp.learnswiping.ui.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.databinding.FragmentExamBinding;

public class ExamFragment extends Fragment {

    private FragmentExamBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExamBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}