package info.androidhive.bottomnavigation;

public class messages {

    private String From,To,Timestamp,Title,Message;

    public  messages(){

    }

    public messages(String From,String To,String Timestamp,String Title,String Message)
    {
        this.From=From;
        this.Timestamp=Timestamp;
        this.To=To;
        this.Message=Message;
        this.Title=Title;
    }

    public String getFrom() {
        return From;
    }

    public String getMessage() {
        return Message;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getTitle() {
        return Title;
    }

    public String getTo() {
        return To;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setTimestamp(String timestamp) {
        this.Timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public void setTo(String to) {
        this.To = to;
    }
}
