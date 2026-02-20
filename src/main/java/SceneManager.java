import java.util.HashMap;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SceneManager {
    public Stage primaryStage;
    public Transitions ts;
    private final HashMap<String, Scene> mapScenes;
    public BetCard card;
    public BetCard.Grid grid;
    public DrawPhase draw;
    public GameOver end;
    private boolean defaultLook;
    public String currScene;
    public final String mangoColor = "#ffd5a6";
    public final String grapeColor = "#ffd5fc";

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.ts = new Transitions();
        this.mapScenes = new HashMap<>();
        this.card = new BetCard();
        this.grid = card.new Grid();
        this.draw = new DrawPhase();
        this.end = new GameOver();
        this.defaultLook = true;
        // Create all scenes and add them to the HashMap
        mainMenuScene();
        betScene();
        drawScene();
        endScene();
    }

    public HashMap<String, Scene> getMapScenes() {
        return mapScenes;
    }

    private MenuBar createMenuBar() {
        // Create and add the Menu at the top
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem rules = new MenuItem("Rules");
        rules.setOnAction((e) -> {
            rulesPopup();
        });
        MenuItem odds = new MenuItem("Odds");
        odds.setOnAction(e->{oddsPopup(1);});
        MenuItem newLook = new MenuItem("New Look");
        newLook.setOnAction((e) -> {
            this.defaultLook = !defaultLook;
            changeLook(); // Change the look of every Scene
            grid.changeLookBetCardButtons(); // Change the look of every button in the Bet scene
            draw.changeLookDrawPhase(); // Change the look of every button in the Draw scene
            ts.changeLookTransitions(); // Change the look of every 'next' button in every scene
            end.changeLookEnd(); // Change the buttons on the ending scene
        });
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((e) -> {
           Platform.exit();
        });
        menu.getItems().addAll(rules, odds, newLook, exit);
        menuBar.getMenus().addAll(menu);

        return menuBar;
    }

    public Pane createBackgroundPane() {
        // Background Pane used for circles on the main menu screen
    	Image mango = new Image("/mango_fruit.gif", true);

    	ImageView iv1 = new ImageView();
        iv1.setImage(mango);
    	iv1.setX(650);
    	iv1.setY(270);
    	iv1.setFitWidth(300);
    	iv1.setPreserveRatio(true);

        Pane background = new Pane();
        if(defaultLook) {
            // Left side circles
            Circle c1 = new Circle(195+50, 80, 15, Color.web("#ffd344"));
            Circle c2 = new Circle(70+50, 195, 25, Color.web("#ffba39"));
            Circle c3 = new Circle(150+50, 500, 30, Color.web("#ffd344"));
            Circle c4 = new Circle(190+50, 670,15, Color.web("#ffba39"));
            Circle c5 = new Circle(280+50, 400,8, Color.web("#ffd344"));
            // Right side circles
            Circle c6 = new Circle(575+250, 155, 20, Color.web("#ffba39"));
            Circle c7 = new Circle(460+250, 320, 10, Color.web("#ffd344"));
            Circle c8 = new Circle(525+250, 600, 20, Color.web("#ffba39"));
            background.getChildren().addAll(c1, c2, c3, c4, c5, c6, c7, c8,iv1);
            background.setStyle("-fx-background-color: " + mangoColor + ";"); // Set background color to bgColor
        }
        else {
            // Stuff for the grape background goes here
            background.setStyle("-fx-background-color: " + grapeColor + ";");
        }

        return background;
    }

    public Region createVerticalGap(int pixels) {
        Region verticalGap = new Region();
        verticalGap.setMinHeight(pixels);
        return verticalGap;
    }

    public Region createHorizontalGap(int pixels) {
        Region horizontalGap = new Region();
        horizontalGap.setMinWidth(pixels);
        return horizontalGap;
    }

    public void mainMenuScene() {
        this.currScene = "mainmenu";

        BorderPane pane = new BorderPane();

        // Creates a Menu button that is different from the Menu in other scenes
        MenuBar mb = new MenuBar();
        Menu m = new Menu("Menu");

        // Creating MenuItems
        MenuItem rule = new MenuItem("Rules");
        rule.setOnAction(e -> {
            rulesPopup();
        });
        MenuItem odd = new MenuItem("Odds");
        odd.setOnAction(e->{oddsPopup(1);});
        // Add setOnAction functionality
        MenuItem ex = new MenuItem("Exit");
        ex.setOnAction((e) -> {
            Platform.exit();
        });

        // Add the items to the Menu and add the Menu to the MenuBar
        m.getItems().addAll(rule, odd, ex);
        mb.getMenus().addAll(m);

        Text title = new Text("Keno");
        title.setStyle("-fx-font-size: 80;");
        Text description = new Text("A short, fun and interactive game!");
        description.setStyle("-fx-font-size: 20;");
        //keno image
        ImageView keno = ts.mainMenuKeno;

        // Create the orange play button
        Button nextButton = ts.mainMenuNext;
        // Make the button grow and shrink based on whether its being hovered
        nextButton.setOnMouseEntered(e -> {
            nextButton.setScaleX(1.05);
            nextButton.setScaleY(1.05);
        });
        nextButton.setOnMouseExited(e -> {
            nextButton.setScaleX(1);
            nextButton.setScaleY(1);
        });
        nextButton.setOnAction(e -> {
            this.currScene = "bet";
            primaryStage.setScene(mapScenes.get("bet"));
            grid.lockGrid();
            ts.allowBetNext(card.isBetsReady(), grid.isGridReady()); // Pressing next should lock the betNext button
        });

        // Space used to separate the button and the text in the VBox mid
        Region space = new Region();
        space.setMinHeight(20);
        VBox mid = new VBox(10, keno, description, space, nextButton);
        mid.setAlignment(Pos.CENTER); // Place the title and description VBox in the center of the pane

        Pane background = createBackgroundPane();

        // Place nodes in the BorderPane
        pane.setTop(mb);
        pane.setCenter(mid);

        StackPane root = new StackPane(background, pane);
        mapScenes.put("mainmenu", new Scene(root, 1000,700)); // Add to the HashMap
    }

    public void betScene() {
        BorderPane pane = new BorderPane();
        Pane background = createBackgroundPane();

        VBox leftPanel = card.leftPanel;
        // #1
        Text nBets = new Text("Bet Amount Per Draw");
        GridPane nBetsGrid1 = card.betsTopHalfGrid;
        GridPane nBetsGrid2 = card.betsBottomHalfGrid;
        // These HBoxes help align the GridPane's towards the center
        HBox topHalf = new HBox(10, nBetsGrid1);
        HBox bottomHalf = new HBox(10, nBetsGrid2);
        topHalf.setAlignment(Pos.CENTER);
        bottomHalf.setAlignment(Pos.CENTER);
        // Set the actions for each button
        for(Button btn : card.betLookup.values()) {
            btn.setOnAction(e -> {
                // Turn off the previous Button chosen
                if(card.prevBet != 0) {
                    card.betLookup.get(card.prevBet).setStyle((defaultLook) ? card.buttonOffMango : card.buttonOffGrape);
                }

                card.moneyPerBet = Integer.parseInt(btn.getText().substring(1));
                card.betLookup.get(card.moneyPerBet).setStyle((defaultLook) ? card.buttonOnMango : card.buttonOnGrape); // Turn on the newly selected Button

                card.prevBet = card.moneyPerBet; // This will turn off a Button if another Button is selected the next time

                ts.allowBetNext(card.isBetsReady(), grid.isGridReady()); // If pressing this button fulfills the BetCard, the user can move on
            });
        }
        // #2
        Text nDraws = new Text("Number of Draws");
        GridPane nDrawsGrid = card.drawsGrid;
        HBox hbDrawsGrid = new HBox(10, nDrawsGrid); // Used to center the GridPane for the Draws boxes
        hbDrawsGrid.setAlignment(Pos.CENTER);
        for(Button btn : card.drawsLookup) {
            btn.setOnAction(e -> {
                // Turn off the previous Button chosen
                if(card.prevDraw != 0) {
                    card.drawsLookup[card.prevDraw - 1].setStyle((defaultLook) ? card.buttonOffMango : card.buttonOffGrape);
                }

                card.numDraws = Integer.parseInt(btn.getText());
                card.drawsLookup[card.numDraws - 1].setStyle((defaultLook) ? card.buttonOnMango : card.buttonOnGrape); // Turn on the newly selected Button

                card.prevDraw = card.numDraws; // This will turn off a Button if another Button is selected the next time

                ts.allowBetNext(card.isBetsReady(), grid.isGridReady()); // If pressing this button fulfills the BetCard, the user can move on
            });
        }
        // #3
        Text nSpots = new Text("Number of Spots");
        GridPane nSpotsGrid = grid.spotsGrid;
        HBox hbSpotsGrid = new HBox(10, nSpotsGrid); // Used to center the GridPane for the Spots boxes
        hbSpotsGrid.setAlignment(Pos.CENTER);
        // Set the actions for each button
        for(Button btn : grid.spotsLookup.values()) {
            btn.setOnAction(e -> {
                grid.numSpots = Integer.parseInt(btn.getText()); // Converts a String into an int
                grid.spotsRemaining = Integer.parseInt(btn.getText()); // Converts a String into an int
                grid.clear(); // Clear selected buttons
                grid.unlockGrid(); // Unlock because the user should now choose their spots
                grid.gridDisabled = false;

                // Turn off the previous Button chosen
                if(grid.prevSpot != 0) {
                    grid.spotsLookup.get(grid.prevSpot).setStyle((defaultLook) ? card.buttonOffMango : card.buttonOffGrape);
                }

                grid.numSpots = Integer.parseInt(btn.getText());
                grid.spotsLookup.get(grid.numSpots).setStyle((defaultLook) ? card.buttonOnMango : card.buttonOnGrape); // Turn on the newly selected Button

                grid.prevSpot = grid.numSpots; // This will turn off a Button if another Button is selected the next time

                ts.allowBetNext(card.isBetsReady(), grid.isGridReady()); // If pressing this button fulfills the BetCard, the user can move on
            });
        }
        // #4
        Text quickSel =  new Text("Quick Select");
        Button qsBtn = new Button("");
        qsBtn.setOnAction(e -> {
            // Only be able to call the function if the number of spots has been chosen
            if(grid.numSpots > 0) {
                grid.quickSelect();
            }

            ts.allowBetNext(card.isBetsReady(), grid.isGridReady()); // If pressing this button fulfills the BetCard, the user can move on
        });
        qsBtn.setPrefSize(card.gridButtonSize, card.gridButtonSize);
        leftPanel.getChildren().addAll(createVerticalGap(20), nBets, topHalf, bottomHalf, createVerticalGap(40), nDraws, hbDrawsGrid, createVerticalGap(40), nSpots, hbSpotsGrid, createVerticalGap(60), quickSel, qsBtn);
        leftPanel.setAlignment(Pos.TOP_CENTER); // Position the children horizontally
        leftPanel.setPadding(new Insets(30)); // Padding on the inside

        MenuBar menuBar = createMenuBar();
        VBox rightPanel = new VBox(10);
        rightPanel.setPrefWidth(710); // The right panel will take around 2/3 of the screen
        Text instruction = new Text("Select Your Numbers OR Press Quick Select");
        HBox hbGrid = new HBox(10, grid.gridSpots); // Used to center the Grid
        hbGrid.setAlignment(Pos.CENTER);
        // Set the actions for each button
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 10; col++) {
                Button btn = grid.gridLookup[row][col];
                btn.setOnAction(e -> {
                    // If selected returns true then change the color of the button to indicate it is selected
                    // Otherwise we should change the color back to normal
                    if(grid.selected(Integer.parseInt(btn.getText()))) {
                        btn.setStyle((defaultLook) ? card.buttonOnMango : card.buttonOnGrape);
                    }
                    else {
                        btn.setStyle((defaultLook) ? card.buttonOffMango : card.buttonOffGrape);
                    }

                    ts.allowBetNext(card.isBetsReady(), grid.isGridReady()); // If pressing this button fulfills the BetCard, the user can move on
                });
            }
        }
        Button next = ts.betNext;
        next.setOnAction(e -> {
            this.currScene = "draw";
            primaryStage.setScene(mapScenes.get("draw"));
            draw.playPhase(card, grid, ts);
        });
        rightPanel.getChildren().addAll(instruction, createVerticalGap(20), hbGrid, createVerticalGap(20), next);
        rightPanel.setAlignment(Pos.TOP_CENTER); // Position the children horizontally
        rightPanel.setPadding(new Insets(20)); // Padding on the inside

        // Place nodes in the BorderPane
        pane.setTop(menuBar);
        pane.setLeft(leftPanel);
        pane.setRight(rightPanel);

        StackPane root = new StackPane(background, pane);
        mapScenes.put("bet", new Scene(root, 1000,700));
    }

    public void drawScene() {
        BorderPane pane = new BorderPane();

        HBox hbDrawGrid = new HBox(10, draw.drawGrid); // Used to center the Grid
        hbDrawGrid.setAlignment(Pos.CENTER);
        hbDrawGrid.setPadding(new Insets(20));

        // Create the next button
        Button next = ts.drawNext;
        next.setOnAction(e -> {
            if(card.numDraws == draw.numPhase) {
                this.currScene = "gameover";
                // Update total winnings
                VBox vb2Total = (VBox) end.totEarnings.getChildren().get(1);
                Text total = (Text) vb2Total.getChildren().get(1);
                total.setText(String.valueOf("$" + String.valueOf(draw.totalEarnings)));
                primaryStage.setScene(mapScenes.get("gameover")); // Change to the final scene
            }
            else {
                draw.clear();
                draw.updatePhase();
                draw.playPhase(card, grid, ts);
            }
        });

        // Bottom panel
        HBox bottomPanel = draw.bottomPanel;
        bottomPanel.getChildren().addAll(next, createHorizontalGap(15));
        bottomPanel.setAlignment(Pos.CENTER_RIGHT);

        VBox container = new VBox();
        container.getChildren().addAll(hbDrawGrid, bottomPanel);
        container.setAlignment(Pos.BOTTOM_CENTER);

        Pane background = createBackgroundPane();

        // Set nodes in the BorderPane
        pane.setTop(createMenuBar());
        pane.setCenter(container);

        StackPane root = new StackPane(background, pane);
        mapScenes.put("draw", new Scene(root, 1000,700));
    }

    public void endScene() {
        Pane background = createBackgroundPane();

        // Make Buttons out of the StackPane's
        Button bRetry =  new Button();
        bRetry.setGraphic(end.retry);
        bRetry.setStyle("-fx-background-color: transparent;");
        bRetry.setOnAction(e -> {
            this.currScene = "bet";
            draw.clearAll();
            primaryStage.setScene(mapScenes.get("bet"));
        });
        Button bExit  = new Button();
        bExit.setGraphic(end.exit);
        bExit.setStyle("-fx-background-color: transparent;");
        bExit.setOnAction(e -> {
            Platform.exit();
        });
        Button bMainMenu = new Button();
        bMainMenu.setGraphic(end.mainmenu);
        bMainMenu.setStyle("-fx-background-color: transparent;");
        bMainMenu.setOnAction(e -> {
            this.currScene = "mainmenu";
            primaryStage.setScene(mapScenes.get("mainmenu"));
        });

        HBox retryAndExit = new HBox(50, bRetry, bExit);
        retryAndExit.setAlignment(Pos.CENTER);
        // Change the text inside the Earnings StackPane
        VBox totEarningsVBox = (VBox) end.totEarnings.getChildren().get(1);
        Text totEarningsText = (Text) totEarningsVBox.getChildren().get(1);
        totEarningsText.setText("$" + String.valueOf(draw.totalEarnings));

        VBox container = new VBox(50, ts.endKeno, end.totEarnings, retryAndExit, bMainMenu);
        container.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, container);
        mapScenes.put("gameover", new Scene(root, 1000,700));
    }

    public void rulesPopup(){
        BorderPane pane = new BorderPane();
        if(this.defaultLook) {
            pane.setStyle("-fx-background-color: " + mangoColor);
        }
        else {
            pane.setStyle("-fx-background-color: " + grapeColor);
        }
        Text title = new Text("Rules");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text description = new Text("1. Decide how much to play per draw. Each play costs $1. Play for $2 to double your prize; play for $3 to triple your prize and so on up to $10 per play.\n\n" +
                "2. Select how many consecutive draws to play. Pick up to 4.\n\n" +
                "3. Select how many numbers to match from 1, 4, 8 or 10. In Keno, these are called Spots. The number of Spots you choose and the amount you play per draw will determine the amount you could win.\n\n" +
                "4. Pick as many numbers as you did Spots. You can select numbers from 1 to 80 or choose Quick Select and let the computer terminal randomly pick some or all of these numbers for you.");
        description.setStyle("-fx-font-size: 20px;");
        description.setWrappingWidth(600);
        VBox popup = new VBox(createVerticalGap(10), title, createVerticalGap(60), description);
        popup.setAlignment(Pos.TOP_CENTER);
        popup.setPadding(new Insets(30));

        pane.setCenter(popup);

        Alert a = new Alert(Alert.AlertType.NONE,null,ButtonType.OK);
        a.setTitle("INFO:RULES");
        a.setHeaderText(null);
        a.getDialogPane().setContent(pane);
        a.setWidth(250);
        a.setGraphic(null);
        a.setOnCloseRequest(e->{a.close();});
        a.showAndWait().ifPresent(e -> {if (e == ButtonType.YES);});
    }

    public void oddsPopup(int c) {
        BorderPane pane = new BorderPane();
        if(this.defaultLook) {
            pane.setStyle("-fx-background-color: " + mangoColor);
        }
        else {
            pane.setStyle("-fx-background-color: " + grapeColor);
        }
        Text title = new Text("Odds:");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text description = new Text("10 spot game: Overall odds 1 in 9.05\n match          prize\n   10       $100,000\n    9      	 $4,250\n    8      	 $450\n    7    	 $40\n    6 	 	$15\n    5           $2\n    0           $5\n"
                + "8 spot game: Overall odds 1 in 9.77\n match          prize\n    9          $30,000\n    8      	 $3,000\n    7      	 $150\n    6    	 $25\n    5 		 $6\n    4           $1\n"+
                "4 spot game: Overall odds 1 in 3.86\n match          prize\n    4              $75\n    3             $5\n    1             $1\n"
                + "1 spot game: Overall odds 1 in 4.00\n match          prize\n    1               $1\n"+"");

        description.setWrappingWidth(250);
        VBox popup = new VBox( createVerticalGap(10),title,createVerticalGap(10), description);
        pane.setCenter(popup);
        popup.setAlignment(Pos.TOP_CENTER);
        popup.setPadding(new Insets(30));
        Alert a = new Alert(Alert.AlertType.NONE,null,ButtonType.OK);
        a.setTitle("INFO:ODDS");
        a.setHeaderText(null);
        a.getDialogPane().setContent(pane);
        a.setWidth(250);
        a.setGraphic(null);
        a.setOnCloseRequest(e->{a.close();});
        a.showAndWait().ifPresent(e -> {if (e == ButtonType.YES);});
    }

    public void changeLook() {
        // Change all Scene backgrounds to either mango or grape
        for(String name : mapScenes.keySet()) {
            StackPane sp = (StackPane) mapScenes.get(name).getRoot();

            if(name.equals("rules") || name.equals("odds")) {
                sp.getChildren().get(0).setStyle("-fx-background-color: " + ((defaultLook) ? mangoColor : grapeColor));
                continue;
            }

            sp.getChildren().set(0, createBackgroundPane());
        }
    }
}
