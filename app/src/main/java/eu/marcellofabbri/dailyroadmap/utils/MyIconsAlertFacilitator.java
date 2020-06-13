package eu.marcellofabbri.dailyroadmap.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.constraintlayout.widget.ConstraintLayout;

import eu.marcellofabbri.dailyroadmap.R;

public class MyIconsAlertFacilitator {
    private GridView gridView;
    private AlertDialog.Builder builder;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private Context context;

    public MyIconsAlertFacilitator(ArrayAdapter<CharSequence> arrayAdapter, Context context) {
        this.gridView = new GridView(context);
        this.builder = new AlertDialog.Builder(context);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
        createMyGrid();
        setUpAlert();
    }

    private void createMyGrid() {
        gridView.setAdapter(arrayAdapter);
        gridView.setNumColumns(8);
    }

    private void setUpAlert() {
        builder.setView(gridView);
        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public GridView getGridView() {
        return gridView;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }
}
