# Inventory console Java app

This is a simple inventory management console application created as part of a practice project. Its functionality includes recording stock receipts, deletions, new and existing customers, and purchases. Data is read from files, and the information displayed on the console is color-coded. During this project, I had the opportunity to practice unit testing, ensuring near-complete coverage to the best of my ability.

Through unit testing, I also learned the importance of applying dependency injection. This not only facilitates loose coupling of the codebase but also significantly improves testability. With this approach, the tests became more isolated and maintainable.

Integration and end-to-end tests will be uploaded soon.

_note:_ 
>  In this project, I deliberately did not extract the repetitive assert/verify logs into a separate "assertLog" class because I felt that the test would be more readable if an actual comment remained in place instead of an additional method call.

#
#

Ez egy egyszerű készletnyilvántartó konzolalkalmazás, amely egy gyakorlóprojekt keretében jött létre. A funkcióit tekintve regisztrálja az árukészlet bevételezését, törlését, az új és meglévő ügyfeleket és a vásárlásokat. Az adatok fájlokból kerülnek beolvasásra, és a konzolon megjelenő információk színesek. A projekt során lehetőségem nyílt a unit tesztelés gyakorlására is, amely legjobb tudásom szerint közel teljes lefedettséget biztosít.

A unit tesztelés során megtanultam azt is, hogy mennyire fontos a dependency injection alkalmazása. Ez nemcsak a kódbázis laza csatolását segíti elő, hanem a tesztelhetőséget is jelentősen megkönnyíti. Ezzel a megközelítéssel a tesztek izoláltabbak és karbantarthatóbbak lettek.

Hamarosan az integrációs és az end-to-end tesztek is feltöltésre kerülnek.

_megjegyzés:_
>  Ebben a projektben az ismétlődő assert/verify logokat direkt nem szerveztem ki egy "assertLog" osztályba, mert úgy éreztem, hogy maga az adott teszt olvashatóbb úgy, ha a komment helyén valóban egy komment van és nem egy újabb metódushívás.
  
