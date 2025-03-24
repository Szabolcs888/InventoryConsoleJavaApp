# Inventory console Java app (with unit tests)

This is a simple inventory management console application created as part of a practice project. Its functionality includes recording stock receipts, deletions, new and existing customers, and purchases. Data is read from files, and the information displayed on the console is color-coded. During this project, I had the opportunity to practice unit testing, ensuring near-complete coverage to the best of my ability.

There are also plans to implement integration and end-to-end tests.

_note:_ 
>  In this project, I deliberately did not extract the repetitive assert/verify logs into a separate "AssertLog" class because I felt that the test would be more readable if an actual comment remained in place instead of an additional method call.

#
#

Ez egy egyszerű készletnyilvántartó konzolalkalmazás, amely egy gyakorlóprojekt keretében jött létre. A funkcióit tekintve regisztrálja az árukészlet bevételezését, törlését, az új és meglévő ügyfeleket és a vásárlásokat. Az adatok fájlokból kerülnek beolvasásra, és a konzolon megjelenő információk színesek. A projekt során lehetőségem nyílt a unit tesztelés gyakorlására is, amely legjobb tudásom szerint közel teljes lefedettséget biztosít.

Tervben van az integrációs és az end-to-end tesztek implementálása is.

_megjegyzés:_
>  Ebben a projektben az ismétlődő assert/verify logokat direkt nem szerveztem ki egy "AssertLog" osztályba, mert úgy éreztem, hogy maga az adott teszt olvashatóbb úgy, ha a komment helyén valóban egy komment van és nem egy újabb metódushívás.
  
