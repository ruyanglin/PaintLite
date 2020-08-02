import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public final class Constants {
    private Constants(){}

    public static final double PREF_BTN_WIDTH = 80;
    public static final double PREF_BTN_HEIGHT = 60;

    public static final double INIT_SCREEN_WIDTH = 1200;
    public static final double INIT_SCREEN_HEIGHT = 700;

    public static final Double[] FULL_ARRAY = {};
    public static final Double[] HALF_ARRAY = {25d, 25d};
    public static final Double[] QUARTER_ARRAY = {2d, 12d};

    public static Background HIGHLIGHT = new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, CornerRadii.EMPTY, Insets.EMPTY));
    public static Border BHIGHLIGHT = new Border(
            new BorderStroke(Color.GREENYELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

}
