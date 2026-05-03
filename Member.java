package Studi_Kasus3;

import java.util.*;

public class Member extends Pelanggan{
    private Date tanggalDaftar;
    public Member(String id, String nama, long saldo, Date tanggalDaftar) {
        super(id, nama, saldo);
        this.tanggalDaftar = tanggalDaftar;
    }

    public Date getTanggalDaftar() {
        return tanggalDaftar;
    }
}
