package top.yll.familytree.family.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yll.familytree.family.FamilyApplication;
import top.yll.familytree.family.pojo.Person;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FamilyApplication.class)
public class AdminServiceTest {

    @Autowired
    private AdminServiceImpl adminService;
    @Test
    public void deleteTest() throws IOException {
        String msg = adminService.deleteOneById(2);
        System.out.println(msg);
    }

    @Test
    public void getInfoTest() throws IOException {
//        Person person = adminService.getPersonByName("杨二");
//        List<Person> info = adminService.getInfoByPerson(person);
        Person person = adminService.getPersonByBirthday("1902-08-27");
        List<Person> info = adminService.getInfoByPerson(person);
        System.out.println(info);
    }

    @Test
    public void getRankTest() throws IOException {
//        System.out.println(adminService.getRankByName("杨一"));
        System.out.println(adminService.getRankByBirthday("1970-12-07"));
//        System.out.println(adminService.getRank("杨"));
    }

    @Test
    public void modifyTest() throws IOException {
        Person person = new Person();
        person.setId(21);
        person.setName("yll");
        person.setBirthday("1999-10-30");
        person.setAlive(true);
        adminService.modify(person);
    }
}
