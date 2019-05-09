package dang.program.id.dangapps.menu_history;

public class ModelMenuHistory {
    private String nama_restoran;
    private String nama_daerah;
    private String pesanan;
    private String total_harga;
    private String date;
    private String time;

    public ModelMenuHistory(String nama_restoran, String nama_daerah, String pesanan, String total_harga, String date, String time) {
        this.nama_restoran = nama_restoran;
        this.nama_daerah = nama_daerah;
        this.pesanan = pesanan;
        this.total_harga = total_harga;
        this.date = date;
        this.time = time;
    }

    ModelMenuHistory(){}

    public String getNama_restoran() {
        return nama_restoran;
    }

    public void setNama_restoran(String nama_restoran) {
        this.nama_restoran = nama_restoran;
    }

    public String getNama_daerah() {
        return nama_daerah;
    }

    public void setNama_daerah(String nama_daerah) {
        this.nama_daerah = nama_daerah;
    }

    public String getPesanan() {
        return pesanan;
    }

    public void setPesanan(String pesanan) {
        this.pesanan = pesanan;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
