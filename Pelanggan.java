package Studi_Kasus3;

import java.util.*;

public abstract class Pelanggan {
    protected String id;
    protected String nama;
    protected long saldo;
    protected List<ItemKeranjang> keranjang = new ArrayList<>();
    protected List<Pesanan> riwayatPesanan = new ArrayList<>();
    protected Promo promoAktif = null;

    public Pelanggan(String id, String nama, long saldo) {
        this.id = id;
        this.nama = nama;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }

    public List<ItemKeranjang> getKeranjang() {
        return keranjang;
    }

    public List<Pesanan> getRiwayatPesanan() {
        return riwayatPesanan;
    }

    public Promo getPromoAktif() {
        return promoAktif;
    }

    public void setPromoAktif(Promo promo) {
        this.promoAktif = promo;
    }

    public void tampilkanHistory() {
    System.out.println("Kode Pemesan: " + this.id);
    System.out.println("Nama: " + this.nama);
    System.out.println("Saldo: " + this.saldo); 
    
    System.out.printf("  %2s| %-13s | %5s | %5s |%9s | %-10s \n", "No", "Nomor Pesanan", "Motor", "Mobil", "Subtotal", "PROMO");
    System.out.println("=======================================================");
    
    int i = 1;
    for (Pesanan p : riwayatPesanan) {
        int jmlMotor = 0;
        int jmlMobil = 0;
        for (ItemKeranjang item : p.getItems()) {
            if (item.getMenu() instanceof Motor) jmlMotor += 1;
            else if (item.getMenu() instanceof Mobil) jmlMobil += 1;
        }
        
        String namaPromo = (p.getPromo() != null) ? p.getPromo().getKode() : "-";
        System.out.printf("  %2d| %13d | %5d | %5d |%9d | %-10s \n", 
            i, p.getNoPesanan(), jmlMotor, jmlMobil, p.getSubTotal(), namaPromo);
        i++;
    }
    System.out.println("=======================================================");
}
}
