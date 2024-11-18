package org.example.events;

import org.example.enums.MENU;
import org.example.enums.SEZONA;
import org.example.enums.Salla;

public class Oferta {

    String oferta;
    SEZONA sezona;
    MENU menu;
    Salla salla;

    public Oferta(String oferta,SEZONA sezona, MENU menu, Salla salla) {
        this.oferta = oferta;
        this.sezona = sezona;
        this.menu = menu;
        this.salla = salla;
    }
    public void displayOferta() {
        System.out.println(oferta);
        System.out.println("Ne sezonen: " + sezona);
        System.out.println("Me menu: " + menu);
        System.out.println("Ne shfrytezim salla e: " + salla);
    }

    public void searchOferta(SEZONA llojiSezones,MENU llojiMenus,Salla llojiSallave,Oferta [] listaKerkimit) {
        for(int i=0; i<listaKerkimit.length; i++) {
            if(listaKerkimit[i].sezona == llojiSezones || listaKerkimit[i].menu == llojiMenus || listaKerkimit[i].salla == llojiSallave){
                listaKerkimit[i].displayOferta();
            }
        }
    }

    public static SEZONA findBySezona(String userAns) {
        if (userAns.contains("vere")) {
            return SEZONA.VERE;
        } else if (userAns.contains("pranvere")) {
            return SEZONA.PRANVERE;
        } else if (userAns.contains("vjeshte")) {
            return SEZONA.VJESHTE;
        } else if (userAns.contains("dimer")) {
            return SEZONA.DIMER;
        } else {
            return null;
        }
    }

    public static Salla findBySalla(String userAns) {
        if(userAns.contains("madhe")) {
            return Salla.MADHE;
        }else if(userAns.contains("vogel")) {
            return Salla.VOGEL;
        }else if(userAns.contains("mesme")) {
            return Salla.MESME;
        } else {
            return null;
        }
    }

    public static MENU findByMenu(String userAns) {
        if(userAns.contains("lire")) {
            return MENU.LIRE;
        } else if(userAns.contains("mesatare")) {
            return MENU.MESATARE;
        } else if (userAns.contains("shtrenjt")) {
            return MENU.SHTRENJT;
        } else {
            return null;
        }
    }
}

