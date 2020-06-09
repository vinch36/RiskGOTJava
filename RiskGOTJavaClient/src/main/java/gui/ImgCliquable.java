package gui;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ImgCliquable extends VBox {

    public ImgCliquable(String pathToImage, int requestedWidth, int requestedHeight) {
        super();
        cliquable = true;
        selectionnee=false;
        this.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.imgCarte =new Image(getClass().getResourceAsStream(pathToImage),requestedWidth,requestedHeight,false,false);
        this.imageView = new ImageView(imgCarte);
        imageView.setOnMouseClicked(e->{
            clickOnImageView();
        });
        this.getChildren().add(imageView);
    }


    private Image imgCarte;
    protected ImageView imageView;

    public boolean isSelectionnee() {
        return selectionnee;
    }

    public void setSelectionnee(boolean selectionnee) {
        this.selectionnee = selectionnee;
        if (selectionnee == true) {
            this.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        } else {
            this.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        }
    }

    protected boolean selectionnee;

    public boolean isCliquable() {
        return cliquable;
    }

    public void setCliquable(boolean cliquable) {
        this.cliquable = cliquable;
        if (cliquable)
        {
            this.imageView.setCursor(Cursor.HAND);
        }
        else
        {
            this.imageView.setCursor(Cursor.DEFAULT);
        }

    }

    protected boolean cliquable;

    private void clickOnImageView(){
        if (cliquable) {
            selectionnee = !selectionnee;
            if (selectionnee == true) {
                this.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
            } else {
                this.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            }
        }
    }
}
