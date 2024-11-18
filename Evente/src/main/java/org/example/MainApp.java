package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.dao.ReservationDAO;
import org.example.dao.ReservationDetailsDAO;
import org.example.dao.UserDAO;
import org.example.datas.ReservationDetails;
import org.example.datas.User;
import org.example.events.Rezervimi;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainApp extends Application {
    private User loggedInUser;
    private UserDAO userDAO;
    private ReservationDAO reservationDAO;
    private ReservationDetailsDAO reservationDetailsDAO;

    @Override
    public void start(Stage primaryStage) {

        userDAO = new UserDAO();
        reservationDAO = new ReservationDAO();
        reservationDetailsDAO = new ReservationDetailsDAO();

        primaryStage.setScene(createStartScene(primaryStage));
        primaryStage.setTitle("Events Application");
        primaryStage.show();
    }

    private Scene createStartScene(Stage primaryStage) {
        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Button registerButton = new Button("Register");
        Button loginButton = new Button("Log in");

        registerButton.setStyle("-fx-background-color: #A5CFCF; -fx-font-size: 16px;");
        loginButton.setStyle("-fx-background-color: #A5CFCF; -fx-font-size: 16px;");

        registerButton.setPrefWidth(100);
        loginButton.setPrefWidth(100);

        registerButton.setOnAction(event -> primaryStage.setScene(createRegisterScene(primaryStage)));
        loginButton.setOnAction(event -> primaryStage.setScene(createLoginScene(primaryStage)));

        HBox buttonBox = new HBox(20, registerButton, loginButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(30, titleLabel, buttonBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(mainLayout, 1000, 800);
    }

    private Scene createRegisterScene(Stage primaryStage) {
        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        TextField nameField = new TextField();
        Label name = new Label("Emri:");
        nameField.setPromptText("Emri:");
        nameField.setMaxWidth(300);

        TextField lastNameField = new TextField();
        Label surname = new Label("Mbiemri:");
        lastNameField.setPromptText("Mbiemri:");
        lastNameField.setMaxWidth(300);

        TextField contactField = new TextField();
        Label nr = new Label("Numri kontaktues:");
        contactField.setPromptText("Numri kontaktues:");
        contactField.setMaxWidth(300);

        TextField emailField = new TextField();
        Label emaill = new Label("Email:");
        emailField.setPromptText("Email:");
        emailField.setMaxWidth(300);

        TextField addressField = new TextField();
        Label adress = new Label("Adresa:");
        addressField.setPromptText("Adresa:");
        addressField.setMaxWidth(300);

        TextField idField = new TextField();
        Label di = new Label("ID:");
        idField.setPromptText("ID:");
        idField.setMaxWidth(300);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        Button registerSubmitButton = new Button("Register");
        registerSubmitButton.setOnAction(event -> {
            String emri = nameField.getText().trim();
            String mbiemri = lastNameField.getText().trim();
            String nrTel = contactField.getText().trim();
            String email = emailField.getText().trim();
            String adresa = addressField.getText().trim();
            String idText = idField.getText().trim();

            if (emri.isEmpty() || mbiemri.isEmpty() || nrTel.isEmpty() || email.isEmpty() || adresa.isEmpty() || idText.isEmpty()) {
                messageLabel.setText("Të gjitha fushat janë të detyrueshme.");
                return;
            }

            if (!nrTel.matches("\\+383\\d{8}")) {
                messageLabel.setText("Numri i kontaktit duhet të jetë në formatin +383XXXXXXXX.");
                return;
            }

            if (!email.contains("@") || !email.contains(".") || email.indexOf("@") > email.lastIndexOf(".")) {
                messageLabel.setText("Email-i nuk është në formatin e saktë.");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                messageLabel.setText("ID duhet të jetë një numër.");
                return;
            }

            try {
                if (userDAO.isEmailRegistered(email)) {
                    messageLabel.setText("Ky email është regjistruar tashmë.");
                    return;
                }
                if (userDAO.isIdRegistered(id)) {
                    messageLabel.setText("Ky ID është regjistruar tashmë.");
                    return;
                }

                User newUser = new User();
                newUser.setEmri(emri);
                newUser.setMbiemri(mbiemri);
                newUser.setNrTel(nrTel);
                newUser.setEmail(email);
                newUser.setAdresa(adresa);
                newUser.setID(id);

                userDAO.addUser(newUser);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Ju u regjistruat me sukses!");
                alert.showAndWait();

                primaryStage.setScene(createStartScene(primaryStage));

            } catch (SQLException e) {
                messageLabel.setText("Gabim gjatë regjistrimit. Provoni përsëri.");
                e.printStackTrace();
            }
        });

        Label haveAccountLabel = new Label("Already have an account?");
        Button loginButton = new Button("Log in");
        loginButton.setOnAction(event -> primaryStage.setScene(createLoginScene(primaryStage)));

        HBox loginBox = new HBox(5, haveAccountLabel, loginButton);
        loginBox.setAlignment(Pos.CENTER);

        VBox registerLayout = new VBox(10, titleLabel,name ,nameField, surname,lastNameField, nr,contactField, emaill,emailField, adress,addressField, di,idField, registerSubmitButton, messageLabel, loginBox);
        registerLayout.setAlignment(Pos.CENTER);
        registerLayout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(registerLayout, 1000, 800);
    }


    private Scene createLoginScene(Stage primaryStage) {
        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        TextField emailField = new TextField();
        Label emailLabel = new Label("Email:");
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        TextField idField = new TextField();
        Label di = new Label("ID:");
        idField.setPromptText("ID:");
        idField.setMaxWidth(300);

        Label errorMessageLabel = new Label();
        errorMessageLabel.setTextFill(Color.RED);
        errorMessageLabel.setVisible(false);


        Button loginButton = new Button("Log in");
        loginButton.setOnAction(event -> {
            errorMessageLabel.setVisible(false);
            try {
                String email = emailField.getText().trim();
                int userId = Integer.parseInt(idField.getText().trim());

                User userFromDB = userDAO.getUserByEmail(email);
                if (userFromDB != null && userFromDB.getID() == userId) {
                    loggedInUser = userFromDB;
                    System.out.println("Login successful!");

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Login Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("You have logged in successfully!");
                    successAlert.showAndWait();

                    primaryStage.setScene(createHomeScene(primaryStage));
                } else {
                    errorMessageLabel.setText("Invalid credentials. Please try again.");
                    errorMessageLabel.setVisible(true);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error during login process.");
                errorMessageLabel.setText("An error occurred during login. Please try again later.");
                errorMessageLabel.setVisible(true);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid ID.");
                errorMessageLabel.setText("Please enter a valid numeric ID.");
                errorMessageLabel.setVisible(true);
            }
        });

        Label noAccountLabel = new Label("Don't have an account?");
        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> primaryStage.setScene(createRegisterScene(primaryStage)));

        HBox registerBox = new HBox(5, noAccountLabel, registerButton);
        registerBox.setAlignment(Pos.CENTER);

        VBox loginLayout = new VBox(10, titleLabel, emailLabel,emailField, di,idField, loginButton, errorMessageLabel, registerBox);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(loginLayout, 1000, 800);
    }

    private Scene createHomeScene(Stage primaryStage) {
        if (loggedInUser == null) {
            System.out.println("User not logged in. Redirecting to login page.");
            return createLoginScene(primaryStage);
        }


        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Button profileButton = new Button("Profili im");
        profileButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        profileButton.setOnAction(event -> {
            try {
                User userProfile = userDAO.getUserById(loggedInUser.getID());
                primaryStage.setScene(createProfileScene(primaryStage, userProfile));
            } catch (SQLException e) {
                System.out.println("Error fetching user profile: " + e.getMessage());
            }
        });



        Button offersButton = new Button("Ofertat");
        offersButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        offersButton.setOnAction(event -> {
            primaryStage.setScene(createAllOffersScene(primaryStage));
        });

        Button reserveButton = new Button("Rezervo");
        reserveButton.setOnAction(event -> primaryStage.setScene(createReservationScene(primaryStage)));



        Button viewReservationsButton = new Button("Shiko rezervimet");
        viewReservationsButton.setOnAction(event -> {
            try {
                int userId = loggedInUser.getID();
                System.out.println("Fetching reservations for user ID: " + userId);

                List<Rezervimi> userReservations = reservationDAO.getReservationsByUserId(userId);

                if (userReservations.isEmpty()) {
                    showAlert("No Reservations", "Nuk keni asnjë rezervim të regjistruar.");
                } else {
                    primaryStage.setScene(createReservationsScene(primaryStage, userReservations));
                }
            } catch (SQLException e) {
                System.out.println("Error fetching reservations.");
                e.printStackTrace();
            }
        });


        Button invoiceButton = new Button("Fatura");
        invoiceButton.setOnAction(event -> {
            try {
                List<ReservationDetails> unpaidReservations = reservationDetailsDAO.getUnpaidReservationDetails(loggedInUser.getID());
                double totalAmount = unpaidReservations.stream()
                        .mapToDouble(ReservationDetails::calculateTotal)
                        .sum();

                showInvoice(primaryStage, unpaidReservations, totalAmount);
            } catch (SQLException e) {
                showAlert("Error", "Gabim gjatë marrjes së faturës.");
                e.printStackTrace();
            }
        });


        Button exitButton = new Button("Exit");

        profileButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        offersButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        reserveButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        viewReservationsButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        invoiceButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");
        exitButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #007a73;");

        exitButton.setOnAction(event -> primaryStage.close());

        VBox homeLayout = new VBox(15, titleLabel, profileButton, offersButton, reserveButton,viewReservationsButton, invoiceButton, exitButton);
        homeLayout.setAlignment(Pos.CENTER);
        homeLayout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(homeLayout, 1000, 800);
    }

    private Scene createProfileScene(Stage primaryStage, User user) {
        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Label profileTitle = new Label("Profili im");
        profileTitle.setFont(new Font("Arial", 24));
        profileTitle.setTextFill(Color.TEAL);

        Label nameLabel = new Label("Emri: " + user.getEmri());
        Label surnameLabel = new Label("Mbiemri: " + user.getMbiemri());
        Label phoneLabel = new Label("Numri telefonit: " + user.getNrTel());
        Label emailLabel = new Label("Email: " + user.getEmail());
        Label addressLabel = new Label("Adresa: " + user.getAdresa());
        Label idLabel = new Label("ID: " + user.getID());

        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007a73;");
        surnameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007a73;");
        phoneLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007a73;");
        emailLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007a73;");
        addressLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007a73;");
        idLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007a73;");

        Button backButton = new Button("Kthehu");
        backButton.setOnAction(event -> primaryStage.setScene(createHomeScene(primaryStage)));

        VBox profileLayout = new VBox(10, titleLabel, profileTitle, nameLabel, surnameLabel, phoneLabel, emailLabel, addressLabel, idLabel, backButton);
        profileLayout.setAlignment(Pos.CENTER);
        profileLayout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(profileLayout, 1000, 800);
    }

    private Scene createAllOffersScene(Stage primaryStage) {
        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Label subTitleLabel = new Label("Zgjidhni njërën prej ofertave tona:");
        subTitleLabel.setFont(new Font("Arial", 24));
        subTitleLabel.setTextFill(Color.TEAL);

        VBox offersLayout = new VBox(20, titleLabel, subTitleLabel);
        offersLayout.setAlignment(Pos.TOP_CENTER);
        offersLayout.setStyle("-fx-background-color: #D9D9D9;");

        Label offer1Label = new Label("Oferta 1\nVjen me çmim prej 5000 euro, për 120 persona\nMe menu: 45 euro/personi");
        offer1Label.setFont(new Font("Arial", 16));
        offer1Label.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-text-alignment: center; -fx-alignment: center;");

        Button chooseOffer1 = new Button("Zgjidh");
        chooseOffer1.setStyle("-fx-background-color: #007a73; -fx-text-fill: white;");
        chooseOffer1.setOnAction(event -> {
            primaryStage.setScene(createReservationPage(primaryStage, "Oferta 1", 5000, 120, 45));
        });

        VBox offer1Box = new VBox(10, offer1Label, chooseOffer1);
        offer1Box.setAlignment(Pos.CENTER);

        Label offer2Label = new Label("Oferta 2\nVjen me çmim prej 1000 euro, për 40 persona\nMe menu: 35 euro/personi");
        offer2Label.setFont(new Font("Arial", 16));
        offer2Label.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-text-alignment: center; -fx-alignment: center;");

        Button chooseOffer2 = new Button("Zgjidh");
        chooseOffer2.setStyle("-fx-background-color: #007a73; -fx-text-fill: white;");
        chooseOffer2.setOnAction(event -> {
            primaryStage.setScene(createReservationPage(primaryStage, "Oferta 2", 1000, 40, 35));
        });

        VBox offer2Box = new VBox(10, offer2Label, chooseOffer2);
        offer2Box.setAlignment(Pos.CENTER);

        Label offer3Label = new Label("Oferta 3\nVjen me çmim prej 10000 euro, për 200 persona\nMe menu: 60 euro/personi");
        offer3Label.setFont(new Font("Arial", 16));
        offer3Label.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-text-alignment: center; -fx-alignment: center;");

        Button chooseOffer3 = new Button("Zgjidh");
        chooseOffer3.setStyle("-fx-background-color: #007a73; -fx-text-fill: white;");
        chooseOffer3.setOnAction(event -> {
            primaryStage.setScene(createReservationPage(primaryStage, "Oferta 3", 10000, 200, 60));
        });

        VBox offer3Box = new VBox(10, offer3Label, chooseOffer3);
        offer3Box.setAlignment(Pos.CENTER);

        Button backButton = new Button("Kthehu");
        backButton.setOnAction(event -> primaryStage.setScene(createHomeScene(primaryStage)));

        offersLayout.getChildren().addAll(offer1Box, offer2Box, offer3Box, backButton);

        return new Scene(offersLayout, 1000, 800);
    }


    private Scene createReservationPage(Stage primaryStage, String offerName, int offerPrice, int guests, int menuPrice) {
        Label titleLabel = new Label("EVENTS Rezervimi");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Label offerLabel = new Label("Ju keni zgjedhur: " + offerName);
        offerLabel.setFont(new Font("Arial", 18));
        offerLabel.setTextFill(Color.TEAL);

        Label priceLabel = new Label("Çmimi total: " + offerPrice + " euro");
        priceLabel.setFont(new Font("Arial", 16));

        Label guestsLabel = new Label("Numri i pjesëmarrësve: " + guests);
        guestsLabel.setFont(new Font("Arial", 16));

        Label menuLabel = new Label("Menuja: " + (menuPrice == 45 ? "45 euro/personi" : menuPrice == 35 ? "35 euro/personi" : "60 euro/personi"));
        menuLabel.setFont(new Font("Arial", 16));

        Label eventNameLabel = new Label("Emri i eventit:");
        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Shkruani emrin e eventit...");
        eventNameField.setMaxWidth(300);

        Label eventDateLabel = new Label("Data e eventit (yyyy-MM-dd):");
        TextField dateField = new TextField();
        dateField.setPromptText("Data e eventit (yyyy-MM-dd)");
        dateField.setMaxWidth(300);

        Label durationLabel = new Label("Kohëzgjatja e eventit (në orë):");
        TextField durationField = new TextField();
        durationField.setPromptText("Kohëzgjatja e eventit (orë)");
        durationField.setMaxWidth(300);

        Label decorLabel = new Label("Zgjidhni dekorimin:");
        CheckBox decorCheckBox = new CheckBox("Dekor - 2000 euro");

        Label musicLabel = new Label("Zgjidhni muzikën:");
        CheckBox musicCheckBox = new CheckBox("Muzikë - 3000 euro");


        Button reserveButton = new Button("Rezervo");
        reserveButton.setOnAction(event -> {
            try {
                String eventName = eventNameField.getText().trim();
                LocalDate eventDate = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                double eventDuration = Double.parseDouble(durationField.getText());

                if (eventName.isEmpty()) {
                    showAlert("Error", "Ju lutem shkruani emrin e eventit.");
                    return;
                }
                if (eventDate.isBefore(LocalDate.now())) {
                    showAlert("Error", "Data e eventit nuk mund të jetë në të kaluarën!");
                    return;
                }
                if (eventDuration <= 0) {
                    showAlert("Error", "Kohëzgjatja e eventit duhet të jetë më e madhe se zero.");
                    return;
                }

                int decorPrice = decorCheckBox.isSelected() ? 2000 : 0;
                int musicPrice = musicCheckBox.isSelected() ? 3000 : 0;

                saveReservation(loggedInUser.getEmri(), eventName,eventDuration,LocalDate.now(),eventDate,guests,menuPrice,offerPrice,true,decorPrice,musicPrice);
                primaryStage.setScene(createHomeScene(primaryStage));
            } catch (DateTimeParseException e) {
                showAlert("Error", "Ju lutem shkruani një datë të vlefshme (yyyy-MM-dd).");
            }
        });

        Button backButton = new Button("Kthehu te ofertat");
        backButton.setOnAction(event -> primaryStage.setScene(createAllOffersScene(primaryStage)));

        VBox layout = new VBox(20, titleLabel, offerLabel, priceLabel, guestsLabel, menuLabel,eventNameLabel, eventNameField, eventDateLabel,dateField, durationLabel,durationField,decorLabel,decorCheckBox,musicLabel,musicCheckBox ,reserveButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(layout, 1000, 800);
    }


    private Scene createReservationScene(Stage primaryStage) {

        Label titleLabel = new Label("EVENTS Rezervimi");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Label rezervatorNameLabel = new Label("Emri i rezervatorit:");
        TextField rezervatorNameField = new TextField();
        rezervatorNameField.setPromptText("Shkruani emrin tuaj...");
        rezervatorNameField.setMaxWidth(300);

        Label eventNameLabel = new Label("Emri i eventit:");
        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Shkruani emrin e eventit...");
        eventNameField.setMaxWidth(300);

        Label durationLabel = new Label("Kohëzgjatja e eventit (në orë):");
        TextField durationField = new TextField();
        durationField.setPromptText("Shkruani numrin e orëve...");
        durationField.setMaxWidth(300);

        Label eventDateLabel = new Label("Data e eventit (yyyy-MM-dd):");
        TextField eventDateField = new TextField();
        eventDateField.setPromptText("Shkruani datën e eventit...");
        eventDateField.setMaxWidth(300);

        Label numGuestsLabel = new Label("Numri i pjesëmarrësve:");
        TextField numGuestsField = new TextField();
        numGuestsField.setPromptText("Shkruani numrin e pjesëmarrësve...");
        numGuestsField.setMaxWidth(300);

        Label menuLabel = new Label("Zgjidhni një menu:");
        menuLabel.setFont(new Font("Arial", 18));

        ToggleGroup menuToggleGroup = new ToggleGroup();
        RadioButton menu30Button = new RadioButton("Menu e lirë - 30 euro/person");
        menu30Button.setToggleGroup(menuToggleGroup);
        RadioButton menu45Button = new RadioButton("Menu mesatare - 45 euro/person");
        menu45Button.setToggleGroup(menuToggleGroup);
        RadioButton menu60Button = new RadioButton("Menu e shtrenjtë - 60 euro/person");
        menu60Button.setToggleGroup(menuToggleGroup);

        Label decorLabel = new Label("Zgjidhni dekorimin:");
        decorLabel.setFont(new Font("Arial", 18));
        CheckBox decorCheckBox = new CheckBox("Dekor - 2000 euro");

        Label musicLabel = new Label("Zgjidhni muzikën:");
        musicLabel.setFont(new Font("Arial", 18));
        CheckBox musicCheckBox = new CheckBox("Muzikë - 3000 euro");

        Button submitButton = new Button("Rezervo");
        Button backButton = new Button("Kthehu");
        backButton.setOnAction(event -> primaryStage.setScene(createHomeScene(primaryStage)));

        submitButton.setOnAction(event -> {
            String rezervatorName = rezervatorNameField.getText().trim();
            String eventName = eventNameField.getText().trim();
            String durationText = durationField.getText().trim();
            String eventDateText = eventDateField.getText().trim();
            String numGuestsText = numGuestsField.getText().trim();
            RadioButton selectedMenuButton = (RadioButton) menuToggleGroup.getSelectedToggle();
            int decorPrice = decorCheckBox.isSelected() ? 2000 : 0;
            int musicPrice = musicCheckBox.isSelected() ? 3000 : 0;

            if (rezervatorName.isEmpty() || eventName.isEmpty() || durationText.isEmpty() ||
                    eventDateText.isEmpty() || numGuestsText.isEmpty() || selectedMenuButton == null) {
                showAlert("Error", "Ju lutem plotësoni të gjitha fushat!");
                return;
            }

            try {
                double duration = Double.parseDouble(durationText);
                int numGuests = Integer.parseInt(numGuestsText);
                LocalDate eventDate = LocalDate.parse(eventDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (eventDate.isBefore(LocalDate.now())) {
                    showAlert("Error", "Data e eventit nuk mund të jetë në të kaluarën!");
                    return;
                }

                int menuPrice = selectedMenuButton == menu30Button ? 30 : selectedMenuButton == menu45Button ? 45 : 60;

                saveReservation(loggedInUser.getEmri(), eventName, duration, LocalDate.now(), eventDate, numGuests, menuPrice,0, false, decorPrice,musicPrice);
                primaryStage.setScene(createHomeScene(primaryStage));
            } catch (NumberFormatException e) {
                showAlert("Error", "Kohëzgjatja dhe numri i pjesëmarrësve duhet të jenë numra të vlefshëm!");
            } catch (DateTimeParseException e) {
                showAlert("Error", "Data e eventit duhet të jetë në formatin yyyy-MM-dd!");
            }
        });

        VBox layout = new VBox(15,
                titleLabel,
                rezervatorNameLabel, rezervatorNameField,
                eventNameLabel, eventNameField,
                durationLabel, durationField,
                eventDateLabel, eventDateField,
                numGuestsLabel, numGuestsField,
                menuLabel, menu30Button, menu45Button, menu60Button,
                decorLabel,decorCheckBox, musicLabel, musicCheckBox,
                submitButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #D9D9D9;");

        return new Scene(layout, 1000, 800);
    }

    private void saveReservation(String rezervatorName, String eventName, double duration, LocalDate resDate , LocalDate eventDate, int numGuests, int menuPrice, int offerPrice,boolean isOffer,int decorPrice, int musicPrice) {
        if (eventDate == null) {
            showAlert("Error", "Data e eventit nuk është vendosur saktë. Ju lutem provoni përsëri!");
            return;
        }

        Rezervimi rezervimi = new Rezervimi();
        rezervimi.setEmriRezervatorit(rezervatorName);
        rezervimi.setEmriEventit(eventName);
        rezervimi.setKohezgjatjaEventit(duration);
        rezervimi.setDataRezervimit(LocalDate.now());
        rezervimi.setDataEventit(eventDate);
        rezervimi.setNumriPresonave(numGuests);
        rezervimi.setId(loggedInUser.getID());

        try {
            reservationDAO.addReservation(rezervimi, loggedInUser.getID());
            ReservationDetails reservationDetails = new ReservationDetails(numGuests, menuPrice, decorPrice, musicPrice, offerPrice, isOffer,loggedInUser.getID());
            reservationDetailsDAO.addReservationDetails(reservationDetails);
            showAlert("Success", "Rezervimi është shtuar me sukses!");
        } catch (SQLException e) {
            showAlert("Error", "Gabim gjatë shtimit të rezervimit në databazë.");
            e.printStackTrace();
        }
    }
    private Scene createReservationsScene(Stage primaryStage, List<Rezervimi> reservations) {
        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setTextFill(Color.BLACK);

        Label subTitleLabel = new Label("Shiko rezervimet");
        subTitleLabel.setFont(new Font("Arial", 24));
        subTitleLabel.setTextFill(Color.TEAL);

        VBox reservationsBox = new VBox(20);
        reservationsBox.setAlignment(Pos.TOP_CENTER);
        reservationsBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-border-radius: 10;");

        Button backButton = new Button("Kthehu");
        backButton.setOnAction(event -> primaryStage.setScene(createHomeScene(primaryStage)));

        HBox topLayout = new HBox(backButton);
        topLayout.setAlignment(Pos.CENTER_LEFT);
        topLayout.setStyle("-fx-padding: 10;");

        VBox mainLayout = new VBox(20, topLayout, titleLabel, subTitleLabel);
        mainLayout.setAlignment(Pos.TOP_CENTER);

        for (Rezervimi reservation : reservations) {
            VBox reservationBox = new VBox(10);
            reservationBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
            reservationBox.setMaxWidth(800);

            Label reservationLabel = new Label(
                    "Emri i rezervatorit: " + reservation.getEmriRezervatorit() + "\n" +
                            "Emri i eventit: " + reservation.getEmriEventit() + "\n" +
                            "Kohëzgjatja e eventit: " + reservation.getKohezgjatjaEventit() + "\n" +
                            "Data e eventit: " + reservation.getDataEventit() + "\n" +
                            "Numri i pjesëmarrësve: " + reservation.getNumriPresonave() + "\n" +
                            "Data e rezervimit: " + reservation.getDataRezervimit()
            );
            reservationLabel.setFont(new Font("Arial", 16));
            reservationLabel.setWrapText(true);

            Button deleteButton = new Button("Fshij");
            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
            deleteButton.setOnAction(event -> {
                try {
                    reservationDAO.deleteReservation(reservation.getId());
                    reservationsBox.getChildren().remove(reservationBox);
                    showAlert("Success", "Rezervimi është fshirë me sukses.");
                } catch (SQLException e) {
                    showAlert("Error", "Gabim gjatë fshirjes së rezervimit.");
                    e.printStackTrace();
                }
            });

            HBox actionsBox = new HBox(deleteButton);
            actionsBox.setAlignment(Pos.CENTER_RIGHT);

            reservationBox.getChildren().addAll(reservationLabel, actionsBox);
            reservationsBox.getChildren().add(reservationBox);
        }

        ScrollPane scrollPane = new ScrollPane(reservationsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        mainLayout.getChildren().add(scrollPane);

        return new Scene(mainLayout, 1000, 800);
    }
    private void showInvoice(Stage primaryStage, List<ReservationDetails> unpaidReservations, double totalAmount) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ffffff;");

        VBox topLayout = new VBox(10);
        topLayout.setAlignment(Pos.CENTER);

        Label eventsLabel = new Label("EVENTS");
        eventsLabel.setFont(new Font("Arial", 36));
        eventsLabel.setStyle("-fx-text-fill: #000000;");

        Label faturaLabel = new Label("Fatura");
        faturaLabel.setFont(new Font("Arial", 28));
        faturaLabel.setStyle("-fx-text-fill: #007a73;");

        topLayout.getChildren().addAll(eventsLabel, faturaLabel);

        Button backButton = new Button("Kthehu");
        backButton.setOnAction(event -> primaryStage.setScene(createHomeScene(primaryStage)));

        VBox leftLayout = new VBox(backButton);
        leftLayout.setAlignment(Pos.TOP_LEFT);
        leftLayout.setPadding(new Insets(10));

        VBox invoiceLayout = new VBox(20);
        invoiceLayout.setAlignment(Pos.CENTER);
        invoiceLayout.setStyle("-fx-padding: 20;");

        for (ReservationDetails details : unpaidReservations) {
            Label invoiceLabel = new Label(
                    "Numri i pjesëmarrësve: " + details.getNrFtuarve() + "\n" +
                            "Çmimi i menusë: " + details.getMenuPrice() + " euro\n" +
                            "Dekorimi: " + (details.getDecorationPrice() > 0 ? details.getDecorationPrice() + " euro" : "Nuk ka") + "\n" +
                            "Muzika: " + (details.getMusicPrice() > 0 ? details.getMusicPrice() + " euro" : "Nuk ka") + "\n" +
                            "Totali: " + details.calculateTotal() + " euro"
            );
            invoiceLabel.setFont(new Font("Arial", 16));
            invoiceLabel.setWrapText(true);
            invoiceLabel.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 10;");

            Label reservationTotalLabel = new Label("Totali për këtë rezervim: " + details.calculateTotal() + " euro");
            reservationTotalLabel.setFont(new Font("Arial", 18));
            reservationTotalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #007a73;  -fx-alignment: center;");

            VBox reservationBox = new VBox(10, invoiceLabel, reservationTotalLabel);
            reservationBox.setStyle("-fx-padding: 10; -fx-background-color: #f1f1f1; -fx-border-color: #dcdcdc; -fx-border-radius: 8;");
            reservationBox.setAlignment(Pos.CENTER);

            invoiceLayout.getChildren().add(reservationBox);
        }

        Label totalLabel = new Label("Shuma Totale: " + totalAmount + " euro");
        totalLabel.setFont(new Font("Arial", 24));
        totalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");

        Button payButton = new Button("Paguaj");
        payButton.setStyle("-fx-font-size: 16px; -fx-background-color: #28a745; -fx-text-fill: white;");
        payButton.setOnAction(event -> showPaymentPage(primaryStage, unpaidReservations, totalAmount));

        VBox bottomLayout = new VBox(20, totalLabel, payButton);
        bottomLayout.setAlignment(Pos.CENTER);
        bottomLayout.setStyle("-fx-padding: 20;");

        mainLayout.setTop(topLayout);
        mainLayout.setLeft(leftLayout);
        mainLayout.setCenter(invoiceLayout);
        mainLayout.setBottom(bottomLayout);

        Scene scene = new Scene(mainLayout, 1000, 800);
        primaryStage.setScene(scene);
    }

    private void showPaymentPage(Stage primaryStage, List<ReservationDetails> unpaidReservations, double totalAmount) {

        if (totalAmount <= 0) {
            showAlert("Info", "Ju nuk keni asgjë për të paguar. Shuma totale është 0.");
            showInvoice(primaryStage, unpaidReservations, 0.0);
            return;
        }

        VBox paymentLayout = new VBox(20);
        paymentLayout.setAlignment(Pos.CENTER);
        paymentLayout.setStyle("-fx-background-color: #ffffff; -fx-padding: 20;");

        Label titleLabel = new Label("EVENTS");
        titleLabel.setFont(new Font("Arial", 36));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label paymentLabel = new Label("Pagesa");
        paymentLabel.setFont(new Font("Arial", 28));
        paymentLabel.setStyle("-fx-text-fill: #007a73;");

        TextField cardNumberField = new TextField();
        Label cardLabel = new Label("Numri i Kartës");
        cardNumberField.setPromptText("XXXX XXXX XXXX XXXX");
        cardNumberField.setMaxWidth(300);

        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            String formatted = newValue.replaceAll("[^\\d]", "").replaceAll("(.{4})", "$1 ").trim();
            if (formatted.length() > 19) formatted = formatted.substring(0, 19);
            cardNumberField.setText(formatted);
        });

        TextField expiryField = new TextField();
        Label expiryLabel = new Label("MM/YY");
        expiryField.setPromptText("MM/YY");
        expiryField.setMaxWidth(150);

        TextField cvvField = new TextField();
        Label cvvLabel = new Label("CVV");
        cvvField.setPromptText("CVV");
        cvvField.setMaxWidth(100);

        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);
        errorMessage.setVisible(false);

        Button payButton = new Button("Paguaj");
        payButton.setStyle("-fx-font-size: 16px; -fx-background-color: #28a745; -fx-text-fill: white;");
        payButton.setOnAction(event -> {
            String cardNumber = cardNumberField.getText().trim();
            String expiryDate = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                errorMessage.setText("Ju lutem plotësoni të gjitha fushat.");
                errorMessage.setVisible(true);
                return;
            }

            if (!cardNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}")) {
                errorMessage.setText("Numri i kartës duhet të jetë në formatin XXXX XXXX XXXX XXXX.");
                errorMessage.setVisible(true);
                return;
            }


            if (!expiryDate.matches("\\d{2}/\\d{2}")) {
                errorMessage.setText("Data duhet të jetë në formatin MM/YY.");
                errorMessage.setVisible(true);
                return;
            }

            if (!cvv.matches("\\d{3}")) {
                errorMessage.setText("CVV duhet të jetë 3 shifra.");
                errorMessage.setVisible(true);
                return;
            }

            payButton.setDisable(true);
            errorMessage.setVisible(false);

            Stage progressStage = new Stage();
            progressStage.initOwner(primaryStage);
            progressStage.setTitle("Duke u kryer...");
            VBox progressLayout = new VBox(20, new Label("Pagesa është duke u kryer..."));
            progressLayout.setAlignment(Pos.CENTER);
            progressStage.setScene(new Scene(progressLayout, 300, 150));
            progressStage.show();

            new Thread(() -> {
                try {
                    Thread.sleep(3000);

                    Platform.runLater(() -> {
                        progressStage.close();

                        unpaidReservations.forEach(details -> {
                            try {
                                reservationDetailsDAO.markInvoiceAsPaid(details.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });

                        showAlert("Sukses", "Pagesa është kryer me sukses!");

                        primaryStage.setScene(createHomeScene(primaryStage));
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });


        Button backButton = new Button("Kthehu");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #007a73; -fx-text-fill: white;");
        backButton.setOnAction(event -> showInvoice(primaryStage, unpaidReservations, totalAmount));

        HBox cardLayout = new HBox(10, cardLabel,cardNumberField, expiryLabel,expiryField, cvvLabel,cvvField);
        cardLayout.setAlignment(Pos.CENTER);

        VBox formLayout = new VBox(15, cardLayout, errorMessage, payButton);
        formLayout.setAlignment(Pos.CENTER);

        paymentLayout.getChildren().addAll(titleLabel, paymentLabel, formLayout, backButton);

        Scene paymentScene = new Scene(paymentLayout, 1000, 800);
        primaryStage.setScene(paymentScene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
