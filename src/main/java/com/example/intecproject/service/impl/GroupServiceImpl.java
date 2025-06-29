package com.example.intecproject.service.impl;

import com.example.intecproject.exception.NoUsersInGroupException;
import com.example.intecproject.model.Group;
import com.example.intecproject.model.Role;
import com.example.intecproject.model.User;
import com.example.intecproject.repository.GroupRepository;
import com.example.intecproject.repository.UserRepository;
import com.example.intecproject.service.GroupService;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


@Service
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group saveGroup(Group g) {
        return groupRepository.save(g);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteGroup(Long Id) {

        Group g=findById(Id);

        groupRepository.delete(g);

    }

    @Override
    public Group createGroup(String groupName) {
        Group newGroup=new Group(groupName);
        return groupRepository.save(newGroup);
    }

    @Override
    public void addUserToGroup(Long userId, Long groupId) throws Exception {
        Group g=findById(groupId);
        User u=userRepository.findById(userId).orElse(null);

        if(g!=null && u!=null)
        {
            if(!g.getUsers().isEmpty())
            {
                g.addUser(u);
                groupRepository.save(g);
            }
            else
            {
                throw new NoUsersInGroupException(groupId);
            }
        }
        else
        {
            throw new Exception("There are no groups and users with the provided ID");
        }
    }

    @Override
    public void RemoveUserFromGroup(Long userId, Long groupId) throws Exception {
        Group g=findById(groupId);
        User u=userRepository.findById(userId).orElse(null);


        if(g!=null && u!=null) {
            if (g.getUsers().contains(u)) {
                g.getUsers().remove(u);

                u.setGroup(null);


                groupRepository.save(g);
                userRepository.save(u);
            } else
            {
                throw new NoUsersInGroupException(groupId);

            }
        }
        else
        {
            throw new Exception("There are no groups and users with the provided ID");
        }
    }

    @Override
    public byte[] exportToExcel(Long groupId) throws IOException {

        Group group=findById(groupId);

        Workbook workbook=new XSSFWorkbook();
        Sheet sheet=workbook.createSheet(group.getGroupName());

        Row metarow=sheet.createRow(0);

        metarow.createCell(0).setCellValue("Group Name:");
        metarow.createCell(1).setCellValue(group.getGroupName());
        metarow.createCell(2).setCellValue("Created At:");
        metarow.createCell(3).setCellValue(group.getCreatedAt().toString());


        Row headerRow=sheet.createRow(2);

        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Role");

    int rowNum=3;

    for(User user:group.getUsers())
    {
        Row row=sheet.createRow(rowNum++);

        row.createCell(1).setCellValue(user.getFirstName());
        row.createCell(2).setCellValue(user.getLastName());
        row.createCell(3).setCellValue(user.getEmail());
        row.createCell(4).setCellValue(user.getRole().toString());
    }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> cell.toString();
        };
    }

    @Override
    public void importUsersFromExcel(Long groupId, MultipartFile file) throws IOException {
        Group group = findById(groupId);

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String firstName = getCellValueAsString(row.getCell(0));
                String lastName = getCellValueAsString(row.getCell(1));
                String email = getCellValueAsString(row.getCell(2));
                String password = getCellValueAsString(row.getCell(3));
                boolean isAvailable = Boolean.parseBoolean(getCellValueAsString(row.getCell(4)));
                String createdAtStr = getCellValueAsString(row.getCell(5));
                String role = getCellValueAsString(row.getCell(6));

                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPassword(password);
                user.setAvailable(isAvailable);
                user.setCreatedAt(LocalDateTime.parse(createdAtStr)); // Ensure ISO_LOCAL_DATE_TIME format
                user.setRole(Role.valueOf(role.toUpperCase()));
                user.setGroup(group);

                user = userRepository.save(user);
                group.getUsers().add(user);
            }

            groupRepository.save(group);

        } catch (IOException e) {
            throw new RuntimeException("Failed to import users", e);
        }
    }


}

