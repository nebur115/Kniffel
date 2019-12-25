package rechner.ai.kniffel.kniffel;

public class NodeGeneratingRunnable implements Runnable {
    private boolean[] privategefuellt = new boolean[13];

    public NodeGeneratingRunnable(boolean[] pgefuellt){
        privategefuellt = pgefuellt;
    }
    @Override
    public void run() {

    }

    public boolean[] getgefuellt() {
        return privategefuellt;
    }
}
