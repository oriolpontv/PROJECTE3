package servidorchat;

import java.util.Observable;

public class MissatgeXat extends Observable{

    private String missatge;
    
    public MissatgeXat(){
    }
    
    public String getMissatge(){
        return missatge;
    }
    
    public void setMissatge(String missatge){
        this.missatge = missatge;
        // Indica que el missatge canvia
        this.setChanged();
        this.notifyObservers(this.getMissatge());
    }
}