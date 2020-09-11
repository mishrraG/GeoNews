package com.example.geo_news;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.CLIPBOARD_SERVICE;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextView share, copy, open;
    private String receivedUrl, receivedSource;
    private ClipboardManager clipboardManager;
    private ClipData clipData;



    public BottomSheetFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_detail,container,false);
        share = view.findViewById(R.id.share_tv);
        copy = view.findViewById(R.id.copy_tv);
        open = view.findViewById(R.id.openinbrowser_tv);

        assert getArguments() != null;
        receivedUrl = getArguments().getString("durl");
        receivedSource = getArguments().getString("dsource");

        clipboardManager = (ClipboardManager)
                getContext().getSystemService(CLIPBOARD_SERVICE);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, receivedSource);
                intent.putExtra(Intent.EXTRA_TEXT, receivedUrl + "\n\nShared from SM news.");
                startActivity(Intent.createChooser(intent, "Share link:"));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipData = ClipData.newPlainText("url", receivedUrl);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Opening in browser!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(receivedUrl));
                startActivity(intent);
            }
        });

        return view;
    }
}