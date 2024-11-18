package org.example.datas;

import java.util.ArrayList;

public class User {
    String emri;
    String mbiemri;
    String nrTel;
    String email;
    String adresa;
    int ID;

    private boolean isLoggeddIn = false;

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getMbiemri() {
        return mbiemri;
    }

    public void setMbiemri(String mbiemri) {
        this.mbiemri = mbiemri;
    }

    public String getNrTel() {
        return nrTel;
    }

    public void setNrTel(String nrTel) {
        this.nrTel = nrTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private static ArrayList<User> registeredUsers = new ArrayList<>();

    public void register(String emri, String mbiemri, String nrTel, String email, String adresa, int ID) {
        if (!isRegistered(email, ID)) {
            User newUser = new User();
            newUser.setEmri(emri);
            newUser.setMbiemri(mbiemri);
            newUser.setNrTel(nrTel);
            newUser.setEmail(email);
            newUser.setAdresa(adresa);
            newUser.setID(ID);

            registeredUsers.add(newUser);
            System.out.println("Ju u regjistruat me sukses!");
        } else {
            System.out.println("Ju jeni regjistruar me pare.");
        }
    }

    public boolean isRegistered(String email, int ID) {
        for (User user : registeredUsers) {
            if (user.getEmail().equals(email) && user.getID() == ID) {
                return true;
            }
        }
        return false;
    }

    public boolean isLogin(String email, int ID) {
        if (isRegistered(email, ID)) {
            System.out.println("Ju jeni kyqur me sukses!");
            return true;
        } else {
            System.out.println("Nuk mund te kyqeni, user-i nuk u gjend.");
            return false;
        }
    }

    public void userInfo() {
        System.out.println("Emri: " + getEmri());
        System.out.println("Mbiemri: " + getMbiemri());
        System.out.println("Numri telefonit: " + getNrTel());
        System.out.println("Email: " + getEmail());
        System.out.println("Adresa: " + getAdresa());
        System.out.println("ID: " + getID());
    }
}

