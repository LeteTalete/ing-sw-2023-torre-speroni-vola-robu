package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.GUIApplication;
import it.polimi.ingsw.view.SceneNames;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class BoardPlayer extends GenericController {
    @FXML private VBox chooseTileBox, all;
    @FXML private StackPane boxChat;
    @FXML private HBox scorePlayer;
    @FXML private TextArea boxMessage;
    @FXML private Label labelTurn;
    @FXML private ImageView chair, tileEndGame, imageCommonCard1, imageCommonCard2, imagePersonalGoalCard, token1, token2, tile1, tile2, tile3;
    @FXML private ChoiceBox choiceChat;
    @FXML private GridPane myShelf, livingRoom, shelfPlayer1;
    private Color colorTileTaken = new Color(0.9548, 1.0, 0.21, 1.0);//Yellow
    private double xOffset = 0, yOffset = 0;
    private ArrayList<String> tileChoosenP = new ArrayList<>();
    private ArrayList<Integer> tileOrderPosition = new ArrayList<>();
    private ArrayList<Image> tileChoosenI = new ArrayList<>();
    private String[] colorChat = {"#f0fff0", "#ffc07e", "#93c47d", "#ffd049"}; //Ciano, arancione, verde, giallo
    private boolean columnActive = false;
    //private Stage windowShelfPlayers = new Stage();


    //Voglio caricare la board quando mi mostra la finestra in cui mi dice che sta caricando, in questo modo quando apro la finestra della partita è già tutto caricato
    public void setBoadPlayer(GameView gameView){
        int CGC1 = gameView.getCommonGoalCards().get(0).getID();
        int CGC2 = gameView.getCommonGoalCards().get(1).getID();
        int PGC = Objects.requireNonNull(gameView.getPlayersView().stream()
                .filter(p -> p.getNickname().equals(GUIApplication.clientGUI.getMaster().getUsername())).findFirst().orElse(null)).getPersonalGoalCard().getNumPGC();
        Image PersonalGC = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/PersonalCards/Personal_Goals" + PGC + ".png")));
        Image CommonGC1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/CommonCards/"+ CGC1 +".jpg")));
        Image CommonGC2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/CommonCards/" + CGC2 + ".jpg")));
        imageCommonCard1.setImage(CommonGC1);
        imageCommonCard2.setImage(CommonGC2);
        imagePersonalGoalCard.setImage(PersonalGC);
        choiceChat.getItems().add("all");
        for(PlayerView player : gameView.getPlayersView()){
            if(!player.getNickname().equals( GUIApplication.clientGUI.getMaster().getUsername() )){
                choiceChat.getItems().add(player.getNickname());
                VBox chatBox = new VBox();
                chatBox.setPrefWidth(all.getPrefWidth());
                chatBox.setPrefHeight(all.getPrefHeight());
                chatBox.setStyle("-fx-background-color:  #f4dfc6");
                chatBox.setId(player.getNickname());
                chatBox.setAlignment(Pos.BOTTOM_CENTER);
                chatBox.setVisible(false);
                chatBox.setFillWidth(true);
                boxChat.getChildren().add(chatBox);
            }
        }
        choiceChat.setValue("all"); //Per settare il valore predefinito
        choiceChat.setOnAction(event -> {
            for(Node chat: boxChat.getChildren()) {
                if(chat.getId().equals(choiceChat.getValue())){
                    chat.setVisible(true);
                } else {
                    if(chat.isVisible()) chat.setVisible(false);
                }
            }
        });
        setToken(1);
        setToken(2);
        //setto la sedia: da aggiungeree!!
        if(GUIApplication.clientGUI.getName().equals(gameView.getPlayersView().get(0).getNickname()) ) chair.setVisible(true);
    }

    public void updateShelfOthers(ArrayList<ShelfView> shelfViews){
        setShelf(shelfPlayer1, shelfViews.get(0));
    }

    /*
    private void setWindowShelfPlayers(int numPlayers){
            VBox newPlayer = new VBox();
            newPlayer.getChildren().addAll(player1.getChildren());
            players.getItems().add(newPlayer);
        //windowShelfPlayers.setHeight(550);
        //windowShelfPlayers.setWidth(1509);
        //windowShelfPlayers = GUIApplication.setUpStage(SceneNames.SHELFPLAYERS);
        setWindow(SceneNames.SHELFPLAYERS);
        //((VBox) playersShelf.getItems().get(0)).getChildren().

    }


    private void setWindow(SceneNames sceneNames){
        try {
            if (sceneNames.equals(SceneNames.SHELFPLAYERS)) {
                Image icon = new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Icon.png")));
                FXMLLoader loaderController = new FXMLLoader(GUIApplication.class.getResource(sceneNames.scaneString()));
                Parent root = loaderController.load();
                Scene newScene = new Scene(root);
                windowShelfPlayers.getIcons().add(icon);
                windowShelfPlayers.setScene(newScene);
                windowShelfPlayers.setTitle("Shelf Other Players");
                windowShelfPlayers.centerOnScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */

    public void setToken(int id){
        if(id == 1){
            int score1 =  GUIApplication.clientGUI.getGameView().getCGC1Points();
            token1.setImage(new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + score1 + ".jpg") ) ) );
        }
        else if(id == 2){
            int score2 = GUIApplication.clientGUI.getGameView().getCGC2Points();
            token2.setImage(new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + score2 + ".jpg") ) ) );
        }
    }

    private void setScoreCGC(HBox boxScore, int token){
        Image score;
        if(token == 1){
            score = new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/endgame.jpg")));
        } else {
            score = new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + token + ".jpg")));
        }
        for(Node node : boxScore.getChildren()){
            if( ((ImageView) node).getImage() == null ){
                ( (ImageView) node).setImage(score);
                //( (ImageView) node).setEffect( );
                break;
            }
        }
    }

    public void updateScore(int id, int token, boolean myScore){
        if(myScore){
            setScoreCGC(scorePlayer, token);
            setToken(id);
        } else {
            // ancora da fare
        }
    }

    public void updateBoard(LivingRoomView livingRoomView){
        //aggiorno i token, aggiorno la tabella dei miei punteggi:
        livingRoom.getChildren().clear();
        tileChoosenP.clear();
        tileChoosenI.clear();
        tileOrderPosition.clear();
        columnActive = false;
        setLivingRoom(livingRoomView);
    }

    public void setLivingRoom(LivingRoomView livingRoomView){
        Couple[][] board = livingRoomView.getBoard();
        for(int row = 0; row < board[0].length; row++){
            for (int colum = 0; colum < board.length; colum++) {
                Couple tile = board[row][colum];
                if(!(tile.getState().equals(State.INVALID) || tile.getState().equals(State.EMPTY))){
                    ImageView tileLR = new ImageView(showTile(tile.getTile()));
                    tileLR.setFitHeight(47);
                    tileLR.setFitWidth(47);
                    setInnerShadowTile(tileLR);
                    livingRoom.add(tileLR, colum, row);
                }
            }
        }
    }

    public void updateShelfClient(ShelfView shelfView){
        clearOrderPosition();
        setShelf(myShelf, shelfView);
    }

    private void setShelf(GridPane shelfGUI, ShelfView shelfView){
        Couple[][] shelf = shelfView.getShelfsMatrixView();
        for(Node node : shelfGUI.getChildren()){
            int row = GridPane.getRowIndex(node);
            int column = GridPane.getColumnIndex(node);
            if(!shelf[row][column].getState().equals(State.EMPTY)){
                if(((ImageView) node).getImage() == null){
                    Image tile = showTile( shelf[row][column].getTile() );
                    ((ImageView) node).setImage(tile);
                }
            }
        }
    }

    private Image showTile(Tile tile){
        T_Type typeTile = tile.getTileType();
        String tileString;
        if(typeTile.equals(T_Type.BOOK)) tileString = "Libri1.";
        else if(typeTile.equals(T_Type.CAT)) tileString = "Gatti1.";
        else if(typeTile.equals(T_Type.GAMES)) tileString = "Giochi1.";
        else if(typeTile.equals(T_Type.FRAME)) tileString = "Cornici1.";
        else if(typeTile.equals(T_Type.TROPHY)) tileString = "Trofei1.";
        else tileString = "Piante1.";
        return new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Tiles/" + tileString + tile.getFigure() + ".png" )));
    }

    public void chooseColumn(MouseEvent mouseEvent){
        if(columnActive){
            String orderT = "";
            for(Integer pos: tileOrderPosition ){
                orderT += " " + (pos + 1);
            }
            System.out.println("ORDER: " + orderT);
            GUIApplication.clientGUI.getCommPars().elaborateInput("order" + orderT );
            int columnChoosen =  Integer.parseInt( ( (Label) ((StackPane) mouseEvent.getSource()).getChildren().get(1) ).getText() ) - 1;
            GUIApplication.clientGUI.getCommPars().elaborateInput("column " + columnChoosen );
        } else {
            GUIApplication.clientGUI.printError("First you have to choose the tiles and press OK");
        }
    }

    //Quando un giocatore prende la tileEndGame deve essere resa invisibile in quanto è stata presa dal giocatore,
    //se è stata presa dal giocatore stesso allora deve essere mostrata nello ScoreBox
    public void tileEndGameTaken(Boolean currentPlayer){
        if(currentPlayer){
            ((ImageView) scorePlayer.getChildren().get(2)).setImage(tileEndGame.getImage());
        }
        tileEndGame.setVisible(false);
    }

    public void clickedPlayers(ActionEvent actionEvent){

    }

    public void clickedButtonOK(MouseEvent mouseEvent){
        String posTile = "";
        for(String tile: tileChoosenP){
            posTile += " " + tile;
        }
        GUIApplication.clientGUI.getCommPars().elaborateInput("tiles" + posTile);
    }

    //Questa funzione mi permette di gestire il trascinamento delle tile 2/3 tile per cui dovrò decidere l'ordine:
    private void setDraggingTile(ImageView imageView){
        imageView.setOnMousePressed(event -> {
            // Salva la posizione iniziale dell'immagine quando viene premuto il mouse
            xOffset = event.getSceneX(); // event.getX(); //imageView.getX();
            yOffset = event.getSceneY(); //event.getY();
        });

        imageView.setOnMouseDragged(event -> {
            //Sposta l'immagine sulla nuova posizione
            imageView.setTranslateX(event.getSceneX() - xOffset);
            imageView.setTranslateY(event.getSceneY() - yOffset);
        });

        imageView.setOnMouseReleased(event -> {
            int positionOther = -1;
            int positionImage = -1;
            for (int position = 2; position >= 0; position--) {
                ImageView otherImageView = (ImageView) chooseTileBox.getChildren().get(position);
                if(otherImageView.getImage() != null){
                    if (imageView == otherImageView) positionImage = position;
                    else if (imageView.getBoundsInParent().intersects(otherImageView.getBoundsInParent())) {
                        positionOther = position;

                    }
                }
            }
            if(positionOther >= 0 && positionImage >= 0){
                Image tempImage = imageView.getImage();
                tileChoosenI.set(2 - positionImage, ((ImageView) chooseTileBox.getChildren().get(positionOther)).getImage());
                tileChoosenI.set(2 - positionOther, tempImage);

                int tempPosImage = tileOrderPosition.get(2 - positionImage);
                tileOrderPosition.set(2 - positionImage, tileOrderPosition.get( 2 - positionOther ));
                tileOrderPosition.set(2 - positionOther, tempPosImage);
                imageView.setImage(((ImageView) chooseTileBox.getChildren().get(positionOther)).getImage());
                ((ImageView) chooseTileBox.getChildren().get(positionOther)).setImage(tempImage);

            }
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
        });
    }

    //Con queste due funzioni mi permettono di mettere in rilievo i bottoni quando ci passo sopra divenetando opachi
    public void activeButton(MouseEvent event){
        StackPane stackPane = (StackPane) event.getSource();
        stackPane.getChildren().get(0).setOpacity(0.7);
    }

    public void deactivateButton(MouseEvent event){
        StackPane stackPane = (StackPane) event.getSource();
        stackPane.getChildren().get(0).setOpacity(1);
    }

    //Devo poi gestire i vari comportamenti a seconda dei bottini su cui clicco:
    //...

    //Seleziona le Tile su cui clicco e se ci passo sopra diventano opache:
    public void setInnerShadowTile(ImageView tile){
        tile.setOnMouseClicked( event -> {
            int row = GridPane.getRowIndex(tile);
            int column = GridPane.getColumnIndex(tile);
            String positionTile = row + "," + column;
            if(tile.getEffect() == null){
                tileChoosenP.add(positionTile);
                tileChoosenI.add(tile.getImage());
                InnerShadow tileShadow = new InnerShadow();
                tileShadow.setColor(colorTileTaken);
                tileShadow.setChoke(0.35);
                tileShadow.setHeight(20);
                tileShadow.setWidth(20);
                tile.setEffect(tileShadow);
                columnActive = false;
            } else {
                tile.setEffect(null);
                int pos = tileChoosenP.indexOf(positionTile);
                tileChoosenP.remove(pos);
                tileChoosenI.remove(pos);
            }

        });
        tile.setOnMouseEntered( event -> {
            tile.getScene().setCursor(Cursor.HAND);
            tile.setOpacity(0.7);
        });
        tile.setOnMouseExited( event -> {
            tile.getScene().setCursor(Cursor.DEFAULT);
            tile.setOpacity(1);
        });
    }


    public void sendMessage(MouseEvent mouseEvent){
        String text = boxMessage.getText();
        if(!text.isBlank()){
            //saveMessage(choiceChat.getValue().toString(), GUIApplication.clientGUI.getMaster().getUsername(), text);
            GUIApplication.clientGUI.getCommPars().elaborateInput("@" + choiceChat.getValue() + " " + text);
            System.out.println("INVIO MESSAGGIO: " + choiceChat.getValue() + "Text: " + text );
        }
        boxMessage.setText(null);
    }

    public void setMessageEntry(String fromPerson, String message, String idChat){
        for(Node chat: boxChat.getChildren()) {
            if (idChat.equals(chat.getId()) || fromPerson.equals(chat.getId()) ) {
                Label text = new Label();
                String backgroundColorText = colorBackgroundChat(fromPerson);
                text.setWrapText(true);
                text.setText("@" + fromPerson + ":\n" + message);
                text.setTextFill(Color.BLACK);
                VBox.setMargin(text, new Insets(5, 5, 5, 5));
                text.setStyle("-fx-background-radius: 7px; -fx-border-radius: 7px; -fx-padding: 5 5 5 5; -fx-font-style: italic; -fx-font-family: Calibri; -fx-font-size: 14; -fx-background-color: " + backgroundColorText);
                text.setPrefWidth(195);
                text.setMinHeight(Region.USE_PREF_SIZE);
                ((VBox) chat).getChildren().add(text);
                break;
            }
        }
        System.out.println("ARRIVO MESSAGGIO!!: " + fromPerson + " , " + idChat + " , " + message );
    }


    private String colorBackgroundChat(String player){
        if(Objects.equals(player, "you")) player = GUIApplication.clientGUI.getMaster().getUsername();
        for(int i= 0; i < GUIApplication.clientGUI.getGameView().getPlayersView().size(); i++){
            if(GUIApplication.clientGUI.getGameView().getPlayersView().get(i).getNickname().equals(player)){
                return colorChat[i];
            }
        }
        return "#ffffff";
    }


    public void setLabelTurn(String turnOf){
        labelTurn.setText(turnOf);
    }

    public void clearOrderPosition(){
        tileOrderPosition.clear();
        for(Node node: chooseTileBox.getChildren()){
            ((ImageView) node).setImage(null);
        }
    }

    //La chiamo quando il giocatore deve scegliere l'ordine delle tile
    public void setTileOrderPosition(){
        columnActive = true;
        clearOrderPosition();
        for(int i = 0; i < tileChoosenI.size(); i++){
            tileOrderPosition.add(i);
            ImageView tile = (ImageView) chooseTileBox.getChildren().get(2 - i);
            tile.setImage(tileChoosenI.get(i));
            setDraggingTile(tile);
        }
    }








}
