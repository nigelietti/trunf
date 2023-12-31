package model;

import java.util.ArrayList;

public class Giocatore {
    private String nome;
    private ArrayList<Card> mazzoGiocatore;
    private ArrayList<Card> cartePrese;

    private int punteggio;

    public void cartaPresa(Card carta) {
        cartePrese.add(carta);
    }

    public void reset() {
        punteggio = 0;
        mazzoGiocatore.clear();
        cartePrese.clear();
    }

    public Giocatore(String nome) {
        punteggio = 0;
        mazzoGiocatore = new ArrayList<Card>();
        cartePrese = new ArrayList<Card>();
        this.nome = nome;
    }


    public ArrayList<Card> getMazzoGiocatore() {
        return mazzoGiocatore;
    }

    public void setMazzoGiocatore(ArrayList<Card> mazzoGiocatore) {
        this.mazzoGiocatore = mazzoGiocatore;
    }

    public void ifStuck(Seed trunf) {//metodo
        boolean trovato = false;
        for (Card k : getMazzoGiocatore()) { //itera su tutte le carte(c) col metodo che eredita da mazzo

            if (k.getValue().equals('K') && k.getSeed().equals(trunf)) {
                for (Card q : getMazzoGiocatore()) {
                    if (q.getValue().equals('Q') && q.getSeed().equals(trunf)) {
                        punteggio = punteggio + 20;
                    }
                }
            }
        }
    }

    public ArrayList<Value> ifpoker() {
        ArrayList<Value> poker = new ArrayList<Value>();
        int contatore;
        for (Value v : Value.values()) {
            contatore = 0;
            if (v.compareTo(Value.DIECI) >= 0) {
                for (Card k : getMazzoGiocatore()) {
                    if (k.getValue().compareTo(v) == 0)
                        contatore++;

                }
            }
            if (contatore == 4) {
                poker.add(v);
            }
        }
        return poker;
    }

    public Value pokermax() {
        ArrayList<Value> poker = ifpoker();
        if (poker.isEmpty()) {
            return null;
        }
        Value pokermax = null;

        int i;
        for (i = 0; i < poker.size(); i++) {
            if (poker.get(i).equals('J')) {
                pokermax = Value.J;
                break;
            } else {
                pokermax = poker.get(i);
            }


        }
        return pokermax;
    }


    public ArrayList<Scala> scale() {
        Value valorePiuAlto = mazzoGiocatore.get(9).getValue();
        Value valorePrecedente = mazzoGiocatore.get(9).getValue();
        Seed seme = mazzoGiocatore.get(9).getSeed();
        ;
        int carteInFila = 1;

        ArrayList<Scala> scale = new ArrayList<Scala>();

        for (int i = 8; i >= 0; i--) {
            if (mazzoGiocatore.get(i).getSeed().equals(seme) && mazzoGiocatore.get(i).getValue().compareTo(valorePrecedente) == -1) {
                if (i == 0 && carteInFila > 1 && carteInFila < 5) {
                    carteInFila++;
                    scale.add(new Scala(carteInFila, valorePiuAlto, seme));
                }
                if (carteInFila < 5) {
                    carteInFila++;
                    valorePrecedente = mazzoGiocatore.get(i).getValue();
                } else if (i != 0) {
                    scale.add(new Scala(carteInFila, valorePiuAlto, seme));
                    carteInFila = 1;
                    valorePiuAlto = mazzoGiocatore.get(i).getValue();
                    seme = mazzoGiocatore.get(i).getSeed();
                    valorePrecedente = valorePiuAlto;
                }

            } else {
                if (carteInFila > 2) {
                    scale.add(new Scala(carteInFila, valorePiuAlto, seme));
                }
                carteInFila = 1;
                valorePiuAlto = mazzoGiocatore.get(i).getValue();
                valorePrecedente = valorePiuAlto;
                seme = mazzoGiocatore.get(i).getSeed();
            }


        }

        for (Scala s : scale) {
            System.out.println("Scala di " + s.getNum() + " carte di " + s.getSeed() + " con carta più alta " + s.getValue());
        }

        return scale;
    }

    public Scala scalamax(Seed trunf) {

        ArrayList<Scala> scale = scale();
        if (scale.isEmpty()) {
            return null;
        }

        Scala scalaMax = scale.get(0);
        int i;

        for (i = 1; i < scale.size(); i++) {
            if (scalaMax.getNum() < scale.get(i).getNum()) {
                scalaMax = scale.get(i);
            } else if (scalaMax.getNum() == scale.get(i).getNum()) {
                if (scalaMax.getValue().compareTo(scale.get(i).getValue()) < 0) {
                    scalaMax = scale.get(i);
                } else if (scalaMax.getValue().compareTo(scale.get(i).getValue()) == 0) {

                    if (scale.get(i).getSeed() == trunf) {
                        scalaMax = scale.get(i);
                    }
                }
            }
        }

        return scalaMax;
    }


    public void daiCarta(Card card) {
        if (mazzoGiocatore.isEmpty()) {
            mazzoGiocatore.add(card);
            return;
        }

        for (Card c : mazzoGiocatore) {
            if (card.getSeed().compareTo(c.getSeed()) < 0) {
                mazzoGiocatore.add(mazzoGiocatore.indexOf(c), card);
                return;
            } else {
                if (card.getSeed().compareTo(c.getSeed()) == 0) {
                    if (card.getValue().compareTo(c.getValue()) < 0) {
                        mazzoGiocatore.add(mazzoGiocatore.indexOf(c), card);
                        return;
                    }
                }
            }

        }

        mazzoGiocatore.add(card);
    }

    public void puntiPoker() {
        for (Value v : ifpoker()) {
            if (v.equals("J")) {
                punteggio = punteggio + 200;
            } else {
                punteggio = punteggio + 100;
            }
        }
    }

    public void puntiScale() {
        for (Scala s : scale()) {
            switch (s.getNum()) {
                case 3:
                    punteggio = punteggio + 20;
                    break;
                case 4:
                    punteggio = punteggio + 40;
                    break;
                case 5:
                    punteggio = punteggio + 100;
                    break;
            }
        }
    }


    public int getPunteggio() {
        return punteggio;
    }

    public Card giocaCarta(Card carta) throws IllegalArgumentException {
        if (mazzoGiocatore.remove(carta))
            return carta;
        else
            throw new IllegalArgumentException();

    }

}