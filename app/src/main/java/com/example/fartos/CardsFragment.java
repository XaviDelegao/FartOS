package com.example.fartos;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CardsFragment extends Fragment {

    private List<Integer> mCardImages; // Lista de imágenes de cartas
    private RecyclerView mRecyclerView;

    public CardsFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mRecyclerView = view.findViewById(R.id.card_list);
        mCardImages = Arrays.asList(
                R.drawable.mouuna,
                R.drawable.moudos,
                R.drawable.moutres,
                R.drawable.broma,
                R.drawable.patada,
                R.drawable.zancadilla,
                R.drawable.hundimiento,
                R.drawable.teleport
        ); // Ejemplo de imágenes de cartas

        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false); // Configurar el layout manager para mostrar la lista horizontalmente
        mRecyclerView.setLayoutManager(layoutManager);

        MyCardsRecyclerViewAdapter adapter = new MyCardsRecyclerViewAdapter(mCardImages);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    public void updateCards(List<Integer> newCards) {
        mCardImages = newCards;
        MyCardsRecyclerViewAdapter adapter = new MyCardsRecyclerViewAdapter(mCardImages);
        mRecyclerView.setAdapter(adapter);
    }


}