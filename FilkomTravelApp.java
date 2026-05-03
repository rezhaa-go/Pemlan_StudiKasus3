package Studi_Kasus3;

import java.text.*;
import java.util.*;

public class FilkomTravelApp {
    private static Map<String, Pelanggan> daftarPelanggan = new LinkedHashMap<>();
    private static Map<String, Menu> daftarMenu = new LinkedHashMap<>();
    private static Map<String, Promo> daftarPromo = new LinkedHashMap<>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private static int counterPesanan = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String command = sc.next();
            String rawParams = sc.nextLine().trim();
            try {
                switch (command) {
                    case "CREATE": handleCreate(rawParams); break;
                    case "TOPUP": handleTopup(rawParams); break;
                    case "ADD_TO_CART": handleAddToCart(rawParams); break;
                    case "REMOVE_FROM_CART": handleRemoveFromCart(rawParams); break;
                    case "APPLY_PROMO": handleApplyPromo(rawParams); break;
                    case "CHECK_OUT": handleCheckOut(rawParams); break;
                    case "PRINT": handlePrint(rawParams); break;
                    case "PRINT_HISTORY": handleHistory(rawParams); break;
                    default: break;
                }
            } catch (Exception e) {
                System.out.println("DEBUG ERROR: Terjadi kesalahan pada " + command);
            }
        }
        sc.close();
    }

    private static void handleCreate(String raw) throws Exception {
        String[] parts = raw.split(" ", 2);
        if (parts.length < 2) return;
        String kategori = parts[0];
        String data = parts[1];

        if (kategori.equals("MEMBER")) {
            String[] d = data.split("\\|");
            if (!daftarPelanggan.containsKey(d[0])) {
                daftarPelanggan.put(d[0], new Member(d[0], d[1], Long.parseLong(d[3]), sdf.parse(d[2])));
                System.out.println("CREATE MEMBER SUCCESS: " + d[0] + " " + d[1]);
            } else {
                System.out.println("CREATE MEMBER FAILED: " + d[0] + " IS EXISTS");
            }
        } else if (kategori.equals("GUEST")) {
            String[] d = data.split("\\|");
            if (!daftarPelanggan.containsKey(d[0])) {
                daftarPelanggan.put(d[0], new Guest(d[0], Long.parseLong(d[1])));
                System.out.println("CREATE GUEST SUCCESS: " + d[0]);
            } else {
                System.out.println("CREATE GUEST FAILED: " + d[0] + " IS EXISTS");
            }
        } else if (kategori.equals("MENU")) {
            String[] sub = data.split(" ", 2);
            String jenis = sub[0];
            String[] d = sub[1].split("\\|");
            String idMenu = d[0];
            String plat = d[2];

            if (daftarMenu.containsKey(idMenu)) {
                System.out.println("CREATE MENU FAILED: " + idMenu + " IS EXISTS");
                return;
            }
            for (Menu m : daftarMenu.values()) {
                if (m.getPlat().equalsIgnoreCase(plat)) {
                    System.out.println("CREATE MENU FAILED: " + plat + " IS EXISTS");
                    return;
                }
            }

            if (jenis.equals("MOTOR")) {
                daftarMenu.put(idMenu, new Motor(idMenu, d[1], plat, Long.parseLong(d[3])));
            } else {
                daftarMenu.put(idMenu, new Mobil(idMenu, d[1], plat, Long.parseLong(d[3]), d[4]));
            }
            System.out.println("CREATE MENU SUCCESS: " + idMenu + " " + d[1] + " " + plat);

        } else if (kategori.equals("PROMO")) {
            String[] sub = data.split(" ", 2);
            String jenisP = sub[0];
            String[] d = sub[1].split("\\|");
            String kode = d[0];

            if (!daftarPromo.containsKey(kode)) {
                double discVal = Double.parseDouble(d[3].replace("%", "")) / 100.0;
                if (jenisP.equals("DISCOUNT")) {
                    daftarPromo.put(kode, new Diskon(kode, sdf.parse(d[1]), sdf.parse(d[2]), discVal, Long.parseLong(d[4]), Long.parseLong(d[5])));
                } else {
                    daftarPromo.put(kode, new Cashback(kode, sdf.parse(d[1]), sdf.parse(d[2]), discVal, Long.parseLong(d[4]), Long.parseLong(d[5])));
                }
                System.out.println("CREATE PROMO " + jenisP + " SUCCESS: " + kode);
            } else {
                System.out.println("CREATE PROMO " + jenisP + " FAILED: " + kode + " IS EXISTS");
            }
        }
    }

    private static void handleTopup(String raw) {
        String[] d = raw.split("\\s+");
        Pelanggan plg = daftarPelanggan.get(d[0]);
        if (plg != null) {
            long nominal = Long.parseLong(d[1]);
            long lama = plg.getSaldo();
            plg.setSaldo(lama + nominal);
            System.out.println("TOPUP SUCCESS: " + plg.getNama() + " " + lama + " => " + plg.getSaldo());
        } else {
            System.out.println("TOPUP FAILED: NON EXISTENT CUSTOMER");
        }
    }

    private static void handleAddToCart(String raw) throws Exception {
        String[] d = raw.split("\\s+");
        Pelanggan plg = daftarPelanggan.get(d[0]);
        Menu menu = daftarMenu.get(d[1]);
        if (plg == null || menu == null) {
            System.out.println("ADD_TO_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
            return;
        }
        int qtyBaru = Integer.parseInt(d[2]);
        Date tgl = sdf.parse(d[3]);
        ItemKeranjang itemLama = null;
        for (ItemKeranjang i : plg.getKeranjang()) {
            if (i.getMenu().getId().equals(menu.getId())) {
                itemLama = i; break;
            }
        }
        if (itemLama != null) {
            int totalQty = itemLama.getJumlah() + qtyBaru;
            itemLama.setJumlah(totalQty);
            System.out.println("ADD_TO_CART SUCCESS: " + totalQty + (totalQty > 1 ? " days " : " day ") + menu.getNama() + " " + menu.getPlat() + " (UPDATE)");
        } else {
            plg.getKeranjang().add(new ItemKeranjang(menu, qtyBaru, tgl));
            System.out.println("ADD_TO_CART SUCCESS: " + qtyBaru + (qtyBaru > 1 ? " days " : " day ") + menu.getNama() + " " + menu.getPlat() + " (NEW)");
        }
        Collections.sort(plg.getKeranjang());
    }

    private static void handleRemoveFromCart(String raw) {
        String[] d = raw.split("\\s+");
        Pelanggan plg = daftarPelanggan.get(d[0]);
        Menu menu = daftarMenu.get(d[1]);
        if (plg == null || menu == null) {
            System.out.println("REMOVE_FROM_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
            return;
        }
        int qtyHapus = Integer.parseInt(d[2]);
        ItemKeranjang found = null;
        for (ItemKeranjang i : plg.getKeranjang()) {
            if (i.getMenu().getId().equals(menu.getId())) {
                found = i; break;
            }
        }
        if (found != null) {
            int res = found.getJumlah() - qtyHapus;
            if (res <= 0) {
                plg.getKeranjang().remove(found);
                System.out.println("REMOVE_FROM_CART SUCCESS: " + menu.getNama() + " IS REMOVED");
            } else {
                found.setJumlah(res);
                System.out.println("REMOVE_FROM_CART SUCCESS: " + menu.getNama() + " QUANTITY IS DECREMENTED");
            }
        } else {
            System.out.println("REMOVE_FROM_CART FAILED: MENU NOT IN CART");
        }
    }

    private static void handleApplyPromo(String raw) throws Exception {
        String[] d = raw.split("\\s+");
        Pelanggan plg = daftarPelanggan.get(d[0]);
        Promo p = daftarPromo.get(d[1]);

        if (plg == null || p == null) {
            System.out.println("APPLY_PROMO FAILED: NON EXISTENT CUSTOMER OR PROMO");
            return;
        }

        Date hariIni = sdf.parse("2024/06/01"); 
        long sub = 0;
        for (ItemKeranjang i : plg.getKeranjang()) sub += i.getSubtotal();
        if (sub < p.getMinPembelian()) {
            System.out.println("APPLY_PROMO FAILED: " + p.getKode());
            return;
        }

        if (plg instanceof Member) {
            long diff = hariIni.getTime() - ((Member) plg).getTanggalDaftar().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if (days <= 30) {
                System.out.println("APPLY_PROMO FAILED: " + p.getKode());
                return;
            }
        }

        if (hariIni.before(p.getTanggalMulai()) || hariIni.after(p.getTanggalSelesai())) {
            System.out.println("APPLY_PROMO FAILED: " + p.getKode() + " is EXPIRED");
            return;
        }

        plg.setPromoAktif(p);
        System.out.println("APPLY_PROMO SUCCESS: " + p.getKode());
    }

    private static void handleCheckOut(String id) {
        Pelanggan plg = daftarPelanggan.get(id.trim());
        if (plg == null) {
            System.out.println("CHECK_OUT FAILED: NON EXISTENT CUSTOMER");
            return;
        }

        if (plg.getKeranjang().isEmpty()) return;

        long subtotal = 0;
        for (ItemKeranjang i : plg.getKeranjang()) subtotal += i.getSubtotal();

        long potongan = 0;
        Promo p = plg.getPromoAktif();
        if (p != null && subtotal >= p.getMinPembelian()) {
            potongan = Math.min((long)(subtotal * p.getDiskon()), p.getMaksPotongan());
        }
        long totalBayar;
        if (p instanceof Diskon) {
            totalBayar = subtotal - potongan; 
        } else {
            totalBayar = subtotal;
        }
        if (plg.getSaldo() < totalBayar) {
            System.out.println("CHECK_OUT FAILED: " + plg.getId() + " " + plg.getNama() + " INSUFFICIENT BALANCE");
        } else {
            plg.setSaldo(plg.getSaldo() - totalBayar);
            if (p instanceof Cashback) {
                plg.setSaldo(plg.getSaldo() + potongan);
            }
            plg.getRiwayatPesanan().add(new Pesanan(counterPesanan++, new Date(), new ArrayList<>(plg.getKeranjang()), p, subtotal, totalBayar));
            plg.getKeranjang().clear();
            plg.setPromoAktif(null);
            System.out.println("CHECK_OUT SUCCESS: " + plg.getId() + " " + plg.getNama());
        }
    }

    private static void handlePrint(String id) {
        Pelanggan plg = daftarPelanggan.get(id.trim());
        if (plg == null) {
            System.out.println("PRINT FAILED: NON EXISTENT CUSTOMER");
            return;
        }
        if (!plg.getKeranjang().isEmpty()) {
            long subtotal = 0;
            for (ItemKeranjang i : plg.getKeranjang()) subtotal += i.getSubtotal();
            Pesanan temp = new Pesanan(0, new Date(), plg.getKeranjang(), null, subtotal, subtotal);
            temp.tampilkanKeranjang(plg); // Memanggil method baru yang kita buat tadi
        } 
        else if (!plg.getRiwayatPesanan().isEmpty()) {
            Pesanan terakhir = plg.getRiwayatPesanan().get(plg.getRiwayatPesanan().size() - 1);
            terakhir.tampilkanPesanan(plg);
        } 
        else {
             System.out.println("CUSTOMERS: " + plg.getId() + " " + plg.getNama() + " " + plg.getSaldo());
        }
    }

    private static void handleHistory(String id) {
    Pelanggan plg = daftarPelanggan.get(id.trim());
    if (plg == null) {
        System.out.println("HISTORY FAILED: NON EXISTENT CUSTOMER");
        return;
    }
    
    if (plg.getRiwayatPesanan().isEmpty()) {
        System.out.println("Kode Pemesan: " + plg.getId());
        System.out.println("Nama: " + plg.getNama());
        System.out.println("Saldo: " + plg.getSaldo());
        System.out.println("BELUM ADA RIWAYAT PESANAN");
        return;
    }
    plg.tampilkanHistory();
    }
}