package com.example.fartos;

import android.media.Image;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Jugador {

    private String nom;
    private List<Carta> ma;
    private boolean patada = false;
    private boolean zancadilla = false;
    private RelativeLayout casilla;
    private int icon;

    public String getNom() {
        return nom;
    }
    public List<Carta> getMa() {
        return ma;
    }
    public boolean isPatada() {
        return patada;
    }
    public boolean isZancadilla() {
        return zancadilla;
    }

    public void setMa(List<Carta> ma) {
        this.ma = new ArrayList<>();
        this.ma.addAll(ma);
    }
    public void setPatada(boolean patada) {
        this.patada = patada;
    }
    public void setZancadilla(boolean zancadilla) {
        this.zancadilla = zancadilla;
    }

    public Jugador (String nom) {
        this.nom = nom;
        this.ma = new ArrayList<>();
    }

    public RelativeLayout getCasilla() {
        return casilla;
    }

    public void setCasilla(RelativeLayout casilla) {
        this.casilla = casilla;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return nom;
    }
}
