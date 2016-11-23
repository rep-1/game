package ru.rep1.game.fx;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by lshi on 22.11.2016.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Game");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(612, 612);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image earth = new Image("room.png");
        gc.drawImage(earth, 0, 0);

        Image bullet = new Image("bullet.png", 20, 50, true, true);

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(Duration.seconds(0.017),// 60 FPS
                new EventHandler<ActionEvent>() {
                    double y=0;

                    public void handle(ActionEvent ae) {
                        // Clear the canvas
                        gc.clearRect(0, 0, 512, 512);

                        gc.drawImage(earth, 0, 0);

                        y += 0.5;
                        gc.drawImage(bullet, 100, y);
                    }
                });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();

        theStage.show();
    }
}
