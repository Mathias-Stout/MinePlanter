import java.util.HashSet;
import java.util.List;

public class BNBMinePlanter implements MinePlanter {

    private Sign[][] current, best;

    private int currentMines, bestMines;

    //alle vrije locaties (i,j)
    boolean[][] free;
    int numFree;

    @Override
    public Sign[][] plant(int rows, int cols, Sign[][] field) {
        //initialisatie
        current = new Sign[field.length][field[0].length];
        best = new Sign[field.length][field[0].length];

        copyField(field, current);
        copyField(field,best);
        currentMines = 0;
        bestMines = 0;

        free = new boolean[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (canPlace(i, j)) {
                    addFree(i, j);
                }
            }
        }

        //probleem recursief oplossen
        plant(0, 0);

        //we zetten onze oplossing om naar de juiste vorm: we zetten de vlagjes die we aanpasten terug naar hun oorspronkelijke waarde
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (best[i][j].isBound()) {
                    best[i][j] = field[i][j].copy();
                }
            }
        }
        return best;
    }

    private void plant(int i, int j) {
        //geval 1: we zijn alle rijen afgegaan en hebben dus een oplossing
        if (i >= current.length) {
            if (currentMines > bestMines) {
                copyField(current, best);
                bestMines = currentMines;
            }
            //geval 2: we zijn nog niet alle rijen afgegaan
        } else if(currentMines + numRemainingPos(i,j) > bestMines) {
            //recursief verder uitwerken, met eerst de berekening van welk vakje we na (i,j) zullen behandelen
            int k = nextI(i, j);
            int l = nextJ(i, j);

            //probeer een mijn te leggen op positie i,j
            if (free[i][j]) {
                placeMine(i, j);
                currentMines++;


                //recursie
                plant(k, l);

                //hersteloperatie
                removeMine(i, j);
                currentMines--;

            }
            //recursief verder werken zonder mijn op positie i,j
            plant(k, l);
        }
    }

    //hulpmethodes om te bepalen welk vaje we als volgende zullen behandelen
    private int nextI(int i, int j) {
        if (j + 1 >= current[i].length)
            return i + 1;
        else
            return i;
    }

    private int nextJ(int i, int j) {
        if (j + 1 >= current[i].length)
            return 0;
        else
            return j+1;
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
        updateFree(i,j);

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
        updateFree(i,j);
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

    //wanneer we een bom zetten of verwijderen, dan
    // 1) de locatie van de bom is nu wel vrij/niet meer vrij
    // 2) wanneer een vlagje van 0 naar 1 gaat, dan moeten eventueel een aantal plaatsen worden vrijgegeven
    // 3) wanneer een vlagje van 1 naar 0 gaat, dan zijn er een aantal plaatsen waar vanaf dan geen bom meer mag geleged worden

    private void addFree(int i, int j) {
        if (inField(i,j) && !free[i][j]){
            free[i][j] = true;
            numFree++;
        }

    }

    private void rmFree(int i, int j) {
        if (inField(i,j) && free[i][j]){
            free[i][j] = false;
            numFree--;
        }
    }

    //als we iets veranderen aan een positie, update vrije posities in een 5*5 vierkant errond
    private void updateFree(int i, int j) {
        for (int k = -2; k <= 2; k++) {
            for (int l = -2; l <= 2; l++) {
                if (canPlace(i + k, l + j)) {
                    addFree(i + k, l + j);
                }else{
                    rmFree(i+k,l+j);
                }
            }
        }
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

    //een eerste bound
    //we kunnen nooit beter doen dan op alle nog vrije plaatsen een bom leggen
    private int numFreeFields(){
        return numFree;
//        int numFree = 0;
//        for (int i = 0; i < current.length; i++) {
//            for (int j = 0; j < current[i].length; j++) {
//                if (canPlace(i, j)) {
//                    numFree++;
//                }
//            }
//        }
//        return numFree;
    }

}
