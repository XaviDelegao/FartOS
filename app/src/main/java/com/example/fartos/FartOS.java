package com.example.fartos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class FartOS extends AppCompatActivity {


    EditText player1;
    EditText player2;
    EditText player3;
    static Jugador[] jugadores;
    TextView jugador1;
    TextView jugador2;
    TextView jugador3;
    static List<Integer> valoresCartas;
    public static Boolean partida = false;
    static CardsFragment cardsFragment;
    static Taulell taulell;

    public enum ResultatMoviment {GUANYADOR, CASELLA_ESPECIAL, CONTINUAR_JOC}

    private static final int NumRondes = 5;
    private static final int RondaElimCela = 3;
    private final int MovCasellaEspecial = 5;
    public static final int MinJugadors = 3;
    public static final int MaxJugadors = 6;

    private static int numRonda;
    private static Jugador jugadorActual;
    private static List<Carta> baralla;
    private static int cartesJugades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fart_os);

        cardsFragment = new CardsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cards_fragment_container, cardsFragment)
                .commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nombra a los jugadores:");
        View alertView = LayoutInflater.from(this).inflate(R.layout.player_names, null);
        builder.setView(alertView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player1 = alertView.findViewById(R.id.editTextPlayer1);
                player2 = alertView.findViewById(R.id.editTextPlayer2);
                player3 = alertView.findViewById(R.id.editTextPlayer3);

                jugadores = new Jugador[3];
                jugadores[0] = new Jugador(player1.getText().toString());
                jugadores[1] = new Jugador(player2.getText().toString());
                jugadores[2] = new Jugador(player3.getText().toString());
                taulell = new Taulell(jugadores);
                iniciarJoc(taulell);

                setValoresCartas();
                cardsFragment.updateCards(valoresCartas);
                System.out.println(valoresCartas);
                jugador1 = findViewById(R.id.jugador1_nombre);
                jugador2 = findViewById(R.id.jugador2_nombre);
                jugador3 = findViewById(R.id.jugador3_nombre);
                jugador1.setText(taulell.getJugadors().get(0).getNom());
                jugador2.setText(taulell.getJugadors().get(1).getNom());
                jugador3.setText(taulell.getJugadors().get(2).getNom());
                taulell.getJugadors().get(0).setCasilla(findViewById(R.id.slot1_casilla1));
                taulell.getJugadors().get(1).setCasilla(findViewById(R.id.slot2_casilla1));
                taulell.getJugadors().get(2).setCasilla(findViewById(R.id.slot3_casilla1));
                taulell.getJugadors().get(0).setIcon(R.drawable.jugador1);
                taulell.getJugadors().get(1).setIcon(R.drawable.jugador2);
                taulell.getJugadors().get(2).setIcon(R.drawable.jugador3);
                builder.setCancelable(true);
            }
        });
        builder.show();


    }

    public static void setValoresCartas() {
        List<Integer> vCartas = new ArrayList<>();
        for (Carta carta : getJugadorActual().getMa()) {
            vCartas.add(carta.getNumero());
        }
        FartOS.valoresCartas = vCartas;
    }

    public Taulell getTaulell() {
        return taulell;
    }

    public static Jugador getJugadorActual() {
        return jugadorActual;
    }

    public int getNumRonda() {
        return numRonda;
    }

    public void iniciarJoc(Taulell taulell) {
        this.taulell = taulell;


        // Estebleix la ronda actual i les cartes jugades
        numRonda = 1;
        cartesJugades = 0;

        // Reparteix les cartes
        repartirCartes();

        // Estableix el primer jugador
        canviarJugadorActual();
    }


    public Carta seleccionarCarta(int numCarta, Jugador jugador) throws Exception {
        Carta cartaAJugar;


        // Busca la carta
        cartaAJugar = jugador.getMa().stream().filter(c -> c.getNumero() == numCarta)
                .findAny()
                .orElse(null);

        // Comprovo que la carta existeix
        if (cartaAJugar == null)
            throw new Exception("La carta no existeix.");


        return cartaAJugar;
    }


    public Jugador seleccionarJugador(int numJugador) throws Exception {
        Jugador jugador = null;


        // Comprueba que existeix el número del jugador
        if (numJugador > 0 && numJugador <= taulell.getNumJugadors())
            return taulell.getJugadors().get(numJugador - 1);

            // Si no existeix salta un error
        else
            throw new Exception("No existeix el jugador.");
    }

    public Jugador seleccionarJugador(String nom) throws Exception {
        Jugador jugador;


        // Busca el jugador
        jugador = getTaulell().getJugadors().stream()
                .filter(e -> e.getNom().equals(nom))
                .findAny()
                .orElse(null);

        // Comprova que la carta existeix
        if (jugador == null)
            throw new Exception("No existeix el jugador.");


        return jugador;
    }


    public static ResultatMoviment jugar(Carta carta, Jugador jugadorContrari) {
        // Fa jugar al jugador
        realizarMovimient(jugadorActual, carta, jugadorContrari);
        cartesJugades++;


        // Comprova si està a una casella especial
        if (taulell.comprobarCasellaEspecial(jugadorActual))
            return ResultatMoviment.CASELLA_ESPECIAL;


        // Si se ha arribat a la ronda d'eliminació s'elimina una casella
        if (numRonda > RondaElimCela && cartesJugades % taulell.getTaulell().size() == 0) {
            taulell.eliminarCasella();
            cartesJugades = 0;
        }


        // Canvia el torn comprovant si hi ha guanyador o si s'ha acabat el juego
        if (canviarTorn()) return ResultatMoviment.GUANYADOR;
        else return ResultatMoviment.CONTINUAR_JOC;
    }


    public ResultatMoviment aplicarCasellaEspecial(Jugador jugadorContrari) {
        // Mou el jugador
        if (jugadorContrari != jugadorActual)
            taulell.moureJugador(jugadorContrari, -MovCasellaEspecial);
        else
            taulell.moureJugador(jugadorContrari, MovCasellaEspecial);


        // Comprova si ha de eliminar una casella especial
        if (numRonda > RondaElimCela && cartesJugades % taulell.getTaulell().size() == 0)
            taulell.eliminarCasella();


        // Canvia el torn
        if (canviarTorn()) return ResultatMoviment.GUANYADOR;
        else return ResultatMoviment.CONTINUAR_JOC;
    }


    private static void repartirCartes() {
        List<Carta> ma;


        // Crea la baralla
        baralla = Carta.generarBaralla();

        // Reparteix a cada jugador la ma
        for (Jugador jugador : taulell.getJugadors()) {
            ma = baralla.subList(0, taulell.NumCartes); // Crea la ma
            jugador.setMa(ma); // Assigna la ma al jugador
            baralla.removeAll(ma); // Elimina les cartas de la baralla
        }
    }


    private static void realizarMovimient(Jugador jugador, Carta carta, Jugador jugadorContrari) {
        int efecte;

        // Elimina la carta de la mà del jugador
        jugador.getMa().remove(carta);


        // Realiza l'acció
        switch (carta.getEfecte()) {
            case MOV_1:
                if (!jugador.isPatada()) efecte = 1;
                else efecte = 0;
                if (jugador != jugadorContrari) efecte *= -1;

                taulell.moureJugador(jugadorContrari, efecte);
                break;

            case MOV_2:
                if (!jugador.isPatada()) efecte = 2;
                else efecte = 1;
                if (jugador != jugadorContrari) efecte *= -1;

                taulell.moureJugador(jugadorContrari, efecte);
                break;

            case MOV_3:
                if (!jugador.isPatada()) efecte = 3;
                else efecte = 2;
                if (jugador != jugadorContrari) efecte *= -1;

                taulell.moureJugador(jugadorContrari, efecte);
                break;

            case TELEPORT:
                int posJugador = (int) taulell.getTaulell().get(jugador);
                int posJugadorContrari = (int) taulell.getTaulell().get(jugadorContrari);

                taulell.colocarJugador(jugador, posJugadorContrari);
                taulell.colocarJugador(jugadorContrari, posJugador);
                break;

            case ZANCADILLA:
                if (!jugadorContrari.isZancadilla()) {
                    Random rnd = new Random();
                    jugadorContrari.setZancadilla(true);
                    if (jugadorContrari.getMa().size() >= 1)
                        jugadorContrari.getMa().remove(rnd.nextInt(jugadorContrari.getMa().size()));
                }
                break;

            case PATADA:
                jugadorContrari.setPatada(true);
                break;

            case HUNDIMIENTO:
                taulell.colocarJugador(jugadorContrari, 0);
                break;

            case BROMA:
                List<Carta> cartesJugador = jugador.getMa();
                List<Carta> cartasJugadorContrari = jugadorContrari.getMa();

                jugador.setMa(cartasJugadorContrari);
                jugadorContrari.setMa(cartesJugador);
                break;
        }

        // Elimina la patada si es que la tenia posada
        if (jugador.isPatada()) jugador.setPatada(false);
        if (jugador.isZancadilla()) jugador.setZancadilla(false);
    }


    private static boolean canviarTorn() {
        boolean guanyador;


        // Comprova si hi ha guanyador
        guanyador = taulell.comprobarGuanyador(false);


        if (!guanyador) {
            // Comprova si s'ha acabat la ronda (quan els jugadores no tenen cartes)
            if (!comprovarTenenCartesJugadors()) {

                // Si quedan rondas reparteix més cartes
                if (numRonda <= NumRondes) {
                    numRonda++;
                    repartirCartes();
                }

                // Si no quedan rondes acaba el joc i guanya el més avançat
                else {
                    taulell.comprobarGuanyador(true);
                    guanyador = true;
                }
            }


            // Si pot continuar canvia el jugador actual
            if (!guanyador) canviarJugadorActual();
        }


        return guanyador;
    }


    private static boolean comprovarTenenCartesJugadors() {
        for (Jugador jugador : taulell.getJugadors()) {
            if (jugador.getMa().size() > 0) return true;
        }

        return false;
    }


    private static void canviarJugadorActual() {
        Random rnd = new Random();
        int indexJugadorActual;
        List<Jugador> jugadors = taulell.getJugadors();

        // Si no hi ha un jugador actual n'assigna un aleatori
        if (jugadorActual == null) {
            indexJugadorActual = rnd.nextInt(taulell.getTaulell().size());
            int i = 0;
            for (ListIterator<Jugador> jugador = taulell.getJugadors().listIterator();
                 jugador.hasNext() && i <= indexJugadorActual;
                 i++) {

                if (i == indexJugadorActual) jugadorActual = jugador.next();
            }
        }

        // Si hi ha un jugador assignat canvia al següent
        else {
            if (jugadors.indexOf(jugadorActual) == jugadors.size() - 1)
                jugadorActual = jugadors.get(0);

            else
                jugadorActual = jugadors.listIterator(jugadors.indexOf(jugadorActual) + 1).next();
        }


        // Comprova que tingui cartes el nou jugador i si no el salta
        if (jugadorActual.getMa().size() == 0)
            canviarJugadorActual();
    }

    public void selectCasilla() {

        //TODO






        }

    }
}