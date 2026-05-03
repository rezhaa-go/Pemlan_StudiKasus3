package Studi_Kasus3;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class Pesanan {
    private int noPesanan;
    private Date tanggalPesanan;
    private List<ItemKeranjang> items;
    private Promo promo;
    private long subTotal;
    private long jumlahAkhir;

    public Pesanan (int noPesanan, Date tanggalPesanan, List<ItemKeranjang> items, Promo promo, long subTotal, long jumlahAkhir){
        this.noPesanan = noPesanan;
        this.tanggalPesanan = tanggalPesanan;
        this.items = new ArrayList<>(items);
        this.promo = promo;
        this.subTotal = subTotal;
        this.jumlahAkhir = jumlahAkhir;
    }

    public int getNoPesanan(){
        return noPesanan;
    }

    public long getSubTotal(){
        return subTotal;
    }

    public Promo getPromo(){
        return promo;
    }

    public List<ItemKeranjang> getItems(){
        return items;
    }

    public void tampilkanKeranjang(Pelanggan p) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY); 
    symbols.setGroupingSeparator('.');
    DecimalFormat formatter = new DecimalFormat("#,###", symbols);
    SimpleDateFormat sdfSewa = new SimpleDateFormat("yyyy/MM/dd");
    
    System.out.println("Kode Pemesan: " + p.getId());
    System.out.println("Nama: " + p.getNama());

    System.out.printf(" %-2s | %-25s | %3s |   %3s \n", "No", "Menu", "Dur.", "Subtotal");
    System.out.println("===================================================");
    
    int i = 1;
    for (ItemKeranjang item : items) {
        String infoMenu = item.getMenu().getNama() + " " + item.getMenu().getPlat();
        if (infoMenu.length() > 25) infoMenu = infoMenu.substring(0, 22) + "...";
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.getTanggalMulai());
        Date tglMulai = cal.getTime();
        cal.add(Calendar.DATE, item.getJumlah()); 
        Date tglSelesai = cal.getTime();
        
        String periode = sdfSewa.format(tglMulai) + " - " + sdfSewa.format(tglSelesai);
        System.out.printf(" %-2d | %-25s | %4d |   %3s \n", i, infoMenu, item.getJumlah(), formatter.format(item.getSubtotal()));
        System.out.printf(" %-2s | %-25s | %4s |   %3s \n", "", periode, "", "");
        i++;
    }

    System.out.println("===================================================");
    System.out.printf("%-32s: %16s\n", "Sub Total", formatter.format(this.subTotal));

    Promo promoAktif = p.getPromoAktif();
    long nilaiPotongan = 0;

    if (promoAktif instanceof Diskon) {
        nilaiPotongan = Math.min((long)(this.subTotal * promoAktif.getDiskon()), promoAktif.getMaksPotongan());
        System.out.printf("%-32s: %16s\n", "PROMO: " + promoAktif.getKode(), "-" + formatter.format(nilaiPotongan));
    }

    System.out.println("===================================================");
    
    long totalTampil = (promoAktif instanceof Diskon) ? (this.subTotal - nilaiPotongan) : this.subTotal;
    System.out.printf("%-32s: %16s\n", "Total", formatter.format(totalTampil));

    if (promoAktif instanceof Cashback) {
        long cb = Math.min((long)(this.subTotal * promoAktif.getDiskon()), promoAktif.getMaksPotongan());
        System.out.printf("%-32s: %16s\n", "PROMO: " + promoAktif.getKode(), formatter.format(cb));
    }

    System.out.printf("%-32s: %16s\n", "Saldo", formatter.format(p.getSaldo()));
}

    public void tampilkanPesanan(Pelanggan p) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY); 
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        SimpleDateFormat sdfPrint = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
        SimpleDateFormat sdfSewa = new SimpleDateFormat("yyyy/MM/dd");
        
        System.out.println("Kode Pemesan: " + p.getId());
        System.out.println("Nama: " + p.getNama());
        System.out.println("Nomor Pesanan: " + this.noPesanan);
        System.out.println("Tanggal Pesanan: " + sdfPrint.format(this.tanggalPesanan));

        System.out.printf(" %-2s | %-25s | %3s |  %3s \n", "No", "Menu", "Dur.", "Subtotal");
        System.out.println("===================================================");
        
        int i = 1;
        for (ItemKeranjang item : items) {
            String infoMenu = item.getMenu().getNama() + " " + item.getMenu().getPlat();
            if (infoMenu.length() > 25) infoMenu = infoMenu.substring(0, 22) + "...";
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getTanggalMulai());
            Date tglMulai = cal.getTime();
            cal.add(Calendar.DATE, item.getJumlah()); 
            Date tglSelesai = cal.getTime();
            
            String periode = sdfSewa.format(tglMulai) + " - " + sdfSewa.format(tglSelesai);
            String hargaCetak = formatter.format(item.getSubtotal());
            
            System.out.printf(" %-2d | %-25s | %4d |   %3s \n", i, infoMenu, item.getJumlah(), hargaCetak);

            System.out.printf(" %-2s | %-25s | %4s |   %3s \n", "", periode, "", "");
            i++;
        }

        System.out.println("===================================================");
        System.out.printf ("%-32s: %16s\n", "Sub Total", formatter.format(this.subTotal));
        System.out.println("===================================================");

        System.out.printf ("%-32s: %16s\n", "Total", formatter.format(this.jumlahAkhir));

        if (this.promo != null) {
            if (this.promo instanceof Diskon) {
                long pot = this.subTotal - this.jumlahAkhir;
                System.out.printf ("%-32s: %16s\n", "PROMO: " + promo.getKode(), "-" + formatter.format(pot));
            } else if (this.promo instanceof Cashback) {
                long cb = (long)(this.subTotal * promo.getDiskon()); 
                if (cb > promo.getMaksPotongan()) cb = promo.getMaksPotongan();
                System.out.printf ("%-32s: %16s\n", "PROMO: " + promo.getKode(), formatter.format(cb));
            }
        }
        System.out.printf ("%-32s: %16s\n", "Saldo", formatter.format(p.getSaldo()));
    }
}
