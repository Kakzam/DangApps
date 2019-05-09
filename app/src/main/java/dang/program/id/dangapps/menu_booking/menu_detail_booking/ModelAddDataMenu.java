package dang.program.id.dangapps.menu_booking.menu_detail_booking;

public class ModelAddDataMenu {
    String menu_title;
    String menu_price;
    String img_resto_item;

    public String getImg_resto_item() {
        return img_resto_item;
    }

    public void setImg_resto_item(String img_resto_item) {
        this.img_resto_item = img_resto_item;
    }

    public ModelAddDataMenu(String menu_title, String menu_price, String img_resto_item) {
        this.menu_title = menu_title;
        this.menu_price = menu_price;
        this.img_resto_item = img_resto_item;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }
}
