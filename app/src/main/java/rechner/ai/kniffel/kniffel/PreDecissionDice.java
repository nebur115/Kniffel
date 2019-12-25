package rechner.ai.kniffel.kniffel;

class PreDecissionDice {

    PreDecissionDice(PostDecissionDice postDecissionDice){
        BestOptionValues(postDecissionDice);
    }

    private float[] values = new float[252];

    private void BestOptionValues(PostDecissionDice pPostDecissionDice){
        float[] givenValues = pPostDecissionDice.getValues();
        for(int i=0; i<values.length;i++){
            for (int j=0; j<MainActivity.genereallOptions[i].size(); j++){
                if(values[i]<givenValues[MainActivity.genereallOptions[i].get(j)])
                    values[i]=givenValues[MainActivity.genereallOptions[i].get(j)];

            }
        }
    }

    float[] getValues() {
        return values;
    }

    static int[] diceFromNumber(int pinput){
        int[] r = new int[6];
        int input = pinput;
        int dice = 0;

        if(input<1){
            r[0]=5-dice;
            dice = 5;

        }else if(input<6){
            r[0]=4-dice;
            dice = 4;
            input= input-1;
        }else if(input<21){
            r[0]=3-dice;
            dice = 3;
            input= input-6;
        }else if(input<56){
            r[0]=2-dice;
            dice = 2;
            input= input-21;
        }else if(input<126){
            r[0]=1-dice;
            dice = 1;
            input= input-56;
        }else{
            r[0]=0-dice;
            dice = 0;
            input= input-126;
        }

        if(input<1){
            r[1]=5-dice;
            dice = 5;

        }else if(input<5){
            r[1]=4-dice;
            dice = 4;
            input= input-1;
        }else if(input<15){
            r[1]=3-dice;
            dice = 3;
            input= input-5;
        }else if(input<35){
            r[1]=2-dice;
            dice = 2;
            input= input-15;
        }else if(input<70){
            r[1]=1-dice;
            dice = 1;
            input= input-35;
        }else{
            r[1]=0-dice;
            dice = 0;
            input= input-70;
        }

        if(input<1){
            r[2]=5-dice;
            dice = 5;

        }else if(input<4){
            r[2]=4-dice;
            dice = 4;
            input= input-1;
        }else if(input<10){
            r[2]=3-dice;
            dice = 3;
            input= input-4;
        }else if(input<20){
            r[2]=2-dice;
            dice = 2;
            input= input-10;
        }else if(input<35){
            r[2]=1-dice;
            dice = 1;
            input= input-20;
        }else{
            r[2]=0-dice;
            dice = 0;
            input= input-35;
        }


        if(input<1){
            r[3]=5-dice;
            dice = 5;

        }else if(input<3){
            r[3]=4-dice;
            dice = 4;
            input= input-1;
        }else if(input<6){
            r[3]=3-dice;
            dice = 3;
            input= input-3;
        }else if(input<10){
            r[3]=2-dice;
            dice = 2;
            input= input-6;
        }else if(input<15){
            r[3]=1-dice;
            dice = 1;
            input= input-10;
        }else{
            r[3]=0-dice;
            dice = 0;
            input= input-15;
        }

        if(input<1){
            r[4]=5-dice;
            dice = 5;
        }else if(input<2){
            r[4]=4-dice;
            dice = 4;
        }else if(input<3){
            r[4]=3-dice;
            dice = 3;
        }else if(input<4){
            r[4]=2-dice;
            dice = 2;
        }else if(input<5){
            r[4]=1-dice;
            dice = 1;
        }else{
            r[4]=0-dice;
            dice = 0;
        }

        r[5]=5-dice;

        return r;

    }


}
