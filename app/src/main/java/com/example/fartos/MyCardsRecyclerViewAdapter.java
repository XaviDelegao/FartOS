package com.example.fartos;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
public class MyCardsRecyclerViewAdapter extends RecyclerView.Adapter<MyCardsRecyclerViewAdapter.ViewHolder> {

    private List<Integer> mCards;
    private Context mContext;

    public MyCardsRecyclerViewAdapter(List<Integer> cards) {
        mCards = cards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int card = mCards.get(position);

        if (card <= 28) {
            holder.mImageView.setImageResource(R.drawable.mouuna);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.mouuna);
                return true;
            });

        }
        if (card >= 29 && card <= 46) {
            holder.mImageView.setImageResource(R.drawable.moudos);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.moudos);
                return true;
            });

        }
        if (card >= 47 && card <= 56) {
            holder.mImageView.setImageResource(R.drawable.moutres);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.moutres);
                return true;
            });
        }
        if (card >= 57 && card <= 59) {
            holder.mImageView.setImageResource(R.drawable.teleport);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.teleport);
                return true;
            });
        }
        if (card >= 66 && card <= 67) {
            holder.mImageView.setImageResource(R.drawable.hundimiento);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.hundimiento);
                return true;
            });
        }
        if (card >= 60 && card <= 63) {
            holder.mImageView.setImageResource(R.drawable.zancadilla);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.zancadilla);
                return true;
            });
        }
        if (card >= 64 && card <= 66) {
            holder.mImageView.setImageResource(R.drawable.patada);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.patada);
                return true;
            });
        }
        if (card >= 69 && card <= 70) {
            holder.mImageView.setImageResource(R.drawable.broma);
            holder.itemView.setOnLongClickListener(v -> {
                Context context = v.getContext();
                showImageDialog(context, R.drawable.broma);
                return true;
            });
        }

        holder.mImageView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Selecciona jugador objetivo");
            View view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.select_target, null);
            ImageButton p1 = view.findViewById(R.id.p1);
            ImageButton p2 = view.findViewById(R.id.p2);
            ImageButton p3 = view.findViewById(R.id.p3);
            Carta cartaAJugar = FartOS.getJugadorActual().getMa().stream().filter(c -> c.getNumero() == card)
                    .findAny()
                    .orElse(null);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();

            p1.setOnClickListener(v1 -> {
                Jugador jSelect = FartOS.taulell.getJugadors().get(0);
                jSelect.getCasilla().setVisibility(View.INVISIBLE);
                FartOS.jugar(cartaAJugar, jSelect);
                FartOS.setValoresCartas();
                FartOS.cardsFragment.updateCards(FartOS.valoresCartas);

                jSelect.getCasilla().setVisibility(View.VISIBLE);

                dialog.dismiss();
            });
            p2.setOnClickListener(v1 -> {
                Jugador jSelect = FartOS.taulell.getJugadors().get(1);
                jSelect.getCasilla().setVisibility(View.INVISIBLE);
                FartOS.jugar(cartaAJugar, jSelect);
                FartOS.setValoresCartas();
                FartOS.cardsFragment.updateCards(FartOS.valoresCartas);
                jSelect.getCasilla().setVisibility(View.VISIBLE);
                dialog.dismiss();
            });
            p3.setOnClickListener(v1 -> {
                Jugador jSelect = FartOS.taulell.getJugadors().get(2);
                jSelect.getCasilla().setVisibility(View.INVISIBLE);
                FartOS.jugar(cartaAJugar, jSelect);
                FartOS.setValoresCartas();
                FartOS.cardsFragment.updateCards(FartOS.valoresCartas);;
                jSelect.getCasilla().setVisibility(View.VISIBLE);
                dialog.dismiss();
            });
        });




    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;


        public ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.card_image_view);
        }
    }

    public void showImageDialog(Context context, int imageResId) {
        // Infla el diseño personalizado del diálogo
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image, null);

        // Encuentra la vista de imagen y establece la imagen correspondiente
        ImageView imageView = dialogView.findViewById(R.id.image_view);
        imageView.setImageResource(imageResId);

        // Crea el diálogo y muéstralo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }



}




