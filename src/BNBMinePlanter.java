import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BNBMinePlanter implements MinePlanter {

    private Sign[][] current, best;

    private int currentMines, bestMines;


    //lege posities
    List<Pair> empties;

    @Override
    public Sign[][] plant(int rows, int cols, Sign[][] field) {
        //initialisatie
        current = new Sign[field.length][field[0].length];
        best = new Sign[field.length][field[0].length];

        copyField(field, current);
        copyField(field,best);
        currentMines = 0;
        bestMines = 0;

        empties = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].isEmpty()) {
                    empties.add(p(i, j));
                }
            }
        }

        //probleem recursief oplossen
        plant(0);

        //we zetten onze oplossing om naar de juiste vorm:
        //we zetten de vlagjes die we aanpasten terug naar hun oorspronkelijke waarde
        Sign[][] result = new Sign[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].isBound()) {
                    result[i][j] = field[i][j].copy();
                }else{
                    result[i][j] = best[i][j].copy();
                }
            }
        }
        return result;
    }

    private void plant(int l) {
        //geval 1: we zijn alle lege vakjes afgegaan, en hebben dus een (al dan niet optimale) oplossing gevonden
        if (l >= empties.size()) {
            if (currentMines > bestMines) {
                copyField(current, best);
                bestMines = currentMines;
            }
            //geval 2: we zijn nog niet alle lege vakjes afgegaan, en we kunnen misschien nog beter doen dan de huidige oplossing
        } else if(currentMines + (empties.size() - l) > bestMines ) {
            //recursief verder uitwerken, met eerst de berekening van welk vakje we na (i,j) zullen behandelen
            int i = empties.get(l).getI();
            int j = empties.get(l).getJ();

            //probeer een mijn te leggen op positie i,j
            if (canPlace(i,j)) {
                placeMine(i, j);
                currentMines++;


                //recursie
                plant(l + 1);

                //hersteloperatie
                removeMine(i, j);
                currentMines--;

            }
            //recursief verder werken zonder mijn op positie i,j
            plant(l +1);
        }
    }



    //plaats bom op positie i,j in een gegeven partiële oplossinge en pas omliggende vlaggen aan
    //we gaan er van uit dat deze methode alleen opgeroepen wordt nadat we gekeken hebben of we op de gekozen positie al dan niet een bom kunnen plaatsen
    private void placeMine(int i, int j) {
        //plaats de mijn
        current[i][j] = new Mine();
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                //pas de omliggende vlaggetjes aan
                if ((k != 0 || l != 0) && inField(i + k, j + l) && current[i + k][j + l].isBound()) {
                    current[i + k][j + l].decrement();
                }
            }
        }
    }

    //we roepen deze methode alleen op wanneer er effectief een mijn ligt op positie i,j
    private void removeMine(int i, int j) {
        current[i][j] = new Empty();
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                //pas de omliggende vlaggetjes aan
                if ((k != 0 || l != 0) && inField(i + k, j + l) && current[i + k][j + l].isBound()) {
                    current[i + k][j + l].increment();
                }
            }
        }
    }

    private boolean canPlace(int i, int j) {
        if (inField(i, j) && current[i][j].isEmpty()) {
            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if ((k != 0 || l != 0) && inField(i + k, j + l) && current[i + k][j + l].saturated()) {
                        return false;
                    }
                }
            }
            return true;

        } else {
            return false;
        }
    }

    private boolean inField(int i, int j) {
        return 0 <= i && i < current.length && 0 <= j && j < current[i].length;
    }


    private void copyField(Sign[][] origin, Sign[][] dest) {
        for (int i = 0; i < origin.length && i < dest.length; i++) {
            for (int j = 0; j < origin[i].length && j < dest[i].length; j++) {
                dest[i][j] = origin[i][j].copy();
            }
        }
    }

    //een heel eenvoudige bound: hoeveel posities zullen ten hoogste nog worden afgegaan?
    //A: het aantal posities op huidige rij + de rijlengte * het aantal rijen die we nog moeten doen
    private int numRemainingPos(int i, int j) {
        return current[i].length - j + (current.length - (i+1)) * current[i].length;
    }


    //hulpklasse voor coordinaten
    private class Pair{
        int i;
        int j;

        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }
    }

    //hulpmethode om gemakkelijker pairs te creeren
    private Pair p(int i, int j) {
        return new Pair(i, j);
    }
}
