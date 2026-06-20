# Sinsata Visuals

Minecraft uchun **faqat estetik va FPS-optimizatsiya** modi. Bu modda hech qanday
cheat (ESP, aimbot, x-ray, fly va h.k.) funksiyasi yo'q — faqat:

- Zarrachalar sonini kamaytirish (FPS uchun)
- Bulutlarni "fast" rejimga o'tkazish (FPS uchun)
- Tuman/blok-sindirish effektlarini soddalashtirish (FPS uchun)
- Yengil kosmetik rang filtri va vinyetka effekti (faqat ko'rinish uchun)
- To'liq o'zbek tilidagi sozlamalar oynasi (o'yin ichida **Right Shift** tugmasi)

## ⚠️ Muhim — qurishdan oldin o'qing

Bu kod menga (Claude) internetga ulanish imkoni bo'lmagan muhitda yozildi,
shuning uchun men uni shu yerda compile qilib **sinab ko'rolmadim**. Sizga
quyidagilarni tavsiya qilaman:

1. `gradle.properties` faylidagi `minecraft_version`, `yarn_mappings`,
   `loader_version`, `fabric_version` qiymatlarini https://fabricmc.net/develop/
   saytidan **aniq Minecraft 1.21.11 uchun mos versiyalar** bilan tekshirib,
   to'g'rilang.
2. Loyihani **IntelliJ IDEA** + **Minecraft Development** plagini bilan oching —
   u sizga mixin metod nomlari (`addParticle`, `getCloudRenderMode` va h.k.)
   to'g'ri yoki noto'g'ri ekanini ko'rsatadi va avtomatik to'g'irlashga yordam
   beradi (versiyalar orasida bu nomlar ozgina farq qilishi mumkin).
3. Birinchi marta build qilishdan oldin internetga ulangan bo'lishingiz shart
   (Gradle Minecraft va mapping fayllarini yuklab oladi).

## Gradle Wrapper haqida

Bu loyihada `gradlew`/`gradlew.bat` fayllari **yo'q** (chunki ularni yuklab olish
uchun internet kerak, men esa bu muhitda internetga ulanolmadim). Birinchi marta
ochishda, agar kompyuteringizda Gradle o'rnatilgan bo'lsa:

```bash
gradle wrapper --gradle-version 8.8
```

buyrug'ini ishga tushiring — bu `gradlew` fayllarini avtomatik yaratadi.
Agar Gradle umuman o'rnatilmagan bo'lsa, IntelliJ IDEA loyihani ochganda
buni avtomatik taklif qiladi ("Setup Gradle wrapper").

## Qurish (build qilish)

Terminalda loyiha papkasida turib:

```bash
# Linux/macOS
./gradlew build

# Windows
gradlew.bat build
```

Muvaffaqiyatli yakunlangandan so'ng, tayyor `.jar` fayl shu yerda paydo bo'ladi:

```
build/libs/sinsata-visuals-1.0.0.jar
```

## O'rnatish

1. [Fabric Loader](https://fabricmc.net/use/) ni o'rnating (Minecraft 1.21.11 uchun).
2. [Fabric API](https://modrinth.com/mod/fabric-api) modini yuklab, `.minecraft/mods`
   papkasiga qo'ying.
3. Yuqorida yaratilgan `sinsata-visuals-1.0.0.jar` faylini ham `mods` papkasiga
   qo'ying.
4. O'yinni ishga tushiring → o'yin ichida **Right Shift** tugmasini bosib,
   "Sinsata Visuals" sozlamalar oynasini ochasiz.

## Loyiha tuzilishi

```
SinsataVisuals/
├── build.gradle
├── gradle.properties
├── settings.gradle
└── src/main/
    ├── java/uz/sinsata/visuals/
    │   ├── SinsataVisualsClient.java      <- mod kirish nuqtasi
    │   ├── config/VisualsConfig.java      <- sozlamalarni saqlash
    │   ├── gui/SinsataVisualsScreen.java  <- o'zbekcha sozlamalar oynasi
    │   └── mixin/
    │       ├── ParticleManagerMixin.java  <- zarrachalarni kamaytirish
    │       └── CloudRenderMixin.java      <- bulutlarni soddalashtirish
    └── resources/
        ├── fabric.mod.json
        ├── sinsatavisuals.mixins.json
        └── assets/sinsatavisuals/lang/
            ├── uz_uz.json
            └── en_us.json
```

## Keyingi qadamlar (qo'shimcha rivojlantirish g'oyalari)

- **Haqiqiy shader effektlari** uchun [Iris](https://modrinth.com/mod/iris) modi
  bilan moslashtirish (compat mod) qilish mumkin — to'liq shader pipeline'ni
  noldan yozish o'rniga, Iris orqali tashqi `.zip` shader-pack'larni qo'llab-
  quvvatlash ancha amaliy yo'l.
- **Qo'shimcha FPS optimizatsiyasi** uchun [Sodium](https://modrinth.com/mod/sodium)
  bilan birga ishlatish tavsiya etiladi — Sinsata Visuals Sodium bilan to'qnashmaydi,
  ularni birga o'rnatish mumkin.
- Kelajakda HUD elementlari (kompas, koordinatalar ko'rsatkichi va h.k.) qo'shish.

---

**Eslatma:** Ushbu mod faqat shaxsiy/kosmetik foydalanish uchun mo'ljallangan.
Agar uni biror serverda ishlatmoqchi bo'lsangiz, server qoidalarini albatta
tekshirib oling.
