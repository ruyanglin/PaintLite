import javafx.scene.control.Toggle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Model {
    // all views of this model
    private ArrayList<IView> views = new ArrayList<>();
    public ArrayList<AppShapes> shapes = new ArrayList<>();

    AppShapes curDrawingShape;
    AppShapes curSelectedShape;
    AppShapes ccShape;
    Tool curTool = Tool.Selection;
    LineType curLineType = LineType.Full;
    Color curLineColor = Color.BLACK;
    Color curFillColor = Color.WHITE;
    double curLineThickness = 3;
    double x;
    double y;
    boolean hasChanged = false;

    public Model() { }


    public void addDrawingShape(double initX, double initY, double curX, double curY) {
        hasChanged = true;
        double optX = Math.min(initX, curX);
        double optY = Math.min(initY, curY);
        double width = Math.abs(curX - initX);
        double height = Math.abs(curY - initY);

        switch(curTool) {
            case Rectangle:
                curDrawingShape = new AppShapes(
                        new Rectangle(optX, optY, width, height),
                        Tool.Rectangle,
                        new MyColor(curLineColor.getRed(), curLineColor.getGreen(), curLineColor.getBlue()),
                        new MyColor(curFillColor.getRed(), curFillColor.getGreen(), curFillColor.getBlue()),
                        curLineThickness,
                        curLineType);
                break;
            case Circle:
                curDrawingShape = new AppShapes(
                        new Circle(optX, optY, width),
                        Tool.Circle,
                        new MyColor(curLineColor.getRed(), curLineColor.getGreen(), curLineColor.getBlue()),
                        new MyColor(curFillColor.getRed(), curFillColor.getGreen(), curFillColor.getBlue()),
                        curLineThickness,
                        curLineType);
                break;
            case Line:
                curDrawingShape = new AppShapes(
                        new Line(optX, optY, width, height),
                        Tool.Line,
                        new MyColor(curLineColor.getRed(), curLineColor.getGreen(), curLineColor.getBlue()),
                        new MyColor(curFillColor.getRed(), curFillColor.getGreen(), curFillColor.getBlue()),
                        curLineThickness,
                        curLineType);
                break;
        }

        notifyObservers();
    }

    public void selectShape(double x, double y) {
        boolean isOnShape = false;
        for (int i = shapes.size() - 1; i > -1; i--) {
            // Second or statement is for line selection
            if (shapes.get(i).shape.contains(x, y) ||
                    (shapes.get(i).shape.intersects(x-2, y-2, 4, 4) && shapes.get(i).shapeType == Tool.Line)) {
                unselectShape();
                setCurSelectedShape(shapes.get(i));
                setCurFillColor(Color.color(curSelectedShape.fillColor.r, curSelectedShape.fillColor.g, curSelectedShape.fillColor.b));
                setCurLineColor(Color.color(curSelectedShape.lineColor.r, curSelectedShape.lineColor.g, curSelectedShape.lineColor.b));
                setCurLineType(curSelectedShape.lineType);
                setCurLineThickness(curSelectedShape.lineThickness);
                isOnShape = true;
                hasChanged = true;
                break;
            }
        }

        if(!isOnShape) {
            this.x = x;
            this.y = y;
            unselectShape();
        }

        notifyObservers();
    }

    public void unselectShape() {
        if (curSelectedShape != null) {
            this.curSelectedShape.shape.setOpacity(1);
            this.curSelectedShape.shape.setStrokeWidth(this.curSelectedShape.lineThickness);
            this.curSelectedShape.shape.setStroke(
                    Color.color(
                            this.curSelectedShape.lineColor.r,
                            this.curSelectedShape.lineColor.g,
                            this.curSelectedShape.lineColor.b));
        }

        curSelectedShape = null;
        notifyObservers();
    }

    public void moveShape(double x, double y) {
        if (curSelectedShape == null) {
            return;
        }
        hasChanged = true;
        double deltaX;
        double deltaY;
        switch (curSelectedShape.shapeType) {
            case Rectangle:
                deltaX = ((Rectangle) curSelectedShape.shape).getWidth()/2;
                deltaY = ((Rectangle) curSelectedShape.shape).getHeight()/2;
                ((Rectangle) curSelectedShape.shape).setX(x - deltaX);
                ((Rectangle) curSelectedShape.shape).setY(y - deltaY);
                break;
            case Circle:
                ((Circle) curSelectedShape.shape).setCenterX(x);
                ((Circle) curSelectedShape.shape).setCenterY(y);
                break;
            case Line:
                double midpointX = ((((Line) curSelectedShape.shape).getEndX() - ((Line) curSelectedShape.shape).getStartX()))/2;
                double midpointY = ((((Line) curSelectedShape.shape).getEndY() - ((Line) curSelectedShape.shape).getStartY()))/2;
                deltaX = x - ((Line) curSelectedShape.shape).getStartX();
                deltaY = y - ((Line) curSelectedShape.shape).getStartY();
                ((Line) curSelectedShape.shape).setStartX(x - midpointX);
                ((Line) curSelectedShape.shape).setStartY(y - midpointY);
                ((Line) curSelectedShape.shape).setEndX(((Line) curSelectedShape.shape).getEndX() + deltaX - midpointX);
                ((Line) curSelectedShape.shape).setEndY(((Line) curSelectedShape.shape).getEndY() + deltaY - midpointY);
                break;
        }

        notifyObservers();
    }

    public void eraseShape(double x, double y) {
        for (int i = shapes.size() - 1; i > -1; i--) {
            // Second or statement is for line selection
            if (shapes.get(i).shape.contains(x, y) ||
                    (shapes.get(i).shape.intersects(x-2, y-2, 4, 4) && shapes.get(i).shapeType == Tool.Line)) {
                shapes.remove(i);
                hasChanged = true;
                break;
            }
        }
        notifyObservers();
    }

    // For keyevent
    public int eraseShape() {
        if (curSelectedShape == null) {
            return -1;
        }
        hasChanged = true;
        int index = shapes.indexOf(curSelectedShape);
        shapes.remove(index);
        notifyObservers();
        return index;
    }

    public void fillShape(double x, double y) {
        for (int i = shapes.size() - 1; i > -1; i--) {
            if (shapes.get(i).shape.contains(x, y)) {
                shapes.get(i).fillColor = new MyColor(curFillColor.getRed(), curFillColor.getGreen(), curFillColor.getBlue());
                shapes.get(i).shape.setFill(curFillColor);
                hasChanged = true;
                break;
            }
        }
        notifyObservers();
    }

    public void addDrawnShape() {
        hasChanged = true;
        shapes.add(curDrawingShape);
        notifyObservers();
    }

    public void cut() {
        if (curSelectedShape == null) {
            return;
        }
        ccShape = AppShapes.deepCopy(curSelectedShape);
        this.eraseShape();
        hasChanged = true;
        notifyObservers();
    }

    public void copy() {
        if (curSelectedShape == null) {
            return;
        }
        ccShape = AppShapes.deepCopy(curSelectedShape);
        hasChanged = true;
        notifyObservers();
    }

    public void paste() {
        if (ccShape == null) {
            return;
        }

        hasChanged = true;
        shapes.add(ccShape);
        switch (ccShape.shapeType) {
            case Rectangle:
                ((Rectangle) ccShape.shape).setX(this.x);
                ((Rectangle) ccShape.shape).setY(this.y);
                break;
            case Circle:
                ((Circle) ccShape.shape).setCenterX(this.x);
                ((Circle) ccShape.shape).setCenterY(this.y);
                break;
            case Line:
                double midpointX = ((((Line) ccShape.shape).getEndX() - ((Line) ccShape.shape).getStartX()))/2;
                double midpointY = ((((Line) ccShape.shape).getEndY() - ((Line) ccShape.shape).getStartY()))/2;
                double deltaX = x - ((Line) ccShape.shape).getStartX();
                double deltaY = y - ((Line) ccShape.shape).getStartY();
                ((Line) ccShape.shape).setStartX(x - midpointX);
                ((Line) ccShape.shape).setStartY(y - midpointY);
                ((Line) ccShape.shape).setEndX(((Line) ccShape.shape).getEndX() + deltaX - midpointX);
                ((Line) ccShape.shape).setEndY(((Line) ccShape.shape).getEndY() + deltaY - midpointY);
                break;
        }
        ccShape = null;
        notifyObservers();
    }


    public void setTool(Tool tool) {
        this.unselectShape();
        this.curTool = tool;
        notifyObservers();
    }

    public void setCurLineType(LineType lineType) {
        this.curLineType = lineType;
        if (this.curSelectedShape != null) {
            this.curSelectedShape.shape.getStrokeDashArray().removeAll(getLineTypeArray(this.curSelectedShape.lineType));
            this.curSelectedShape.lineType = lineType;
            this.curSelectedShape.shape.getStrokeDashArray().addAll(getLineTypeArray(lineType));
        }
        notifyObservers();
    }

    public Double[] getLineTypeArray(LineType lineType) {
        switch(lineType) {
            case Half:
                return Constants.HALF_ARRAY;
            case Quarter:
                return Constants.QUARTER_ARRAY;
            default:
                return Constants.FULL_ARRAY;
        }
    }

    public void setCurLineThickness(double lineThickness) {
        this.curLineThickness = lineThickness;
        if (curSelectedShape != null) {
            this.curSelectedShape.lineThickness = lineThickness;
            this.curSelectedShape.shape.setStrokeWidth(lineThickness);
        }
        notifyObservers();
    }

    public void setCurSelectedShape(AppShapes shape) {
        this.curSelectedShape = shape;
        this.curSelectedShape.shape.setOpacity(0.5);
        this.curSelectedShape.shape.setStroke(Color.GREEN);
        notifyObservers();
    }

    public void setCurLineColor(Color color) {
        this.curLineColor = color;
        if (this.curSelectedShape != null) {
            this.curSelectedShape.lineColor = new MyColor(color.getRed(), color.getGreen(), color.getBlue());
            this.curSelectedShape.shape.setStroke(color);
        }
        notifyObservers();
    }

    public void setCurFillColor(Color color) {
        this.curFillColor = color;
        if (this.curSelectedShape != null) {
            this.curSelectedShape.fillColor = new MyColor(color.getRed(), color.getGreen(), color.getBlue());
            this.curSelectedShape.shape.setFill(color);
        }
        notifyObservers();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    public void clearCanvas() {
        shapes = new ArrayList<>();
        curTool = Tool.Selection;
        curLineType = LineType.Full;
        curLineColor = Color.BLACK;
        curFillColor = Color.WHITE;
        curLineThickness = 3;
        hasChanged = false;
        curSelectedShape = null;
        curDrawingShape = null;
        setCurLineThickness(curLineThickness);
        setCurLineType(curLineType);
        setTool(curTool);
        setCurFillColor(curFillColor);
        setCurLineColor(curLineColor);
        notifyObservers();
    }

    public void loadCanvas(ArrayList<AppShapes> shapes) {
        clearCanvas();
        this.shapes = shapes;
        notifyObservers();
    }

    public void addView(IView view) {
        views.add(view);
        view.update();
    }

    private void notifyObservers() {
        for (IView view : this.views) {
            view.update();
        }
    }


}
