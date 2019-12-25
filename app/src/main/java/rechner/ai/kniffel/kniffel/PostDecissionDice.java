package rechner.ai.kniffel.kniffel;

import java.util.List;

class PostDecissionDice {

    PostDecissionDice(List<State> pPossibilities, List<State> prequierments, List<Float> pValues, List<Integer> pField) {
        StateTransitionToValues(pPossibilities, prequierments, pValues, pField);
    }

    PostDecissionDice(PreDecissionDice preDecissionDice) {
        Probabilities(preDecissionDice);
    }

    private float[] values = new float[462];
    private State[] bestpossiblities = new State[462];
    private State[] bestrequieremts = new State[462];

    private void StateTransitionToValues(List<State> ppossibilities, List<State> prequierments, List<Float> pValues, List<Integer> pField) {

        for (int i = 0; i < values.length; i++) {

            int[] Dice = diceFromNumber(i);
            values[i] = 0;
            int alleAugen = alleaugenzählen(Dice);

            for (int j = 0; j < pField.size(); j++) {

                int obererValue = prequierments.get(j).getObererValue();
                float value = pValues.get(j);
                boolean isUpperValue = false;
                int counter;

                if (pField.get(j) >= 6) {
                    if (value >= values[i]) {
                        values[i] = value;
                        bestpossiblities[i] = ppossibilities.get(j).getState();
                        bestrequieremts[i] = prequierments.get(j).getState();
                    }

                    switch (pField.get(j)) {
                        case 6:
                            //Dreierpsh
                            for (int die : Dice) {
                                if (die >= 3 && alleAugen + value > values[i]) {
                                    values[i] = alleAugen + value;
                                    bestpossiblities[i] = ppossibilities.get(j).getState();
                                    bestrequieremts[i] = prequierments.get(j).getState();
                                }
                            }
                            break;
                        case 7:
                            //Viererpash
                            for (int die : Dice) {
                                if (die >= 4 && alleAugen + value > values[i]) {
                                    values[i] = alleAugen + value;
                                    bestpossiblities[i] = ppossibilities.get(j).getState();
                                    bestrequieremts[i] = prequierments.get(j).getState();
                                }
                            }
                            break;
                        case 8:
                            //Fullhouse
                            boolean two = false;
                            boolean three = false;
                            for (int die : Dice) {
                                if (die == 2) {
                                    two = true;
                                }
                                if (die == 3) {
                                    three = true;
                                }

                            }
                            if (two && three && 25.0 + value > values[i]) {
                                values[i] = value + 25;
                                bestpossiblities[i] = ppossibilities.get(j).getState();
                                bestrequieremts[i] = prequierments.get(j).getState();
                            }
                            break;
                        case 9:
                            //kl. Straße
                            counter = 0;
                            for (int die : Dice) {
                                if (die > 0) {
                                    counter++;
                                    if (counter > 3) {
                                        break;
                                    }
                                } else {
                                    counter = 0;
                                }
                            }
                            if (counter > 3 && (30.0 + value) > values[i]) {
                                values[i] = value + 30;
                                bestpossiblities[i] = ppossibilities.get(j).getState();
                                bestrequieremts[i] = prequierments.get(j).getState();
                            }
                            break;
                        case 10:
                            //gr. Straße
                            counter = 0;
                            for (int die : Dice) {
                                if (die > 0) {
                                    counter++;
                                    if (counter > 4) {
                                        break;
                                    }
                                } else {
                                    counter = 0;
                                }
                            }
                            if (counter > 4 && (40.0 + value) > values[i]) {
                                values[i] = value + 40;
                                bestpossiblities[i] = ppossibilities.get(j).getState();
                                bestrequieremts[i] = prequierments.get(j).getState();
                            }
                            break;
                        case 11:
                            //Kniffel
                            for (int die : Dice) {
                                if (die == 5 && 50.0 + value > values[i]) {
                                    values[i] = value + 50;
                                    bestpossiblities[i] = ppossibilities.get(j).getState();
                                    bestrequieremts[i] = prequierments.get(j).getState();
                                }
                            }
                            break;
                        case 12:
                            //Chance
                            if (alleAugen + value > values[i]) {
                                values[i] = alleAugen + value;
                                bestpossiblities[i] = ppossibilities.get(j).getState();
                                bestrequieremts[i] = prequierments.get(j).getState();

                            }
                            break;

                    }

                } else {
                    if (Dice[pField.get(j)] >= obererValue) {
                        float tempValue = value + ((pField.get(j) + 1) * obererValue);
                        if (values[i] <= tempValue) {
                            values[i] = value + ((pField.get(j) + 1) * obererValue);
                            bestpossiblities[i] = ppossibilities.get(j).getState();
                            bestrequieremts[i] = prequierments.get(j).getState();
                        }
                    }
                }
            }
        }

    }

    private void Probabilities(PreDecissionDice pPreDecission) {
        float[] preValues = pPreDecission.getValues();
        for (int i = 0; i < values.length; i++) {
            values[i] = 0;
            for (int j = 0; j < MainActivity.generallProbabiltiesInt[i].size(); j++) {
                values[i] = values[i] + preValues[MainActivity.generallProbabiltiesInt[i].get(j)] * MainActivity.generallProbabiltiesFloat[i].get(j);
            }

        }
    }

    private int alleaugenzählen(int[] Dice) {
        int r = 0;
        for (int i = 0; i < Dice.length; i++) {
            r = r + Dice[i] * (i + 1);
        }
        return r;
    }

    static int[] diceFromNumber(int pinput) {
        int[] r = new int[6];
        int input = pinput;
        int dice = 0;

        if (input < 1) {
            r[0] = 5 - dice;
            dice = 5;
        } else if (input < 7) {
            r[0] = 4 - dice;
            dice = 4;
            input = input - 1;
        } else if (input < 28) {
            r[0] = 3 - dice;
            dice = 3;
            input = input - 7;
        } else if (input < 84) {
            r[0] = 2 - dice;
            dice = 2;
            input = input - 28;
        } else if (input < 210) {
            r[0] = 1 - dice;
            dice = 1;
            input = input - 84;
        } else {
            r[0] = 0 - dice;
            dice = 0;
            input = input - 210;
        }

        if (input < 1) {
            r[1] = 5 - dice;
            dice = 5;
        } else if (input < 6) {
            r[1] = 4 - dice;
            dice = 4;
            input = input - 1;
        } else if (input < 21) {
            r[1] = 3 - dice;
            dice = 3;
            input = input - 6;
        } else if (input < 56) {
            r[1] = 2 - dice;
            dice = 2;
            input = input - 21;
        } else if (input < 126) {
            r[1] = 1 - dice;
            dice = 1;
            input = input - 56;
        } else {
            r[1] = 0 - dice;
            dice = 0;
            input = input - 126;
        }

        if (input < 1) {
            r[2] = 5 - dice;
            dice = 5;
        } else if (input < 5) {
            r[2] = 4 - dice;
            dice = 4;
            input = input - 1;
        } else if (input < 15) {
            r[2] = 3 - dice;
            dice = 3;
            input = input - 5;
        } else if (input < 35) {
            r[2] = 2 - dice;
            dice = 2;
            input = input - 15;
        } else if (input < 70) {
            r[2] = 1 - dice;
            dice = 1;
            input = input - 35;
        } else {
            r[2] = 0 - dice;
            dice = 0;
            input = input - 70;
        }

        if (input < 1) {
            r[3] = 5 - dice;
            dice = 5;
        } else if (input < 4) {
            r[3] = 4 - dice;
            dice = 4;
            input = input - 1;
        } else if (input < 10) {
            r[3] = 3 - dice;
            dice = 3;
            input = input - 4;
        } else if (input < 20) {
            r[3] = 2 - dice;
            dice = 2;
            input = input - 10;
        } else if (input < 35) {
            r[3] = 1 - dice;
            dice = 1;
            input = input - 20;
        } else {
            r[3] = 0 - dice;
            dice = 0;
            input = input - 35;
        }


        if (input < 1) {
            r[4] = 5 - dice;
            dice = 5;
        } else if (input < 3) {
            r[4] = 4 - dice;
            dice = 4;
            input = input - 1;
        } else if (input < 6) {
            r[4] = 3 - dice;
            dice = 3;
            input = input - 3;
        } else if (input < 10) {
            r[4] = 2 - dice;
            dice = 2;
            input = input - 6;
        } else if (input < 15) {
            r[4] = 1 - dice;
            dice = 1;
            input = input - 10;
        } else {
            r[4] = 0 - dice;
            dice = 0;
            input = input - 15;
        }

        if (input < 1) {
            r[5] = 5 - dice;
        } else if (input < 2) {
            r[5] = 4 - dice;
        } else if (input < 3) {
            r[5] = 3 - dice;
        } else if (input < 4) {
            r[5] = 2 - dice;
        } else if (input < 5) {
            r[5] = 1 - dice;
        } else {
            r[5] = 0 - dice;
        }

        return r;

    }


    float[] getValues() {
        return values;
    }

    int[] BestOption(int[] pDice) {
        float value = 0;
        int r = 0;

        for (int j = 0; j < values.length; j++) {
            int[] diceNeeded = diceFromNumber(j);
            boolean possible = true;
            for (int k = 0; k < pDice.length; k++) {
                if (diceNeeded[k] > pDice[k]) {
                    possible = false;
                }
            }
            if (possible & value < values[j]) {
                value = values[j];
                r = j;
            }
        }
        return diceFromNumber(r);
    }

    State BestOptionState(int[] pDice) {
        float value = 0;
        int r = 0;

        for (int j = 0; j < values.length; j++) {
            int[] diceNeeded = diceFromNumber(j);
            boolean possible = true;


            for (int k = 0; k < pDice.length; k++) {
                if (diceNeeded[k] > pDice[k]) {
                    possible = false;
                }
            }

            if (possible) if (value < values[j]) {
                value = values[j];
                r = j;
            }
        }
        return bestpossiblities[r];
    }

    State BestOptionDifference(int[] pDice) {
        float value = 0;
        int r = 0;

        for (int j = 0; j < values.length; j++) {
            int[] diceNeeded = diceFromNumber(j);
            boolean possible = true;

            for (int k = 0; k < pDice.length; k++) {
                if (diceNeeded[k] > pDice[k]) {
                    possible = false;
                    break;
                }
            }

            if (possible) if (value < values[j]) {
                value = values[j];
                r = j;
            }
        }
        return bestrequieremts[r];
    }

    float NoDiceValue() {
        return values[461];
    }


}


