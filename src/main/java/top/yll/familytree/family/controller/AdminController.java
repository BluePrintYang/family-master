package top.yll.familytree.family.controller;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yll.familytree.family.pojo.Person;
import top.yll.familytree.family.pojo.Relationship;
import top.yll.familytree.family.service.AdminService;
import top.yll.familytree.family.utils.FamilyFileUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 查询家谱
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/find")
    public String find(Model model) throws IOException {
        List<Person> list = adminService.find();
        List<String> birthday = adminService.getBirthdayInfo();
        model.addAttribute("list", list);
        model.addAttribute("birthday",birthday);
        return "family";
    }

    /**
     * 按照生日排序
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/order")
    public String order(Model model) throws IOException {
        List<Person> list = adminService.orderListByBirthday();
        List<String> birthday = adminService.getBirthdayInfo();
        model.addAttribute("list", list);
        model.addAttribute("birthday",birthday);
        return "order";
    }

    /**
     * 添加成员
     * @param person
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            produces = "text/plain;charset=utf-8")
    public String add(Person person) throws IOException {
        adminService.add(person);
        return "添加成功";
    }


    /**
     * 根据id删除person，会同时删除配偶，孩子，并修改其父母的孩子集合
     * @param id
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/delete",
            method = RequestMethod.GET,
            produces = "text/plain;charset=utf-8")
    public String delete(int id) throws IOException {
        return adminService.deleteOneById(id);
    }

    /**
     * 清空json文件
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/clear",
            method = RequestMethod.GET,
            produces = "text/plain;charset=utf-8")
    public String clear() throws IOException {
        boolean j = adminService.clear();
        if (!j) return "没有数据可以清除啦(￣▽￣)";
        return "清空成功";
    }

    /**
     * 修改person信息
     * @param person
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/modify")
    public String modify(Person person) throws IOException {
        boolean j = adminService.modify(person);
        if (j) return "修改成功";
        else return "信息有误，再检查一下吧(；′⌒`)";
    }

    /**
     * 根据姓名获取信息
     * @param name
     * @throws IOException
     */
    @RequestMapping("/searchByName")
    public String searchByName(String name,Model model) throws IOException {
        Person person = adminService.getPersonByName(name);
        if (person==null){
            System.out.println("查无此人");
            return "error1";
        }else {
            List<Person> info = adminService.getInfoByPerson(person);
            String rank =adminService.getRankByName(name);
            model.addAttribute("rank",rank);
            model.addAttribute("info",info);
            return "result";
        }
    }

    /**
     * 根据出生日期获取信息
     * @param birthday
     * @throws IOException
     */
    @RequestMapping("/searchByBirthday")
    public String searchByBirthday(String birthday,Model model) throws IOException {
        Person person = adminService.getPersonByBirthday(birthday);
        if (person==null){
            System.out.println("查无此人");
            return "error1";
        }else {
            List<Person> info = adminService.getInfoByPerson(person);
            String rank =adminService.getRankByBirthday(birthday);
            model.addAttribute("rank",rank);
            model.addAttribute("info",info);
            return "result";
        }
    }


    /**
     * 根据两个人的姓名查询其关系
     * @param name1
     * @param name2
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/getRelationshipByTwoName")
    public String getRelationshipByTwoName(String name1, String name2) throws IOException {
        return adminService.findRelationship(name1,name2);
    }

    /**
     * 获取图形化家谱
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/tree")
    public String tree(Model model) throws IOException {
        Relationship relationship = adminService.getFamilyTree();
        String json1 = JSONArray.toJSONString(relationship);
        String json = StringEscapeUtils.unescapeJson(json1);

        model.addAttribute("json",json);
        return "familyTree";
    }


    /**
     * 获取json文件  没用到
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/getJson")
    public String getJson() throws IOException {
        List list = FamilyFileUtils.getFamily();
        String json = JSONArray.toJSONString(list);
        System.out.println(json);
        return JSONArray.toJSONString(list);
    }
}
