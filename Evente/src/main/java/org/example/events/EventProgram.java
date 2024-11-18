package org.example.events;

import java.util.ArrayList;
import java.util.List;

public class EventProgram {

    ArrayList<Rezervimi>rezervimis;

    public EventProgram(){
        this.rezervimis=new ArrayList<>();
    }

    public void addRezervimi(Rezervimi rezervimi){
        rezervimis.add(rezervimi);
    }

    public void displayProgram(List<Rezervimi> rezervimis) {
        System.out.println("-------------------------------");
        System.out.println("Programi i eventeve: ");

        for (Rezervimi rezervimi : rezervimis) {
            System.out.println("Emri i rezervatorit: " + rezervimi.getEmriRezervatorit());
            System.out.println("Emri i eventit: " + rezervimi.getEmriEventit());
            System.out.println("Kohezgjatja e eventit: " + rezervimi.getKohezgjatjaEventit());
            System.out.println("Data e eventit: " + (rezervimi.getDataEventit() != null ? rezervimi.getDataEventit() : "Nuk eshte specifikuar"));
            System.out.println("Numri i pjesmarresve: " + rezervimi.getNumriPresonave());
            System.out.println("Data e rezervimit: " + rezervimi.getDataRezervimit());
            System.out.println("-------------------------------");
        }
    }
}
