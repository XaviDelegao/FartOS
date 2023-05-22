package com.example.fartos;


import static com.example.fartos.FartOS.MaxJugadors;
import static com.example.fartos.FartOS.MinJugadors;

import java.util.*;

public class Taulell {

    public final int LongTaulell = 15 + 1;
    public final int[] PosEspecial = { 8 };
    public final int MaxAforo = 2;
    public final int NumCartes;


    private Dictionary<Jugador, Number> taulell;
    private Dictionary<Jugador, Number> jugadorsEliminats;
    private List<Jugador> guanyadors;
    private int casellaEliminada = 0;


    public Dictionary<Jugador, Number> getTaulell() {
        return taulell;
    }
    public int getNumJugadors() {
        return taulell.size();
    }
    public List<Jugador> getJugadors() {
        return Collections.list(taulell.keys());
    }
    public List<Jugador> getGuanyadors() {
        return guanyadors;
    }
    public int getCasellaEliminada() {
        return casellaEliminada;
    }


    public Taulell(Jugador[] jugadors) {
        taulell = new Hashtable<>();
        jugadorsEliminats = new Hashtable<>();
        guanyadors = new ArrayList<>();


        // Comprova que el número de jugadors sigui correcte
        if (jugadors.length < MinJugadors)
            throw new IllegalArgumentException("No es pot jugar amb menys de 3 jugadors.");
        if (jugadors.length > MaxJugadors)
            throw new IllegalArgumentException("No es pot jugar amb més de 6 jugadors.\"");


        // Afegeix els jugadors al taulell
        for (Jugador jugador : jugadors)
            taulell.put(jugador, 0);


        // Comprova la quantitat de cartes per persona
        if (taulell.size() < 5) NumCartes = 6;
        else NumCartes = 5;
    }









    // * Taulell (Tablero)


    public boolean comprobarCasellaEspecial(int casella) {
        int comprovacioEspecial =
                Arrays.stream(PosEspecial).filter(n -> n == casella).findAny().orElse(-1);
        if (comprovacioEspecial != -1) return true;
        return false;
    }
    public boolean comprobarCasellaEspecial(Jugador jugador) {
        int comprovacioEspecial =
                Arrays.stream(PosEspecial).filter(n -> n == (int) taulell.get(jugador))
                        .findAny()
                        .orElse(-1);
        if (comprovacioEspecial != -1) return true;
        return false;
    }


    private boolean comprovarAforamentSuperat(int casella) {
        int sumaOcupants = 0;


        // Comprova quants jugadors estan a la casella
        for (Enumeration<Number> posicions = taulell.elements(); posicions.hasMoreElements(); ) {
            if (posicions.nextElement().intValue() == casella)
                sumaOcupants++;
        }

        if (sumaOcupants >= MaxAforo) return true;
        else return false;
    }


    public void eliminarCasella() {
        // Elimina la casella
        casellaEliminada++;

        // Elimina als jugadors desbordats
        for (Jugador jugador : getJugadors()) {
            if ((int) taulell.get(jugador) <= casellaEliminada)
                eliminarJugador(jugador);
        }
    }



    // * Jugadors

    public int posicioJugador(Jugador jugador) {
        return (int) taulell.get(jugador);
    }


    public void moureJugador(Jugador jugador, int posicions) {
        int posicioActual;
        int posFinal;

        // Calcula la posició final
        posicioActual = taulell.get(jugador).intValue();
        posFinal = (posicioActual + posicions);


        // Comprova l'aforament de la casilla
        if (comprovarAforamentSuperat(posFinal))
            posFinal--;


        /* - Comprova que no posa al jugador en una casella inexistent
         * - Si passa de la meta el deixa a la meta
         * - Si la casella no existeix el deixa a l'inici */
        if (posFinal >= 0 && posFinal < LongTaulell)
            taulell.put(jugador, posFinal);
        else if (posFinal >= (LongTaulell - 1)) taulell.put(jugador, LongTaulell - 1);
        else taulell.put(jugador, 0);

        // Si va a una casella eliminada elimina al jugador
        if (posFinal <= casellaEliminada && casellaEliminada > 0)
            eliminarJugador(jugador);
    }

    public void colocarJugador(Jugador jugador, int posicio) {
        taulell.put(jugador, posicio);

        // Si va a una casella eliminada elimina al jugador
        if (posicio <= casellaEliminada && casellaEliminada > 0)
            eliminarJugador(jugador);
    }


    public void eliminarJugador(Jugador jugador) {
        if (taulell.size() >= 1) {
            // Afegeix al jugador a la llista de jugadors eliminats
            jugadorsEliminats.put(jugador, taulell.get(jugador));
            // L'elimina del taulell
            taulell.remove(jugador);
        }
    }


    public boolean comprobarGuanyador(boolean finalPartida) {
        // Si ya s'ha establert un guanyador
        if (!guanyadors.isEmpty()) return true;


        // Si no es final de partida
        if (!finalPartida) {
            // Comprova si només queda un jugador
            if (taulell.size() == 1) {
                guanyadors.add(getJugadors().get(0));
                return true;
            }

            // Comprova si no queden jugadors
            else if (taulell.isEmpty()) {
                // Recorre la llista de jugadors eliminados
                guanyadors.addAll(jugadorAdelantat(jugadorsEliminats));
                return true;
            }


            // Comprova si un jugador ha arribat a la meta
            for (Jugador jugador : getJugadors()) {
                if ((int) taulell.get(jugador) == LongTaulell - 1) {
                    guanyadors.add(jugador);
                    return true;
                }
            }
        }


        // Si s'han acabat les rondes Guanya el jugador més avançat
        else {
            guanyadors.addAll(jugadorAdelantat(taulell));
            return true;
        }

        return false;
    }


    public List<Jugador> jugadorAdelantat(Dictionary<Jugador, Number> lista) {
        List<Jugador> jugadorsAdelantats = new ArrayList<>();

        // Recorre el taulell
        for (Enumeration<Jugador> jugadors = lista.keys(); jugadors.hasMoreElements(); ) {
            Jugador jugadorActual = jugadors.nextElement();
            int posResult;
            int posJugAct;


            // Si no s'ha assignat un guanyador inicial se n'assigna un aleatòriament
            if (jugadorsAdelantats.isEmpty()) jugadorsAdelantats.add(jugadorActual);
            else {
                // S'obtie la posició del jugador actual de la lista i de l'assignat com a guanyador temporalment
                posResult = lista.get(jugadorsAdelantats.get(0)).intValue();
                posJugAct = lista.get(jugadorActual).intValue();

                // Comprova si el jugador actual està més luny que el guanyador assignat actual
                if (posJugAct > posResult) {
                    jugadorsAdelantats.removeAll(jugadorsAdelantats);
                    jugadorsAdelantats.add(jugadorActual);
                }
                else if (posJugAct == posResult) jugadorsAdelantats.add(jugadorActual);
            }
        }

        return jugadorsAdelantats;
    }
}
