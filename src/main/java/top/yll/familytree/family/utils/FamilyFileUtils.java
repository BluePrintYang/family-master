package top.yll.familytree.family.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import top.yll.familytree.family.pojo.Person;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FamilyFileUtils {

    /**
     * 获取家谱json文件
     *
     * @return List<Person>
     * @throws IOException
     */
    public static List<Person> getFamily() throws IOException {
        String family = FileUtils.readFileToString(new File("D:\\p.json"));
        return JSON.parseArray(family, Person.class);
    }

    /**
     * 根据传入id返回当前id的person对象
     *
     * @param id
     * @return
     * @throws IOException
     */
    public static Person getPersonById(int id) throws IOException {
        //获取家谱
        List<Person> list = getFamily();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getId() == id) {
                //返回id为传入id的person对象
                return list.get(i);
            }
            //没有则返回空
            if (i == size - 1 && list.get(i).getId() != id) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将list集合（家谱）写回文件中
     *
     * @param list
     * @throws IOException
     */
    public static void writeBack(List<Person> list) throws IOException {
        String json = JSON.toJSONString(list);
        //重新写回到json文件
        FileUtils.writeStringToFile(new File("D:\\p.json"), json);
    }


    /**
     * 传入id，根据id删除成员，在其父亲母亲孩子集合中删除其信息，并修改json文件
     *
     * @param id
     */
    public static void deleteOnlyOne(int id) throws IOException {
        //获取索引
        int index = getPersonIndexById(id);
        if (index == 0) {
            List<Person> list = getFamily();
            list.remove(0);
            FamilyFileUtils.writeBack(list);
        } else if (index > 0) {
            cancelRelationship(id);
            List<Person> list = getFamily();
            list.remove(index);
            FamilyFileUtils.writeBack(list);
        }
    }

    /**
     * 传入person的id，找到其父母，在其父母的孩子list中去除此人的id，有修改文件的操作
     *
     * @param id
     * @throws IOException
     */
    private static void cancelRelationship(int id) throws IOException {
        List<Person> list = getFamily();
        //获取此人在list集合中的索引
        int index = getPersonIndexById(id);
        //获取其父亲的id
        Integer father = list.get(index).getFatherId();
        if (father != -1) {
            //获取父亲的索引
            Integer faIndex = getPersonIndexById(father);
            //找到父亲的孩子
            List<Integer> childrenList = list.get(faIndex).getChildrenId();
            //创建新的孩子list，删除了传入的id
            List<Integer> newChildrenList = new ArrayList<>();
            for (Integer integer : childrenList) {
                if (integer != id) newChildrenList.add(integer);
            }
            for (Person person : list) {
                //在家谱中找到此人的父母，修改其childrenId集合
                if (person.getChildrenId().equals(childrenList)) person.setChildrenId(newChildrenList);
            }
            writeBack(list);
        }

    }

    /**
     * 传入person的id，返回该person在list集合中的索引
     *
     * @param id
     * @return
     * @throws IOException
     */
    private static Integer getPersonIndexById(int id) throws IOException {
        //获取家谱
        List<Person> list = getFamily();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 传入person的name，返回该person在list集合中的索引
     *
     * @param name
     * @return
     * @throws IOException
     */
    private static Integer getPersonIndexByName(String name) throws IOException {
        //获取家谱
        List<Person> list = getFamily();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getName().equals(name)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 传入person的name，返回该person的id
     *
     * @param name
     * @return
     * @throws IOException
     */
    private static Integer getPersonIdByName(String name) throws IOException {
        List<Person> list = getFamily();
        Integer index = getPersonIndexByName(name);
        if (index != null) {
            return list.get(index).getId();
        } else return null;
    }

    /**
     * 传入person的name，返回该person对象
     *
     * @param name
     * @return
     * @throws IOException
     */
    public static Person getPersonByName(String name) throws IOException {
        Integer id = getPersonIdByName(name);
        if (id == null) {
            System.out.println("此人不存在");
            return null;
        } else return getPersonById(id);
    }

    /**
     * 传入person的birthday，返回该person在list集合中的索引
     *
     * @param birthday
     * @return
     * @throws IOException
     */
    private static Integer getPersonIndexByBirthday(String birthday) throws IOException {
        //获取家谱
        List<Person> list = getFamily();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getBirthday().equals(birthday)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 传入person的birthday，返回该person的id
     *
     * @param birthday
     * @return
     * @throws IOException
     */
    private static Integer getPersonIdByBirthday(String birthday) throws IOException {
        List<Person> list = getFamily();
        Integer index = getPersonIndexByBirthday(birthday);
        if (index != null) {
            return list.get(index).getId();
        } else return null;
    }

    /**
     * 传入person的birthday，返回该person对象
     *
     * @param birthday
     * @return
     * @throws IOException
     */
    public static Person getPersonByBirthday(String birthday) throws IOException {
        Integer id = getPersonIdByBirthday(birthday);
        if (id == null) {
            System.out.println("此人不存在");
            return null;
        } else return getPersonById(id);
    }

    /**
     * 传入person对象，给其父母添加此孩子
     *
     * @param person
     */
    public static void addRelationship(Person person) throws IOException {
        List<Person> list = getFamily();
        Integer fatherId = person.getFatherId();
        //有父亲才添加关系
        if (fatherId != -1) {
            Person father = getPersonById(fatherId);
            List<Integer> childrenList = father.getChildrenId();
            List<Integer> newChildrenList = new ArrayList<>(childrenList);
            newChildrenList.add(person.getId());
            if (childrenList.size() != 0 || childrenList == null) {
                for (Person p : list) {
                    //在家谱中找到此人的父母，修改其childrenId集合
                    if (p.getChildrenId().equals(childrenList)) p.setChildrenId(newChildrenList);
                }
            } else {
                for (Person p : list) {
                    if (person.getFatherId().equals(p.getId())) p.setChildrenId(newChildrenList);
                    if (person.getFatherId().equals(p.getSpouseId())) p.setChildrenId(newChildrenList);
                }
            }
            writeBack(list);
        }
    }

    /**
     * 获取老祖宗
     *
     * @param person
     * @return
     * @throws IOException
     */
    public static Person getOldFather(Person person) throws IOException {
        Person father = person;
        while (father.getFatherId() != -1) {
            father = getPersonById(father.getFatherId());
        }
        return father;
    }

    /**
     * 传入两个person对象，判断其是否具有父子或母子关系
     *
     * @param person1
     * @param person2
     * @return
     */
    public static int getNearestRelationship(Person person1, Person person2) throws IOException {
        if (person1 == null) return -1;
        if (person2 == null) return -1;
        Integer person1Id = person1.getId();
        Integer person2Id = person2.getId();
        Integer person1Father = person1.getFatherId();
        Integer person2Father = person2.getFatherId();
        List<Integer> childrenList1 = person1.getChildrenId();
        List<Integer> childrenList2 = person2.getChildrenId();
        if (childrenList1 != null)
            for (Integer integer : childrenList1) {
                if (integer.equals(person2Id))
                    //1是男的，2是1的孩子，返回1:1是2的父亲
                    if (person1.getGender().equals("男")) return 1;
                        //1是女的，2是1的孩子，返回2:1是2的母亲
                    else if (person1.getGender().equals("女")) return 2;
            }
        if (childrenList2 != null)
            for (Integer integer : childrenList2) {
                if (integer.equals(person1Id))
                    //2是男的，1是2的孩子，返回3:2是1的父亲
                    if (person2.getGender().equals("男")) return 3;
                        //2是女的，1是2的孩子，返回4:2是1的母亲
                    else if (person2.getGender().equals("女")) return 4;
            }
        if (person1Father != -1 && person2Father != -1 && person1Father.equals(person2Father))
            //返回5,他们有共同的父亲
            return 5;
        if (getOldFather(person1).getId().equals(getOldFather(person2).getId()))
            //返回6,他们有共同的老祖宗
            return 6;
        return -1;
    }
}
