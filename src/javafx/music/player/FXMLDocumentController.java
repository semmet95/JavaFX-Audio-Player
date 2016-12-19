/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx.music.player;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author LENOVO
 */
public class FXMLDocumentController implements Initializable {

    boolean firstTime = true;
    @FXML
    private ToggleButton playPause;
    @FXML
    private MediaView player;
    @FXML
    private Button prevButton, nextButton;
    @FXML
    private Slider volume;
    @FXML
    private ImageView albumArt;
    private ArrayList<Media> mediaFiles = new ArrayList();
    private int counter = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            playPause.requestFocus();
        });

    }

    @FXML
    private void playPauseClicked(ActionEvent event) {
        if (firstTime) {
            File file = null;
            Node node = (Node) event.getSource();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav");
            fileChooser.getExtensionFilters().add(fileExtension);
            file = fileChooser.showOpenDialog(node.getScene().getWindow());
            System.out.println(file);
            if (file != null) {

                Media media;
                media = new Media(file.toURI().toASCIIString());
                mediaFiles.add(media);
                MediaPlayer mediaplayer = new MediaPlayer(media);
                prevButton.setDisable(false);
                nextButton.setDisable(false);
                ++counter;
                player = new MediaView(mediaplayer);
                mediaplayer.setAutoPlay(true);
                player.getMediaPlayer().getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
                    if (change.wasAdded()) {
                        if (change.getKey().equals("image")) {
                            Image art = (Image) change.getValueAdded();
                            System.out.println();
                            double artWidth = art.getWidth(), viewWidth = albumArt.getFitWidth();
                            albumArt.setX(200);
                            albumArt.setImage(art);
                            albumArt.setX(200);
                        }
                    }
                });
                volume.valueProperty().addListener((Observable observable) -> {
                    if (volume.isValueChanging()) {
                        System.out.println(volume.getValue());
                        player.getMediaPlayer().setVolume(volume.getValue() / 100);
                    }
                });
                player.getMediaPlayer().setOnEndOfMedia(() -> {
                    playPause.setSelected(false);
                });
                firstTime = false;

            } else {
                if (playPause.isSelected()) {
                    player.getMediaPlayer().play();
                } else {
                    player.getMediaPlayer().pause();
                }
            }
        } else {
            System.out.println("I'm here");
            playPause.setSelected(false);
        }
    }

    @FXML
    private void browseClicked(ActionEvent event) {
        firstTime = false;
        Node node = (Node) event.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav");
        fileChooser.getExtensionFilters().add(fileExtension);
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        Media media;
        media = new Media(file.toURI().toASCIIString());
        mediaFiles.add(media);
        MediaPlayer mediaplayer = new MediaPlayer(media);
        try {
            player.getMediaPlayer().dispose();
        } catch (Exception e) {

        }
        prevButton.setDisable(false);
        nextButton.setDisable(false);
        playPause.setSelected(true);
        ++counter;
        mediaplayer.setAutoPlay(true);
        player = new MediaView(mediaplayer);
        player.getMediaPlayer().getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                if (change.getKey().equals("image")) {
                    Image art = (Image) change.getValueAdded();
                    System.out.println();
                    double artWidth = art.getWidth(), viewWidth = albumArt.getFitWidth();
                    albumArt.setX(50);
                    albumArt.setImage(art);
                    albumArt.setX(50);
                }
            }
        });
        volume.valueProperty().addListener((Observable observable) -> {
            if (volume.isValueChanging()) {
                System.out.println(volume.getValue());
                player.getMediaPlayer().setVolume(volume.getValue() / 100);
            }
        });
        player.getMediaPlayer().setOnEndOfMedia(() -> {
            playPause.setSelected(false);
        });
    }

    @FXML
    private void prevClicked(ActionEvent event) {
        if (counter == 0) {
            player.getMediaPlayer().seek(Duration.ZERO);
        } else {
            player.getMediaPlayer().dispose();
            albumArt.setImage(null);
            player = new MediaView(new MediaPlayer(mediaFiles.get(--counter)));
            player.getMediaPlayer().play();
            playPause.setSelected(true);
            player.getMediaPlayer().setOnEndOfMedia(() -> {
                playPause.setSelected(false);
            });
            player.getMediaPlayer().getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                if (change.getKey().equals("image")) {
                    Image art = (Image) change.getValueAdded();
                    System.out.println("I'm in");
                    double artWidth = art.getWidth(), viewWidth = albumArt.getFitWidth();
                    albumArt.setX(50);
                    albumArt.setImage(art);
                    albumArt.setX(50);
                }
            }
        });
        }
    }

    @FXML
    private void nextClicked(ActionEvent event) {
        if (counter + 1 == mediaFiles.size()) {
            player.getMediaPlayer().stop();
            playPause.setSelected(false);
        } else {
            player.getMediaPlayer().dispose();
            albumArt.setImage(null);
            player = new MediaView(new MediaPlayer(mediaFiles.get(++counter)));
            playPause.setSelected(true);
            player.getMediaPlayer().play();
            player.getMediaPlayer().setOnEndOfMedia(() -> {
                playPause.setSelected(false);
            });
            player.getMediaPlayer().getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                if (change.getKey().equals("image")) {
                    Image art = (Image) change.getValueAdded();
                    System.out.println();
                    double artWidth = art.getWidth(), viewWidth = albumArt.getFitWidth();
                    albumArt.setX(50);
                    albumArt.setImage(art);
                    albumArt.setX(50);
                }
            }
        });
        }
    }
}
