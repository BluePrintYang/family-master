package top.yll.familytree.family.pojo;


import java.util.List;

public class Person {
    private Integer id; //角色的id
    private String name;  //姓名
    private String gender;  //性别
    private String birthday;  //生日
    private boolean marriage;  //是否已婚
    private String address;  //地址
    private boolean alive;  //是否健在
    private String death;  //死亡日期

    private Integer fatherId;  //父亲的id
    private Integer spouseId;  //配偶的id
    private List<Integer> childrenId; //孩子的id


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", marriage=" + marriage +
                ", address='" + address + '\'' +
                ", alive=" + alive +
                ", death='" + death + '\'' +
                ", fatherId=" + fatherId +
                ", spouseId='" + spouseId + '\'' +
                ", childrenId=" + childrenId +
                '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isMarriage() {
        return marriage;
    }

    public void setMarriage(boolean marriage) {
        this.marriage = marriage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(Integer spouseId) {
        this.spouseId = spouseId;
    }

    public List<Integer> getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(List<Integer> childrenId) {
        this.childrenId = childrenId;
    }
}
