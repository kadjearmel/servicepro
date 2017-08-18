package com.cfao.app.controllers;

import com.cfao.app.beans.*;
import com.cfao.app.model.CompetenceModel;
import com.cfao.app.model.FormationModel;
import com.cfao.app.model.Model;
import com.cfao.app.model.PersonneModel;
import com.cfao.app.util.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.PropertiesManager;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by JP on 7/25/2017.
 */
public class CiviliteFormationController extends AnchorPane implements Initializable {
    public TableColumn<PersonneCompetence, Competence> intituleCompetenceColumn;
    //public TableColumn<Competence, CompetenceCertification> certificationCompetenceColumn;
    public TableColumn<PersonneCompetence, Boolean> encoursCompetenceColumn;
    public TableColumn<PersonneCompetence, Boolean> certifieeCompetenceColumn;
    public TableColumn<PersonneCompetence, Boolean> acertifierCompetenceColumn;

    public TableView<PersonneCompetence> competenceTable;

    public Button btnPreviousCompetence;
    public Button btnNextCompetence;
    public Button btnPrintCompetence;
    public Button btnEditerCertification;

    public TableView<Formation> formationTable;
    public TableColumn<Formation, String> codeFormationColumn;
    public TableColumn<Formation, String> titreFormationColumn;
    public TableColumn<Formation, LocalDate> datedebutFormationColumn;
    public TableColumn<Formation, LocalDate> datefinFormationColumn;
    public StackPane historiqueStackPane;
    public ComboBox<CompetenceCertification> comboStatut;
    private Personne personne = null;
    public StackPane competenceStackPane;
    public AnchorPane pdfContainer;

    public Button btnSupprimerCompetence;
    public Button btnAddCompetence;

    private boolean editerCertification = false;

    private HashMap<Competence, CompetenceCertification> selectedItems;

    final SwingController swingController = new SwingController();
    private JComponent viewerPanel;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
    }

    public CiviliteFormationController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/civilite/formation.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtil.showErrorMessage(ex);
        }
    }

    public CiviliteFormationController(Personne personne) {
        this();
        this.personne = personne;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
        createViewer(pdfContainer);
        ButtonUtil.delete(btnSupprimerCompetence);
        ButtonUtil.plusIcon(btnAddCompetence);
        formationTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Formation>() {
            @Override
            public void changed(ObservableValue<? extends Formation> observable, Formation oldValue, Formation newValue) {
                //String url = ResourceBundle.getBundle("Bundle").getString("document.dir");
                //openDocument(getClass().getResource(url + "documents.pdf").getPath());

                Path path = Paths.get(ResourceBundle.getBundle("Bundle").getString("document.dir")).toAbsolutePath();
                if (!path.toFile().exists()) {
                    path.toFile().mkdir();
                }
                openDocument(path.toString() + File.separator + "documents.pdf");
            }
        });

    }


    public void openDocument(String document) {
        SwingUtilities.invokeLater(() -> {
            swingController.openDocument(document);
            viewerPanel.revalidate();
        });
    }

    private void initComponents() {
        ButtonUtil.next(btnNextCompetence);
        ButtonUtil.previous(btnPreviousCompetence);
        ButtonUtil.print(btnPrintCompetence);
        ButtonUtil.edit(btnEditerCertification);
        Task<ObservableList<CompetenceCertification>> task = new Task<ObservableList<CompetenceCertification>>() {
            @Override
            protected ObservableList<CompetenceCertification> call() throws Exception {
                return FXCollections.observableArrayList(new Model<CompetenceCertification>("CompetenceCertification").getList());
            }
        };
        comboStatut.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
        task.setOnFailed(event -> {
            System.err.println(task.getException());
            ServiceproUtil.notify("Erreur dans le Thread combo Statut");
        });

        /*competenceTable.setRowFactory(param -> {
            final TableRow<Competence> row = new TableRow<>();
            final Tooltip tooltip = new Tooltip();
            row.hoverProperty().addListener(observable -> {

                final Competence competence = row.getItem();
                if (row.isHover() && competence != null) {
                    tooltip.setText(competence.getDescription());
                    row.setTooltip(tooltip);
                }
            });
            return row;
        });*/

        codeFormationColumn.setCellValueFactory(param -> param.getValue().codeformationProperty());

        titreFormationColumn.setCellValueFactory(param -> param.getValue().titreProperty());

        datedebutFormationColumn.setCellValueFactory(param -> param.getValue().datedebutProperty());
        datedebutFormationColumn.setCellFactory(new DateTableCellFactory<Formation>());

        datefinFormationColumn.setCellValueFactory(param -> param.getValue().datefinProperty());
        datefinFormationColumn.setCellFactory(new DateTableCellFactory<Formation>());
        setColumnFactories();

        intituleCompetenceColumn.setCellValueFactory(param -> param.getValue().competence());
        encoursCompetenceColumn.setCellValueFactory(param -> param.getValue().encoursProperty());
        acertifierCompetenceColumn.setCellValueFactory(param -> param.getValue().acertifierProperty());
        certifieeCompetenceColumn.setCellValueFactory(param -> param.getValue().certifieeProperty());
    }

    private void setColumnFactories() {
        encoursCompetenceColumn.setCellFactory(param ->
                new CheckTable() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (this.getTableRow().getIndex() >= 0 && this.getTableView().getItems().size() > 0)
                            if (newValue != null && newValue) {
                                PersonneCompetence pp = this.getTableView().getItems().get(this.getTableRow().getIndex());
                                pp.setEncours(newValue);
                                pp.setCertifiee(false);
                                pp.setAcertifier(false);
                            }
                    }
                });
                /*{
            CheckBoxTableCell<PersonneCompetence, Boolean> cell = new CheckBoxTableCell<>();
            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow().getIndex() >= 0 && cell.getTableView().getItems().size() > 0) {
                    if (newValue) {
                        PersonneCompetence pp = cell.getTableView().getItems().get(cell.getTableRow().getIndex());
                        pp.setEncours(newValue);
                        pp.setCertifiee(false);
                        pp.setAcertifier(false);
                    }
                }
            });
            return cell;
        });*/
        acertifierCompetenceColumn.setCellFactory(param ->
                new CheckTable() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (this.getTableRow().getIndex() >= 0 && this.getTableView().getItems().size() > 0)
                            if (newValue != null && newValue) {
                                PersonneCompetence pp = this.getTableView().getItems().get(this.getIndex());
                                pp.setAcertifier(newValue);
                                pp.setCertifiee(false);
                                pp.setEncours(false);
                            }

                    }
                });
            /*
            CheckBoxTableCell<PersonneCompetence, Boolean> cell = new CheckBoxTableCell<>();
            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow().getIndex() >= 0 && cell.getTableView().getItems().size() > 0) {
                    if (newValue) {
                        PersonneCompetence pp = cell.getTableView().getItems().get(cell.getIndex());
                        pp.setAcertifier(newValue);
                        pp.setCertifiee(false);
                        pp.setEncours(false);
                    }
                }
            });
            return cell;
        });*/

        certifieeCompetenceColumn.setCellFactory(param ->
                new CheckTable() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (this.getTableRow().getIndex() >= 0 && this.getTableView().getItems().size() > 0)
                            if (newValue != null && newValue) {
                                PersonneCompetence pp = this.getTableView().getItems().get(this.getTableRow().getIndex());
                                pp.setCertifiee(newValue);
                                pp.setEncours(false);
                                pp.setAcertifier(false);
                            }
                    }
                });

            /*
            CheckBoxTableCell<PersonneCompetence, Boolean> cell = new CheckBoxTableCell<>();
            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow().getIndex() >= 0 && cell.getTableView().getItems().size() > 0)
                    if(newValue){
                        PersonneCompetence pp = cell.getTableView().getItems().get(cell.getTableRow().getIndex());
                        pp.setCertifiee(newValue);
                        pp.setEncours(false);
                        pp.setAcertifier(false);
                    }*/
                /*
                if (cell.getTableRow().getIndex() >= 0 && cell.getTableView().getItems().size() > 0) {
                    PersonneCompetence pp = cell.getTableView().getItems().get(cell.getTableRow().getIndex());
                    pp.setCertifiee(newValue);
                    if (newValue) {
                        pp.setEncours(false);
                        pp.setAcertifier(false);
                    }
                    System.out.println(">>> " + pp.getCompetenceCertification().getCertification());
                }

            });
            return cell;
        });*/

    }

    /*private void updatePersonneCompetence(PersonneCompetence pp) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return new PersonneCompetenceModel().update(pp);
            }
        };
        new Thread(task).start();
    }*/

    public void buildFormation() {
        competenceTable.itemsProperty().bind(personne.personneCompetencesProperty());
        //competenceTable.setItems(FXCollections.observableArrayList(personne.getPersonneCompetences()));
        // Historique des formatioetItems().setAll(personne.personneCompetencesPropn
        Task<ObservableList<Formation>> task = new Task<ObservableList<Formation>>() {
            @Override
            protected ObservableList<Formation> call() throws Exception {
                return FXCollections.observableArrayList(new FormationModel().getFormationsByPersonne(personne));
            }
        };

        formationTable.itemsProperty().bind(task.valueProperty());
        ProgressIndicatorUtil.show(historiqueStackPane, task);
        new Thread(task).start();

        task.setOnFailed(event -> {
            ServiceproUtil.notify("Erreur dans le thread de certification");
            task.getException().printStackTrace();
            System.err.println(task.getException());
        });
    }


    public void editerCertificationCompetence(ActionEvent event) {
        if (personne != null) {
            if (!editerCertification) {
                competenceTable.setEditable(true);
                btnEditerCertification.setText(ResourceBundle.getBundle("Bundle").getString("button.save.fr"));
                editerCertification = true;
            } else {
                //personne.getPersonneCompetences()
                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        for (PersonneCompetence pc : personne.getPersonneCompetences()) {

                            if (pc.isCertifiee())
                                pc.getCompetenceCertification().setCertification(Constante.COMPETENCE_CERTIFIEE);
                            else if (pc.isEncours())
                                pc.getCompetenceCertification().setCertification(Constante.COMPETENCE_ENCOURS);
                            else if (pc.isAcertifier())
                                pc.getCompetenceCertification().setCertification(Constante.COMPETENCE_ACERTIFIER);

                            if (pc.isAcertifier() || pc.isEncours() || pc.isCertifiee()) {
                                pc.setCreatedAt(new java.util.Date());
                                new Model<PersonneCompetence>().update(pc);
                            }
                        }
                        return true;
                    }
                };
                new Thread(task).start();
                task.setOnSucceeded(event1 -> {
                    if (task.getValue()) {
                        editerCertification = false;
                        btnEditerCertification.setText("Editer");
                        competenceTable.setEditable(false);
                        ServiceproUtil.notify("Sauvegarde OK");
                    } else {
                        ServiceproUtil.notify("Une erreur de sauvegarde s'est produite");
                    }
                });
                task.setOnFailed(event12 -> {
                    ServiceproUtil.notify("Erreur dans le Thread de formation certification");
                    task.getException().printStackTrace();
                    System.err.println(task.getException());
                });
            }
        }
    }

    public void previousCompetenceAction(ActionEvent event) {
    }

    public void nextCompetenceAction(ActionEvent event) {
    }

    public void printCompetenceAction(ActionEvent event) {
    }

    public void supprimerCompetenceAction(ActionEvent event) {
        PersonneCompetence pp = competenceTable.getSelectionModel().getSelectedItem();
        if (pp != null) {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return new Model<PersonneCompetence>().delete(pp);
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(event1 -> {
                if (task.getValue()) {
                    competenceTable.getItems().remove(pp);
                    setColumnFactories();
                } else {
                    ServiceproUtil.notify("Erreur de suppression");
                }
            });
            task.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    task.getException().printStackTrace();
                    System.err.println(task.getException());

                }
            });
        } else {
            AlertUtil.showSimpleAlert("Information", "Veuillez choisir la compétence à retirer de la civilité");
        }
    }

    public void filterCertification(ActionEvent event) {
        CompetenceCertification certification = comboStatut.getValue();
        if (certification != null) {
            Task<ObservableList<PersonneCompetence>> task = new Task<ObservableList<PersonneCompetence>>() {
                @Override
                protected ObservableList<PersonneCompetence> call() throws Exception {
                    return FXCollections.observableArrayList(new PersonneModel().getCompetencesByCertification(personne, certification));
                }
            };
            new Thread(task).start();
            setColumnFactories();

            competenceTable.itemsProperty().bind(task.valueProperty());
            task.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    ServiceproUtil.notify("Erreur dans le Thread [Filtrage certification]");
                    task.getException().printStackTrace();
                    System.err.println(task.getException());
                }
            });
        }
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public void addCompetenceAction(ActionEvent event) {
        if (personne != null) {
            Dialog<PersonneCompetence> dialog = DialogUtil.dialogTemplate();
            dialog.setHeaderText("Ajouter d'une competence");
            DialogCompetenceController controller = new DialogCompetenceController();
            dialog.getDialogPane().setContent(controller);
            dialog.setResultConverter(param -> {
                if (param.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return controller.getData();
                } else {
                    return null;
                }
            });
            Optional<PersonneCompetence> result = dialog.showAndWait();
            result.ifPresent(personneCompetence -> {
                if (personneCompetence != null) {
                    boolean exiteDeja = false;
                    int i = 0;
                    while (!exiteDeja && i < competenceTable.getItems().size()) {
                        PersonneCompetence pp = competenceTable.getItems().get(i);
                        if (pp.getCompetence().equals(personneCompetence.getCompetence())) {
                            exiteDeja = true;
                        }
                        i++;
                    }
                    if (!exiteDeja) {
                        new Model<PersonneCompetence>().save(personneCompetence);
                        personne.getPersonneCompetences().add(personneCompetence);
                        //competenceTable.getItems().add(personneCompetence);
                    }
                } else {
                    ServiceproUtil.notify("Erreur d'ajout de profil");
                }

            });
        }
    }


    private void createViewer(AnchorPane Pane) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // create the viewer ri components.
                    swingController.setIsEmbeddedComponent(true);
                    PropertiesManager properties = new PropertiesManager(System.getProperties(),
                            ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));

                    // read/store the font cache.
                    ResourceBundle messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
                    new FontPropertiesManager(properties, System.getProperties(), messageBundle);
                    properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1");
                    properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_OPEN, "true");
                    properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_SAVE, "true");
                    properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_PRINT, "true");
                    // hide the status bar
                    properties.set(PropertiesManager.PROPERTY_SHOW_STATUSBAR, "false");
                    // hide a few toolbars, just to show how the prefered size of the viewer changes.
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, "false");
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, "true");
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL, "false");

                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, "false");

                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ZOOM, "true");
                    properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_SEARCH, "false");
                    properties.set(PropertiesManager.PROPERTY_HIDE_UTILITYPANE, "false");


                    swingController.getDocumentViewController().setAnnotationCallback(
                            new org.icepdf.ri.common.MyAnnotationCallback(swingController.getDocumentViewController()));

                    SwingViewBuilder factory = new SwingViewBuilder(swingController, properties);

                    viewerPanel = factory.buildViewerPanel();
                    viewerPanel.revalidate();

                    SwingNode swingNode = new SwingNode();
                    swingNode.setContent(viewerPanel);
                    AnchorPane.setLeftAnchor(swingNode, 0.0);
                    AnchorPane.setRightAnchor(swingNode, 0.0);
                    AnchorPane.setTopAnchor(swingNode, 0.0);
                    AnchorPane.setBottomAnchor(swingNode, 0.0);
                    Pane.getChildren().add(swingNode);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DialogCompetenceController extends AnchorPane implements Initializable {
        @FXML
        public ComboBox<Competence> comboCompetence;
        @FXML
        public ComboBox<CompetenceCertification> comboCertification;
        private ObservableMap<String, ObservableList> map;

        public DialogCompetenceController() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/civilite/dialog/dialogFormationCompetence.fxml"));
                loader.setRoot(this);
                loader.setController(this);
                loader.load();
            } catch (Exception ex) {
                AlertUtil.showErrorMessage(ex);
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            Task<ObservableMap<String, ObservableList>> task = new Task<ObservableMap<String, ObservableList>>() {
                @Override
                protected ObservableMap<String, ObservableList> call() throws Exception {
                    map = FXCollections.observableHashMap();
                    map.put("competence", FXCollections.observableList(new CompetenceModel().getList()));
                    map.put("certification", FXCollections.observableList(new Model<>("CompetenceCertification").getList()));
                    return map;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(event -> {
                Platform.runLater(() -> {
                    comboCompetence.setItems(map.get("competence"));
                    comboCertification.setItems(map.get("certification"));
                });

            });
        }

        public PersonneCompetence getData() {
            if (comboCompetence.getValue() != null && comboCertification.getValue() != null) {
                PersonneCompetence personneCompetence = new PersonneCompetence();
                personneCompetence.setCompetence(comboCompetence.getSelectionModel().getSelectedItem());
                personneCompetence.getId().setCompetence(comboCompetence.getSelectionModel().getSelectedItem().getIdcompetence());
                personneCompetence.setCompetenceCertification(comboCertification.getSelectionModel().getSelectedItem());
                if (personne != null) {
                    personneCompetence.setPersonne(personne);
                    personneCompetence.getId().setPersonne(personne.getIdpersonne());
                }
                personneCompetence.setCreatedAt(new java.util.Date());
                return personneCompetence;
            }
            return null;
        }
    }
}

abstract class CheckTable extends CheckBoxTableCell<PersonneCompetence, Boolean> implements ChangeListener<Boolean> {
    public CheckTable() {
        this.itemProperty().addListener(this);
    }
}