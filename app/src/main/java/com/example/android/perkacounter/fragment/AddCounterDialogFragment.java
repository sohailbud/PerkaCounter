package com.example.android.perkacounter.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.perkacounter.model.Counter;
import com.example.android.perkacounter.activity.MainActivity;
import com.example.android.perkacounter.R;

/**
 * A dialog fragment to add new counters
 */
public class AddCounterDialogFragment extends DialogFragment {

    // instance of the listener
    public NewCounterListener newCounterListener = null;

    private EditText counterNameInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_add_counter, container, false);

        // new counter name input
        counterNameInput = (EditText) view.findViewById(R.id.new_counter_name);

        // ok and cancel actions
        view.findViewById(R.id.add_counter_ok).setOnClickListener(addCounterOkListener);
        view.findViewById(R.id.add_counter_cancel).setOnClickListener(addCounterCancelListener);

        return view;
    }

    /**
     * create the new counter and call the {@link NewCounterListener} callback method
     */
    private View.OnClickListener addCounterOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String newCounterName = counterNameInput.getText().toString();
            Counter counter = new Counter(newCounterName);
            newCounterListener.newCounterListener(counter);
            getDialog().dismiss();

        }
    };

    /**
     * dismiss the dialog when cancel is called
     */
    private View.OnClickListener addCounterCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };

    /**
     * creates a new dialog
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setTitle(getActivity().getResources().getString(R.string.counter_dialog_title));
            return dialog;

        } else return null;
    }

    /**
     * Implemented by {@link MainActivity} and listens for new counter created by {@link AddCounterDialogFragment}
     */
    public interface NewCounterListener {
        void newCounterListener(Counter counter);
    }
}
