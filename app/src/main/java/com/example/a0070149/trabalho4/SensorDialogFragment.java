package com.example.a0070149.trabalho4;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by 0070149 on 04/12/2017.
 */

public class SensorDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener {
    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_SENSOR = "sensor";
    private Activity activity = null;
    private EditText txtNome;
    private EditText txtValor;

    private Sensor sensor;

    public static SensorDialogFragment newInstance(Sensor sensor) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_SENSOR, sensor);
        SensorDialogFragment dialog = new SensorDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensor = (Sensor) getArguments().getSerializable(EXTRA_SENSOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_dialog_sensor, container, false);
        txtNome = (EditText) rootView.findViewById(R.id.txtNome);
        txtNome.requestFocus();
        txtValor = (EditText) rootView.findViewById(R.id.txtValor);
        txtValor.setOnEditorActionListener(this);

        if(sensor==null) sensor = new Sensor(txtNome.getText().toString(), txtValor.getText().toString());
        if (sensor != null) {
            txtNome.setText(sensor.nome);
            txtValor.setText(sensor.valor);

        }
        // Exibe o teclado virtual ao exibir o Dialog
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(R.string.acao_novo);
        return rootView;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (EditorInfo.IME_ACTION_DONE == actionId) {
            activity = getActivity();
            if (activity instanceof SalvarSensor) {
                if (sensor == null) {
                    sensor = new Sensor(
                            txtNome.getText().toString(),
                            txtValor.getText().toString());
                } else {
                    sensor.nome = txtNome.getText().toString();
                    sensor.valor = txtValor.getText().toString();

                }
                SalvarSensor listener = (SalvarSensor) activity;
                listener.salvar(sensor);
                // Feche o dialog
                dismiss();
                return true;
            }
        }
        return false;
    }

    public void abrir(FragmentManager fm) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
        }
    }

    public interface SalvarSensor {
        void salvar(Sensor sensor);
    }
}

