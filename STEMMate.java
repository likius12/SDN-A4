import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ReportPipelineBuilder extends Application {

    // Dynamic UI controls that track live state modifications
    private ComboBox<String> archTypeBox;
    private TextArea syncFlowArea;
    private Slider unitTestSlider;
    private ToggleGroup failoverGroup;
    private TextField repoUrlField;
    private TextField teamLeadField;
    private TextArea compiledCodePreview;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("STEMMate Namibia Report Pipeline Builder");

        // --- Root Layout Splitter ---
        SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.setDividerPositions(0.25, 0.65); // Layout allocations matching sidebars

        // --- 1. LEFT SIDEBAR: Submission Checklist ---
        VBox sidebar = createSidebar();

        // --- 2. CENTER PANEL: Content Engineering Inputs ---
        VBox centerPanel = createCenterPanel();

        // --- 3. RIGHT PANEL: Backend Compiler Output Preview ---
        VBox rightPanel = createRightPanel();

        // Combine layout sections into the primary display board
        mainSplitPane.getItems().addAll(sidebar, centerPanel, rightPanel);

        // Bind layout updates across controls to generate structural code instantly
        setupListeners();
        updateCompiledOutput();

        Scene scene = new Scene(mainSplitPane, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #f4f4f9; -fx-border-color: #dcdcdc; -fx-border-width: 0 1 0 0;");

        Label title = new Label("🎯 Submission Readiness");
        title.setFont(Font.font("Arial", 16));
        
        Label subTitle = new Label("Check report gaps to meet 18-25 pages:");
        subTitle.setWrapText(true);

        CheckBox cb1 = new CheckBox("1. Length Checklist (Target: 18-25 Pages)");
        CheckBox cb2 = new CheckBox("2. Architecture Diagrams Added"); cb2.setSelected(true); cb2.setDisable(true);
        CheckBox cb3 = new CheckBox("3. Testing Results Verified"); cb3.setSelected(true); cb3.setDisable(true);
        CheckBox cb4 = new CheckBox("4. Three Redesign Changes Documented");
        CheckBox cb5 = new CheckBox("5. Team Details & Repo Filled"); cb5.setSelected(true); cb5.setDisable(true);
        CheckBox cb6 = new CheckBox("6. Handover & Operational Evidence");

        Separator sep = new Separator();
        Label infoBox = new Label("💡 Sections 2, 3, and 5 are integrated directly into the text pipeline layout.");
        infoBox.setWrapText(true);
        infoBox.setStyle("-fx-text-fill: #17a2b8; -fx-font-style: italic;");

        sidebar.getChildren().addAll(title, subTitle, new Separator(), cb1, cb2, cb3, cb4, cb5, cb6, sep, infoBox);
        return sidebar;
    }

    private VBox createCenterPanel() {
        VBox center = new VBox(20);
        center.setPadding(new Insets(20));

        Label mainTitle = new Label("🛠️ Component Content Engineering");
        mainTitle.setFont(Font.font("Arial", 20));

        // --- Section 2 Form ---
        TitledPane paneSec2 = new TitledPane();
        paneSec2.setText("📍 Section 2: Architecture Diagrams Data");
        VBox contentSec2 = new VBox(8);
        archTypeBox = new ComboBox<>();
        archTypeBox.getItems().addAll("C4 Component Model", "High-Level System Topology", "Container Layout Diagram");
        archTypeBox.setValue("C4 Component Model");
        
        syncFlowArea = new TextArea("Uses an offline-first state reconciliation engine syncing chunked payloads over HTTPS when a client ping succeeds.");
        syncFlowArea.setPrefHeight(60);
        syncFlowArea.setWrapText(true);
        contentSec2.getChildren().addAll(new Label("Core Topology Diagram Style:"), archTypeBox, new Label("Offline Sync Protocol Note:"), syncFlowArea);
        paneSec2.setContent(contentSec2);

        // --- Section 3 Form ---
        TitledPane paneSec3 = new TitledPane();
        paneSec3.setText("🧪 Section 3: Testing Results & Verification");
        VBox contentSec3 = new VBox(8);
        unitTestSlider = new Slider(0, 100, 85);
        unitTestSlider.setShowTickLabels(true);
        unitTestSlider.setShowTickMarks(true);
        
        failoverGroup = new ToggleGroup();
        RadioButton rb1 = new RadioButton("Verified - 100% Data Retention"); rb1.setToggleGroup(failoverGroup); rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("Incomplete"); rb2.setToggleGroup(failoverGroup);
        RadioButton rb3 = new RadioButton("Requires Validation"); rb3.setToggleGroup(failoverGroup);
        HBox radioBox = new HBox(10, rb1, rb2, rb3);
        
        contentSec3.getChildren().addAll(new Label("Automated Unit Test Coverage (%):"), unitTestSlider, new Label("Network Interruption Failover Simulation:"), radioBox);
        paneSec3.setContent(contentSec3);

        // --- Section 5 Form ---
        TitledPane paneSec5 = new TitledPane();
        paneSec5.setText("👥 Section 5: Team Details & Repository Links");
        VBox contentSec5 = new VBox(8);
        repoUrlField = new TextField("https://github.com");
        teamLeadField = new TextField("Facilitator Workflow Lead");
        contentSec5.getChildren().addAll(new Label("Production Repository URL:"), repoUrlField, new Label("Lead Architect / Engineer Assignment:"), teamLeadField);
        paneSec5.setContent(contentSec5);

        center.getChildren().addAll(mainTitle, paneSec2, paneSec3, paneSec5);
        return center;
    }

    private VBox createRightPanel() {
        VBox right = new VBox(15);
        right.setPadding(new Insets(20));
        right.setStyle("-fx-background-color: #fafafa;");

        Label outputTitle = new Label("⚡ Backend-Ready Array Compiler");
        outputTitle.setFont(Font.font("Arial", 16));

        compiledCodePreview = new TextArea();
        compiledCodePreview.setEditable(false);
        compiledCodePreview.setFont(Font.font("Courier New", 12));
        compiledCodePreview.setPrefHeight(450);

        Label actionsTitle = new Label("🚀 Build Document Outputs");
        Button btnDocx = new Button("Generate DOCX Report");
        btnDocx.setMaxWidth(Double.MAX_VALUE);
        btnDocx.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button btnPdf = new Button("Generate PDF Layout");
        btnPdf.setMaxWidth(Double.MAX_VALUE);

        // Simple runtime dialog logs replacing Streamlit alert popups
        btnDocx.setOnAction(e -> showSystemAlert("DOCX Compilation Signal", "Data compiled! Backend processing active for:\n`STEMMate_A4_Concise_Report_DRAFT.docx`"));
        btnPdf.setOnAction(e -> showSystemAlert("PDF Pipeline Signal", "Data compiled! ReportLab layout configured for:\n`STEMMate_A4_Concise_Report_DRAFT.pdf`"));

        right.getChildren().addAll(outputTitle, compiledCodePreview, actionsTitle, btnDocx, btnPdf);
        return right;
    }

    private void setupListeners() {
        // Track adjustments dynamically across every configuration component
        archTypeBox.valueProperty().addListener((obs, oldVal, newVal) -> updateCompiledOutput());
        syncFlowArea.textProperty().addListener((obs, oldVal, newVal) -> updateCompiledOutput());
        unitTestSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateCompiledOutput());
        failoverGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> updateCompiledOutput());
        repoUrlField.textProperty().addListener((obs, oldVal, newVal) -> updateCompiledOutput());
        teamLeadField.textProperty().addListener((obs, oldVal, newVal) -> updateCompiledOutput());
    }

    private void updateCompiledOutput() {
        String archStyle = archTypeBox.getValue();
        String syncFlow = syncFlowArea.getText().trim();
        int testCoverage = (int) unitTestSlider.getValue();
        
        RadioButton selectedRadio = (RadioButton) failoverGroup.getSelectedToggle();
        String failoverStatus = (selectedRadio != null) ? selectedRadio.getText() : "Incomplete";
        
        String repoUrl = repoUrlField.getText().trim();
        String teamLead = teamLeadField.getText().trim();

        // Assemble payloads inside a raw text injection layout matching your script array structure
        String sec2Payload = String.format("This section contains the core architectural blueprints for the STEMMate system using a %s. Includes offline synchronization flows, container layouts, and component interaction models depicting how low-specification client devices interface with the central synchronization engine over unreliable networks. Note: %s", archStyle, syncFlow);
        String sec3Payload = String.format("Comprehensive verification logs documenting automated unit test coverage at %d%%, integration suites, and network interruption failover simulation results (%s). Detailed matrix demonstrating system performance, data conflict resolution validation, and offline cache state consistency.", testCoverage, failoverStatus);
        String sec5Payload = String.format("Official configuration management indices tracking source infrastructure via %s. Includes team production assignments, core responsibility matrices managed by the %s, and authenticated production repository links mapping specific system subcomponents to engineering sub-teams.", repoUrl, teamLead);

        String pythonSchemaPayload = String.format(
            "sections = [\n" +
            "    (\"1. Executive summary\", [\n" +