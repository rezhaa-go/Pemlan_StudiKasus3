package Studi_Kasus3;

import java.util.Date;

public class Cashback extends Promo {
    public Cashback(String kode, Date tanggalMulai, Date tanggalSelesai, double diskon, long maksPotongan, long minPembelian) {
        super(kode, tanggalMulai, tanggalSelesai, diskon, maksPotongan, minPembelian);
    }
}
