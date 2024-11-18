package org.example;

import org.example.dao.DatabaseConfig;
import org.example.dao.ReservationDAO;
import org.example.dao.ReservationDetailsDAO;
import org.example.dao.UserDAO;
import org.example.datas.Invoice;
import org.example.datas.ReservationDetails;
import org.example.datas.User;
import org.example.enums.MENU;
import org.example.enums.SEZONA;
import org.example.enums.Salla;
import org.example.events.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main extends Thread {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try (Connection connection = DatabaseConfig.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Failed to connect to PostgreSQL.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner scan = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        User user = new User();
        Invoice invoice = new Invoice(2000, 3000);
        Rezervimi rezervimi = new Rezervimi();
        EventProgram eventProgram = new EventProgram();
        ReservationDAO reservationDAO = new ReservationDAO();
        ReservationDetailsDAO reservationDetailsDAO = new ReservationDetailsDAO();

        NrPjesmarresve nrPjesmarresve = () -> 200;
        NrPjesmarresve nrPjesmarresve1 = () -> 150;
        NrPjesmarresve nrPjesmarresve2 = () -> 300;

        Organizator[] organizators = {new Organizator("Erza", "+38349000000")};

        int price1 = 5000;
        Oferta oferta1 = new Oferta("1.Vjen me cmim prej " + price1 + " euro, per 120 persona", SEZONA.VJESHTE, MENU.MESATARE, Salla.MESME);
        int price2 = 1000;
        Oferta oferta2 = new Oferta("2.Vjen me cmim prej " + price2 + " euro, per 40 persona", SEZONA.DIMER, MENU.LIRE, Salla.VOGEL);
        int price3 = 10000;
        Oferta oferta3 = new Oferta("3.Vjen me cmim prej " + price3 + " euro, per 200 persona", SEZONA.VERE, MENU.SHTRENJT, Salla.MADHE);
        Oferta[] listaOfertave = {oferta1, oferta2, oferta3};

        System.out.println("Pershendetje, miresevini ne rezervimin e eventeve!");


        User loggedInUser = null;
        while (loggedInUser == null)  {
            System.out.println("Deshironi te regjistroheni apo te beheni log in?");
            String regLog = scan.nextLine().toLowerCase();

            if (regLog.equals("register")) {

                User newUser = new User();

                System.out.print("Emri: ");
                while (true) {
                    String emri = scan.nextLine().trim();
                    if (!emri.isEmpty()) {
                        newUser.setEmri(emri);
                        break;
                    } else {
                        System.out.println("Ju lutem shkruani emrin. Ky fushe nuk mund te lihet bosh.");
                    }
                }

                System.out.print("Mbiemri: ");
                while (true) {
                    String mbiemri = scan.nextLine().trim();
                    if (!mbiemri.isEmpty()) {
                        newUser.setMbiemri(mbiemri);
                        break;
                    } else {
                        System.out.println("Ju lutem shkruani mbiemrin. Ky fushe nuk mund te lihet bosh.");
                    }
                }

                while (true) {
                    System.out.print("Numri kontaktues: ");
                    String contactNumber = scan.nextLine().replaceAll("\\s", "").trim();
                    if (!contactNumber.isEmpty() && contactNumber.matches("\\+383\\d{8}")) {
                        newUser.setNrTel(contactNumber);
                        try {
                            if (!userDAO.isContactNumberRegistered(contactNumber)) {
                                break;
                            } else {
                                System.out.println("Ky numër kontakti është regjistruar tashmë në një llogari tjetër. Ju lutem shkruani nje numer unik.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Gabim gjate kontrollimit te numrit te kontaktit.");
                        }
                    } else {
                        System.out.println("Ju lutem shkruani numrin ne formatin +383XXXXXXXX. Ky fushe nuk mund te lihet bosh.");
                    }
                }

                while (true) {
                    System.out.print("Email: ");
                    String email = scan.nextLine().trim();
                    if (!email.isEmpty() && email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".")) {
                        newUser.setEmail(email);
                        try {
                            if (!userDAO.isEmailRegistered(email)) {
                                break;
                            } else {
                                System.out.println("Ky email është regjistruar tashmë në një llogari tjetër. Ju lutem shkruani nje email unik.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Gabim gjate kontrollimit te email-it.");
                        }
                    } else {
                        System.out.println("Ju lutem shkruani nje email te vlefshem. Ky fushe nuk mund te lihet bosh.");
                    }
                }

                System.out.print("Adresa: ");
                while (true) {
                    String adresa = scan.nextLine().trim();
                    if (!adresa.isEmpty()) {
                        newUser.setAdresa(adresa);
                        break;
                    } else {
                        System.out.println("Ju lutem shkruani adresen. Ky fushe nuk mund te lihet bosh.");
                    }
                }

                while (true) {
                    System.out.print("ID: ");
                    String idInput = scan.nextLine().trim();
                    if (!idInput.isEmpty()) {
                        try {
                            int id = Integer.parseInt(idInput);
                            newUser.setID(id);
                            if (!userDAO.isIdRegistered(id)) {
                                break;
                            } else {
                                System.out.println("Ky ID është regjistruar tashmë. Ju lutem shkruani nje ID unik.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ju lutem shkruani numer te vlefshem per 'ID'");
                        } catch (SQLException e) {
                            System.out.println("Gabim gjate kontrollimit te ID-se.");
                        }
                    } else {
                        System.out.println("Ju lutem shkruani ID. Ky fushe nuk mund te lihet bosh.");
                    }
                }

                user.register(user.getEmri(), user.getMbiemri(), user.getNrTel(), user.getEmail(), user.getAdresa(), user.getID());


                try {
                    userDAO.addUser(newUser);
                    System.out.println("Ju u regjistruat me sukses ne databaze!");
                    System.out.println("Tani mund te beni log in me kredencialet tuaja.");
                } catch (SQLException e) {
                    System.out.println("Ka ndodhur një gabim gjate regjistrimit.");
                    e.printStackTrace();
                }

            } else if (regLog.equalsIgnoreCase("log in")) {
                System.out.print("Email: ");
                String email = scan.nextLine();

                System.out.print("ID: ");
                int userId;
                while (true) {
                    try {
                        userId = Integer.parseInt(scan.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Ju lutem shkruani numer te vlefshem per 'ID'");
                    }
                }

                try {
                    User userFromDB = userDAO.getUserByEmail(email);
                    if (userFromDB != null && userFromDB.getID() == userId) {
                        System.out.println("Login successful!");
                        loggedInUser = userFromDB;
                        break;
                    } else {
                        System.out.println("Ju lutem shkruani te dhenat e sakta!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Ka ndodhur nje gabim gjate procesit te hyrjes.");
                }
            }
        }

            boolean exitProgram = false;


            while (!exitProgram) {
                System.out.println("----------------------");
                System.out.println("Zgjidh nje numer!");
                System.out.println("1. Profili im");
                System.out.println("2. Ofertat");
                System.out.println("3. Rezervo");
                System.out.println("4. Shiko rezervimet");
                System.out.println("5. Fatura");
                System.out.println("6. Dil");
                System.out.println("----------------------");

                int userChoice = scan.nextInt();
                scan.nextLine();

                switch (userChoice) {
                    case 1:
                        user.userInfo();
                        break;

                    case 2:
                        System.out.println("Deshironi t'i shihni te gjitha ofertat apo jo?");
                        String userAnsw = scan.nextLine().toLowerCase();

                        if (userAnsw.equalsIgnoreCase("po")) {
                            Thread offerThread = new Thread(() -> {
                                try {
                                    System.out.println("Ofertat jane duke u ngarkuar...");
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    System.out.println("Procesi i ngarkimit te ofertave u nderpre.");
                                }
                            });

                            offerThread.start();

                            try {
                                offerThread.join();
                            } catch (InterruptedException e) {
                                System.out.println("Procesi i ngarkimit te ofertave u nderpre.");
                            }

                            try {
                                for (int i = 0; i < listaOfertave.length; i++) {
                                    Thread.sleep(1000); // Delay between offers
                                    System.out.println("Oferta " + (i + 1));
                                    listaOfertave[i].displayOferta();
                                    System.out.println("--------------------");
                                }
                            } catch (InterruptedException ex) {
                                System.out.println("Nje gabim ndodhi.");
                                System.exit(0);
                            }


                            while (true) {

                            System.out.println("Deshironi te zgjidhni njeren nga ofertat? (po/jo)");
                            String zgjedhja = scan.nextLine().toLowerCase();

                            if (zgjedhja.equalsIgnoreCase("po")) {

                                int offerChoice;
                                boolean validOffer = false;

                                while (!validOffer) {
                                    System.out.println("Shtypni numrin e ofertes: ");
                                    offerChoice = scan.nextInt();
                                    scan.nextLine();

                                     Rezervimi offerRezervimi = new Rezervimi();
                                     int offerPrice = 0, menuPrice = 0, numGuests = 0;


                                    switch (offerChoice) {
                                        case 1:
                                            System.out.println("Oferta ka cmimin prej " + price1 + " euro, jepni edhe te dhenat tjera te nevojshme pastaj ju mund te vazhdoni tek fatura!");
                                            offerPrice = price1;
                                            menuPrice = 45;
                                            numGuests = 120;
                                            validOffer = true;
                                            break;
                                         case 2:
                                            System.out.println("Oferta ka cmimin prej " + price2 + " euro, jepni edhe te dhenat tjera te nevojshme pastaj ju mund te vazhdoni tek fatura!");
                                            offerPrice = price2;
                                            menuPrice = 30;
                                            numGuests = 40;
                                            validOffer = true;
                                            break;
                                         case 3:
                                             System.out.println("Oferta ka cmimin prej " + price3 + " euro, jepni edhe te dhenat tjera te nevojshme pastaj ju mund te vazhdoni tek fatura!");
                                             offerPrice = price3;
                                             menuPrice = 60;
                                             numGuests = 200;
                                             validOffer = true;
                                             break;
                                        default:
                                             System.out.println("Ju lutem shtypni nje numer te sakte te ofertave!");
                                             break;
                            }

                            if (validOffer) {

                                offerRezervimi.setNumriPresonave(numGuests);

                                System.out.println("Shkruani emrin tuaj per rezervimin: ");
                                offerRezervimi.setEmriRezervatorit(scan.nextLine());

                                System.out.println("Shkruani emrin e eventit: ");
                                offerRezervimi.setEmriEventit(scan.nextLine());

                                System.out.println("Shkruani kohezgjatjen e eventit: ");
                                while (true) {
                                    try {
                                        offerRezervimi.setKohezgjatjaEventit(Double.parseDouble(scan.nextLine()));
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Ju lutem shkruani numer te vlefshem per 'Kohezgjatja'");
                                    }
                                }

                                offerRezervimi.setDataRezervimit(LocalDate.now());
                                System.out.println("Data e rezervimit eshte vendosur automatikisht: " + offerRezervimi.getDataRezervimit());

                                LocalDate dataEv = null;
                                while (dataEv == null) {
                                    System.out.print("Data e eventit (yyyy-MM-dd): ");
                                    String dateInput = scan.nextLine();
                                    try {
                                        dataEv = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    } catch (DateTimeParseException e) {
                                        System.out.println("Data e pavlefshme! Ju lutem shkruani daten ne formatin yyyy-MM-dd.");
                                    }
                                }

                                rezervimi.setDataEventit(dataEv);
                                offerRezervimi.setDataEventit(dataEv);

                                try {
                                    reservationDAO.addReservation(offerRezervimi, loggedInUser.getID());
                                    System.out.println("Rezervimi per oferten eshte shtuar me sukses ne databaze!");
                                } catch (SQLException e) {
                                    System.out.println("Gabim gjate shtimit te rezervimit ne databaze.");
                                    e.printStackTrace();
                                }

                                System.out.println("Nese deshironi te shtoni dekorim dhe muzike:\n1. Dekor - 2000 euro\n2. Muzike - 3000 euro\n3. Te dyja\n0. Asnjera");
                                int addServiceChoice = scan.nextInt();
                                scan.nextLine();

                                int decorationPrice = 0, musicPrice = 0;
                                switch (addServiceChoice) {
                                    case 1:
                                        decorationPrice = 2000;
                                        break;
                                    case 2:
                                        musicPrice = 3000;
                                        break;
                                    case 3:
                                        decorationPrice = 2000;
                                        musicPrice = 3000;
                                        break;
                                    default:
                                        System.out.println("Nuk u shtua dekor ose muzike.");
                                        break;
                                }

                                Thread reservationThread = new Thread(() -> {
                                    try {
                                        System.out.println("Duke shtuar rezervimin...");
                                        Thread.sleep(2000); // 2-second delay to simulate reservation processing
                                    } catch (InterruptedException e) {
                                        System.out.println("Procesi i shtimit te rezervimit u nderpre.");
                                    }
                                });

                                reservationThread.start();


                                try {
                                    reservationThread.join();
                                } catch (InterruptedException e) {
                                    System.out.println("Procesi i shtimit te rezervimit u nderpre.");
                                }

                                ReservationDetails reservationDetails = new ReservationDetails(numGuests, menuPrice, decorationPrice, musicPrice, offerPrice, true, loggedInUser.getID());
                                invoice.addReservation(reservationDetails);

                                try {
                                    reservationDetailsDAO.addReservationDetails(reservationDetails);
                                    System.out.println("Reservation details successfully added to database.");
                                } catch (SQLException e) {
                                    System.out.println("Error adding reservation details to database.");
                                    e.printStackTrace();
                                }

                                offerRezervimi.setDataRezervimit(LocalDate.now());
                                eventProgram.addRezervimi(offerRezervimi);

                                System.out.println("Rezervimi eshte shtuar me sukses!");
                                break;
                            }
                        } break;
                    } else if (zgjedhja.equalsIgnoreCase("jo")) {
                                System.out.println("Ju mund te vazhdoni me rezervimin manual!");
                                break;
                            } else {
                                System.out.println("Pergjigju me po/jo!");
                            }
                        }
                    } else if (userAnsw.equalsIgnoreCase("jo")) {
                            SEZONA zgjedhjaSezones = null;
                            String userAnswe = null;

                            while (zgjedhjaSezones == null) {
                                System.out.println("Per cfare jeni te interesuar\n Pranvere?\n Vere?\n Vjeshte?\n Dimer?\n");
                                userAnswe = scan.nextLine().trim().toLowerCase();

                                switch (userAnswe) {
                                    case "pranvere":
                                    case "vere":
                                    case "vjeshte":
                                    case "dimer":
                                        zgjedhjaSezones = Oferta.findBySezona(userAnswe);
                                        break;
                                    default:
                                        System.out.println("Ju lutem shkruani nje nga opsionet e sakta: Pranvere, Vere, Vjeshte, Dimer.");
                                        break;
                                }
                            }

                            Salla zgjedhjaSalles = Oferta.findBySalla(userAnswe);
                            MENU zgjedhjaMenus = Oferta.findByMenu(userAnswe);


                            oferta1.searchOferta(zgjedhjaSezones, zgjedhjaMenus, zgjedhjaSalles, listaOfertave);


                            System.out.println("Deshironi ta shfrytezoni?");
                            String zgjedhja = scan.nextLine().toLowerCase();

                            if (zgjedhja.equalsIgnoreCase("po")) {

                                int offerNumber;
                                boolean validOffer = false;

                                while (!validOffer) {
                                    System.out.println("Shtyp numrin e ofertes: ");
                                    offerNumber = scan.nextInt();
                                    scan.nextLine();

                                    Rezervimi offerRezervimi = new Rezervimi();
                                    int offerPrice = 0, menuPrice = 0, numGuests = 0;

                                    switch (offerNumber) {
                                        case 1:
                                            System.out.println("Oferta ka cmimin prej " + price1 + " euro, jepni edhe te dhenat tjera te nevojshme pastaj ju mund te vazhdoni tek fatura!");
                                            offerPrice = price1;
                                            menuPrice = 45;
                                            numGuests = 120;
                                            validOffer = true;
                                            break;
                                        case 2:
                                            System.out.println("Oferta ka cmimin prej " + price2 + " euro, jepni edhe te dhenat tjera te nevojshme pastaj ju mund te vazhdoni tek fatura!");
                                            offerPrice = price2;
                                            menuPrice = 30;
                                            numGuests = 40;
                                            validOffer = true;
                                            break;
                                        case 3:
                                            System.out.println("Oferta ka cmimin prej " + price3 + " euro, jepni edhe te dhenat tjera te nevojshme pastaj ju mund te vazhdoni tek fatura!");
                                            offerPrice = price3;
                                            menuPrice = 60;
                                            numGuests = 200;
                                            validOffer = true;
                                            break;
                                        default:
                                            System.out.println("Ju lutem shtypni nje numer te sakte te ofertave!");
                                            break;
                                    }

                                    if (validOffer) {

                                        offerRezervimi.setNumriPresonave(numGuests);

                                        System.out.println("Shkruani emrin tuaj per rezervimin: ");
                                        offerRezervimi.setEmriRezervatorit(scan.nextLine());

                                        System.out.println("Shkruani emrin e eventit: ");
                                        offerRezervimi.setEmriEventit(scan.nextLine());

                                        System.out.println("Shkruani kohezgjatjen e eventit: ");
                                        while (true) {
                                            try {
                                                offerRezervimi.setKohezgjatjaEventit(Double.parseDouble(scan.nextLine()));
                                                break;
                                            } catch (NumberFormatException e) {
                                                System.out.println("Ju lutem shkruani numer te vlefshem per 'Kohezgjatja'");
                                            }
                                        }

                                        offerRezervimi.setDataRezervimit(LocalDate.now());
                                        System.out.println("Data e rezervimit eshte vendosur automatikisht: " + offerRezervimi.getDataRezervimit());

                                        LocalDate dataEv = null;
                                        while (dataEv == null) {
                                            System.out.print("Data e eventit (yyyy-MM-dd): ");
                                            String dateInput = scan.nextLine();
                                            try {
                                                dataEv = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Data e pavlefshme! Ju lutem shkruani daten ne formatin yyyy-MM-dd.");
                                            }
                                        }

                                        rezervimi.setDataEventit(dataEv);
                                        offerRezervimi.setDataEventit(dataEv);

                                        try {
                                            reservationDAO.addReservation(offerRezervimi, loggedInUser.getID());
                                            System.out.println("Rezervimi per oferten eshte shtuar me sukses ne databaze!");
                                        } catch (SQLException e) {
                                            System.out.println("Gabim gjate shtimit te rezervimit ne databaze.");
                                            e.printStackTrace();
                                        }

                                        System.out.println("Nese deshironi te shtoni dekorim dhe muzike:\n1. Dekor - 2000 euro\n2. Muzike - 3000 euro\n3. Te dyja\n0. Asnjera");
                                        int addServiceChoice = scan.nextInt();
                                        scan.nextLine();

                                        int decorationPrice = 0, musicPrice = 0;
                                        switch (addServiceChoice) {
                                            case 1:
                                                decorationPrice = 2000;
                                                break;
                                            case 2:
                                                musicPrice = 3000;
                                                break;
                                            case 3:
                                                decorationPrice = 2000;
                                                musicPrice = 3000;
                                                break;
                                            default:
                                                System.out.println("Nuk u shtua dekor ose muzike.");
                                                break;
                                        }

                                        Thread reservationThread = new Thread(() -> {
                                            try {
                                                System.out.println("Duke shtuar rezervimin...");
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                System.out.println("Procesi i shtimit te rezervimit u nderpre.");
                                            }
                                        });

                                        reservationThread.start();


                                        try {
                                            reservationThread.join();
                                        } catch (InterruptedException e) {
                                            System.out.println("Procesi i shtimit te rezervimit u nderpre.");
                                        }

                                        ReservationDetails reservationDetails = new ReservationDetails(numGuests, menuPrice, decorationPrice, musicPrice, offerPrice, true, loggedInUser.getID());
                                        invoice.addReservation(reservationDetails);

                                        try {
                                            reservationDetailsDAO.addReservationDetails(reservationDetails);
                                            System.out.println("Reservation details successfully added to database.");
                                        } catch (SQLException e) {
                                            System.out.println("Error adding reservation details to database.");
                                            e.printStackTrace();
                                        }

                                        offerRezervimi.setDataRezervimit(LocalDate.now());
                                        eventProgram.addRezervimi(offerRezervimi);

                                        System.out.println("Rezervimi eshte shtuar me sukses!");
                                        break;
                                    }
                                }

                            } else if (zgjedhja.equalsIgnoreCase("jo")) {
                                System.out.println("Ju mund te vazhdoni me rezervimin manual!");
                            }
                        }
                        break;


                    case 3:
                        Rezervimi newRezervimi = new Rezervimi();

                        System.out.println("Per rezervim shkruani prap emrin tuaj: ");
                        newRezervimi.setEmriRezervatorit(scan.nextLine());

                        System.out.println("Shkruani emrin e eventit: ");
                        newRezervimi.setEmriEventit(scan.nextLine());

                        System.out.println("Shkruani kohezgjatjen e eventit: ");
                        double kohezgjatja;
                        while (true) {
                            try {
                                kohezgjatja = Double.parseDouble(scan.nextLine());
                                newRezervimi.setKohezgjatjaEventit(kohezgjatja);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Ju lutem shkruani numer te vlefshem per 'Kohezgjatja'");
                            }
                        }

                        newRezervimi.setDataRezervimit(LocalDate.now());
                        System.out.println("Data e rezervimit eshte vendosur automatikisht: " + newRezervimi.getDataRezervimit());


                        LocalDate dataEv = null;
                        while (dataEv == null) {
                            System.out.print("Data e eventit (yyyy-MM-dd): ");
                            String dateInput = scan.nextLine();
                            try {
                                dataEv = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            } catch (DateTimeParseException e) {
                                System.out.println("Data e pavlefshme! Ju lutem shkruani daten ne formatin yyyy-MM-dd.");
                            }
                        }

                        newRezervimi.setDataEventit(dataEv);
                        rezervimi.setDataEventit(dataEv);

                        System.out.println("Plani i eventit:");
                        System.out.println("1. Plan per 300 te ftuar!");
                        System.out.println("2. Plan per 200 te ftuar!");
                        System.out.println("3. Plan per 150 te ftuar!");
                        System.out.println("4. Asnjeren!");
                        System.out.println("Shkruaj planin tend: ");
                        int choice = scan.nextInt();
                        scan.nextLine();

                        int numGuests = 0;
                        switch (choice) {
                            case 1:
                                numGuests = 300;
                                new Eventet(nrPjesmarresve2, organizators).displayEventet();
                                break;
                            case 2:
                                numGuests = 200;
                                new Eventet(nrPjesmarresve, organizators).displayEventet();
                                break;
                            case 3:
                                numGuests = 150;
                                new Eventet(nrPjesmarresve1, organizators).displayEventet();
                                break;
                            case 4:
                                System.out.println("Ju nuk zgjodhet asnje! Ju lutem shkruani numrin e te ftuarve:");
                                numGuests = scan.nextInt();
                                scan.nextLine();
                                break;
                            default:
                                System.out.println("Shkruaj nje numer te sakte!");
                                break;
                        }
                        invoice.setNrFtuarve(numGuests);
                        newRezervimi.setNumriPresonave(numGuests);

                        try {
                            reservationDAO.addReservation(newRezervimi, loggedInUser.getID());
                            System.out.println("Rezervimi eshte shtuar me sukses ne databaze!");
                        } catch (SQLException e) {
                            System.out.println("Gabim gjate shtimit te rezervimit ne databaze.");
                            e.printStackTrace();
                        }

                        System.out.println("Zgjidh njeren nga menyt:\n1. Menu e lire --- 30 euro personi\n2. Menu mesatare --- 45 euro personi\n3. Menu e shtrenjte --- 60 euro personi");
                        int menyChoice = scan.nextInt();
                        scan.nextLine();

                        int menuPrice = 0;
                        switch (menyChoice) {
                            case 1:
                                menuPrice = 30;
                                break;
                            case 2:
                                menuPrice = 45;
                                break;
                            case 3:
                                menuPrice = 60;
                                break;
                            default:
                                System.out.println("Zgjedhje e pavlefshme!");
                                break;
                        }
                        invoice.setMenuPrice(menuPrice);

                        System.out.println("Nese deshironi te shtoni dekorim dhe muzike:\n1. Dekor - 2000 euro\n2. Muzike - 3000 euro\n3. Te dyja\n0. Asnjera");
                        int addServiceChoice = scan.nextInt();
                        scan.nextLine();

                        int decorationPrice = 0, musicPrice = 0;

                        switch (addServiceChoice) {
                            case 1:
                                decorationPrice = 2000;
                                break;
                            case 2:
                                musicPrice = 3000;
                                break;
                            case 3:
                                decorationPrice = 2000;
                                musicPrice = 3000;
                                break;
                            default:
                                System.out.println("Nuk u shtua dekor ose muzike.");
                                break;
                        }

                        Thread reservationThread = new Thread(() -> {
                            try {
                                System.out.println("Duke shtuar rezervimin...");
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                System.out.println("Procesi i shtimit te rezervimit u nderpre.");
                            }
                        });

                        reservationThread.start();

                        try {
                            reservationThread.join();
                        } catch (InterruptedException e) {
                            System.out.println("Procesi i shtimit te rezervimit u nderpre.");
                        }

                        ReservationDetails reservationDetails = new ReservationDetails(newRezervimi.getNumriPresonave(), menuPrice, decorationPrice, musicPrice, 0, false, loggedInUser.getID());
                        invoice.addReservation(reservationDetails);

                        try {
                            reservationDetailsDAO.addReservationDetails(reservationDetails);
                            System.out.println("Reservation details successfully added to database.");
                        } catch (SQLException e) {
                            System.out.println("Error adding reservation details to database.");
                            e.printStackTrace();
                        }

                        eventProgram.addRezervimi(newRezervimi);
                        System.out.println("Rezervimi eshte shtuar me sukses!");

                        System.out.println("Deshironi te shtoni tjeter rezervim?");
                        String tjeter = scan.nextLine().toLowerCase();
                        if (tjeter.equalsIgnoreCase("po")) {
                        } else {
                            System.out.println("Ju faleminderit!");
                        }
                        break;

                    case 4:
                        try {
                            int userId = loggedInUser.getID();
                            System.out.println("Fetching reservations for user ID: " + userId);
                            List<Rezervimi> userReservations = reservationDAO.getReservationsByUserId(userId);
                            if (userReservations.isEmpty()) {
                                System.out.println("No reservations found for user.");
                            } else {
                                eventProgram.displayProgram(userReservations);
                            }
                        } catch (SQLException e) {
                            System.out.println("Error fetching reservations.");
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        List<ReservationDetails> unpaidReservations;
                        try {
                            unpaidReservations = reservationDetailsDAO.getUnpaidReservationDetails(loggedInUser.getID());
                        } catch (SQLException e) {
                            System.out.println("Error fetching unpaid invoices.");
                            e.printStackTrace();
                            break;
                        }

                        invoice.printInvoice(unpaidReservations);

                        System.out.println("Fatura eshte shfaqur.");

                        double totalAmount = unpaidReservations.stream()
                                .mapToDouble(ReservationDetails::calculateTotal)
                                .sum();

                        if (totalAmount == 0) {
                            System.out.println("Shuma totale eshte zero. Nuk ka nevoje per pagese.");
                            break;
                        }

                        System.out.println("Deshironi te vazhdoni me pagesen? (po/jo)");
                        String proceedToPayment = scan.nextLine().toLowerCase();

                        if (proceedToPayment.equals("po")) {
                            System.out.println("Ju lutem jepni te dhenat e kartes per pagesen:");

                            String cardNumber;
                            while (true) {
                                System.out.print("Numri i kartes (16 shifra, formati XXXX XXXX XXXX XXXX): ");
                                cardNumber = scan.nextLine().replaceAll("\\s", "").trim();
                                if (!cardNumber.isEmpty() && cardNumber.matches("\\d{16}")) {
                                    break;
                                } else {
                                    System.out.println("Ju lutem shkruani numrin e kartes me 16 shifra. Ky fushe nuk mund te lihet bosh.");
                                }
                            }

                            String expirationDate;
                            while (true) {
                                System.out.print("Data e skadimit (MM/YY): ");
                                expirationDate = scan.nextLine().trim();
                                if (!expirationDate.isEmpty() && expirationDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                                    break;
                                } else {
                                    System.out.println("Ju lutem shkruani daten e skadimit ne formatin e sakte (MM/YY). Ky fushe nuk mund te lihet bosh.");
                                }
                            }

                            String cvvCode;
                            while (true) {
                                System.out.print("Kodi CVV (3 shifra): ");
                                cvvCode = scan.nextLine().trim();
                                if (!cvvCode.isEmpty() && cvvCode.matches("\\d{3}")) {
                                    break;
                                } else {
                                    System.out.println("Ju lutem shkruani nje CVV te vlefshem me 3 shifra. Ky fushe nuk mund te lihet bosh.");
                                }
                            }

                            System.out.println("Pagesa eshte duke u procesuar...");

                            Thread paymentThread = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    System.out.println("Procesimi i pageses u nderpre.");
                                    invoice.setPaid(false);
                                }
                            });

                            paymentThread.start();

                            try {
                                paymentThread.join();
                            } catch (InterruptedException e) {
                                System.out.println("Procesimi i pageses u nderpre.");
                                invoice.setPaid(false);
                            }

                            System.out.println("Pagesa u krye me sukses. Ju faleminderit!");


                          for (ReservationDetails reservation : unpaidReservations) {
                                reservationDetailsDAO.markInvoiceAsPaid(reservation.getId());
                               }
                          System.out.println("Te gjitha faturat e papaguara u shenuan si te paguara.");
                        } else {
                            System.out.println("Kur te jeni gati, mund te procedoni me pagesen. Ju faleminderit!");
                        }
                        break;


                    case 6:
                        System.out.println("Ju dolet nga programi!");
                        exitProgram = true;
                        break;

                    default:
                        System.out.println("Ju lutem zgjidhni nje numer te vlefshem!");
                        break;
                }
            }
            scan.close();
        }
    }

