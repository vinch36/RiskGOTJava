package gui;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MainView extends Application {

    private WebView webView;

    @Override
    public void start( Stage stage ) {
        createComponents(stage);

    }



    public void zoom (ScrollEvent event) {
        if (event.isControlDown()) {
            System.out.println(event.getDeltaY());
            double currentZoomValue = ((WebView) event.getSource()).getZoom();
            double delta = event.getDeltaY() / 40 / 20;
            if (delta > 0 && currentZoomValue < 2 || delta < 0 && currentZoomValue > 0.25) {
                ((WebView) event.getSource()).setZoom(currentZoomValue + delta);
            }
        }
    }


    private void createComponents(Stage primaryStage) {
        primaryStage.setHeight(1024);
        primaryStage.setWidth(1440);
        primaryStage.setTitle("Risk Game of Thrones");
        Scene scene = new Scene(new Group());
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10,10, 10,10));
        root.setStyle("-fx-border-color: black");

        //Création de la zone joueur
        HBox playerZone = new HBox();
        playerZone.setStyle("-fx-border-color: black");
        playerZone.setPrefHeight(80);
        root.setTop(playerZone);

        //Creation du menu
        VBox menu = new VBox();
        menu.setStyle("-fx-border-color: black");
        menu.setPrefWidth(150);
        menu.setFillWidth(true);
        root.setLeft(menu);



        //Création de la WebView pour la carte de GOT
        this.webView = new WebView();
        this.webView.setStyle("-fx-border-color: black");
        this.webView.prefHeightProperty().bind(primaryStage.heightProperty());
        this.webView.prefWidthProperty().bind(primaryStage.widthProperty());
        this.webView.getEngine().load(getClass().getResource("/Index.html").toString());
        this.webView.setOnScroll(event -> zoom(event));
        this.webView.setZoom(0.62);
        this.webView.getEngine().setJavaScriptEnabled(true);



        //Bouton pour appler une fonction JavaScript
        Button btn = new Button();
        btn.setText("fire JS");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (webView.getEngine() != null)
                {
                    webView.getEngine().executeScript("test('text_to_set')");
                }
            }
        });
        menu.getChildren().add(btn);

        //Mise en place de la JavaScriptInterface pour pouvoir appeler du Java (dans la classe JavaScriptInterface depuis JavaScript
        JavaScriptInterface JSI = new JavaScriptInterface();
        webView.getEngine().getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {

                        if (newState == Worker.State.SUCCEEDED) {

                            JSObject jsobject = (JSObject) webView.getEngine().executeScript("window");
                            jsobject.setMember("myJavaMember", JSI);
                        }
                    }
                });

        root.setCenter(this.webView);

        scene.setRoot(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch();
    }




}
