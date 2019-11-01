package top.yll.familytree.family.service;

import top.yll.familytree.family.pojo.Person;
import top.yll.familytree.family.pojo.Relationship;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    /**
     * 查询家谱，返回list集合，装有所有成员信息
     * @return
     */
    List<Person> find() throws IOException;

    /**
     * 添加成员
     * @param person
     */
    void add(Person person) throws IOException;

    /**
     * 根据id删除成员
     * @param id
     * @return
     * @throws IOException
     */
    String deleteOneById(int id) throws IOException;

    /**
     * 清空家谱
     * @return
     * @throws IOException
     */
    boolean clear() throws IOException;

    /**
     * 修改成员信息
     * @param person
     * @return
     * @throws IOException
     */
    boolean modify(Person person) throws IOException;

    /**
     * 根据姓名查询，返回person对象
     * @param name
     * @return
     */
    Person getPersonByName(String name) throws IOException;

    /**
     * 根据出生日期查询，返回person对象
     * @param birthday
     * @return
     */
    Person getPersonByBirthday(String birthday) throws IOException;

    /**
     * 传入person，返回其本人，父亲以及孩子的信息
     * @param person
     * @return
     */
    List<Person> getInfoByPerson(Person person) throws IOException;

    /**
     * 根据姓名查询辈分
     * @param name
     * @return
     */
    String getRankByName(String name) throws IOException;

    /**
     * 根据生日查询辈分
     * @param birthday
     * @return
     */
    String getRankByBirthday(String birthday) throws IOException;

    /**
     * 提示当天生日的健在成员
     * @return
     */
    List<String> getBirthdayInfo() throws IOException;

    /**
     * 按出生日期排序
     * @return
     */
    List<Person> orderListByBirthday() throws IOException;

    /**
     * 根据输入的两个姓名查询其血缘关系
     * @param name1
     * @param name2
     * @return
     */
    String findRelationship(String name1,String name2) throws IOException;

    /**
     * 查询图形化家谱
     * @return
     */
    Relationship getFamilyTree() throws IOException;
}
