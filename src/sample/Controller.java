package sample;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.util.Optional;
import java.util.List;
import java.io.File;

public class Controller {

    @FXML private Rectangle rectangle;
    @FXML private TextArea assemblyCodeArea, lineNum;
    @FXML private Button btnRun, btnStep, btnChoose, btnReset;
    @FXML private TableView<Register> rTable;
    @FXML private TableColumn<Register, String > rName;
    @FXML private TableColumn<Register, Integer> rNo, rValue;
    @FXML private TableView<Data> textSegTable, sTable;
    @FXML private TableColumn<Data, String > textSegAddress, textSegValue, sAddress, sValue;

    private final short TOOLTIP_DELAY = 10;
    private Parser parser;
    private Processor processor;


    @FXML
    private void chooseRect(){ assemblyCodeArea.requestFocus(); }

    @FXML
    private void handleRect(){
        if (btnStep.isDisabled()) {
            int position = assemblyCodeArea.getCaretPosition();
            String text = assemblyCodeArea.getText();
            int count = 0;
            for (int i = 0; i < position; i++) {
                if (i < text.length() && text.charAt(i) == '\n')
                    count++;
            }
            setRectY(count);
        }
    }

    private void setRectY(int count){
        int scrollValue = (int)assemblyCodeArea.getScrollTop() ;
        if(count < 27)
            rectangle.setTranslateY(count * 19 - scrollValue);
        else
            rectangle.setTranslateY((count-1) * 19 - scrollValue);

        // if(count < 27) rectangle.setTranslateY(count * 19);
        // if(count < 28) rectangle.setY(rectStartingY + count*rectangle.getHeight());
    }

    private void setLineNumber(String text){
        StringBuilder sb = new StringBuilder();
        int count = 1;
        sb.append(count);
        for(int i=0; i < text.length(); i++)
        {    if(text.charAt(i) == '\n')
                sb.append("\n").append(++count);
        }
        lineNum.setText(sb.toString());
    }

    @FXML
    public void initialize(){

        setUpAssemblyCodeAreas();
        setUpTablePlaceholders();
        setupRegisterTable();

        createButtonTooltip("Run", TOOLTIP_DELAY, btnRun);
        createButtonTooltip("Choose File", TOOLTIP_DELAY, btnChoose);
        createButtonTooltip("Reset", TOOLTIP_DELAY, btnReset);
        createButtonTooltip("Step", TOOLTIP_DELAY, btnStep);
    }

    private void createButtonTooltip(String tooltip, short delay, Button button) {
        Tooltip runTip = new Tooltip(tooltip);
        runTip.setShowDelay(Duration.millis(delay));
        Tooltip.install(button, runTip);
    }

    private void setUpAssemblyCodeAreas() {

        lineNum.scrollTopProperty().bindBidirectional(assemblyCodeArea.scrollTopProperty());
        assemblyCodeArea.setOnKeyReleased(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    //assemblyCodeArea.setScrollTop(Double.MAX_VALUE);
                    //lineNum.setScrollTop(Double.MAX_VALUE);
                }
            }
        });
        rectangle.setOnMouseClicked(mouseEvent -> chooseRect());
        assemblyCodeArea.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> handleRect());
        assemblyCodeArea.textProperty().addListener((observableValue, s, t1) -> setLineNumber(t1));
        rectangle.widthProperty().bind(assemblyCodeArea.widthProperty().
                add(lineNum.widthProperty()).
                subtract(assemblyCodeArea.getPadding().getRight()));
    }

    private void setUpTablePlaceholders() {
        sTable.setPlaceholder(new Label("Stack is empty"));
        textSegTable.setPlaceholder(new Label("No instruction in memory"));
    }

    private void setupRegisterTable() {
        rNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        rValue.setCellValueFactory(new PropertyValueFactory<>("regValue"));
        rName.setCellValueFactory(new PropertyValueFactory<>("name"));
        rTable.setItems(RegisterFile.getRegisters());
        rTable.getItems().forEach(
                register -> register.addListener((observableValue, o, t1) -> {
                    int regNo =((Register)observableValue).getNo();
                    rTable.getSelectionModel().select(regNo);
                    if (regNo > 3)
                        rTable.scrollTo(regNo - 4);
                }));
    }

    private void setupTextSegmentTable() throws Exception {
        textSegAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        textSegValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        textSegTable.setItems(InstructionMemoryFile.getInstructions());
    }

    private void setupStackTable(ObservableList<Data> data) {
        sAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        sValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        sTable.setItems(data);
        int changed = processor.getChangedMemIdx();
        if(changed != -1){
            changed = (4000 - changed)/4;
            sTable.scrollTo(changed - 1);
            sTable.getSelectionModel().select(changed - 1);
        }
    }

    @FXML
    public void runPressed(ActionEvent event) throws Exception {

        if(assemblyCodeArea.getText().equals("") && parser == null) {
            return;
        }
        setRectY(0);
        btnRun.setDisable(true);
        btnStep.setDisable(false);

        if(assemblyCodeArea.editableProperty().getValue()) {
            parser = new Parser(assemblyCodeArea.getText());
            assemblyCodeArea.setText(parser.getLines());
        }

        try{
            parser.createInstructions();
            assemblyCodeArea.setText(parser.getLines());
            List<Instruction> instructions = parser.getInstructions();
            processor = new Processor();
            processor.loadInstructionsToMemory(instructions);
            setupTextSegmentTable();
            setupStackTable(processor.getStackData());
            selectLine(0);
        } catch (Exception e){
            showAlertDialog("Error", e.getMessage(), true);
        }
    }

    private void startAgain () throws Exception {
        List<Instruction> instructions = parser.getInstructions();
        processor = new Processor();
        processor.loadInstructionsToMemory(instructions);
        rTable.refresh();
        setupTextSegmentTable();
        setupStackTable(processor.getStackData());
        selectLine(0);
    }

    @FXML
    public void onStep(ActionEvent event) throws Exception {
        try {
            if (!processor.isDone()) {
                handleRect();
                processor.step();
                rTable.refresh();
                setupStackTable(processor.getStackData());
                selectLine(processor.getIndex());
            } else {
                //setRectY(0);
                //assemblyCodeArea.selectRange(0,0);
                showAlertDialog("The program has finished!", "Do you want to run again ?", false);
            }
        } catch (Exception e){
            showAlertDialog("Problem at line " + (processor.getIndex() + 1), e.getMessage(), false);
        }
    }

    private int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    private void selectLine(int lineCount){
        textSegTable.getSelectionModel().select(lineCount);
        if(lineCount > 3)
            textSegTable.scrollTo(lineCount - 4);
        setRectY(lineCount);

        /*
        String txt = assemblyCodeArea.getText();

        int start = lineCount == 0 ? 0 : ordinalIndexOf(txt, "\n", lineCount - 1);
        int end = ordinalIndexOf(txt, "\n", lineCount );

        assemblyCodeArea.selectRange(start, end);
        */
    }

    @FXML
    public void chooseFilePressed(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./")); // set initial directory to cwd.

        // filter file formats that can be selected.
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Assembly Files", "*.s")
                ,new FileChooser.ExtensionFilter("Assembly Files", "*.asm")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile == null) return;

        resetApplication();

        assemblyCodeArea.setEditable(false);

        parser = new Parser(selectedFile);

        assemblyCodeArea.setText(parser.getLines());
    }

    @FXML
    public void resetApplication() {
        setRectY(0);
        assemblyCodeArea.setText("");
        assemblyCodeArea.setEditable(true);

        btnRun.setDisable(false);
        btnStep.setDisable(true);
        btnChoose.setDisable(false);

        parser = null;
        textSegTable.setItems(null);

        RegisterFile.resetData();
        rTable.refresh();

        try {
            MemoryFile.resetData();
        } catch (Exception e){

        }

        sTable.setItems(null);
    }

    public void showAlertDialog(String header, String content, boolean isResetApplication) throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType btnExit = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType btnOK =
                isResetApplication ?
                        new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE):
                        new ButtonType("Start Again", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(btnOK, btnExit);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == btnOK) {
            if (isResetApplication)
                resetApplication();
            else
                startAgain();
        } else
            System.exit(0);
    }
}
