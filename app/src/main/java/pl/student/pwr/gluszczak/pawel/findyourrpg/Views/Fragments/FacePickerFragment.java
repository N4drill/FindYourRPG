package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.FacePickerAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

public class FacePickerFragment extends AppCompatDialogFragment {
    private static final String TAG = "FacePickerFragment";
    private static final String ARG_FACE_ID = "face";

    public static final String EXTRA_FACE = "pl.student.pwr.gluszczak.pawel.findyourrpg.date"; //????

    private GridView mGridView;
    private int imageResource = R.drawable.face_placeholder;
    private View clickedView;


    public interface onCompleteListener {
        void sendInput(int resource);
    }

    public onCompleteListener mOnCompleteListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_face_picker, null);

        mGridView = view.findViewById(R.id.dialog_face_picker_grid);
        mGridView.setAdapter(new FacePickerAdapter(getActivity()));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clickedView == null) {
                    view.setBackground(getActivity().getDrawable(R.drawable.border_your_event));
                    clickedView = view;
                } else {
                    clickedView.setBackground(null);
                    view.setBackground(getActivity().getDrawable(R.drawable.border_your_event));
                    clickedView = view;
                }

                imageResource = (int) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemClick: Picked resource =>" + imageResource);
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose your avatar")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOnCompleteListener.sendInput(imageResource);
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnCompleteListener = (onCompleteListener) getActivity();
        } catch (ClassCastException e) {
            Log.d(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }
}
