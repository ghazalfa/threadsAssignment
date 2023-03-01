package stablemarriages;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    /**
     * Check if a matching is stable
     * @param matching is not null, and of length 2 * n,
     *                 where index 2 * i represents a girl
     *                 match with boy at index 2 * i + 1
     * @param preferencesOfGirls is a permutation of [0, 1, ..., n-1]
     * @param preferencesOfBoys is a permutation of [0, 1, ..., n-1]
     * @return true if matching stable and false otherwise
     */
    public static boolean isStable(Integer[] matching,
                                   List<List<Integer>> preferencesOfGirls,
                                   List<List<Integer>> preferencesOfBoys) {
        List<Integer> wives = new ArrayList<>();
        List<Integer> husbands = new ArrayList<>();
        int husband;
        int index;
        int tempWife;

        //making two lists to match up wives and husbands
        for(int i = 0; i<matching.length-1; i = i+2){
            wives.add(matching[i]);
            husbands.add(matching[i+1]);
        }

        for(Integer girl: wives){
            List <Integer> tempPrefOfGirls;

            //list of boys that girl wants
            tempPrefOfGirls = preferencesOfGirls.get(girl);

            //actual husband of girl
            husband = husbands.get(wives.indexOf(girl));

            //index of preference of girl for husband
            index = tempPrefOfGirls.indexOf(husband);

            for(int i = 0; i<=index; i++){
                //man that is at that index in girls list of preferences
                int tempMale = tempPrefOfGirls.get(i);

                //temp wife stores temp males current wife
                tempWife = wives.get(husbands.indexOf(tempMale));

                List <Integer> tempPrefOfTempMale;

                //this list stores the preferences of the temp guy
                tempPrefOfTempMale = preferencesOfBoys.get(tempMale);

                //if our girl is not his wife, iterate through the preferences of tempMale,
                //if our girl is favored more than his current wife than return false

                if(tempWife!=girl) {
                    boolean wifeIsBefore = false;
                    for (int m = 0; m < tempPrefOfTempMale.size(); m++) {

                        if (tempPrefOfTempMale.get(m) == girl && !wifeIsBefore) {
                            return false;
                        }
                        if (tempPrefOfTempMale.get(m) == tempWife) {
                            wifeIsBefore = true;
                        }

                    }
                }

            }
        }
        return true;
    }

    /**
     * Simple helper class for JSON formatting
     */
    static class ProblemInstance {
        public List<List<Integer>> preferencesOfGirls;
        public List<List<Integer>> preferencesOfBoys;
        public Integer[] solution;
    }
}
