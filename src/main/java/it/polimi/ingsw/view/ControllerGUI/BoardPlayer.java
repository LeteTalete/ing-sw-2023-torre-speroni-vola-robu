package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.GUIApplication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;

/**
 * BoardPlayer class manages the scene of the game, along with the shelf, the chat, the board, all the goal cards and
 * the shelves of the other players.
 * */

public class BoardPlayer extends GenericController {
    /**
     * logger to keep track of events, such as errors and info about parameters
     * */
    private static final Logger fileLog = LogManager.getRootLogger();

    /**
     * VBox chooseTileBox is the box in which the tiles chosen in the LivingRoom by the player will be shown,
     * so that he can choose the order of them before placing them in his Shelf.
     * */
    @FXML private VBox chooseTileBox, all;
    /**
     * StackPane boxChat contains the chat of the game
     * */
    @FXML private StackPane boxChat;
    @FXML private HBox scorePlayer, shelfOtherPlayers, commonCard;
    @FXML private TextArea boxMessage;
    @FXML private Label labelTurn;
    @FXML private ImageView chair, tileEndGame, imageCommonCard1, imageCommonCard2, imagePersonalGoalCard, token1, token2;
    @FXML private ChoiceBox choiceChat;
    @FXML private GridPane myShelf, livingRoom;

    /**
     * colorTileTaken is the color of the tile when it is taken
     * */
    private Color colorTileTaken = new Color(0.9548, 1.0, 0.21, 1.0);
    private double xOffset = 0, yOffset = 0;
    /**
     * tileChosenP is an arrayList containing the positions of the tiles chosen by the player
     * */
    private ArrayList<String> tileChosenP = new ArrayList<>();
    /**
     * tileOrderPosition contains the order of the re-arranged tiles
     * */
    private ArrayList<Integer> tileOrderPosition = new ArrayList<>();
    /**
     * tileChoosen contains the pictures of the tiles chosen by the player
     * */
    private ArrayList<Image> tileChosenI = new ArrayList<>();
    /**
     * colorChat contains the colors of the chat
     * */
    private String[] colorChat = {"#f0fff0", "#ffc07e", "#93c47d", "#ffd049"};
    /**
     * columnActive is a boolean signalling whether the player is allowed to choose a column
     * */
    private boolean columnActive = false;

    /**
     * setBoardPlayer method sets the entirety of the game window, i.e. the board, the shelves, the chat, and
     * the goal cards.
     * @param gameView - contains the updated view of the game.
     * */
    public void setBoardPlayer(GameView gameView){
        int CGC1 = gameView.getCommonGoalCards().get(0).getID();
        int CGC2 = gameView.getCommonGoalCards().get(1).getID();
        int PGC = Objects.requireNonNull(gameView.getPlayersView().stream()
                .filter(p -> p.getNickname().equals(GUIApplication.getClientGUI().getMaster().getUsername())).findFirst().orElse(null)).getPersonalGoalCard().getNumPGC();
        Image PersonalGC = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/PersonalCards/Personal_Goals" + PGC + ".png")));
        Image CommonGC1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/CommonCards/"+ CGC1 +".jpg")));
        Image CommonGC2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/CommonCards/" + CGC2 + ".jpg")));
        imageCommonCard1.setImage(CommonGC1);
        imageCommonCard2.setImage(CommonGC2);
        imagePersonalGoalCard.setImage(PersonalGC);
        choiceChat.getItems().add("all");
        ArrayList<String> namePlayers = new ArrayList<>();
        for(PlayerView player : gameView.getPlayersView()){
            if(!player.getNickname().equals( GUIApplication.getClientGUI().getName() )){
                namePlayers.add(player.getNickname());
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
            } else {
                if( player.getChair() ) chair.setVisible(true);
            }
        }
        choiceChat.setValue("all");
        choiceChat.setOnAction(event -> {
            for(Node chat: boxChat.getChildren()) {
                if(chat.getId().equals(choiceChat.getValue())){
                    chat.setVisible(true);
                } else {
                    if(chat.isVisible()) chat.setVisible(false);
                }
            }
        });
        setShelfOtherPlayers(namePlayers);
    }

    /**
     * setShelfOtherPlayers method set the Shelfs of all players in the list, so that they show in the GUI.
     * @param name - list of all players except the player himself.
     */
    private void setShelfOtherPlayers(ArrayList<String> name){
        for(int i = 0; i < name.size(); i++){
            StackPane shelf = (StackPane) shelfOtherPlayers.getChildren().get(i);
            shelf.setVisible(true);
            Label labelName = (Label) shelf.getChildren().get(2);
            labelName.setText(name.get(i) );
        }
    }

    /**
     * updateShelfOthers method updates the Shelf of all players.
     * @param shelfViews - list of all players except the player himself.
     */
    public void updateShelfOthers(ArrayList<ShelfView> shelfViews){
        for(int i = 0; i < shelfViews.size(); i++){
            GridPane shelf = (GridPane) ( (StackPane) shelfOtherPlayers.getChildren().get(i)).getChildren().get(0);
            setShelf(shelf, shelfViews.get(i));
        }
    }

    /**
     * setToken method sets the tokens that are shown in the GUI to the player, so that if they are taken,
     * it shows the scaling of the score.
     * @param gameView - contains the updated view of the game.
     */
    public void setToken(GameView gameView){
        int score1 =  gameView.getCGC1Points();
        if(score1 > 0) token1.setImage(new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + score1 + ".jpg") ) ) );
        else token1.setImage(null);
        int score2 = gameView.getCGC2Points();
        if(score2 > 0) token2.setImage(new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + score2 + ".jpg") ) ) );
        else token2.setImage(null);
    }

    /**
     * setScoreCGC method ets the scores of the player (Client) itself in case it gets Tokens so that they are shown in the GUI.
     * @param token - token number.
     */
    public void setScoreCGC(int token){
        Image score;
        if(token == 1){
            score = new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/endgame.jpg")));
        } else {
            score = new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + token + ".jpg")));
        }
        for(Node node : scorePlayer.getChildren()){
            if( ((ImageView) node).getImage() == null ){
                ( (ImageView) node).setImage(score);
                DropShadow dropShadow = new DropShadow();
                dropShadow.setHeight(20);
                dropShadow.setWidth(20);
                dropShadow.setSpread(0.15);
                dropShadow.setOffsetX(1.2);
                dropShadow.setOffsetY(2.0);
                node.setEffect(dropShadow);
                break;
            }
        }
    }

    /**
     * setEmptyTileEndGame method is called in case a player gets the End of Game Token and then it is removed from the GUI,
     * so that it is no longer shown.
     */
    public void setEmptyTileEndGame(){
        tileEndGame.setVisible(false);
    }

    /**
     * showDescriptionCGC method show the description of the CGC being pressed.
     * @param mouseEvent - if the player presses on the CGC image, it calls the event.
     */
    public void showDescriptionCGC(MouseEvent mouseEvent){
        int i = 0;
        for(Node node : commonCard.getChildren() ){
            if(mouseEvent.getSource().equals(node)){
                String description = GUIApplication.getClientGUI().getGameView().getCommonGoalCards().get(i).getDescription();
                GUIApplication.error(description);
                break;
            }
            ++i;
        }
    }

    /**
     * updateBoard method that updates the GUI Board.
     * @param livingRoomView - contains the updated view of the livingroom.
     */
    public void updateBoard(LivingRoomView livingRoomView){
        livingRoom.getChildren().clear();
        tileChosenP.clear();
        tileChosenI.clear();
        tileOrderPosition.clear();
        columnActive = false;
        setLivingRoom(livingRoomView);
    }

    /**
     * livingRoomView method that sets the livingRoom to show.
     * @param livingRoomView - contains the updated view of the livingroom.
     */
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

    /**
     * updateShelfClient method that updates the player's shelf.
     * @param shelfView - contains the updated view of the shelf.
     */
    public void updateShelfClient(ShelfView shelfView){
        clearOrderPosition();
        setShelf(myShelf, shelfView);
    }

    /**
     * setShelf method used to set the GridPane of the player's shelf, so that its updated Shelf is shown in the GUI.
     * @param shelfGUI - GridPane of the Shelf to be updated in the GUI.
     * @param shelfView - contains the updated view of the shelf.
     */
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

    /**
     * showTile method returns the Image of the type of tile you want to show depending on the Tile entered.
     * @param tile - the type of Tile you want to show.
     * @return - the Image of the type of Tile you want to show.
     */
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

    /**
     * chooseColumn method called when the player presses the button of the column number in which
     * he wants to place the Tiles in his shelf
     * @param mouseEvent - event of the player pressing on the button.
     */
    public void chooseColumn(MouseEvent mouseEvent){
        if(columnActive){
            String orderT = "";
            for(Integer pos: tileOrderPosition ){
                orderT += " " + (pos + 1);
            }
            fileLog.info("ORDER: " + orderT);
            GUIApplication.getClientGUI().getCommPars().elaborateInput("order" + orderT );
            int columnChoosen =  Integer.parseInt( ( (Label) ((StackPane) mouseEvent.getSource()).getChildren().get(1) ).getText() ) - 1;
            GUIApplication.getClientGUI().getCommPars().elaborateInput("column " + columnChoosen );
        } else {
            GUIApplication.getClientGUI().printError("First you have to choose the tiles and press OK");
        }
    }

    /**
     * clickedButtonOK method once the player chooses the Tiles from the livingRoom he presses the OK button,
     * so that the change of the choice can be sent to the CommandParsing.
     * @param mouseEvent - event of the button pressed by the player.
     */
    public void clickedButtonOK(MouseEvent mouseEvent){
        String posTile = "";
        for(String tile: tileChosenP){
            posTile += " " + tile;
        }
        GUIApplication.getClientGUI().getCommPars().elaborateInput("tiles" + posTile);
    }

    /**
     * setDraggingTile method allows to manage tile dragging, 2/3 tiles for which the player will have to decide the order.
     * @param imageView - ImageView on which the drag and drop effect is activated.
     */
    private void setDraggingTile(ImageView imageView){
        imageView.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            imageView.getScene().setCursor(Cursor.CLOSED_HAND);
        });

        imageView.setOnMouseEntered(event -> {
            imageView.getScene().setCursor(Cursor.OPEN_HAND);
        });

        imageView.setOnMouseDragged(event -> {
            imageView.setTranslateX(event.getSceneX() - xOffset);
            imageView.setTranslateY(event.getSceneY() - yOffset);
        });

        imageView.setOnMouseExited(event -> {
            imageView.getScene().setCursor(Cursor.DEFAULT);
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
                tileChosenI.set(2 - positionImage, ((ImageView) chooseTileBox.getChildren().get(positionOther)).getImage());
                tileChosenI.set(2 - positionOther, tempImage);

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

    /**
     * activeButton method allows buttons to be raised when you pass over them, becoming opaque.
     * @param event - event of the button pressed by the player.
     */
    public void activeButton(MouseEvent event){
        StackPane stackPane = (StackPane) event.getSource();
        stackPane.getChildren().get(0).setOpacity(0.7);
    }

    /**
     * deactivateButton method turns off the matte effect on the button.
     * @param event - event of the button pressed by the player
     */
    public void deactivateButton(MouseEvent event){
        StackPane stackPane = (StackPane) event.getSource();
        stackPane.getChildren().get(0).setOpacity(1);
    }


    /**
     * setInnerShadowTile method adds the effect (Yellow outline) to the ImageView of the Tile, which indicates that it
     * has been selected by the player in the LivingRoom. if instead you just hover over it, it becomes opaque.
     * @param tile - ImageView of the tile selected by the player in the LivingRoom.
     */
    public void setInnerShadowTile(ImageView tile){
        tile.setOnMouseClicked( event -> {
            int row = GridPane.getRowIndex(tile);
            int column = GridPane.getColumnIndex(tile);
            String positionTile = row + "," + column;
            if(tile.getEffect() == null) {
                if (tileChosenP.size() != 3) {
                    tileChosenP.add(positionTile);
                    tileChosenI.add(tile.getImage());
                    InnerShadow tileShadow = new InnerShadow();
                    tileShadow.setColor(colorTileTaken);
                    tileShadow.setChoke(0.35);
                    tileShadow.setHeight(20);
                    tileShadow.setWidth(20);
                    tile.setEffect(tileShadow);
                    if (columnActive) {
                        columnActive = false;
                        Platform.runLater(this::clearOrderPosition);
                    }
                }
            } else {
                    tile.setEffect(null);
                    int pos = tileChosenP.indexOf(positionTile);
                    tileChosenP.remove(pos);
                    tileChosenI.remove(pos);
                    if ( columnActive ) {
                        columnActive = false;
                        Platform.runLater(this::clearOrderPosition);
                    }
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

    /**
     * sendMessage method sends the CommandParsing the message the player wants to send once it presses on the specific button.
     * @param mouseEvent - event of the button pressed by the player.
     */
    public void sendMessage(MouseEvent mouseEvent){
        String text = boxMessage.getText();
        if(!text.isBlank()){
            GUIApplication.getClientGUI().getCommPars().elaborateInput("@" + choiceChat.getValue() + " " + text);
            fileLog.info("SENDING MESSAGE: " + choiceChat.getValue() + " Text: " + text );
        }
        boxMessage.setText(null);
    }

    /**
     * setMessageEntry method shows the message sent by the player or those arrived by other players in the GUI.
     * @param fromPerson - string of the name from whom the message arrives.
     * @param message - string of the sent or incoming message.
     * @param idChat - name from whom the message is directed.
     */
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
        fileLog.info("ARRIVAL MESSAGE: " + fromPerson + " , " + idChat + " , " + message );
    }

    /**
     * colorBackgroundChat method allows messages with specific colors for each player to be set in the gui.
     * @param player - string of the name of the player who sent the message.
     * @return - the specific color of the player entered.
     */
    private String colorBackgroundChat(String player){
        if(Objects.equals(player, "you")) player = GUIApplication.getClientGUI().getMaster().getUsername();
        for(int i= 0; i < GUIApplication.getClientGUI().getGameView().getPlayersView().size(); i++){
            if(GUIApplication.getClientGUI().getGameView().getPlayersView().get(i).getNickname().equals(player)){
                return colorChat[i];
            }
        }
        return "#ffffff";
    }


    /**
     * setLabelTurn method sets in the gui the messages to be shown to the player.
     * @param turnOf - string of the message to be shown.
     */
    public void setLabelTurn(String turnOf){
        labelTurn.setText(turnOf);
    }

    /**
     * clearOrderPosition method cleans up the Tile Sorting box so that images are removed.
     */
    public void clearOrderPosition(){
        tileOrderPosition.clear();
        for(Node node: chooseTileBox.getChildren()){
            ((ImageView) node).setImage(null);
        }
    }

    /**
     * setTileOrderPosition method sets the sorting box in order to show the player the chosen tiles and be able to make them hate them.
     */
    public void setTileOrderPosition(){
        columnActive = true;
        clearOrderPosition();
        for(int i = 0; i < tileChosenI.size(); i++){
            tileOrderPosition.add(i);
            ImageView tile = (ImageView) chooseTileBox.getChildren().get(2 - i);
            tile.setImage(tileChosenI.get(i));
            setDraggingTile(tile);
        }
    }








}
