package top.yll.familytree.family.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.yll.familytree.family.pojo.Person;
import top.yll.familytree.family.service.AdminService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private AdminService adminService;


    @RequestMapping("/findOfUser")
    public String find(Model model) throws IOException {
        List<Person> list = adminService.find();
        List<String> birthday = adminService.getBirthdayInfo();
        model.addAttribute("list", list);
        model.addAttribute("birthday",birthday);
        return "familyOfUser";
    }



}
