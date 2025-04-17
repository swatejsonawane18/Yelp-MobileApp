package info.androidhive.assignment9;

public class ModelClass {
    private String name;
    private String date;
    private String time;
    private String email;

    public ModelClass(String name, String email, String date, String time)
    {
        this.name=name;
        this.date=date;
        this.time=time;
        this.email=email;
    }

    public String getName(){ return name;}
    public String getDate(){ return date;}
    public String getTime() { return time;}
    public String getEmail() { return email;}

    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return name;
    }
}
