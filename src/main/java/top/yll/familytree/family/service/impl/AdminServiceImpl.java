package top.yll.familytree.family.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import top.yll.familytree.family.pojo.Person;
import top.yll.familytree.family.pojo.Relationship;
import top.yll.familytree.family.service.AdminService;
import top.yll.familytree.family.utils.FamilyFileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public List<Person> find() throws IOException {
        return FamilyFileUtils.getFamily();
    }

    @Override
    public void add(Person person) throws IOException {
        List<Person> list = FamilyFileUtils.getFamily();
        //如果list为空，重新分配空间,并设置personId为1
        if (list == null) {
            list = new ArrayList<>();
            person.setId(1);
        }
        int maxId = 0;
        //获取最大id
        for (Person p : list) {
            if (p.getId() > maxId) maxId = p.getId();
        }
        person.setId(maxId + 1);
        //没有孩子但为其分配孩子空间
        if (person.getChildrenId()==null)person.setChildrenId(new ArrayList<>());
        if (person.getFatherId()==null)person.setFatherId(-1);
        if (person.getSpouseId()==null)person.setSpouseId(-1);
        //添加person
        list.add(person);
        //写回
        FamilyFileUtils.writeBack(list);
        //给其父母添加此孩子
        FamilyFileUtils.addRelationship(person);
    }

    @Override
    public String deleteOneById(int id) throws IOException {
        //获取person对象
        Person person = FamilyFileUtils.getPersonById(id);
        if (person != null) {
            //获取配偶
            Integer spouse = person.getSpouseId();
            //获取孩子id的集合
            List<Integer> childrenList = person.getChildrenId();
            //判断是否有配偶，若有，一并删除
            if (spouse != null) {
                FamilyFileUtils.deleteOnlyOne(spouse);
            }
            //判断是否有孩子，若有，一并删除
            if (childrenList != null && childrenList.size() != 0) {
                //删除此人
                FamilyFileUtils.deleteOnlyOne(id);
                //删除孩子，删除的次数等于孩子的个数
                for (Integer integer : childrenList) {
                    //递归删除
                    deleteOneById(integer);
                }
                return "你已经删除此人和其配偶，孩子,以及所有后代孩子";

            }

            //无孩子的删除
            FamilyFileUtils.deleteOnlyOne(id);
            return "没有孩子，删除成功";
        }

        return "此人不存在";
    }

    @Override
    public boolean clear() throws IOException {
        //获取家谱
        List<Person> list = FamilyFileUtils.getFamily();
        //为空返回错误
        if (list.isEmpty()) return false;
        //清空家谱集合
        list.clear();
        //写回p.json文件
        FamilyFileUtils.writeBack(list);
        return true;
    }

    @Override
    public boolean modify(Person person) throws IOException {
        List<Person> list = FamilyFileUtils.getFamily();
        for (Person value : list) {
            if (value.getId().equals(person.getId())) {
                if (person.getName()!=null)
                value.setName(person.getName());
                if (person.getAddress()!=null)
                value.setAddress(person.getAddress());
                value.setAlive(person.isAlive());
                if (person.getDeath()!=null)
                value.setDeath(person.getDeath());
                if (person.getBirthday()!=null)
                value.setBirthday(person.getBirthday());
            }
        }
        FamilyFileUtils.writeBack(list);
        return true;
    }

    @Override
    public String getRankByName(String name) throws IOException {
        int rank = 0;
        Integer father ;
        Person person = FamilyFileUtils.getPersonByName(name);
        if (person != null) {
            do {
                rank++;
                father = person.getFatherId();
                if (father != -1) {
                    person = FamilyFileUtils.getPersonById(father);
                }
            } while (father != -1);
            return "第" + rank + "代";
        }
        return null;
    }

    @Override
    public String getRankByBirthday(String birthday) throws IOException {
        int rank = 0;
        Integer father;
        Person person = FamilyFileUtils.getPersonByBirthday(birthday);
        if (person != null) {
            do {
                rank++;
                father = person.getFatherId();
                if (father != -1) {
                    person = FamilyFileUtils.getPersonById(father);
                }
            } while (father != -1);
            return "第" + rank + "代";
        }
        return null;
    }

    @Override
    public Person getPersonByName(String name) throws IOException {
        return FamilyFileUtils.getPersonByName(name);
    }

    @Override
    public Person getPersonByBirthday(String birthday) throws IOException {
       return FamilyFileUtils.getPersonByBirthday(birthday);
    }

    @Override
    public List<Person> getInfoByPerson(Person person) throws IOException {
        if (person == null) {
            System.out.println("查无此人");
            return null;
        }
        List<Person> info = new ArrayList<>();
        info.add(person);
        //获取父亲id
        Integer fatherId = person.getFatherId();
        //获取孩子
        List<Integer> childrenList = person.getChildrenId();
        if (fatherId != -1) {
            Person father = FamilyFileUtils.getPersonById(fatherId);
            info.add(father);
        }
        if (childrenList != null) {
            for (Integer integer : childrenList) {
                Person children = FamilyFileUtils.getPersonById(integer);
                info.add(children);
            }
        }
        return info;
    }

    @Override
    public List<String> getBirthdayInfo() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Date date = new Date();
        String today = sdf.format(date);
        List<Person> list = FamilyFileUtils.getFamily();
        List<String> birthdayList = new ArrayList<>();
        for (Person person : list) {
            String birth = person.getBirthday();
            String[] splitBirthday =birth.split("-");
            String birthday = splitBirthday[1]+"-"+splitBirthday[2];
            if (birthday.equals(today) && person.isAlive()) {
                birthdayList.add(person.getName());
            }
        }
        return birthdayList;
    }

    @Override
    public List<Person> orderListByBirthday() throws IOException {
        List<Person> list = FamilyFileUtils.getFamily();
        List<Person> orderedList =new ArrayList<>(list);
        orderedList.sort((o1, o2) -> {
            if (o1.getBirthday().compareTo(o2.getBirthday()) > 0) {
                return 1;
            } else if (o1.getBirthday().compareTo(o2.getBirthday()) < 0) {
                return -1;
            }
            return 0 ;
        });
        return orderedList;
    }

    @Override
    public String findRelationship(String name1, String name2) throws IOException {
        Person person1 = FamilyFileUtils.getPersonByName(name1);
        Person person2 = FamilyFileUtils.getPersonByName(name2);
        if (person1==null)return "家谱中不存在"+name1+"这个人呀！";
        if (person2==null)return "家谱中不存在"+name2+"这个人呀！";
        if (name1.equals(name2))return "你输入的是同一个人呀！";
        int result = FamilyFileUtils.getNearestRelationship(person1,person2);
        switch (result){
            case 1:return person1.getName()+"是"+person2.getName()+"的父亲";
            case 2:return person1.getName()+"是"+person2.getName()+"的母亲";
            case 3:return person2.getName()+"是"+person1.getName()+"的父亲";
            case 4:return person2.getName()+"是"+person1.getName()+"的母亲";
            case 5:return person1.getName()+"和"+person2.getName()+"有相同的父亲";
            case 6:return person1.getName()+"和"+person2.getName()+"有相同祖先"+FamilyFileUtils.getOldFather(person1).getName();
            case -1:return "他们不属于同一宗族或关系太远查不到啦orz";
        }
        return null;
    }

    @Override
    public Relationship getFamilyTree() throws IOException {
        List<Person> list = FamilyFileUtils.getFamily();
        //创建结点
        List<String> nodes = new ArrayList<>();
        //创建边
        List<String> edges = new ArrayList<>();
        //添加结点
        for (Person person : list) {
            nodes.add(person.getName());
        }
        //添加边
        for (Person person : list) {
            if (person.getFatherId() != -1) {
                Person father = FamilyFileUtils.getPersonById(person.getFatherId());
                edges.add("[\"" + father.getName() + "\",\"" + person.getName() + "\"]");
            }
            if (person.getSpouseId() != -1) {
                Person spouse = FamilyFileUtils.getPersonById(person.getSpouseId());
                edges.add("[\"" + spouse.getName() + "\",\"" + person.getName() + "\"]");
            }
        }
        Relationship relationship = new Relationship();
        relationship.setNodes(nodes);
        relationship.setEdges(edges);
        String rel = JSONArray.toJSONString(relationship);
        String newRel = StringEscapeUtils.unescapeJson(rel);
        FileUtils.writeStringToFile(new File("D:\\relationship.json"), newRel);
        return relationship;
    }
}
