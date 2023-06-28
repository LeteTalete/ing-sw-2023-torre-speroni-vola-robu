package it.polimi.ingsw.view.ControllerGUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ErrorMessage {
    private static int toastDelay = 2000; //2 seconds
    private static int fadeInDelay = 600; // 0.6 seconds
    private static int fadeOutDelay = 600; // 0.6 seconds
    public static void errorMessage(Stage ownerStage, String messageError) {
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(messageError);
        text.setFont(Font.font("Verdana", 30));
        text.setFill(new Color(0.6, 0.1, 0.01, 1) ); //#Color.WHITE 0.45, 0.14, 0.15, 1  : 152, 25, 8

        StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-padding: 10px;");//#523520
        root.setOpacity(0.6);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0.8));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) ->
        {
            new Thread(() -> {
                try {
                    Thread.sleep(toastDelay);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }
}