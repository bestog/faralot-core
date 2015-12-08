# faralot
Android-Framework für Geolocation-Plattformen

[faralot.com / faralot.de](https://faralot.com)

### Was ist faralot?
Das Projekt baut auf die gleichnaimge REST-API auf, zur Abbildung von unterschiedlichen Geolocation-Plattformen und deren spezifischen Eigenschaften. Für Android-Geräte wird ein Framework entwickelt, welches die Plattformen abbilden und maßgeschneiderte Applikationen zu jeweils einer Plattform kreieren kann. Um sie später in der freien Marktwirtschaft produktiv einsetzen zu können. Das Framework kann über alle Funktionen, die die REST-API anbietet, zugreifen und verwenden.

Das Framework sowie die REST-API sind Teil der Bachelorarbeit der Bauhaus Universität Weimar von Sebastian Gottschlich aus dem Jahr 2015.

### Verwendung
Dieses Android-Projekt beinhaltet nur den Core, das Framework.

Kompilieren:

`gradle assembleRelease`

Library:

*.aar* Datei unter `/app/build/outputs/arr`

Um eine eigenständige Geolocation-Plattform für Android zu entwickeln, wird das GitHub-Projekt [faralot-app](https://github.com/bestog/faralot-app) benötigt.

*Getestet mit Android Version 4.4.2 (KitKat) / Motorola Razr i - XT890*

Weitere Details sind unter [faralot.com / faralot.de](https://faralot.com) zu finden.
### License
The components inside this GitHub project are under LGPL v3 licence, with an important simplification: 
The constraints described in Section 5.d and 5.e of the [LGPL LICENCE](https://github.com/bestog/faralot-core/blob/master/LICENSE) are DISCARDED. 

This means that you are allowed to convey a Combined Work without providing the user any way to recombine or relink the application, and without providing any shared library mechanism. 

In other terms, you are allowed to include the faralot-core library in your Android application, without making your application open source. 
