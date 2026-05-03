package Studi_Kasus3;

public class Mobil extends Menu {
    private String tipe;
    public Mobil(String id, String nama, String plat, long harga, String tipe) {
        super(id, nama, plat, harga);
        this.tipe = tipe;
    }

    public String getTipe(){
        return tipe;
    }

    public int getKapasistas() {
        if (this.tipe.equalsIgnoreCase("L")){
            return 6;
        } else if (this.tipe.equalsIgnoreCase("R")){
            return 4;
        }
        return 0;
    }
}
