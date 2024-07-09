package com.sms.sms.controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class FileUploadController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "root";
    private static final String PASS = "123456";
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path directory = Paths.get("uploads");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        try {
            // 定义文件保存路径
            Path path = Paths.get("sms/src/main/resources/uploads/" + "students.xlsx");
            // 保存文件到指定路径
            Files.write(path, file.getBytes());
            // 这里可以根据需要返回文件的URL或其他信息

            //文件处理写入
                String filePath = "sms/src/main/resources/uploads/students.xlsx"; // 替换为你的文件路径

                try (FileInputStream fis = new FileInputStream(filePath);
                     Workbook workbook = new XSSFWorkbook(fis);
                     Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) { // 假设第一行是标题行，跳过
                            continue;
                        }
                        // 设置PreparedStatement的参数
                        /*int id = (int) row.getCell(0).getNumericCellValue(); // 如果ID是整数类型*/
                        String no;
                        if (row.getCell(1).getCellType().toString().equals("STRING")){
                            no = row.getCell(1).getStringCellValue();
                        }else {
                            double no1 = row.getCell(1).getNumericCellValue();
                            no = String.valueOf(no1);
                        }
                        String phone = row.getCell(2).getStringCellValue();
                        String name = row.getCell(3).getStringCellValue();
                        String gender = row.getCell(4).getStringCellValue();
                        String birthDate = row.getCell(5).getStringCellValue();
                        String birthPlace = row.getCell(6).getStringCellValue();
                        String nativePlace = row.getCell(7).getStringCellValue();
                        String ethnicGroup = row.getCell(8).getStringCellValue();
                        int annualHouseholdIncome = (int) row.getCell(9).getNumericCellValue();
                        String grade = row.getCell(10).getStringCellValue();
                        String className = row.getCell(11).getStringCellValue();
                        String currentResidence = row.getCell(12).getStringCellValue();
                        String mailingAddress = row.getCell(13).getStringCellValue();
                        String familyAddress = row.getCell(14).getStringCellValue();
                        String specialGroup = "";
                        String emergencySituation = "";
                        if (row.getCell(16)!=null){
                            emergencySituation = row.getCell(16).getStringCellValue();
                        }
                        if (row.getCell(15)!=null){
                            specialGroup = row.getCell(15).getStringCellValue();
                        }
                        String isPoorStudent = "N";
                        String isvalid = row.getCell(18).getStringCellValue();
                        String familyMember = row.getCell(19).getStringCellValue();
                        int familyMember1 = Integer.parseInt(familyMember);
                        //判断是否符合贫困生条件
                        int count = 0;
                        if ((annualHouseholdIncome/familyMember1/12)<5000){
                            count ++;
                        }
                        if (emergencySituation=="null"){
                            count++;
                        }
                        if (specialGroup=="null"){
                            count++;
                        }
                        System.out.println("count="+count);
                        if (count>=1){
                            isPoorStudent = "Y";
                        }else {
                            isPoorStudent = "N";
                        }
                        // 构建SQL插入语句
                        String sql = "INSERT INTO stu (no, phone, name, gender, birth_date, birth_place, native_place, ethnic_group," +
                                "annual_household_income, grade, class_name, current_residence, mailing_address, family_address, special_group, emergency_situation, " +
                                "is_poor_student, is_valid, family_member) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, no); // 设置第一个占位符的值
                            pstmt.setString(2, phone);
                            pstmt.setString(3, name);
                            pstmt.setString(4, gender);
                            pstmt.setString(5, birthDate);
                            pstmt.setString(6, birthPlace);
                            pstmt.setString(7, nativePlace);
                            pstmt.setString(8, ethnicGroup);
                            pstmt.setInt(9, annualHouseholdIncome);
                            pstmt.setString(10, grade);
                            pstmt.setString(11, className);
                            pstmt.setString(12, currentResidence);
                            pstmt.setString(13, mailingAddress);
                            pstmt.setString(14, familyAddress);
                            if (row.getCell(16)==null){
                                pstmt.setString(16, "null");
                            }else {
                                pstmt.setString(16, emergencySituation);
                            }
                            if (row.getCell(15)==null){
                                pstmt.setString(15, "null");
                            }else {
                                pstmt.setString(15, specialGroup);
                            }
                            pstmt.setString(17, isPoorStudent);
                            pstmt.setString(18, isvalid);
                            pstmt.setInt(19, familyMember1);

                            // 执行SQL语句
                            pstmt.executeUpdate();
                        }
                    }
                    fis.close();
                    System.out.println("Data inserted successfully!");

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            return ResponseEntity.ok("文件上传成功！");

        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件上传失败！");
        }
    }
}