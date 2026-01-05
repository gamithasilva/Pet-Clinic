package lk.ijse.petclinic.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lk.ijse.petclinic.dto.AppointmentDTO;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.dto.PetDTO;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.AppointmentModel;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.model.PetModel;
import lk.ijse.petclinic.model.PetOwnerModel;
import lk.ijse.petclinic.util.Reference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Controller for Add Appointment Form
 */
public class AddAppointmentController {

    @FXML private ComboBox<PetOwnerDTO> petOwnerComboBox;
    @FXML private ComboBox<PetDTO> petComboBox;
    @FXML private VBox petInfoCard;
    @FXML private Label petInfoLabel;
    @FXML private ComboBox<DoctorDTO> doctorComboBox;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minuteComboBox;
    @FXML private TextField consultationFeeField;
    @FXML private TextArea notesArea;
    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    // Models
    private final AppointmentModel appointmentModel = new AppointmentModel();
    private final PetOwnerModel petOwnerModel = new PetOwnerModel();
    private final PetModel petModel = new PetModel();
    private final DoctorModel doctorModel = new DoctorModel();

    // Observable Lists
    private final ObservableList<PetOwnerDTO> petOwners = FXCollections.observableArrayList();
    private final ObservableList<PetDTO> allPets = FXCollections.observableArrayList();
    private final ObservableList<PetDTO> filteredPets = FXCollections.observableArrayList();
    private final ObservableList<DoctorDTO> doctors = FXCollections.observableArrayList();

    public void initialize() {
        setupPetOwnerComboBox();
        setupPetComboBox();
        setupDoctorComboBox();
        setupTimeComboBoxes();
        setupDatePicker();
        setupConsultationFeeField();
        loadData();
    }

    private void setupPetOwnerComboBox() {
        petOwnerComboBox.setItems(petOwners);

        petOwnerComboBox.setCellFactory(param -> new ListCell<PetOwnerDTO>() {
            @Override
            protected void updateItem(PetOwnerDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " - " + item.getPhone());
            }
        });

        petOwnerComboBox.setButtonCell(new ListCell<PetOwnerDTO>() {
            @Override
            protected void updateItem(PetOwnerDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.getName());
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });

        // When owner selected, filter pets
        petOwnerComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterPetsByOwner(newVal.getPet_owner_id());
                petOwnerComboBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 8; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 8; " +
                                "-fx-border-width: 1; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            } else {
                petComboBox.getItems().clear();
                petInfoCard.setVisible(false);
                petInfoCard.setManaged(false);
            }
        });
    }

    private void setupPetComboBox() {
        petComboBox.setItems(filteredPets);

        petComboBox.setCellFactory(param -> new ListCell<PetDTO>() {
            @Override
            protected void updateItem(PetDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getName() + " (" + item.getSpecies() + " - " + item.getBreed() + ")");
            }
        });

        petComboBox.setButtonCell(new ListCell<PetDTO>() {
            @Override
            protected void updateItem(PetDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.getName());
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });

        // Show pet info when selected
        petComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showPetInfo(newVal);
                petComboBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 8; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 8; " +
                                "-fx-border-width: 1; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            } else {
                petInfoCard.setVisible(false);
                petInfoCard.setManaged(false);
            }
        });
    }

    private void setupDoctorComboBox() {
        doctorComboBox.setItems(doctors);

        doctorComboBox.setCellFactory(param -> new ListCell<DoctorDTO>() {
            @Override
            protected void updateItem(DoctorDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getName() + " - Rs. " + String.format("%.2f", item.getConsultation_fee()));
            }
        });

        doctorComboBox.setButtonCell(new ListCell<DoctorDTO>() {
            @Override
            protected void updateItem(DoctorDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.getName());
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });

        // Auto-fill consultation fee when doctor selected
        doctorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                consultationFeeField.setText(String.format("%.2f", newVal.getConsultation_fee()));
                doctorComboBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 8; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 8; " +
                                "-fx-border-width: 1; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            }
        });
    }

    private void setupTimeComboBoxes() {
        // Hours (00-23)
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        hourComboBox.setItems(hours);
        hourComboBox.setValue("09"); // Default 9 AM

        // Minutes (00, 15, 30, 45)
        ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
        minuteComboBox.setItems(minutes);
        minuteComboBox.setValue("00"); // Default :00
    }

    private void setupDatePicker() {
        appointmentDatePicker.setValue(LocalDate.now());
    }

    private void setupConsultationFeeField() {
        // Only allow numbers and decimal point
        consultationFeeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                consultationFeeField.setText(oldVal);
            }
        });
    }

    private void filterPetsByOwner(int ownerId) {
        filteredPets.clear();
        allPets.stream()
                .filter(pet -> pet.getPetOwnerId() == ownerId)
                .forEach(filteredPets::add);

        if (filteredPets.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "This owner has no pets registered. Please add a pet first.").showAndWait();
        }
    }

    private void showPetInfo(PetDTO pet) {
        petInfoLabel.setText(String.format("Species: %s | Breed: %s | Gender: %s | Age: %s",
                pet.getSpecies(),
                pet.getBreed(),
                pet.getGender(),
                calculateAge(pet.getDateOfBirth())
        ));
        petInfoCard.setVisible(true);
        petInfoCard.setManaged(true);
    }

    private String calculateAge(LocalDate birthDate) {
        if (birthDate == null) return "Unknown";
        int years = LocalDate.now().getYear() - birthDate.getYear();
        return years > 0 ? years + " years" : "< 1 year";
    }

    @FXML
    private void handleSave() {
        // Validation
        if (petOwnerComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a pet owner.");
            return;
        }

        if (petComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a pet.");
            return;
        }

        if (doctorComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a doctor.");
            return;
        }

        if (appointmentDatePicker.getValue() == null) {
            showAlert("Validation Error", "Please select an appointment date.");
            return;
        }

        if (hourComboBox.getValue() == null || minuteComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select appointment time.");
            return;
        }

        if (consultationFeeField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter consultation fee.");
            return;
        }

        try {
            // Build appointment datetime
            LocalDate date = appointmentDatePicker.getValue();
            int hour = Integer.parseInt(hourComboBox.getValue());
            int minute = Integer.parseInt(minuteComboBox.getValue());
            LocalTime time = LocalTime.of(hour, minute);
            LocalDateTime appointmentDatetime = LocalDateTime.of(date, time);

            // Check if appointment is in the past
            if (appointmentDatetime.isBefore(LocalDateTime.now())) {
                showAlert("Invalid Date", "Cannot schedule appointment in the past.");
                return;
            }

            // Create AppointmentDTO
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setDoctorId(doctorComboBox.getValue().getDoctor_id());
            appointmentDTO.setPetId(petComboBox.getValue().getPetId());
            appointmentDTO.setAppointmentDatetime(appointmentDatetime);
            appointmentDTO.setConsultationFee(Double.parseDouble(consultationFeeField.getText()));
            appointmentDTO.setStatus("scheduled");
            appointmentDTO.setNotes(notesArea.getText().trim().isEmpty() ? null : notesArea.getText().trim());

            // Save via Model
            boolean saved = appointmentModel.saveAppointment(appointmentDTO);

            if (saved) {
                showAlert("Success", "Appointment scheduled successfully!", Alert.AlertType.INFORMATION);

                // Refresh the appointment list if loaded
                if (Reference.appointmentListView != null) {
                    Reference.appointmentListView.initialize();
                }

                // Close the window
                saveBtn.getScene().getWindow().hide();
            } else {
                showAlert("Error", "Failed to schedule appointment.");
            }

        } catch (Exception e) {
            showAlert("Error", "Error saving appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void loadData() {
        try {
            // Load pet owners
            List<PetOwnerDTO> ownerList = petOwnerModel.getALlPetOwner();
            if (ownerList != null) {
                petOwners.setAll(ownerList);
            }

            // Load all pets
            List<PetDTO> petList = petModel.getAllPet();
            if (petList != null) {
                allPets.setAll(petList);
            }

            // Load doctors (only available ones)
            List<DoctorDTO> doctorList = doctorModel.getAvailableDoctors();
            if (doctorList != null) {
                doctors.setAll(doctorList);
            }

        } catch (Exception e) {
            showAlert("Error", "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        showAlert(title, content, Alert.AlertType.WARNING);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}