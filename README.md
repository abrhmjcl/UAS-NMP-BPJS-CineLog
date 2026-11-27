# CineLog - Aplikasi Katalog & Catatan Film

Aplikasi Android Native **CineLog** dibangun untuk mencatat daftar film yang sudah ditonton (*Watched*), rencana tontonan (*Plan to Watch*), serta daftar film favorit lengkap dengan rating, ulasan pribadi, dan trailer. Proyek ini dikembangkan menggunakan **Kotlin** dengan **XML Layout (ViewBinding)** dan REST API **PHP MySQLi + MySQL** sesuai materi mata kuliah Native Mobile Programming (1604C052).

---

## 👥 Tim Pengembang (Kelompok BPJS)

| Nama Lengkap | NRP |
| :--- | :--- |
| **Abraham Jeconiah Loasari** | 160424126 |
| **Geraldine Steny Lae** | 160424108 |
| **Elgift Antonio Mananti** | 160424111 |

---

## 🚀 Fitur Utama & Fungsionalitas

1. **Onboarding & Penyimpanan Sesi Pengguna**
   - Halaman awal (`IntroActivity`) meminta input nama dan tanggal lahir menggunakan **DatePickerDialog**.
   - Data profil disimpan secara lokal menggunakan **SharedPreferences**, sehingga pengguna yang sudah mengisi data akan langsung diarahkan ke halaman utama pada pembukaan berikutnya.

2. **Navigasi Multi-Tab (ViewPager2 & TabLayout)**
   - Halaman utama (`MainActivity`) membagi katalog film ke dalam 3 tab: **Now Playing**, **Plan to Watch**, dan **Favorites** menggunakan `ViewPager2` dan `TabLayout`.

3. **Katalog Film, Picasso & Filter Grayscale**
   - Menampilkan daftar film melalui `RecyclerView` dengan `MaterialCardView` bertema 2 warna (Putih & Merah `#D32F2F`).
   - Poster dimuat dari URL menggunakan library **Picasso**, dengan filter **Grayscale (`ColorMatrix`)** otomatis pada film berstatus *Plan to Watch* (`!isAvailable`) dan reset ke warna normal pada film yang sudah ditonton.

4. **Tambah & Edit Film + Kamera**
   - `NewMovieActivity` dan `EditMovieActivity` berbagi satu layout form (`activity_new_movie.xml`) yang berisi `EditText`, `Spinner` (Genre & Platform), `RadioGroup` (Rating Usia), dan `CheckBox` (Watched, Spoiler, Favorite).
   - Mendukung preview gambar poster dari URL (via tombol Enter) serta pengambilan foto langsung menggunakan kamera perangkat (`MediaStore.ACTION_IMAGE_CAPTURE` dengan runtime permission).

5. **Detail Film, Proteksi Spoiler & Implicit Intent**
   - Film bertanda spoiler memicu konfirmasi `AlertDialog` sebelum membuka dialog detail (`MovieDetailFragment` berbasis `DialogFragment`).
   - Tersedia fitur putar trailer YouTube di dalam aplikasi (`CinemaWebActivity` WebView) serta aksi **Implicit Intent**: bagikan ulasan (`ACTION_SEND`), cari bioskop Cinema XXI di Google Maps (`ACTION_VIEW`), dan hubungi hotline bioskop (`ACTION_DIAL`).

6. **Integrasi Web Service & Database MySQL**
   - Aplikasi terhubung ke backend PHP (`getmovies.php`, `insertmovie.php`, `editmovie.php`, `deletemovie.php`) menggunakan **Volley** (`StringRequest` & `Volley.newRequestQueue`) dengan query MySQLi Prepared Statements ke database `cinelogdb`, serta fallback data lokal di `MovieData`.

---

## ⚙️ Petunjuk Menjalankan Aplikasi

### Prasyarat
- **Android Studio** (dengan Android SDK 37, Min SDK 24, dan JDK 11)
- **XAMPP** (modul **Apache** dan **MySQL**)
- Emulator Android atau perangkat fisik (Android 7.0+)

### 1. Setup Backend PHP & Database MySQL
1. Jalankan **Apache** dan **MySQL** melalui **XAMPP Control Panel**.
2. Buka `http://localhost/phpmyadmin`, lalu import file `database/cinelogdb.sql` untuk membuat database `cinelogdb`, tabel `movies`, dan data awal.
3. Buat folder `cinelog` di dalam `C:\xampp\htdocs\` (`C:\xampp\htdocs\cinelog\`), lalu salin file PHP berikut ke dalamnya:
   - `getmovies.php`
   - `insertmovie.php`
   - `editmovie.php`
   - `deletemovie.php`
4. Uji koneksi backend melalui browser di `http://localhost/cinelog/getmovies.php` dan pastikan respon JSON menampilkan `"result": "SUCCESS"`.

### 2. Jalankan Aplikasi Android
1. Clone repository:
   ```bash
   git clone https://github.com/abrhmjcl/UAS-NMP-BPJS-CineLog.git
   ```
2. Buka folder proyek di **Android Studio**, lalu tunggu proses **Gradle Sync** selesai.
3. Jalankan aplikasi (**Run**) ke Android Emulator.
   *(Catatan: URL Volley di aplikasi menggunakan `http://10.0.2.2/cinelog/` agar Emulator dapat mengakses localhost PC. Jika menggunakan HP fisik pada jaringan Wi-Fi yang sama, sesuaikan `10.0.2.2` dengan IPv4 PC Anda).*

---

## 📂 Struktur Folder

```text
UAS-NMP-BPJS-CineLog/
├── database/
│   ├── cinelogdb.sql                # Skema tabel movies & seed data awal
│   ├── getmovies.php                # Endpoint ambil daftar film (SELECT)
│   ├── insertmovie.php              # Endpoint tambah film baru (INSERT)
│   ├── editmovie.php                # Endpoint ubah data film (UPDATE)
│   └── deletemovie.php              # Endpoint hapus film (DELETE)
├── gradle/
│   └── libs.versions.toml           # Daftar versi dependensi (Material, Picasso, Volley, ViewPager2)
└── app/
    ├── build.gradle.kts             # Konfigurasi SDK, ViewBinding & kotlin-parcelize
    └── src/main/
        ├── AndroidManifest.xml      # Izin INTERNET, CAMERA & deklarasi Activity
        ├── java/com/bpjs/cinelog/
        │   ├── IntroActivity.kt     # Halaman awal, DatePickerDialog & SharedPreferences
        │   ├── MainActivity.kt      # Host ViewPager2, TabLayout, FAB & Volley fetch/delete
        │   ├── NewMovieActivity.kt  # Form tambah film, kamera & Volley insert
        │   ├── EditMovieActivity.kt # Form edit film (layout reuse) & Volley edit
        │   ├── CinemaWebActivity.kt # WebView pemutar trailer
        │   ├── MovieItem.kt         # Model data @Parcelize
        │   ├── MovieData.kt         # Data fallback in-memory & array Spinner
        │   ├── MovieAdapter.kt      # Adapter RecyclerView, Picasso, Grayscale & Dialog
        │   ├── MovieViewPagerAdapter.kt # Adapter FragmentStateAdapter untuk 3 tab
        │   ├── MovieListFragment.kt # Tab daftar Now Playing & Plan to Watch
        │   ├── FavoriteFragment.kt  # Tab daftar film favorit
        │   └── MovieDetailFragment.kt # DialogFragment ulasan & Implicit Intent
        └── res/
            ├── layout/
            │   ├── activity_intro.xml
            │   ├── activity_main.xml
            │   ├── activity_new_movie.xml
            │   ├── activity_cinema_web.xml
            │   ├── movie_card.xml
            │   ├── fragment_movie_list.xml
            │   └── fragment_movie_detail.xml
            └── values/
                ├── colors.xml       # Palet 2 warna (White & Red)
                ├── strings.xml      # Label tab & pesan dialog
                └── themes.xml       # Tema Theme.CineLog (Material 3 Light)
```
