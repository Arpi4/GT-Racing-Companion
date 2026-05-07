# GT Racing Companion – Komponens terv

## Navigációs struktúra
- **MainActivity**
  - **Scaffold**
    - **BottomNavBar** (fő szekciók közti váltás, aktív állapot)
    - **NavHost** (`AppNavigation`)

## Képernyők (Screen-ek)
- **HomeScreen**
  - belépési pont / modulválasztó
- **DriversScreen**
  - lista + FAB → `AddDriverScreen`
- **TeamsScreen**
  - lista + FAB → `AddTeamScreen`
- **RacesScreen**
  - lista + FAB → `AddRaceScreen`
- **TracksScreen**
  - lista + FAB → `AddTrackScreen`
- **TrainingScreen**
  - lista + FAB → `AddTrainingScreen`
- **Add…** képernyők (form alapú)
  - mezők + Save gomb + visszanavigálás

## Újrafelhasználható UI komponensek (common components)
- **BottomNavBar**
  - `NavigationBar` + `NavigationBarItem` aktív route alapján
- **CustomAppBar**
  - egységes TopAppBar megjelenés (későbbi bővítés: vissza gomb, action-ök)
- **LoadingSpinner**
  - közös betöltés jelző
- **ErrorSnackbar**
  - egységes hibajelzés, dismiss action-nel
- **ConfirmationDialog**
  - egységes “Biztosan?” megerősítés törléshez

## Design tokenek / theme
- **GTRacingTheme**
  - Material 3 `colorScheme` + `typography`
- **Spacing**
  - egységes padding/margin skála
- **Shadows**
  - egységes elevation/shadow skála (későbbi UI finomításokhoz)

