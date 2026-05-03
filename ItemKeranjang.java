package Studi_Kasus3;

import java.util.*;

public class ItemKeranjang implements Comparable<ItemKeranjang> {
    private Menu menu;
    private int jumlah;
    private Date tanggalMulai;

    public ItemKeranjang (Menu menu, int jumlah, Date tanggalMulai){
        this.menu = menu;
        this.jumlah = jumlah;
        this.tanggalMulai = tanggalMulai;
    }

    public Menu getMenu(){
        return menu;
    }

    public int getJumlah(){
        return jumlah;
    }

    public Date getTanggalMulai(){
        return tanggalMulai;
    }

    public long getSubtotal(){
        return menu.getHarga() * jumlah;
    }

    @Override
    public int compareTo (ItemKeranjang object){
        int tanggalComp = this.tanggalMulai.compareTo(object.tanggalMulai);
        if (tanggalComp != 0){
            return tanggalComp;
        }
        return Long.compare(this.getSubtotal(), object.getSubtotal());
    }

    void setJumlah(int i) {
        this.jumlah = i;
    }
}
