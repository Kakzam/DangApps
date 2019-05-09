package dang.program.id.dangapps.menu_booking.menu_place_resto;

public class ModelPlaceCity {
    private String img_resto_city;
    private String name_resto_place;
    private String status_resto_place;
    private String address_resto_place;
    private String time_resto_place;

    public ModelPlaceCity(String img_resto_city, String name_resto_place, String status_resto_place, String address_resto_place, String time_resto_place) {
        this.img_resto_city = img_resto_city;
        this.name_resto_place = name_resto_place;
        this.status_resto_place = status_resto_place;
        this.address_resto_place = address_resto_place;
        this.time_resto_place = time_resto_place;
    }

    public ModelPlaceCity() {}

    public String getImg_resto_city() {
        return img_resto_city;
    }

    public void setImg_resto_city(String img_resto_city) {
        this.img_resto_city = img_resto_city;
    }

    public String getName_resto_place() {
        return name_resto_place;
    }

    public void setName_resto_place(String name_resto_place) {
        this.name_resto_place = name_resto_place;
    }

    public String getStatus_resto_place() {
        return status_resto_place;
    }

    public void setStatus_resto_place(String status_resto_place) {
        this.status_resto_place = status_resto_place;
    }

    public String getAddress_resto_place() {
        return address_resto_place;
    }

    public void setAddress_resto_place(String address_resto_place) {
        this.address_resto_place = address_resto_place;
    }

    public String getTime_resto_place() {
        return time_resto_place;
    }

    public void setTime_resto_place(String time_resto_place) {
        this.time_resto_place = time_resto_place;
    }
}
