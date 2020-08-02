import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;


public class PaintLite extends Application {
    Model model = new Model();

    @Override
    public void start(Stage stage) {
        DrawingCanvasView drawingCanvasView = new DrawingCanvasView(model);
        ToolsView toolsView = new ToolsView(model);
        VBox root = new VBox();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem closeItem = new MenuItem("Close");

        Menu editMenu = new Menu("Edit");
        MenuItem cutItem = new MenuItem("Cut");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");

        newItem.setOnAction(newHandler());
        loadItem.setOnAction(loadHandler());
        saveItem.setOnAction(saveHandler());
        closeItem.setOnAction(closeHandler());

        cutItem.setOnAction(cutHandler());
        copyItem.setOnAction(copyHandler());
        pasteItem.setOnAction(pasteHandler());

        editMenu.getItems().addAll(cutItem, copyItem, pasteItem);
        fileMenu.getItems().addAll(newItem, loadItem, saveItem, closeItem);
        menuBar.getMenus().addAll(fileMenu, editMenu);

        SplitPane mainContainer = new SplitPane();
        mainContainer.getItems().addAll(toolsView, drawingCanvasView);
        mainContainer.setDividerPosition(0, Constants.PREF_BTN_WIDTH * 2/1200);


        root.getChildren().addAll(menuBar, mainContainer);

        root.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE:
                    model.unselectShape();
                    break;
                case BACK_SPACE:
                case DELETE:
                    model.eraseShape();
                    break;
            }
        });

        Scene scene = new Scene(root, Constants.INIT_SCREEN_WIDTH, Constants.INIT_SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("PaintLite");
        stage.show();
    }

    public EventHandler<ActionEvent> newHandler() {
        return actionEvent -> {
            if (model.hasChanged) {
                alertLoadNew("new");
            }
            model.clearCanvas();
        };
    }

    public EventHandler<ActionEvent> loadHandler() {
        return actionEvent -> {
            if (model.hasChanged && model.shapes.size() != 0) {
                alertLoadNew("load");
            } else {
                load();
            }
        };
    }

    public EventHandler<ActionEvent> saveHandler() {
        return actionEvent -> {
            if (model.hasChanged) {
                save();
            }
        };
    }

    public EventHandler<ActionEvent> closeHandler() {
        return actionEvent -> {
            if (!model.hasChanged ) {
                Platform.exit();
                System.exit(0);
            } else {
                if (model.shapes.size() != 0) {
                    alertClose();
                }
            }
        };
    }

    public EventHandler<ActionEvent> cutHandler() {
        return actionEvent -> {
            model.cut();
        };
    }

    public EventHandler<ActionEvent> copyHandler() {
        return actionEvent -> {
            model.copy();
        };
    }

    public EventHandler<ActionEvent> pasteHandler() {
        return actionEvent -> {
            model.paste();
        };
    }



    public void alertLoadNew(String loadNew) {
        String phrase = loadNew.equals("load") ? "loading a new file": "opening a new file";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Work not Saved");
        alert.setHeaderText("You're " + phrase + " without saving, are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if (loadNew.equals("load")) {
                load();
            } else if (loadNew.equals("new")) {
                model.clearCanvas();
            }
        } else {
            save();
        }
    }

    public void alertClose() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Work not Saved");
        alert.setHeaderText("You're closing without saving, are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
           Platform.exit();
        } else {
            System.out.println("Smart...");
            save();
        }
    }


    public void load() {
        TextInputDialog dialog = new TextInputDialog("unnamed");
        dialog.setTitle("Load File");
        dialog.setHeaderText("Load file");
        dialog.setContentText("Please enter your file name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            File f = new File(result.get());
            if (!f.exists()) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("File with that name doesn't exist!");
                errorAlert.showAndWait();
            }
            FileHandler fileHandler = new FileHandler();
            ArrayList<AppShapes> shapes = fileHandler.readObjects(result.get());
            model.loadCanvas(shapes);
        }
    }

    public void save() {
        TextInputDialog dialog = new TextInputDialog("unnamed");
        dialog.setTitle("Save File");
        dialog.setHeaderText("Save file");
        dialog.setContentText("Please enter your file name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            File f = new File(result.get());
            if (f.exists()) {
                Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                errorAlert.setTitle("WARNING");
                errorAlert.setHeaderText("File with that name already exist! Overwritting...");
                errorAlert.showAndWait();
            }
            FileHandler fileHandler = new FileHandler();
            fileHandler.writeObjectsToFile(result.get(), model.shapes);
            model.hasChanged = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
