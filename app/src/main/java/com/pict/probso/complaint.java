package info.androidhive.bottomnavigation;

public class complaint {

    private String desc,image,title,uid,Dept,total_votes;

    public complaint() {
    }

    public complaint(String desc, String image, String title,String uid,String Dept,String vote) {
        this.desc = desc;
        this.image = image;
        this.title = title;
        this.uid = uid;
        this.Dept = Dept;
        this.total_votes = vote;
    }

    public String getDept() {
        return Dept;
    }

    public String getUid() {
        return uid;
    }

    public String getDesc() {

        return desc;
    }

    public void setDept(String dept) {
        this.Dept = dept;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVotes() {
        return total_votes;
    }

    public void setVotes(String total_votes) {
        this.total_votes = total_votes;
    }



}