// File: Mobil.java
// Kelas Mobil
class mobil {
    // Atribut atau properti dari Mobil
    String merk;
    String warna;
    int tahunProduksi;

    // Constructor untuk kelas Mobil
    public mobil(String merk, String warna, int tahunProduksi) {
        this.merk = merk;
        this.warna = warna;
        this.tahunProduksi = tahunProduksi;
    }

    // Method untuk menghidupkan mobil
    public void hidupkanMesin() {
        System.out.println("Mesin mobil " + merk + "warna" + warna + "dinyalakan.");
    }

    // Method untuk mengemudi mobil
    public void kemudi() {
        System.out.println("Mobil " + merk + "warna" + warna + " sedang dikemudikan.");
    }
}
