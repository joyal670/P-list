package com.ashtechlabs.teleporter.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;

import java.util.ArrayList;

/**
 * Created by VIDHU on 2/10/2017.
 */

public class SingleChoiceActivitytDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    Dialog dialog;
    private ListView lvList;
    private SingleChoiceItemDialogListener singleChoiceItemDialogListener;
    private String title;
    private ArrayList<String> listArray = new ArrayList<String>();
    private int selectedPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            title = getArguments().getString("title", "");
            listArray = getArguments().getStringArrayList("list_array");
            selectedPosition = getArguments().getInt("selected_position",0);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleChoiceItemDialogListener) {
            singleChoiceItemDialogListener = (SingleChoiceItemDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTDashFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        singleChoiceItemDialogListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_fragment_single_choise);
        dialog.show();

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        lvList = (ListView) dialog.findViewById(R.id.lvList);
        lvList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvList.setAdapter(new SingleListItemAdapter(getActivity(), listArray));
        lvList.setItemChecked(selectedPosition, true);
        lvList.setOnItemClickListener(this);

        return dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.dismiss();
        singleChoiceItemDialogListener.setOnSelectItem(listArray.get(position),position, this.getTag());
    }

}
