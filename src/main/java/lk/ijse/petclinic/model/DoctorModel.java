package lk.ijse.petclinic.model;

import lk.ijse.petclinic.db.DBConnection;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.util.Crudutil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorModel {

//    public List<DoctorDTO> getDoctor() throws SQLException {
//        String sql = "SELECT * FROM doctors";
//        String sql2 = "SELECT * FROM users WHERE user_id = ?";
//        ResultSet rs = Crudutil.execute(sql);
//        List <DoctorDTO>doctors = new ArrayList<>();
//        int user_id = 0;
//        double consultation_fee = 0;
//        int doctor_id = 0;
//        if(rs.next()) {
//            while (rs.next()) {
//                user_id = rs.getInt("user_id");
//                consultation_fee = rs.getDouble("consultation_fee");
//                doctor_id = rs.getInt("doctor_id");
//
//                ResultSet rs2 = Crudutil.execute(sql2, user_id);
//                if (rs2.next()) {
//
//                    doctors.add(new DoctorDTO(doctor_id, user_id, consultation_fee, rs2.getString("name"), rs2.getString("email"), rs2.getString("phone"), rs2.getString("password_hash")));
//
//                }
//
//            }
//            return doctors;
//        }
//        return null;
//    }
        public List<DoctorDTO> getAllDoctor() throws SQLException{
            String sql = "SELECT * FROM doctors";
            String sql2 = "SELECT * FROM users WHERE user_id = ?";

            ResultSet rs = Crudutil.execute(sql);
            List<DoctorDTO> doctors = new ArrayList<>();

            while(rs.next()){
                int user_id = rs.getInt("user_id");
                Double consultation_fee = rs.getDouble("consultation_fee");
                int doctor_id = rs.getInt("doctor_id");
                String status = rs.getString("status");

                ResultSet rs2 = Crudutil.execute(sql2,user_id);
                if(rs2.next()){
                    doctors.add(new DoctorDTO(doctor_id,user_id,consultation_fee,rs2.getString("name"),rs2.getString("email"),rs2.getString("phone"),status));
                }
            }
            if (doctors.isEmpty()){
                return null;
            }
            return doctors;
        }

        public void updateDoctorStatus(int doctorID,String status) throws SQLException{
            String sql = "UPDATE doctors SET status = ? WHERE doctor_id = ?";
            Crudutil.execute(sql,status,doctorID);
        }

        public void deleteDoctor(String doctorId) {
            String sql = "DELETE FROM doctors WHERE doctor_id = ?";

            try{Crudutil.execute(sql,doctorId);}catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        public Boolean updateDoctor(DoctorDTO doctorDTO)throws SQLException{
            String sql = "UPDATE doctors SET consultation_fee = ?, status = ? WHERE doctor_id = ?";
            String sql2 = "UPDATE users SET name = ?, email = ?, phone = ? WHERE user_id = ?";
            boolean isSaved = Crudutil.execute(sql2,doctorDTO.getName(),doctorDTO.getEmail(),doctorDTO.getPhone(),doctorDTO.getUser_id());
            boolean isSaved2 = Crudutil.execute(sql,doctorDTO.getConsultation_fee(),doctorDTO.getStatus(),doctorDTO.getDoctor_id());
            return isSaved && isSaved2;
        }

        public DoctorDTO getOneDoctor(String id) {
            String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
            String sql2 = "SELECT * FROM users WHERE user_id = ?";
            try{
            ResultSet rs = Crudutil.execute(sql,id);
            if (rs.next()){
                int user_id = rs.getInt("user_id");
                Double consultation_fee = rs.getDouble("consultation_fee");
                int doctor_id = rs.getInt("doctor_id");
                String status = rs.getString("status");

                ResultSet rs2 = Crudutil.execute(sql2,user_id);
                if (rs2.next()){
                    return new DoctorDTO(doctor_id,user_id,consultation_fee,rs2.getString("name"),rs2.getString("email"),rs2.getString("phone"),status);
                }
            }} catch (Exception e){
                throw new RuntimeException(e);
            }
            return null;
        }

        public boolean addDoctor(DoctorDTO dto){

            String sql = "INSERT INTO users (name, email, phone, password_hash, role) VALUES (?,?,?,?,?)";
            String role = "doctor";
            String sql2 = "INSERT INTO doctors (user_id, consultation_fee) VALUES (?,?)";
            String sql3 = "SELECT * FROM users WHERE email = ?";
            try{
                boolean isSaved = Crudutil.execute(sql,dto.getName(),dto.getEmail(),dto.getPhone(),dto.getPassword(),role);
                ResultSet rs = Crudutil.execute(sql3,dto.getEmail());
                if (rs.next()){
                 dto.setUser_id(rs.getInt("user_id"));
                }
                boolean isSaved2 = Crudutil.execute(sql2,dto.getUser_id(),dto.getConsultation_fee());

                return isSaved && isSaved2;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }



        }

    public List<DoctorDTO> getAvailableDoctors() {
        String sql = "SELECT d.doctor_id, d.user_id, d.consultation_fee, d.status, u.name, u.email, u.phone " +
                "FROM doctors d " +
                "INNER JOIN users u ON d.user_id = u.user_id " +
                "WHERE d.status = 'Available' " +
                "ORDER BY u.name";

        List<DoctorDTO> doctors = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                DoctorDTO dto = new DoctorDTO();
                dto.setDoctor_id(rs.getInt("doctor_id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setName(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setPhone(rs.getString("phone"));
                dto.setConsultation_fee(rs.getDouble("consultation_fee"));
                dto.setStatus(rs.getString("status"));
                doctors.add(dto);
            }

            return doctors.isEmpty() ? null : doctors;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void printDoctorReport() throws SQLException , JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream inputStream = getClass().getResourceAsStream("/reports/docRepo.jrxml");
        JasperReport jr = JasperCompileManager.compileReport(inputStream);
        JasperPrint jp = JasperFillManager.fillReport(jr,null,conn);
        JasperViewer.viewReport(jp);
    }
}
