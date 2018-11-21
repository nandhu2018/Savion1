package savion.tns.com.savion;

/**
 * Created by DELL on 22-Aug-18.
 */

public class tickets {
    private String name,mobile,place,servicetype,date,time,vehicleno,vehicletype,remarks;

    public tickets(String name,String mobile,String place,String servicetype,String date,String time,String vehicleno,String vehicletype,String remarks){
        this.name=name;
        this.place=place;
        this.mobile=mobile;
        this.servicetype=servicetype;
        this.date=date;
        this.time=time;
        this.vehicleno=vehicleno;
        this.vehicletype=vehicletype;
        this.remarks=remarks;

    }

    public tickets() {

    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
