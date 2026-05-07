# GT Racing Companion – Specifikáció

## 1. Projekt célja
A **GT Racing Companion** egy motorsport témájú Android alkalmazás (Kotlin + Jetpack Compose), amely támogatja a GT-versenyzéshez kapcsolódó adatok kezelését. A cél egy **vizuálisan áttekinthető, gyorsan használható** felület biztosítása, ahol a felhasználó:

- pilótákat, csapatokat, versenyeket és pályákat kezel,
- saját edzésnaplót vezet,
- meg tudja nézni az entitások részleteit és logikai kapcsolatait.

## 2. Célplatform és technológiák
- **Platform**: Android
- **Nyelv**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Navigáció**: Navigation Compose (route alapú)
- **Architektúra (jelenlegi milestone)**: UI réteg + ViewModel réteg (később adatforrás / backend)

## 3. Funkcionális követelmények (CRUD + kapcsolatok)
Az alkalmazás az alábbi entitásokat kezeli:

- **Driver (Pilóta)**
- **Team (Csapat)**
- **Race (Verseny)**
- **Track (Pálya)**
- **TrainingSession (Edzésnapló bejegyzés)**

### 3.1. Alap műveletek
Minden entitáshoz az alábbi alapműveletek célként szerepelnek (milestone-onként fokozatosan bővítve):

- **Create**: új elem felvétele külön “Add …” képernyőn
- **Read**: lista nézet + részletek nézet
- **Update**: meglévő elem módosítása (későbbi bővítés)
- **Delete**: törlés megerősítő dialógussal (későbbi bővítés)

### 3.2. Kapcsolatok
Az entitások közti logikai kapcsolatok célja:

- **Team 1:N Driver**: egy csapatnak több pilótája lehet
- **Track 1:N Race**: egy pályán több verseny lehet
- **Driver 1:N TrainingSession**: egy pilótához több edzésbejegyzés tartozhat
- (későbbi bővítés) **Race N:M Team/Driver**: egy verseny több csapatot/pilótát érinthet

## 4. Képernyők és navigáció
### 4.1. Fő képernyők
- **Home**: belépési pont, gyors belépés a modulokba
- **Drivers / Teams / Races / Tracks / Training**: lista képernyők
- **Add Driver / Add Team / Add Race / Add Track / Add Training**: létrehozó képernyők
- (későbbi bővítés) **Details** képernyők entitásonként

### 4.2. Navigációs elv
- **Bottom Navigation Bar** (aktív állapot kiemeléssel) a fő szekciók között
- Route-ok pl.: `home`, `drivers`, `teams`, `races`, `tracks`, `training`
- Az “Add …” képernyők a megfelelő listából érhetők el (FAB vagy gomb)

## 5. UI/UX követelmények
- **Material 3** egységes megjelenés
- **Design tokenek** használata:
  - központi színpaletta (theme)
  - tipográfia skála
  - egységes spacing skála (pl. `Spacing.small/medium/large`)
- **Touch-friendly** interakciók: ikon gombok és fő vezérlők legalább ~48dp környéke

## 6. Adaptív megjelenés és Safe Area
- A tartalom nem lóghat be status/navigation bar alá: **WindowInsets / safe drawing** használata
- Későbbi bővítés: nagy kijelzőn (tablet/landscape) rugalmasabb elrendezés (pl. grid / master-detail)

## 7. Akadálymentesség (a11y) – minimum célok
- Interaktív ikonoknál **contentDescription**
- Hosszú szövegeknél ésszerű **maxLines/overflow**
- Később: semantics/heading jelölések, kontraszt ellenőrzés, fókusz-kezelés

## 8. Nem funkcionális követelmények
- Stabil navigáció (nincs “dead end” route)
- Egységes komponensek újrafelhasználása (AppBar, Loading, Snackbar, ConfirmationDialog)
- Kód olvashatóság és moduláris felépítés

