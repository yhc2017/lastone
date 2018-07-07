package lastone.com.echo.utils;

import org.litepal.crud.LitePalSupport;

/**
 * 项目名称：LastOne
 * 类描述：用户信息库
 * 创建人：zhou-jx
 * 创建时间：2018/7/7 9:49
 * 修改人：zhou-jx
 * 修改时间：2018/7/7 9:49
 * 修改备注：
 */

public class User extends LitePalSupport{

    private int id;
    private int user_id;
    private String nickname;
    private String pwd;
    private String send_grade;
    private String receive_grade;
    private String sex;
    private String icon;
    private String introduce;
    private String address;
    private String other_address;
    private String tel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSend_grade() {
        return send_grade;
    }

    public void setSend_grade(String send_grade) {
        this.send_grade = send_grade;
    }

    public String getReceive_grade() {
        return receive_grade;
    }

    public void setReceive_grade(String receive_grade) {
        this.receive_grade = receive_grade;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOther_address() {
        return other_address;
    }

    public void setOther_address(String other_address) {
        this.other_address = other_address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
