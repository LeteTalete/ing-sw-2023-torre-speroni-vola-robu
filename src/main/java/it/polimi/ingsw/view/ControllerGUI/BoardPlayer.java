package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.GUIApplication;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.util.ArrayList;
import java.util.Objects;


public class BoardPlayer extends GenericController {
    @FXML private VBox chooseTileBox, All;
    @FXML private StackPane boxChat;
    @FXML private HBox scorePlayer;
    @FXML private TextArea boxMessage;
    @FXML private Label labelTurn;
    @FXML private ImageView chair, tileEndGame, imageCommonCard1, imageCommonCard2, imagePersonalGoalCard, token1, token2, tile1, tile2, tile3;
    @FXML private ChoiceBox choiceChat;
    @FXML private GridPane myShelf, livingRoom;
    private Color colorTileTaken = new Color(0.9548, 1.0, 0.21, 1.0);//Yellow
    private double xOffset = 0, yOffset = 0;
    private ArrayList<String> tileChoosenP = new ArrayList<>();
    private ArrayList<Image> tileChoosenI = new ArrayList<>();
    private String[] colorChat = {"#f0fff0", "#ffc07e", "#93c47d", "#ffd049"}; //Ciano, arancione, verde, giallo

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
        choiceChat.getItems().add("All");
        for(PlayerView player : gameView.getPlayersView()){
            if(!player.getNickname().equals( GUIApplication.clientGUI.getMaster().getUsername() )){
                choiceChat.getItems().add(player.getNickname());
                VBox chatBox = new VBox();
                chatBox.setPrefWidth(All.getPrefWidth());
                chatBox.setPrefHeight(All.getPrefHeight());
                chatBox.setStyle(All.getStyle());
                chatBox.setId(player.getNickname());
                chatBox.setAlignment(Pos.BOTTOM_CENTER);
                chatBox.setVisible(false);
                chatBox.setFillWidth(true);
                boxChat.getChildren().add(chatBox);
            }
        }
        choiceChat.setValue("All"); //Per settare il valore predefinito
        choiceChat.setOnAction(event -> {
            for(Node chat: boxChat.getChildren()) {
                if(chat.getId().equals(choiceChat.getValue())){
                    chat.setVisible(true);
                } else {
                    if(chat.isVisible()) chat.setVisible(false);
                }
            }
        });

        setToken();
    }

    public void setToken(){
        int score1 =  GUIApplication.clientGUI.getGameView().getCGC1Points();
        int score2 = GUIApplication.clientGUI.getGameView().getCGC2Points();
        token1.setImage(new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + score1 + ".jpg") ) ) );
        token2.setImage(new Image( Objects.requireNonNull( getClass().getResourceAsStream("/imgs/ScoreTiles/scoring_" + score2 + ".jpg") ) ) );

    }

    public void setLivingRoom(LivingRoomView livingRoomView){
        Couple[][] board = livingRoomView.getBoard();
        for(int row = 0; row < board[0].length; row++){
            for (int colum = 0; colum < board.length; colum++) {
                Couple tile = board[row][colum];
                if(!(tile.getState().equals(State.INVALID) || tile.getState().equals(State.EMPTY))){
                    ImageView tileLR = new ImageView(showTile(tile));
                    tileLR.setFitHeight(47);
                    tileLR.setFitWidth(47);
                    setInnerShadowTile(tileLR);
                    livingRoom.add(tileLR, colum, row);
                }
            }
        }
    }

    private Image showTile(Couple tile){
        T_Type typeTile = tile.getTile().getTileType();
        String tileString;
        if(typeTile.equals(T_Type.BOOK)) tileString = "Libri1.";
        else if(typeTile.equals(T_Type.CAT)) tileString = "Gatti1.";
        else if(typeTile.equals(T_Type.GAMES)) tileString = "Giochi1.";
        else if(typeTile.equals(T_Type.FRAME)) tileString = "Cornici1.";
        else if(typeTile.equals(T_Type.TROPHY)) tileString = "Trofei1.";
        else tileString = "Piante1.";
        return new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Tiles/" + tileString + tile.getTile().getFigure() + ".png" )));
    }


    public boolean chooseTilesController(ImageView tile, Position position){
        Image image = tile.getImage();
        //Aggiungere controllo delle tile prese
        //tilesChoose.add(position);
        return true;
    }


    public void chooseColumn(MouseEvent mouseEvent){
        //((Label) ((StackPane) mouseEvent.getSource())).getChildren(1) ??
        int columnChoosen =  Integer.parseInt( ( (Label) ((StackPane) mouseEvent.getSource()).getChildren().get(1) ).getText() ) - 1;
        //Trovo prima di tutto la posizione in cui devo inizare a inserire le immagini nella Shelf,
        //Per farlo devo prima scansionare tutta la schelf al fine di trovare qual è la prima riga libera di quella colonna ( partendo dal basso )
        int rowFree = 0;
        for(Node node : myShelf.getChildren()){
            if (GridPane.getColumnIndex(node) == columnChoosen && ((ImageView) node).getImage() == null ) {
                rowFree =  rowFree > GridPane.getRowIndex(node) ? rowFree : GridPane.getRowIndex(node);
            }
        }
        //Devo controllare se la può inserire nelle shelf: creare una funzione che llo controlla
        for( Image image : tileChoosenI ){
            for(Node node : myShelf.getChildren()){
                if(GridPane.getColumnIndex(node) == columnChoosen && GridPane.getRowIndex(node) == rowFree){
                    ((ImageView) node).setImage(image);
                    --rowFree;
                    break;
                }
            }
        }
        //Devo poi togliere dalla colonna in cui scelgo l'ordine
        tileChoosenP.clear();
        tileChoosenI.clear();
    }

    //Quando un giocatore prende la tileEndGame deve essere resa invisibile in quanto è stata presa dal giocatore,
    //se è stata presa dal giocatore stesso allora deve essere mostrata nello ScoreBox
    public void tileEndGameTaken(Boolean currentPlayer){
        if(currentPlayer){
            ((ImageView) scorePlayer.getChildren().get(2)).setImage(tileEndGame.getImage());
        }
        tileEndGame.setVisible(false);
    }

    public void clickedButtonOK(MouseEvent mouseEvent){
        for(int i = 0; i < tileChoosenI.size(); i++){
            ImageView tile = (ImageView) chooseTileBox.getChildren().get(2 - i);
            tile.setImage(tileChoosenI.get(i));
            setDraggingTile(tile);
        }
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

                        // Scambia le immagini tra imageView corrente e altre immagini sovrapposte
                        //Image tempImage = imageView.getImage();
                        //imageView.setImage(otherImageView.getImage());
                        //otherImageView.setImage(tempImage);


                        ////Devo spostare la posizione delle tile dell'ArrayList anche
                        //break;
                    }
                }
            }
            if(positionOther >= 0 && positionImage >= 0){
                Image tempImage = imageView.getImage();

                tileChoosenI.set(2 - positionImage, ((ImageView) chooseTileBox.getChildren().get(positionOther)).getImage());
                tileChoosenI.set(2 - positionOther, tempImage);
                String tempPosImage = tileChoosenP.get(2 - positionImage);
                tileChoosenP.set(2 - positionImage, tileChoosenP.get( 2 - positionOther ) );
                tileChoosenP.set(2 - positionOther, tempPosImage);

                imageView.setImage(((ImageView) chooseTileBox.getChildren().get(positionOther)).getImage());
                ((ImageView) chooseTileBox.getChildren().get(positionOther)).setImage(tempImage);

            }
            System.out.print("Scelta ordine: ");
            tileChoosenP.forEach( n -> System.out.println(n + " "));
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
            /* prima di far vedere la tile selezionata devo verificare che sia possibile prenderla. faccio il controllo */
            //((ImageView) event.getSource()).getImage()
            //chooseTilesController(  )
            int row = GridPane.getRowIndex(tile);
            int column = GridPane.getColumnIndex(tile);
            String positionTile = row + "," + column;
            if(tile.getEffect() == null){
                //Controllo che le posizioni della tile selezionata siano valide sia per la LivingRoom che per la Shelf//
                //Se è valida:
                tileChoosenP.add(positionTile);
                tileChoosenI.add(tile.getImage());
                InnerShadow tileShadow = new InnerShadow();
                tileShadow.setColor(colorTileTaken);
                tileShadow.setChoke(0.35);
                tileShadow.setHeight(20);
                tileShadow.setWidth(20);
                tile.setEffect(tileShadow);
            } else {
                //Vuol dire la tile è già stata presa quindi devo toglierla dall'array
                int pos = tileChoosenP.indexOf(positionTile);
                tileChoosenP.remove(pos);
                tileChoosenI.remove(pos);
                tile.setEffect(null);
            }

        });
        tile.setOnMouseEntered( event -> {
            tile.setOpacity(0.7);
        });
        tile.setOnMouseExited( event -> {
            tile.setOpacity(1);
        });

    }


    public void sendMessage(MouseEvent mouseEvent){
        String text = boxMessage.getText();
        if(!text.isBlank()){
            saveMessage(choiceChat.getValue().toString(), GUIApplication.clientGUI.getMaster().getUsername(), text);
        }
        boxMessage.setText(null);
    }

    public void saveMessage(String idChat, String fromPerson, String message){
        for(Node chat: boxChat.getChildren()) {
            if (idChat.equals(chat.getId())) {
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
    }

    //Mi serve per eliminare le tile che ho selezionato dalla living room, se sono corrette se no toglie la selezione
    public void delateTilesLR(){

    }

    public String colorBackgroundChat(String player){
        for(int i= 0; i < GUIApplication.clientGUI.getGameView().getPlayersView().size(); i++){
            if(GUIApplication.clientGUI.getGameView().getPlayersView().get(i).getNickname().equals(player)){
                return colorChat[i];
            }
        }
        return "ffffff";
    }







}
