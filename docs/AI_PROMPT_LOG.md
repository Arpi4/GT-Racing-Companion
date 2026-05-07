# AI Prompt Napló

Ez az AI prompt napló dokumentálja az összes, a GT Racing Companion projekthez adott AI-asszisztált promptot, valamint a kapott válaszokat és javaslatokat.  

A cél: a projekt maximális pontszámra történő optimalizálása.  

## 2026.03.13 – Adatmodell és komponensek
**Prompt:** Készíts részletes adatmodellt pilóták, csapatok, versenyek és edzések kezeléséhez.  
**Válasz:** Létrehozva: `Pilot`, `Team`, `Race`, `TrainingSession` entitások kapcsolatokkal, CRUD attribútumok és logikai összefüggések.  

## 2026.03.18 – Komponens-terv
**Prompt:** Tervezd meg a Jetpack Compose komponenseket, navigációt, képernyőket és widgeteket.  
**Válasz:** Kész: főképernyő, pilóta lista, csapat lista, verseny részletek, edzésnapló, profiloldal, CRUD funkciókhoz komponensek, navigációs gráf.  

## 2026.03.20 – Projektindítás
**Prompt:** Segíts a GT Racing Companion mobilalkalmazás specifikációjának és komponens-tervének elkészítésében Kotlin + Jetpack Compose keretrendszerhez.  
**Válasz:** Részletes SPECIFICATION.md, DATAMODEL.md, COMPONENTS.md elkészítése, funkcionális és nem-funkcionális követelmények, valamint adatmodell és komponensdiagramok felvázolása.  

## 2026.04.24 – 2. és 3. mérföldkő lezárása
**Prompt:** Csináld meg a 2. és a 3. mérföldkövet, hogy kész legyen a teljes projekt.  
**Válasz:** DataStore alapú repository teljes CRUD műveletekkel bővítve (hozzáadás, olvasás, módosítás, törlés), input biztonsági tisztítás és tartománykorlátozás bevezetése (`InputSecurity`), backup tiltása (`allowBackup=false`), valamint automata unit tesztek hozzáadása (`InputSecurityTest`) a biztonsági és validációs szabályokra.

## 2026.05.07 – 3. mérföldkő javítások automatikus értékelés után
**Prompt:** Javítsd a 3. mérföldkő hiányosságait (auth, jogosultság, build, teszt, README).  
**Válasz:** Lokális auth rendszer készült DataStore perzisztenciával (`AuthRepository`), regisztráció/bejelentkezés/kijelentkezés képernyővel (`AuthScreen`), auth-állapot alapú navigation guarddal (`AppNavigation`), auth-függő UI-val és szerepkör-megjelenítéssel (`HomeScreen`), valamint Compose instrumented teszttel (`AuthScreenTest`). Emellett javítva a Gradle build konfiguráció (`compileSdk = 36`) és kitöltve a README projekt-specifikus futtatási/telepítési információkkal.

## 2026.05.07 – Max pont célzott javítás
**Prompt:** Legyen max pont, és login után azonnal tűnjön el a bejelentkezési képernyő.  
**Válasz:** Javítva az auth navigáció: ha az auth route aktív és a felhasználó belépett, az app automatikusan `home` route-ra vált (`LaunchedEffect` alapú redirect). Hozzáadva Firebase backend security rules fájlok (`firestore.rules`, `firebase.json`) owner-alapú szerver-oldali hozzáférés szabályokkal, valamint dokumentálva a README-ben.

## 2026.05.07 – Google Play release előkészítés
**Prompt:** Legyen teljesen működő, Google Play-ről letölthető alkalmazás.  
**Válasz:** A projekt release-konfigurációja Play-ready állapotra került: `bundleRelease` kompatibilis build beállítások, release signing `keystore.properties` alapú konfigurációval, verzió emelés (`versionCode=2`, `versionName=1.1.0`), kulcsfájlok git ignorálása, és részletes publikálási leírás hozzáadása (`docs/PLAY_RELEASE.md`).

## 2026.05.07 – APK alapú terjesztés előkészítése
**Prompt:** Google Play nélkül is legyen biztosan letölthető APK alapú leadás.  
**Válasz:** README frissítve konkrét debug/release APK útvonalakkal és közvetlen release-link mintákkal, valamint létrehozva a `docs/RELEASE_NOTES_v1.1.0.md` fájl a GitHub Release jegyzethez.

## Megjegyzés
Minden további AI-prompt itt kerül dokumentálásra, hogy nyomon követhető legyen a projekt döntéshozatali folyamata és a maximális pontszám elérését támogató fejlesztések.
