package com.sepidsa.fortytwocalculator;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sepidsa.fortytwocalculator.data.ConstantContract;

/**
 * Created by Farshid on 5/20/2015.
 */
public class ConstantUseFragment  extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ConstantUseAdapter mConstantUseAdapter;
    private ListView mListView;
    private Button mGotoSelect;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private static final int CONSTANT_LOADER = 0;

    private static final String[] CONSTANT_COLUMNS = {
            ConstantContract.ConstantEntry.TABLE_NAME + "." + ConstantContract.ConstantEntry._ID,
            ConstantContract.ConstantEntry.COLUMN_NAME,
            ConstantContract.ConstantEntry.COLUMN_NUMBER,
    };

    public ConstantUseFragment() {
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONSTANT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mConstantUseAdapter = new ConstantUseAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_constant_use, container, false);


        mListView = (ListView) rootView.findViewById(R.id.listview_constant);
        mGotoSelect = (Button) rootView.findViewById(R.id.button_goto_select);
        mGotoSelect.setTextColor(((MainActivity)getActivity()).getAccentColorCode());
        mGotoSelect.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "yekan.ttf"));
        TextView empty = (TextView) rootView.findViewById(R.id.empty_list);
        mListView.setEmptyView(empty);
        mListView.setAdapter(mConstantUseAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, final View view, final int position, long l) {
                TextView resultView = (TextView) view.findViewById(R.id.constant_number);
                String result = resultView.getText().toString();
                ((MainActivity) getActivity()).addNumberToCalculation(result);
                ((MainActivity) getActivity()).switchToMainFragment();
                dismiss();


            }

        });

        mGotoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ConstantSelectFragment constantSelectDialog = new ConstantSelectFragment();
                constantSelectDialog.show(fm, "fragment_constant_select");

            }
        });

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mConstantUseAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mConstantUseAdapter.swapCursor(null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                ConstantContract.ConstantEntry.CONTENT_URI,
                CONSTANT_COLUMNS,
                ConstantContract.ConstantEntry.COLUMN_SELECTED + "=?",
                new String[]{"1"},
                null);
    }

}
