package br.com.aguido.financas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import br.com.aguido.financas.R;
import br.com.aguido.financas.model.Despesa;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Inmetrics on 15/09/2016.
 */
public class NovaDespesa extends AppCompatActivity {

    @BindView(R.id.titulo)
    EditText editTitulo;

    @BindView(R.id.valor)
    EditText editValor;

    @BindView(R.id.tipoDespesa_spinner)
    Spinner spinnerTipoDespesa;

    @BindView(R.id.seekBar_valores)
    SeekBar seekBar;

    @BindView(R.id.cadastrar)
    Button botao;

    private ArrayAdapter<String> adapter;
    private String dataHoje;
    private Despesa despesa;
    private int lastIndex;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("despesas");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_despesa);
        ButterKnife.bind(this);
        setUielements();

        try {
            despesa = (Despesa) getIntent().getExtras().getSerializable(getString(R.string.param_despesa));
            botao.setText("Alterar");

            editValor.setText(despesa.getValorDespesa());
            editTitulo.setText(despesa.getTituloDespesa());

        } catch (NullPointerException npe) {
            //não faz nada
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editValor.setText("R$ " + progress + ",00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        assert botao != null;
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (despesa != null) { //ALTERAR

                    Log.e("ALTERAR", "PASSOU AAQUI");

                    myRef.orderByChild("id").equalTo(despesa.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot d : dataSnapshot.getChildren()) {

                                String key = d.getKey();
                                //myRef.child(key).child("id").setValue(despesa.getId());  //não rpecisa atualizar o id, pois ja esta no objeto 9dentro do 'for')
                                myRef.child(key).child("dataDespesa").setValue(dataHoje);  //pegar do calendarView
                                myRef.child(key).child("imagemDespesa").setValue(adapter.getItem(spinnerTipoDespesa.getSelectedItemPosition()));
                                myRef.child(key).child("tipoDespesa").setValue("Fixo");
                                myRef.child(key).child("tituloDespesa").setValue(editTitulo.getText().toString());
                                myRef.child(key).child("valorDespesa").setValue(editValor.getText().toString());

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(NovaDespesa.this, Despesas.class));
                    finish();

                } else { //CADASTRAR

                    Log.e("CADASTRAR", "PASSOU AAQUI");
                    myRef.child(String.valueOf(lastIndex+1)).child("id").setValue(randomNumberByRange(999999999, 1));
                    myRef.child(String.valueOf(lastIndex+1)).child("dataDespesa").setValue(dataHoje);
                    myRef.child(String.valueOf(lastIndex+1)).child("imagemDespesa").setValue(adapter.getItem(spinnerTipoDespesa.getSelectedItemPosition()));
                    myRef.child(String.valueOf(lastIndex+1)).child("tipoDespesa").setValue("FixoDizmo");
                    myRef.child(String.valueOf(lastIndex+1)).child("tituloDespesa").setValue(editTitulo.getText().toString());
                    myRef.child(String.valueOf(lastIndex+1)).child("valorDespesa").setValue(editValor.getText().toString());

                    startActivity(new Intent(NovaDespesa.this, Despesas.class));
                    finish();
                }
            }
        });

    }

    private void setUielements() {

        ArrayList<String> strings = new ArrayList<>();
        strings.add("Presente");
        strings.add("Banco");
        strings.add("Apartamento");
        strings.add("Casa");
        strings.add("Igreja");
        strings.add("Inesperado");
        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, strings);
        spinnerTipoDespesa.setAdapter(adapter);

        seekBar.setMax(10000);
        editValor.setText("R$ " + seekBar.getProgress() + ",00");

        setDataHoje();
    }

    private void setDataHoje() {

        Calendar hoje = new GregorianCalendar();

        String mes = "", dia = "";
        int ano;

        if ((hoje.get(Calendar.MONTH) + 1) < 10) {
            mes = "0" + (hoje.get(Calendar.MONTH) + 1);
        }

        if ((hoje.get(Calendar.DAY_OF_MONTH) + 1) < 10) {
            dia = "0" + (hoje.get(Calendar.DAY_OF_MONTH) + 1);
        } else {
            dia = String.valueOf(hoje.get(Calendar.DAY_OF_MONTH));
        }

        ano = hoje.get(Calendar.YEAR);

        dataHoje = dia + "/" + mes + "/" + ano;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    lastIndex++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Despesa validarSpinner(Despesa d) {

//        if(spinner.getSelectedIndex() == 0){
//            despesa.setImagem("banco");
//        } else if(spinner.getSelectedIndex() == 1){
//            despesa.setImagem("presente");
//        } else if(spinner.getSelectedIndex() == 2){
//            despesa.setImagem("igreja");
//        } else if(spinner.getSelectedIndex() == 3){
//            despesa.setImagem("despesaAP");
//        } else if(spinner.getSelectedIndex() == 4){
//            despesa.setImagem("despesaCasa");
//        } else {
//            despesa.setImagem("inesperado");
//        }

        return d;
    }

    public int randomNumberByRange(int max, int min) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
