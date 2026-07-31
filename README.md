# 🎬 Pro Kamera Film

Profesyonel video çekimi için özel **Film Mode** özelliğine sahip kamera uygulaması.

## ✨ Özellikler

✅ **Tam Kamera Fonksiyonu**
- 📷 Fotoğraf çekimi
- 🎥 Video kayıt (yüksek kalite)
- ⚙️ Kamera ayarları

✅ **Film Mode (Sahne Takibi)**
- 🎬 Video duraklama sırasında motion tracking
- 📍 Kenarlar yeşil gösterge
- 🔄 Sorunsuz sahne geçişleri
- ✨ Profesyonel video çekimi

✅ **Ayarlar**
- 🎥 Kalite seçimi
- 🔊 Ses kaydı kontrolü
- 📊 Film Mode ayarları

## 🎨 Arayüz

- Koyu tema (AMOLED-friendly)
- Basit ve sezgisel butonlar
- Gerçek zamanlı kayıt sayacı
- Profesyonel tasarım

## 📱 Gereksinimler

- Android 5.1+ (API 21)
- Kamera izni
- Ses kaydı izni
- Depolama izni

## 🚀 Kurulum

### GitHub'dan APK İndirme

1. Repository → **Actions** sekmesi
2. Son **Build APK** çalıştırmasını seç
3. **Artifacts** → `ProCameraFilm-debug` indir
4. Telefona kur

### Android Studio'dan Derlemek

```bash
git clone https://github.com/yunopm5-design/ProCameraFilm.git
cd ProCameraFilm
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

## 📖 Kullanım

1. Uygulamayı aç
2. İzinleri onayla
3. **● Kayıt** ile video başlat
4. 🎬 **Film Mode** ile sahne takibini aç
5. Video durduğunda kenarlar yeşil gösterge verdi → Mükemmel frame
6. ⚙️ **Ayarlar** ile video kalitesini kontrol et

## 🎬 Film Mode Nasıl Çalışır?

Video çekimi sırasında:
1. Sahneyi sabit tut
2. **Film Mode** butonu aktif et
3. Video duraklat
4. Kameradan hareket etme
5. **Yeniden başlat** butonu gördüğünde
6. Kenarlar yeşil olunca = Perfect alignment
7. Video kesintisiz görünür!

## 👨‍💻 Yapımcı

**By Yuno**

© 2024 Pro Kamera Film

---

**Teknoloji:** Kotlin, CameraX, Android Camera2 API
