package rechner.ai.kniffel.kniffel;

import android.util.Log;

class State {


    State(int number){
        setNumber(number);
    }
    State(){
        for (int i=0; i<gefuellt.length;i++){
            gefuellt[i]=false;
        }
    }
    State(boolean[] pGefuellt, int pObererValue){
        obererValue=pObererValue;
        gefuellt=pGefuellt;
    }

    private int obererValue = 0;
    private boolean[] gefuellt = new boolean[13];


    private void setNumber(int input){
        obererValue = input%106;
        String sgefuellt = (String.format("%13s", Integer.toBinaryString((input-obererValue)/106))).replace(" ", "0");

        for (int i=0;i<gefuellt.length;i++) {
            gefuellt[i]= sgefuellt.charAt(sgefuellt.length() - i - 1) == '1';
        }
    }

    private int getNumber(boolean stopRecusion) {
        int r=0;
        for (int i = 0; i < gefuellt.length; i++) {
            if(gefuellt[i]) r = r+ (int) Math.pow(2,i);
        }
        State tempState = new State(r*106+obererValue);
        if(!stopRecusion)
            if(!(tempState.getNumber(true)==r*106+obererValue))
                Log.println(Log.ERROR,"Error","Numbererror");

        return r*106+obererValue;
    }

    int getNumber(){
        return getNumber(false);
    }

    boolean[] getBooleanArray() {
        return gefuellt;
    }

    boolean getBoolean(int feld){
        return gefuellt[feld];
    }

    void setBoolean(int feld, boolean wert){
        gefuellt[feld] = wert;
    }

    int getObererValue() {
        return obererValue;
    }

    void setObererValue(int pObererValue){
        obererValue=pObererValue;
    }

    State getState(){
        return new State(getNumber());
    }

}



