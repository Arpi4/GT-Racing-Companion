[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/JoHdmBvg)
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Ew36zBjj)
# Mobil alkalmazásfejlesztés — Projektmunka

> **Hallgató neve:** Tóth Árpád Zsolt  
> **Neptun kód:** CXX2OI  
> **Projekt téma:** GT Racing Companion — Pilóták, csapatok, versenyek és edzések kezelése  
> **Keretrendszer:** Kotlin + Jetpack Compose

---

## 🚀 A projekt indítása (lokális futtatás)

### Előfeltételek

- Android Studio (latest stable)
- JDK 17 vagy 21
- Android SDK Platform 36
- Android Emulator vagy valós Android eszköz (API 26+)

### Telepítés és futtatás

```bash
git clone <repo-url>
cd projektmunka-Arpi4
./gradlew assembleDebug
./gradlew installDebug
```

Android Studio-ból is futtatható: `app` modul kiválasztása után **Run 'app'**.

---

## 📱 Letöltés / Telepítés

- Lokálisan buildelt APK útvonalak:
  - Debug: `app/build/outputs/apk/debug/app-debug.apk`
  - Release: `app/build/outputs/apk/release/app-release.apk`
- GitHub Releases oldal: [projektmunka-Arpi4 Releases](https://github.com/mobil-alkalmazasfejlesztes-2026/projektmunka-Arpi4/releases)
- Közvetlen letöltési linkek:
  - Debug APK: https://github.com/mobil-alkalmazasfejlesztes-2026/projektmunka-Arpi4/releases/download/v1.1.0/app-debug.apk
  - Release APK: https://github.com/mobil-alkalmazasfejlesztes-2026/projektmunka-Arpi4/releases/download/v1.1.0/app-release.apk
- Telepítés ADB-vel:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## 🔐 Backend szabályok

- A repó tartalmaz Firebase Firestore szabályfájlt: `firestore.rules`
- A szabályok tulajdonos-alapú hozzáférést kényszerítenek (`ownerId == request.auth.uid`)
- A konfiguráció a `firebase.json` fájlban hivatkozik a rules fájlra

---

## 📁 Projekt struktúra

```
├── app/src/main/java/com/arpitoth/gtracingcompanion/
│   ├── data/                # DataStore, repository, auth, input security
│   ├── ui/                  # Compose képernyők, navigáció, komponensek, theme
│   └── MainActivity.kt      # app entry point
├── app/src/test/            # Unit tesztek (pl. InputSecurityTest)
├── app/src/androidTest/     # Instrumented/Compose UI tesztek (pl. AuthScreenTest)
├── docs/                    # Specifikáció és projekt dokumentáció
└── .github/workflows/       # Automatikus értékelés
```

---

## 📅 Mérföldkövek

| # | Tartalom | Határidő | Állapot |
|---|----------|----------|---------|
| 1 | Specifikáció, UI és megjelenés | 2026.03.29. 23:59 | ✅ |
| 2 | Backend és adatok | 2026.04.26. 23:59 | ✅ |
| 3 | Biztonság és tesztelés | 2026.05.10. 23:59 | ✅ |

### Hogyan kérd az értékelést?

1. Commitold és push-old a munkádat a `main` vagy `master` branch-re
2. Menj a repód **Actions** fülére
3. Válaszd a **"Mérföldkő értékelés"** workflow-t
4. Kattints a **"Run workflow"** → válaszd ki a mérföldkövet → **"Run workflow"**
5. Az eredmény egy **GitHub Issue**-ban jelenik meg

> ⚠️ Mérföldkőnként **maximum 2 alkalommal** futtathatod az értékelést. Használd bölcsen!  
> ⚠️ A határidőkön automatikus értékelés is fut.

---

## ⚠️ Fontos

- A `.github/workflows/` könyvtár tartalmát **ne módosítsd**!
- A `docs/` mappába rakd a dokumentációs fájlokat.
- Az `AI_PROMPT_LOG.md` fájlt a `docs/` mappában vezesd.
