package com.cfao.app.controllers;

import com.cfao.app.StageManager;
import com.cfao.app.beans.Competence;
import com.cfao.app.beans.Profil;
import com.cfao.app.model.Model;
import com.cfao.app.model.ProfilModel;
import com.cfao.app.util.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by JP on 6/11/2017.
 */
public class ProfilController implements Initializable {
    static Logger logger = Logger.getLogger(ProfilController.class);
    public TableColumn<Profil, String> abbreviationColumn;
    public TableColumn<Profil, String> libelleColumn;
    public TableView<Profil> profilTable;
    public StackPane profilStackPane;

    public TableColumn<Competence, Boolean> fondamentalColumn;
    public TableColumn<Competence, Boolean> initialColumn;
    public TableColumn<Competence, Boolean> avanceColumn;
    public TableColumn<Competence, Boolean> expertColumn;
    public TableColumn<Competence, Competence> listecompetenceColumn;
    public TableColumn<Competence, Boolean> connaissanceColumn;
    public TableColumn<Competence, Boolean> competenceColumn;
    public TableView<Competence> competenceTable;
    public VBox researchBox;
    public AnchorPane rootPane1;

    public VBox researchBox2;
    public TabPane tabPane;
    public Tab firstTab;
    public Button btnPrevious;
    public Button btnNext;
    public Button btnPrint;
    public Button btnNouveau;
    public Button btnModifier;
    public Button btnSupprimer;
    public Button btnAnnuler;
    private SearchBox searchBox = new SearchBox();
    private SearchBox searchBox2 = new SearchBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
        setColumnSettings();
        //buildProfilTable();
        firstTab.setClosable(false);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        profilTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillCompetenceTable(newValue);
        });
    }
    private void initComponents(){
        abbreviationColumn.setCellValueFactory(param -> param.getValue().abbreviationProperty());
        libelleColumn.setCellValueFactory(param -> param.getValue().libelleProperty());
        Task<ObservableList<Profil>> task = new Task<ObservableList<Profil>>() {
            @Override
            protected ObservableList<Profil> call() throws Exception {
                return FXCollections.observableArrayList(new Model<Profil>("Profil").getList());
            }
        };
        ProgressIndicatorUtil.show(profilStackPane, task);
        //profilTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                task.getException().printStackTrace();
                logger.error(task.getException());
            }
        });
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                profilTable.setItems(task.getValue());
                profilTable.getSelectionModel().selectFirst();
            }
        });
        /*task.setOnSucceeded(event -> {
            FilteredList<Profil> filteredList = new FilteredList<Profil>(task.getValue(), p -> true);
            SortedList<Profil> sortedList = new SortedList<Profil>(filteredList);
            sortedList.comparatorProperty().bind(profilTable.comparatorProperty());
            profilTable.setItems(sortedList);
        });*/

        HBox.setHgrow(searchBox, Priority.ALWAYS);
        HBox.setHgrow(searchBox2, Priority.ALWAYS);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        searchBox2.setMaxWidth(Double.MAX_VALUE);
        researchBox.setMaxWidth(Double.MAX_VALUE);
        researchBox2.setMaxWidth(Double.MAX_VALUE);
        researchBox.getChildren().setAll(new Label("Profils : "), searchBox);
        researchBox2.getChildren().setAll(new Label("Compétences associées au profil : "), searchBox2);
        ButtonUtil.print(btnPrint);
        ButtonUtil.previous(btnPrevious);
        ButtonUtil.next(btnNext);
        ButtonUtil.add(btnNouveau);
        ButtonUtil.edit(btnModifier);
        ButtonUtil.cancel(btnAnnuler);
        ButtonUtil.delete(btnSupprimer);
        setColumnProperty(initialColumn,  ProfilModel.INITIAL);
        setColumnProperty(fondamentalColumn,  ProfilModel.FONDAMENTAL);
        setColumnProperty(avanceColumn, ProfilModel.AVANCE);
        setColumnProperty(expertColumn,  ProfilModel.EXPERT);
        competenceColumn.setCellValueFactory(param -> {
            Competence competence = param.getValue();
            if (competence.getType().equals(Constante.COMPETENCE) || competence.getType().equals(Constante.CONNAISSANCE_COMPETENCE)) {
                return new SimpleBooleanProperty(true);
            } else {
                return new SimpleBooleanProperty(false);
            }
        });
        competenceColumn.setCellFactory(param -> new CheckBoxTableCell<>());
        connaissanceColumn.setCellValueFactory(param -> {
            Competence competence = param.getValue();
            if (competence.getType().equals(Constante.CONNAISSANCE) || competence.getType().equals(Constante.CONNAISSANCE_COMPETENCE)) {
                return new SimpleBooleanProperty(true);
            } else {
                return new SimpleBooleanProperty(false);
            }
        });
        connaissanceColumn.setCellFactory(param -> new CheckBoxTableCell<>());
        listecompetenceColumn.setCellValueFactory(param -> {
            Competence competence = param.getValue();
            return new SimpleObjectProperty<>(competence);
        });
    }

    private void fillCompetenceTable(Profil profil) {
        if(profil.getCompetences() != null) {
            competenceTable.setItems(FXCollections.observableArrayList(profil.getCompetences()));
        }else{
            competenceTable.getItems().clear();
        }
    }

    private void setColumnProperty(TableColumn<Competence, Boolean> tableColumn, int niveau) {
        tableColumn.setCellFactory(param -> new CheckBoxTableCell<>());
        tableColumn.setCellValueFactory(param -> {
            Competence competence = param.getValue();
            if(competence.getNiveau() != null) {
                if (competence.getNiveau().getIdniveau() == niveau) {
                    return new SimpleBooleanProperty(true);
                }
            }
            return new SimpleBooleanProperty(false);
        });

    }

    /**
     * Transformer les text des colonnes en vertical et definir le contenu des colonnes
     */
    private void setColumnSettings() {
        setSingleColumnSetting(fondamentalColumn);
        setSingleColumnSetting(avanceColumn);
        setSingleColumnSetting(connaissanceColumn);
        setSingleColumnSetting(competenceColumn);
        setSingleColumnSetting(expertColumn);
        setSingleColumnSetting(initialColumn);
    }

    /**
     * Transformer une colonne passer en parametre en vertical, utiliser dans setColumnSettings
     * Ajouter une rotation pour que les texte soit vertical
     *
     * @param column
     */
    private void setSingleColumnSetting(TableColumn column) {
        Label label = new Label(column.getText());
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        VBox vbox = new VBox(label);
        vbox.setPadding(new Insets(5, 5, 5, 5));
        vbox.setMaxWidth(120);
        column.setText("");
        vbox.setRotate(-90);
        Group group = new Group(vbox);
        column.setGraphic(group);
    }

    public void clickNouveau(ActionEvent actionEvent) {
        try {
            Tab tab = new Tab("Nouveau profil");
            tab.setClosable(true);
            tab.setContent(new ProfilAddEditController());
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtil.showErrorMessage(ex);

        }
    }

    public void clickModifier(ActionEvent actionEvent) {
        if (profilTable.getSelectionModel().getSelectedItem() != null) {
            try {
                Profil profil = profilTable.getSelectionModel().getSelectedItem();
                Tab tab = new Tab("Modification du profil " + profil.getLibelle());
                tab.setContent(new ProfilAddEditController(profil));
                tab.setClosable(true);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
            } catch (Exception ex) {
                ex.printStackTrace();
                AlertUtil.showErrorMessage(ex);
            }
        } else {
            AlertUtil.showSimpleAlert("Information", "Veuillez choisir le profil à modifier");
        }
    }

    public void clickSupprimer(ActionEvent actionEvent) {
        if (profilTable.getSelectionModel().getSelectedItem() != null) {
            Profil profil = profilTable.getSelectionModel().getSelectedItem();
            boolean bool = AlertUtil.showConfirmationMessage("Etes vous sûr  de vouloir supprimer\n le profil " + profil);
            if (bool) {
                Model<Profil> model = new Model<>(Model.getBeanPath("Profil"));
                if (model.delete(profil)) {
                    ServiceproUtil.notify("Suppression OK");
                    StageManager.loadContent("/views/profil/profil.fxml");
                } else {
                    ServiceproUtil.notify("Erreur de suppression");
                }
            }
        } else {
            AlertUtil.showSimpleAlert("Information", "Veuillez choisir le profil à supprimer ");
        }
    }

    public void clickAnnuler(ActionEvent actionEvent) {
        StageManager.loadContent("/views/profil/profil.fxml");
    }

    public void previousAction(ActionEvent event) {
        if(profilTable.getSelectionModel().getSelectedIndex() > 0){
            profilTable.getSelectionModel().selectPrevious();
        }
    }

    public void nextAction(ActionEvent event) {
        if(profilTable.getSelectionModel().getSelectedIndex() < profilTable.getItems().size()){
            profilTable.getSelectionModel().selectNext();
        }
    }


    public void printAction(ActionEvent event) {
    }
}
