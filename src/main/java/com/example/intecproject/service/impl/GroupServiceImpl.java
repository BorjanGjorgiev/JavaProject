package com.example.intecproject.service.impl;

import com.example.intecproject.exception.NoUsersInGroupException;
import com.example.intecproject.model.Group;
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
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
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
    public byte[] exportToPDF(String name) {
        Group g = groupRepository.findByGroupName(name);
        if (g == null) {
            throw new RuntimeException("Group not found: " + name);
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font tableCellFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("Group: " + g.getGroupName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(4); // FirstName, LastName, Email, Role
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            // Table headers
            Stream.of("First Name", "Last Name", "Email", "Role").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(1);
                header.setPhrase(new Phrase(headerTitle, tableHeaderFont));
                table.addCell(header);
            });

            List<User> users = g.getUsers();
            for (User user : users) {
                table.addCell(new Phrase(user.getFirstName(), tableCellFont));
                table.addCell(new Phrase(user.getLastName(), tableCellFont));
                table.addCell(new Phrase(user.getEmail(), tableCellFont));
                table.addCell(new Phrase(user.getRole().toString(), tableCellFont));
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}

