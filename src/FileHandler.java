
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandler {
   private final String curPath = Paths.get("").toAbsolutePath().toString();

   public void writeObjectsToFile(String filename, ArrayList<AppShapes> objects) {
       String filePath = curPath + "/" + filename;

       try (PrintWriter fout = new PrintWriter(filePath)) {
           objects.forEach(obj -> fout.println(formatString(obj)));
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   public ArrayList<AppShapes> readObjects(String filename) {
       try (Stream<String> stream = Files.lines(Paths.get(filename))) {
           return (ArrayList<AppShapes>) stream.map(line ->
                   AppShapes.parseString(line.split(",")))
                   .collect(Collectors.toList());

       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }


   public String formatString(AppShapes object) {
       switch (object.shapeType) {
           case Rectangle:
               Rectangle rect = (Rectangle) object.shape;
               return String.format("%s,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%s",
                       object.shapeType, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(),
                       object.fillColor.r,object.fillColor.g,object.fillColor.b,
                       object.lineColor.r,object.lineColor.g,object.lineColor.b,
                       object.lineThickness, object.lineType);
           case Circle:
               Circle circle = (Circle) object.shape;
               return String.format("%s,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%s",
                       object.shapeType, circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius(),
                       object.fillColor.r,object.fillColor.g,object.fillColor.b,
                       object.lineColor.r,object.lineColor.g,object.lineColor.b,
                       object.lineThickness, object.lineType);
           default:
               Line line = (Line) object.shape;
               return String.format("%s,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%s",
                       object.shapeType, line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(),
                       object.fillColor.r,object.fillColor.g,object.fillColor.b,
                       object.lineColor.r,object.lineColor.g,object.lineColor.b,
                       object.lineThickness, object.lineType);
       }
   }
}
