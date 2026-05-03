package Studi_Kasus3;

import java.util.Date;

public abstract class Promo {
    protected String kode;
    protected Date tanggalMulai, tanggalSelesai;
    protected double diskon;
    protected long maksPotongan, minPembelian;

    public Promo(String kode, Date tanggalMulai, Date tanggalSelesai, double diskon, long maksPotongan, long minPembelian) {
        this.kode = kode;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.diskon = diskon;
        this.maksPotongan = maksPotongan;
        this.minPembelian = minPembelian;
    }

    public String getKode() {
        return kode;
    }

    public double getDiskon() {
        return diskon;
    }

    public long getMaksPotongan() {
        return maksPotongan;
    }

    public long getMinPembelian() {
        return minPembelian;
    }

    public Date getTanggalMulai(){
        return tanggalMulai;
    }

    public Date getTanggalSelesai(){
        return tanggalSelesai;
    }
}
