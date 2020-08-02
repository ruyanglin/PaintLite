import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrawingCanvasView extends Pane implements IView {

    Model model;
    Pane drawingPane;
    AppShapes preview;

    public DrawingCanvasView(Model model) {
        this.model = model;
        this.registerLayout();

        model.addView(this);
    }


    public void registerLayout() {
        drawingPane = new Pane();
        drawingPane.setPrefSize(Constants.INIT_SCREEN_WIDTH-Constants.PREF_BTN_WIDTH * 2, Constants.INIT_SCREEN_HEIGHT);
        drawingPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        drawingPane.setOnMousePressed(mousePressedEventHandler());
        drawingPane.setOnMouseDragged(mouseDraggedEventHandler());
        drawingPane.setOnMouseReleased(mouseReleasedEventHandler());

        this.getChildren().addAll(drawingPane);
    }

    public EventHandler<MouseEvent> mousePressedEventHandler() {
        return e -> {
            model.setX(e.getX());
            model.setY(e.getY());
            model.hasChanged = true;
            switch(model.curTool) {
                case Rectangle:
                case Circle:
                case Line:
                    model.unselectShape();
                    model.addDrawingShape(0, 0, 0, 0);
                    preview = new AppShapes(model.curDrawingShape);
                    preview.shape.setOpacity(0.5);
                    preview.shape.setFill(model.curFillColor);
                    preview.shape.setStroke(model.curLineColor);
                    preview.shape.setStrokeWidth(model.curLineThickness);
                    preview.shape.getStrokeDashArray().addAll(model.getLineTypeArray(model.curLineType));
                    drawingPane.getChildren().add(preview.shape);
                    break;
                case Selection:
                    model.selectShape(e.getX(), e.getY());
                    break;
                case Eraser:
                    model.eraseShape(e.getX(), e.getY());
                    break;
                case Fill:
                    model.unselectShape();
                    model.fillShape(e.getX(), e.getY());
                    break;
            }
        };
    }

    public EventHandler<MouseEvent> mouseDraggedEventHandler() {
        return e -> {
            double initialX = model.x;
            double initialY = model.y;

            double curX = e.getX();
            double curY = e.getY();

            double actX = Math.min(initialX, curX);
            double actY = Math.min(initialY, curY);

            double width = Math.abs(curX - initialX);
            double height = Math.abs(curY - initialY);

            model.hasChanged = true;
            switch(model.curTool) {
                case Rectangle:
                    ((Rectangle) preview.shape).setX(actX);
                    ((Rectangle) preview.shape).setY(actY);
                    ((Rectangle) preview.shape).setWidth(width);
                    ((Rectangle) preview.shape).setHeight(height);
                    break;
                case Circle:
                    ((Circle) preview.shape).setCenterX(actX);
                    ((Circle) preview.shape).setCenterY(actY);
                    ((Circle) preview.shape).setRadius(Math.max(width,height));
                    break;
                case Line:
                    ((Line) preview.shape).setStartX(initialX);
                    ((Line) preview.shape).setStartY(initialY);
                    ((Line) preview.shape).setEndX(curX);
                    ((Line) preview.shape).setEndY(curY);
                    break;
                case Selection:
                    model.moveShape(curX, curY);
                default:
                    break;
            }

        };
    }

    public EventHandler<MouseEvent> mouseReleasedEventHandler() {
        return e -> {
            model.hasChanged = true;
            switch(model.curTool) {
                case Rectangle:
                case Circle:
                case Line:
                    model.curDrawingShape.shape.setOpacity(1);
                    model.addDrawnShape();
                    break;
                default:
            }
        };
    }

    @Override
    public void update() {
        if (model.shapes.size() == 0) {
           drawingPane.getChildren().clear();
        }
        model.shapes.forEach(shape -> {
            if (drawingPane.getChildren().indexOf(shape.shape) == -1) {
                drawingPane.getChildren().add(shape.shape);
            }
        });

        List<Shape> modelShapes = model.shapes.stream().map(node -> node.shape).collect(Collectors.toList());
        List<Node> toBeDelete = new ArrayList<>();
        drawingPane.getChildren().forEach(node ->
        {
            if (!modelShapes.contains(node)) {
                toBeDelete.add(node);
            }
        });

        for (Node node: toBeDelete) {
            drawingPane.getChildren().remove(node);
        }
    }

}
