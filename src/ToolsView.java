import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ToolsView extends Pane implements IView {

    Model model;

    // Buttons
    ToggleGroup btnGroup;
    ToggleButton selectionBtn;
    ToggleButton eraserBtn;
    ToggleButton lineBtn;
    ToggleButton circleBtn;
    ToggleButton rectangleBtn;
    ToggleButton fillBtn;

    // Color Pickers
    ColorPicker lineColorPicker;
    ColorPicker fillColorPicker;

    // Slider
    VBox lineBox;
    Slider lineThickness;

    // Line type
    Pane fullLinePane;
    Pane halfLinePane;
    Pane qtrLinePane;

    Line fullLine;
    Line halfLine;
    Line qtrLine;


    public ToolsView(Model model) {
        this.model = model;

        this.setMinSize(160, 600);
        this.registerLayout();

        model.addView(this);
    }

    public void registerLayout() {

        // Container for the tools pallete
        GridPane tools = new GridPane();

        btnGroup = new ToggleGroup();
        selectionBtn = new ToggleButton(Tool.Selection.name());
        selectionBtn.setTextAlignment(TextAlignment.CENTER);
        selectionBtn.setMinSize(40, 40);
        selectionBtn.setPrefSize(Constants.PREF_BTN_WIDTH, Constants.PREF_BTN_HEIGHT);
        selectionBtn.setFont(new Font(10));
        selectionBtn.setOnMouseClicked(btnOnclickHandler(Tool.Selection));

        eraserBtn = new ToggleButton(Tool.Eraser.name());
        eraserBtn.setTextAlignment(TextAlignment.CENTER);
        eraserBtn.setMinSize(40, 40);
        eraserBtn.setPrefSize(Constants.PREF_BTN_WIDTH, Constants.PREF_BTN_HEIGHT);
        eraserBtn.setFont(new Font(10));
        eraserBtn.setOnMouseClicked(btnOnclickHandler(Tool.Eraser));


        lineBtn = new ToggleButton(Tool.Line.name());
        lineBtn.setTextAlignment(TextAlignment.CENTER);
        lineBtn.setMinSize(40, 40);
        lineBtn.setPrefSize(Constants.PREF_BTN_WIDTH, Constants.PREF_BTN_HEIGHT);
        lineBtn.setFont(new Font(10));
        lineBtn.setOnMouseClicked(btnOnclickHandler(Tool.Line));


        circleBtn = new ToggleButton(Tool.Circle.name());
        circleBtn.setTextAlignment(TextAlignment.CENTER);
        circleBtn.setMinSize(40, 40);
        circleBtn.setPrefSize(Constants.PREF_BTN_WIDTH, Constants.PREF_BTN_HEIGHT);
        circleBtn.setFont(new Font(10));
        circleBtn.setOnMouseClicked(btnOnclickHandler(Tool.Circle));


        rectangleBtn = new ToggleButton(Tool.Rectangle.name());
        rectangleBtn.setTextAlignment(TextAlignment.CENTER);
        rectangleBtn.setMinSize(40, 40);
        rectangleBtn.setPrefSize(Constants.PREF_BTN_WIDTH, Constants.PREF_BTN_HEIGHT);
        rectangleBtn.setFont(new Font(10));
        rectangleBtn.setOnMouseClicked(btnOnclickHandler(Tool.Rectangle));


        fillBtn = new ToggleButton(Tool.Fill.name());
        fillBtn.setTextAlignment(TextAlignment.CENTER);
        fillBtn.setMinSize(40, 40);
        fillBtn.setPrefSize(Constants.PREF_BTN_WIDTH, Constants.PREF_BTN_HEIGHT);
        fillBtn.setFont(new Font(10));
        fillBtn.setOnMouseClicked(btnOnclickHandler(Tool.Fill));

        selectionBtn.setToggleGroup(btnGroup);
        eraserBtn.setToggleGroup(btnGroup);
        lineBtn.setToggleGroup(btnGroup);
        circleBtn.setToggleGroup(btnGroup);
        rectangleBtn.setToggleGroup(btnGroup);
        fillBtn.setToggleGroup(btnGroup);
        selectionBtn.setSelected(true);

        tools.addColumn(0, selectionBtn,lineBtn,rectangleBtn);
        tools.addColumn(1, eraserBtn, circleBtn, fillBtn);

        // Color Pickers
        GridPane colorPickers = new GridPane();
        colorPickers.setLayoutX(10);
        colorPickers.setLayoutY(Constants.PREF_BTN_HEIGHT * 3 + 20);
        colorPickers.setHgap(15);
        colorPickers.setVgap(5);

        Text lineColorText = new Text("Line Color");
        lineColorText.resize(20, Constants.PREF_BTN_HEIGHT);
        lineColorPicker = new ColorPicker();
        lineColorPicker.setValue(Color.BLACK);
        lineColorPicker.setMaxWidth(45);
        lineColorPicker.setOnAction(colorLinePickerHandler());

        Text fillColorText = new Text("Fill Color");
        fillColorText.setTextAlignment(TextAlignment.RIGHT);
        fillColorPicker = new ColorPicker();
        fillColorPicker.setValue(Color.WHITE);
        fillColorPicker.setMaxWidth(45);
        fillColorPicker.setOnAction(colorFillPickerHandler());

        colorPickers.addColumn(0, lineColorText, lineColorPicker);
        colorPickers.addColumn(1, fillColorText, fillColorPicker);
        colorPickers.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        // Line thickness/style
        lineBox = new VBox();
        fullLinePane = new Pane();
        halfLinePane = new Pane();
        qtrLinePane = new Pane();

        fullLine = new Line(5, 30, 145, 30);
        fullLinePane.getChildren().add(fullLine);
        fullLinePane.setMinSize(80, 60);
        fullLinePane.setPrefSize(150, 60);
        fullLinePane.setOnMouseClicked(linePickerHandler(LineType.Full));
        fullLinePane.setBackground(Constants.HIGHLIGHT);
        fullLinePane.setBorder(Constants.BHIGHLIGHT);

        halfLine = new Line(5, 30, 145, 30);
        halfLine.getStrokeDashArray().addAll(Constants.HALF_ARRAY);
        halfLinePane.getChildren().add(halfLine);
        halfLinePane.setMinSize(80, 60);
        halfLinePane.setPrefSize(150, 60);
        halfLinePane.setOnMouseClicked(linePickerHandler(LineType.Half));

        qtrLine = new Line(5, 30, 145, 30);
        qtrLine.getStrokeDashArray().addAll(Constants.QUARTER_ARRAY);
        qtrLinePane.getChildren().add(qtrLine);
        qtrLinePane.setMinSize(80, 60);
        qtrLinePane.setPrefSize(150, 60);
        qtrLinePane.setOnMouseClicked(linePickerHandler(LineType.Quarter));

        lineThickness = new Slider();
        lineThickness.setMin(1);
        lineThickness.setMax(10);
        lineThickness.setValue(3);
        lineThickness.setBlockIncrement(1);
        lineThickness.setShowTickLabels(true);
        lineThickness.setShowTickMarks(true);
        lineThickness.setOnMouseDragged(lineThicknessHandler());

        lineBox.getChildren().addAll(fullLinePane, halfLinePane, qtrLinePane, lineThickness);
        lineBox.setLayoutX(5);
        lineBox.setLayoutY(270);

        this.getChildren().addAll(tools, colorPickers, lineBox);
    }


    // Control
    public EventHandler<MouseEvent> btnOnclickHandler(Tool tool) {
        return e -> model.setTool(tool);
    }

    public EventHandler<ActionEvent> colorLinePickerHandler() {
        return t -> model.setCurLineColor(lineColorPicker.getValue());
    }

    public EventHandler<ActionEvent> colorFillPickerHandler() {
        return t -> model.setCurFillColor(fillColorPicker.getValue());
    }

    public EventHandler<MouseEvent> linePickerHandler(LineType lineType) {

        return e -> {
            model.setCurLineType(lineType);
            highlight(lineType);
        };
    }

    public EventHandler<MouseEvent> lineThicknessHandler() {
        return e -> {
            model.setCurLineThickness(lineThickness.getValue());
        };
    }

    public void highlight(LineType lineType) {
        switch (lineType) {
            case Half:
                halfLinePane.setBackground(Constants.HIGHLIGHT);
                halfLinePane.setBorder(Constants.BHIGHLIGHT);

                qtrLinePane.setBorder(null);
                qtrLinePane.setBackground(null);
                fullLinePane.setBorder(null);
                fullLinePane.setBackground(null);
                break;
            case Quarter:
                qtrLinePane.setBackground(Constants.HIGHLIGHT);
                qtrLinePane.setBorder(Constants.BHIGHLIGHT);

                halfLinePane.setBorder(null);
                halfLinePane.setBackground(null);
                fullLinePane.setBorder(null);
                fullLinePane.setBackground(null);
                break;
            default:
                fullLinePane.setBackground(Constants.HIGHLIGHT);
                fullLinePane.setBorder(Constants.BHIGHLIGHT);

                qtrLinePane.setBorder(null);
                qtrLinePane.setBackground(null);
                halfLinePane.setBorder(null);
                halfLinePane.setBackground(null);
                break;
        }
    }

    @Override
    public void update() {
        lineColorPicker.setValue(model.curLineColor);
        fillColorPicker.setValue(model.curFillColor);
        lineThickness.setValue(model.curLineThickness);
        updateToggle();
        highlight(model.curLineType);
        updateLines();
    }


    public void updateLines() {
        fullLine.setStrokeWidth(model.curLineThickness);
        fullLine.setStroke(model.curLineColor);
        halfLine.setStrokeWidth(model.curLineThickness);
        halfLine.setStroke(model.curLineColor);
        qtrLine.setStrokeWidth(model.curLineThickness);
        qtrLine.setStroke(model.curLineColor);
    }

    public void updateToggle() {
        switch (model.curTool) {
            case Selection:
                btnGroup.selectToggle(selectionBtn);
                selectionBtn.setSelected(true);
                break;
            case Rectangle:
                btnGroup.selectToggle(rectangleBtn);
                rectangleBtn.setSelected(true);
                break;
            case Circle:
                btnGroup.selectToggle(circleBtn);
                circleBtn.setSelected(true);
                break;
            case Line:
                btnGroup.selectToggle(lineBtn);
                lineBtn.setSelected(true);
                break;
            case Eraser:
                btnGroup.selectToggle(eraserBtn);
                eraserBtn.setSelected(true);
                break;
            case Fill:
                btnGroup.selectToggle(fillBtn);
                fillBtn.setSelected(true);
                break;

        }
    }

}
