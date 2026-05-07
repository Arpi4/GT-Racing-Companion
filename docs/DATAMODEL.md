# GT Racing Companion – Adatmodell

Az alkalmazás célja legalább 5, motorsport témába illő entitás kezelése, értelmes mezőkkel és logikai kapcsolatokkal.

## Entitások

### 1) Driver (Pilóta)
- **id**: String
- **name**: String
- **age**: Int
- **teamId**: String? (opcionális kapcsolás csapathoz)

### 2) Team (Csapat)
- **id**: String
- **name**: String
- **country**: String

### 3) Track (Pálya)
- **id**: String
- **name**: String
- **lengthKm**: Double
- **country**: String?

### 4) Race (Verseny)
- **id**: String
- **name**: String
- **location**: String
- **date**: String (később: LocalDate)
- **trackId**: String? (pálya kapcsolat)

### 5) TrainingSession (Edzésnapló)
- **id**: String
- **driverId**: String? (pilóta kapcsolat)
- **type**: String (pl. “Simulator”, “Gym”, “Track practice”)
- **durationMinutes**: Int
- **notes**: String?
- **createdAt**: String (később: Instant/LocalDateTime)

## Kapcsolatok
- **Team 1:N Driver**: egy csapat több pilótát foghat össze
  - Team.id → Driver.teamId
- **Track 1:N Race**: egy pályán több verseny rendezhető
  - Track.id → Race.trackId
- **Driver 1:N TrainingSession**: egy pilótának több edzésbejegyzése lehet
  - Driver.id → TrainingSession.driverId

## Megjegyzés (későbbi bővítés)
- **Race N:M Team/Driver** megvalósítható kapcsolótáblával (pl. RaceEntry), ha szükséges lesz eredmények/indulók kezeléséhez.

