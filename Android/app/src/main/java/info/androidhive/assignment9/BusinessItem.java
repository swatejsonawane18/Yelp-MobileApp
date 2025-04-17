package info.androidhive.assignment9;

public class BusinessItem {
    private String imageurl;
    private String business_name;
    private String business_rating;
    private String business_distance;
    private String index;
    private String business_id;

    public BusinessItem(String index, String imageurl, String business_name, String business_rating,String business_distance, String business_id){
        this.index=index;
        this.imageurl=imageurl;
        this.business_name=business_name;
        this.business_rating=business_rating;
        this.business_distance=business_distance;
        this.business_id=business_id;
    }

    public String getIndex(){
        return index;
    }
    public String getImageurl(){
        return imageurl;
    }
    public String getBusiness_name(){
        return business_name;
    }
    public String getBusiness_rating(){
        return business_rating;
    }
    public String getBusiness_distance(){
        return business_distance;
    }
    public String getBusiness_id(){ return business_id; }
}
