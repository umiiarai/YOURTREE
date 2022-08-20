package com.example.yourtree;

import android.graphics.ImageDecoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link studytreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class studytreeFragment extends Fragment {

    TextView text;
    ImageButton tree;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public studytreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment studytreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static studytreeFragment newInstance(String param1, String param2) {
        studytreeFragment fragment = new studytreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void StudyTree(int m){
        if(m >= 0 && m <= 10){}
        if(m > 10 && m <= 30){}
        if(m > 30 && m <= 60){}
        if(m > 60){}
    }

    public void StudyCheck(int i, int j){
        int n = i-j;
        if(n > 0) {
            text.setText("오늘은 어제보다"+n+"시간 더 많이 공부했어요!");
        }
        if(n < 0) {
            text.setText("오늘은 어제보다"+n+"시간 더 적게 공부했어요!");
        }
        if(n == 0) {
            text.setText("오늘은 어제와 같은 시간 공부했어요!");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studytree, container, false);
        text = v.findViewById(R.id.text);
        tree = v.findViewById(R.id.main_tree);
        text.setText("성공");
        // Inflate the layout for this fragment
        return v;
    }
}