import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.Serializable;

public class AppShapes implements Serializable {

    Shape shape;
    Tool shapeType;
    MyColor lineColor;
    MyColor fillColor;
    double lineThickness;
    LineType lineType;

    public AppShapes(Shape shape, Tool shapeType, MyColor lineColor, MyColor fillColor, double lineThickness, LineType lineType) {
        this.shape = shape;
        this.shapeType = shapeType;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        this.lineThickness = lineThickness;
        this.lineType = lineType;
    }

    public AppShapes(AppShapes other) {
        this.shape = other.shape;
        this.shapeType = other.shapeType;
        this.lineColor = other.lineColor;
        this.fillColor = other.fillColor;
        this.lineThickness = other.lineThickness;
        this.lineType = other.lineType;
    }

    public static AppShapes parseString(String[] shapeInfo) {
        Tool type = Tool.valueOf(shapeInfo[0]);

        switch (type) {
            case Rectangle:
                Rectangle tmp = new Rectangle(Double.parseDouble(shapeInfo[1]), Double.parseDouble(shapeInfo[2]),
                        Double.parseDouble(shapeInfo[3]), Double.parseDouble(shapeInfo[4]));
                        tmp.setFill(Color.color(Double.parseDouble(shapeInfo[5]), Double.parseDouble(shapeInfo[6]), Double.parseDouble(shapeInfo[7])));
                        tmp.setStroke(Color.color(Double.parseDouble(shapeInfo[8]), Double.parseDouble(shapeInfo[9]), Double.parseDouble(shapeInfo[10])));
                        tmp.setStrokeWidth(Double.parseDouble(shapeInfo[11]));
                        tmp.getStrokeDashArray().addAll(getLineTypeArray(LineType.valueOf(shapeInfo[12])));
                        return new AppShapes(tmp, type,
                                new MyColor(Double.parseDouble(shapeInfo[8]), Double.parseDouble(shapeInfo[9]), Double.parseDouble(shapeInfo[10])),
                                new MyColor(Double.parseDouble(shapeInfo[5]), Double.parseDouble(shapeInfo[6]), Double.parseDouble(shapeInfo[7])),
                                Double.parseDouble(shapeInfo[11]),
                                LineType.valueOf(shapeInfo[12]));
            case Circle:
                Circle circle = new Circle(Double.parseDouble(shapeInfo[1]), Double.parseDouble(shapeInfo[2]), Double.parseDouble(shapeInfo[3]));
                circle.setFill(Color.color(Double.parseDouble(shapeInfo[5]), Double.parseDouble(shapeInfo[6]), Double.parseDouble(shapeInfo[7])));
                circle.setStroke(Color.color(Double.parseDouble(shapeInfo[8]), Double.parseDouble(shapeInfo[9]), Double.parseDouble(shapeInfo[10])));
                circle.setStrokeWidth(Double.parseDouble(shapeInfo[11]));
                circle.getStrokeDashArray().addAll(getLineTypeArray(LineType.valueOf(shapeInfo[12])));
                return new AppShapes(circle, type,
                        new MyColor(Double.parseDouble(shapeInfo[8]), Double.parseDouble(shapeInfo[9]), Double.parseDouble(shapeInfo[10])),
                        new MyColor(Double.parseDouble(shapeInfo[5]), Double.parseDouble(shapeInfo[6]), Double.parseDouble(shapeInfo[7])),
                        Double.parseDouble(shapeInfo[11]),
                        LineType.valueOf(shapeInfo[12]));
            default:
                Line line = new Line(Double.parseDouble(shapeInfo[1]), Double.parseDouble(shapeInfo[2]),
                        Double.parseDouble(shapeInfo[3]), Double.parseDouble(shapeInfo[4]));
                line.setFill(Color.color(Double.parseDouble(shapeInfo[5]), Double.parseDouble(shapeInfo[6]), Double.parseDouble(shapeInfo[7])));
                line.setStroke(Color.color(Double.parseDouble(shapeInfo[8]), Double.parseDouble(shapeInfo[9]), Double.parseDouble(shapeInfo[10])));
                line.setStrokeWidth(Double.parseDouble(shapeInfo[11]));
                line.getStrokeDashArray().addAll(getLineTypeArray(LineType.valueOf(shapeInfo[12])));
                return new AppShapes(line, type,
                        new MyColor(Double.parseDouble(shapeInfo[8]), Double.parseDouble(shapeInfo[9]), Double.parseDouble(shapeInfo[10])),
                        new MyColor(Double.parseDouble(shapeInfo[5]), Double.parseDouble(shapeInfo[6]), Double.parseDouble(shapeInfo[7])),
                        Double.parseDouble(shapeInfo[11]),
                        LineType.valueOf(shapeInfo[12]));
        }
    }

    public static AppShapes deepCopy(AppShapes shape) {
        Shape newShape = null;
        switch (shape.shapeType) {
            case Rectangle:
                newShape = new Rectangle(((Rectangle) shape.shape).getX(),
                        ((Rectangle) shape.shape).getY(),
                        ((Rectangle) shape.shape).getWidth(),
                        ((Rectangle) shape.shape).getHeight());
                newShape.setStroke(
                        Color.color(shape.lineColor.r, shape.lineColor.g, shape.lineColor.b));
                newShape.setFill(
                        Color.color(shape.fillColor.r, shape.fillColor.g, shape.fillColor.b));
                newShape.getStrokeDashArray().addAll(getLineTypeArray(shape.lineType));
                newShape.setStrokeWidth(shape.lineThickness);
                break;
            case Circle:
                newShape = new Circle(((Circle) shape.shape).getCenterX(),
                        ((Circle) shape.shape).getCenterY(),
                        ((Circle) shape.shape).getRadius());
                newShape.setStroke(
                        Color.color(shape.lineColor.r, shape.lineColor.g, shape.lineColor.b));
                newShape.setFill(
                        Color.color(shape.fillColor.r, shape.fillColor.g, shape.fillColor.b));
                newShape.getStrokeDashArray().addAll(getLineTypeArray(shape.lineType));
                newShape.setStrokeWidth(shape.lineThickness);
                break;
            case Line:
                newShape = new Line(((Line) shape.shape).getStartX(),
                        ((Line) shape.shape).getStartY(),
                        ((Line) shape.shape).getEndX(),
                        ((Line) shape.shape).getEndY());
                newShape.setStroke(
                        Color.color(shape.lineColor.r, shape.lineColor.g, shape.lineColor.b));
                newShape.getStrokeDashArray().addAll(getLineTypeArray(shape.lineType));
                newShape.setStrokeWidth(shape.lineThickness);
                break;
        }
        return new AppShapes(newShape, shape.shapeType, shape.lineColor, shape.fillColor, shape.lineThickness, shape.lineType);
    }

    public static Double[] getLineTypeArray(LineType lineType) {
        switch(lineType) {
            case Half:
                return Constants.HALF_ARRAY;
            case Quarter:
                return Constants.QUARTER_ARRAY;
            default:
                return Constants.FULL_ARRAY;
        }
    }


}
