package Studi_Kasus3;

import java.util.*;

public abstract class Menu {
    protected String id;
    protected String nama;
    protected String plat;
    protected long harga;

    public Menu(String id, String nama, String plat, long harga) {
        this.id = id;
        this.nama = nama;
        this.plat = plat;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getPlat() {
        return plat;
    }

    public long getHarga() {
        return harga;
    }
}
