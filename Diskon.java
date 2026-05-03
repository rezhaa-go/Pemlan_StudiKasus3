package Studi_Kasus3;

import java.util.Date;

public class Diskon extends Promo {
    public Diskon(String kode, Date tanggalMulai, Date tanggalSelesai, double diskon, long maksPotongan, long minPembelian) {
        super(kode, tanggalMulai, tanggalSelesai, diskon, maksPotongan, minPembelian);
    }
}
