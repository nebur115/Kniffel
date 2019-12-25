package rechner.ai.kniffel.kniffel;

import java.util.ArrayList;
import java.util.List;

class Move {

    private PostDecissionDice Post3DeccissionDice;
    private PostDecissionDice Post1DeccissionDice;
    private PostDecissionDice Post0DeccissionDice;
    private PostDecissionDice post2DeccissionDice;


    Move(State pState, float[] pgamePlan) {
        State state = pState.getState();
        final boolean[] statebooleanArray = state.getBooleanArray();

        List<State> requierments = new ArrayList<>();
        List<State> possibilities = new ArrayList<>();
        List<Integer> requieredField = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (!state.getBoolean(i)) {
                State tempPossibility = state.getState();
                State tempRequierments = new State();

                tempPossibility.setBoolean(i, true);
                tempRequierments.setBoolean(i, true);
                tempRequierments.setObererValue(0);

                for (int j = 0; j < 6; j++) {
                    requierments.add(tempRequierments.getState());
                    possibilities.add(tempPossibility.getState());
                    requieredField.add(i);
                    if ((tempPossibility.getObererValue() + i + 1) > 105) {
                        break;
                    } else {
                        tempPossibility.setObererValue(tempPossibility.getObererValue() + i + 1);
                        tempRequierments.setObererValue(j + 1);
                    }
                }
            }
        }

        for (int i = 6; i < statebooleanArray.length; i++) {
            State tempPossibilities = state.getState();
            State tempRequierments = new State();
            tempRequierments.setBoolean(i, true);
            tempPossibilities.setBoolean(i, true);
            if (!state.getBoolean(i)) {
                requierments.add(tempRequierments);
                possibilities.add(tempPossibilities);
                requieredField.add((i));
            }
        }

        List<Float> values = new ArrayList<>();
        for (int i = 0; i < possibilities.size(); i++) {
            int GameNumber = possibilities.get(i).getNumber();
            values.add(pgamePlan[GameNumber]);
        }


        Post3DeccissionDice = new PostDecissionDice(possibilities, requierments, values, requieredField);
        PreDecissionDice pre3DeccissionDice = new PreDecissionDice(Post3DeccissionDice);
        post2DeccissionDice = new PostDecissionDice(pre3DeccissionDice);
        PreDecissionDice pre2DeccissionDice = new PreDecissionDice(post2DeccissionDice);
        Post1DeccissionDice = new PostDecissionDice(pre2DeccissionDice);
        PreDecissionDice pre1DeccissionDice = new PreDecissionDice(Post1DeccissionDice);
        Post0DeccissionDice = new PostDecissionDice(pre1DeccissionDice);

    }


    int[] Decission0ne(int[] pDice) {
        return Post1DeccissionDice.BestOption(pDice);
    }


    int[] DecissionTwo(int[] pDice) {
        return post2DeccissionDice.BestOption(pDice);
    }

    int FinalDeccissionState(int[] pDice) {
        return Post3DeccissionDice.BestOptionState(pDice).getNumber();
    }


    State FinalDeccissionDifference(int[] pDice) {
        return Post3DeccissionDice.BestOptionDifference(pDice);
    }


    float Value() {
        return Post0DeccissionDice.NoDiceValue();
    }


}



