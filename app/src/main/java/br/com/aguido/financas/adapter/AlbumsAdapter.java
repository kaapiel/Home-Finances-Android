package br.com.aguido.financas.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.aguido.financas.R;
import br.com.aguido.financas.model.Album;
import br.com.aguido.financas.model.Despesa;
import br.com.aguido.financas.ui.Despesas;
import br.com.aguido.financas.ui.NovaDespesa;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>
        implements DialogInterface.OnClickListener {

    private Context mContext;
    private List<Despesa> despesasList;
    private Despesa d;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("despesas");

    public Despesa getDespesa(int position) {
        return despesasList.get(position);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE: // yes

                myRef.orderByChild("id").equalTo(d.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String index = null;
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            index = messageSnapshot.getKey();
                            Log.e("ID", String.valueOf(d.getId()));
                            Log.e("INDEX", index);
                        }

                        FirebaseDatabase.getInstance().getReference().child("despesas").child(index).removeValue();

                        Toast toast = Toast.makeText(mContext, "Despesa excluida com sucesso.", Toast.LENGTH_LONG);
                        toast.show();

                        mContext.startActivity(new Intent(mContext, Despesas.class)); //atualiza activity

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case DialogInterface.BUTTON_NEGATIVE: // no
                break;
            case DialogInterface.BUTTON_NEUTRAL: // neutral
                break;
            default:
                // nothing
                break;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, count, data;
        ImageView thumbnail, overflow;
        View v;

        MyViewHolder(View view) {
            super(view);
            this.v = view;
            title = view.findViewById(R.id.title);
            data = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
            count = view.findViewById(R.id.value);
        }
    }

    public AlbumsAdapter(Context mContext, List<Despesa> despesasList) {
        this.mContext = mContext;
        this.despesasList = despesasList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Despesa d = despesasList.get(position);
        holder.title.setText(d.getTituloDespesa());
        holder.count.setText(d.getValorDespesa());
        holder.data.setText(d.getDataDespesa());

        // loading album cover using Glide library
        Glide.with(mContext).load(
                d.getImagemDespesa().equals("Casa") ? R.drawable.casa :
                        d.getImagemDespesa().equals("Apartamento") ? R.drawable.ap :
                                d.getImagemDespesa().equals("Igreja") ? R.drawable.dizimo :
                                        d.getImagemDespesa().equals("Presente") ? R.drawable.presente :
                                                d.getImagemDespesa().equals("Banco") ? R.drawable.banks :
                                                        R.drawable.cifrao).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, d);
            }
        });


        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, NovaDespesa.class);
                i.putExtra(mContext.getString(R.string.param_despesa), d);
                mContext.startActivity(i);
            }
        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Despesa d) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(d));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private Despesa despesa;

        public MyMenuItemClickListener(Despesa d) {
        this.despesa = d;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.deletar_menu:

                    d = despesa;

                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("Exclusão de Despesa")
                            .setMessage("Tem certeza que deseja excluir despesa "+d.getTituloDespesa()+" ?")
                            .setPositiveButton("Sim", AlbumsAdapter.this)
                            .setNegativeButton("Não", AlbumsAdapter.this)
                            .setCancelable(false)
                            .create();

                    dialog.show();

                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return despesasList.size();
    }

}
